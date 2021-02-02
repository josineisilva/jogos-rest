package br.unitau.inf.jogos.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.unitau.inf.jogos.dto.ResultadoGetDTO;
import br.unitau.inf.jogos.dto.VotoGetDTO;
import br.unitau.inf.jogos.model.Projeto;
import br.unitau.inf.jogos.model.Usuario;
import br.unitau.inf.jogos.model.VotoCount;
import br.unitau.inf.jogos.repository.ProjetoRepository;
import br.unitau.inf.jogos.repository.UsuarioRepository;
import br.unitau.inf.jogos.security.AuthenticationService;

@RestController
@RequestMapping("/voto")
public class VotoController {
	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private ProjetoRepository projetoRepository;
	@Autowired
	private AuthenticationService authenticationService;

	@Value("${voto.inicio}")
	private String voto_inicio;
	@Value("${voto.final}")
	private String voto_final;

	@GetMapping
	public ResponseEntity<VotoGetDTO> get() {
		ResponseEntity<VotoGetDTO> ret = ResponseEntity.badRequest().build();
		Usuario logged = authenticationService.loggedUser();
		if (logged.hasVoto()) {
			ret = ResponseEntity.ok(new VotoGetDTO(logged));
		} else {
			System.out.println("Usuario ainda nao votou");
			ret = ResponseEntity.notFound().build();
		}
		return ret;
	}

	@GetMapping("/resultados")
	public ResultadoGetDTO resultados() {
		long total_votantes = repository.countVotantes();
		long total_votos = 0;
		List<Object[]> somas = repository.countVotos();
		for (Object[] item : somas) {
			total_votos += (long) item[1];
		}
		List<VotoCount> resultados = new ArrayList<>();
		for (Object[] item : somas) {
			Integer projeto = (Integer) item[0];
			long votos = (long) item[1];
			VotoCount elemento = new VotoCount(projeto, votos, (double) votos / (double) total_votos);
			resultados.add(elemento);
		}
		return new ResultadoGetDTO(total_votantes, total_votos, resultados);
	}

	@PostMapping("/{id}")
	@Transactional
	public ResponseEntity<VotoGetDTO> add(@PathVariable Integer id, UriComponentsBuilder uriBuilder) {
		ResponseEntity<VotoGetDTO> ret = ResponseEntity.badRequest().build();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate inicio = LocalDate.parse(voto_inicio, formatter);
		LocalDate fim = LocalDate.parse(voto_final, formatter);
		LocalDate hoje = LocalDate.now();
		if (!(hoje.isBefore(inicio) || hoje.isAfter(fim))) {
			Usuario logged = authenticationService.loggedUser();
			if (!logged.hasVoto()) {
				Optional<Projeto> search = projetoRepository.findById(id);
				if (search.isPresent()) {
					Usuario usuario = repository.findById(logged.getId()).get();
					usuario.setVoto_data(LocalDateTime.now());
					usuario.setVoto_projeto(id);
					VotoGetDTO item = new VotoGetDTO(usuario);
					URI uri = uriBuilder.path("/voto").buildAndExpand(usuario.getId()).toUri();
					ret = ResponseEntity.created(uri).body(item);
				} else {
					System.out.println("Projeto nao encontrado");
					ret = ResponseEntity.notFound().build();
				}
			} else
				System.out.println("Usuario ja votou");
		} else {
			System.out.println("Fora do perido de voacao");
			ret = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		return ret;
	}

	@DeleteMapping
	@Transactional
	public ResponseEntity<?> remove() {
		ResponseEntity<?> ret = ResponseEntity.badRequest().build();
		Usuario logged = authenticationService.loggedUser();
		if (logged.hasVoto()) {
			Usuario usuario = repository.findById(logged.getId()).get();
			usuario.setVoto_data(null);
			usuario.setVoto_projeto(null);
			ret = ResponseEntity.ok().build();
		} else
			System.out.println("Usuario ainda nao votou");
		return ret;
	}
}