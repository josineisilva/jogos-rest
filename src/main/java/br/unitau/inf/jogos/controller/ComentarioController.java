package br.unitau.inf.jogos.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.unitau.inf.jogos.dto.ComentarioGetDTO;
import br.unitau.inf.jogos.dto.ComentarioPostDTO;
import br.unitau.inf.jogos.model.Comentario;
import br.unitau.inf.jogos.repository.ComentarioRepository;
import br.unitau.inf.jogos.repository.ProjetoRepository;
import br.unitau.inf.jogos.repository.UsuarioRepository;
import br.unitau.inf.jogos.security.AuthenticationService;

@RestController
@RequestMapping("/comentario")
public class ComentarioController {
	@Autowired
	private ComentarioRepository repository;
	@Autowired
	private ProjetoRepository projetoRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping
	public List<ComentarioGetDTO> get() {
		List<Comentario> lista = repository.findByBloqueadoFalseOrderByDataDesc();
		return ComentarioGetDTO.convert(lista);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ComentarioGetDTO> getById(@PathVariable Integer id) {
		ResponseEntity<ComentarioGetDTO> ret = ResponseEntity.notFound().build();
		Optional<Comentario> search = repository.findById(id);
		if (search.isPresent()) {
			Comentario item = search.get();
			if (!item.isBloqueado()) {
				ret = ResponseEntity.ok(new ComentarioGetDTO(item));
			} else {
				System.out.println("Comentario bloquado");
				ret = new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
			}
		}
		else
			System.out.println("Comentario nao encontrado");
		return ret;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<ComentarioGetDTO> post(@RequestBody @Valid ComentarioPostDTO body,
			UriComponentsBuilder uriBuilder) {
		Comentario item = body.convert(projetoRepository, usuarioRepository);
		item.setUsuario(authenticationService.loggedUser());
		repository.save(item);
		URI uri = uriBuilder.path("/comentario/{id}").buildAndExpand(item.getId()).toUri();
		return ResponseEntity.created(uri).body(new ComentarioGetDTO(item));
	}
}