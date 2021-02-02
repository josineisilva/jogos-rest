package br.unitau.inf.jogos.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

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

import br.unitau.inf.jogos.dto.ScreenshotComentarioGetDTO;
import br.unitau.inf.jogos.dto.ScreenshotGetDTO;
import br.unitau.inf.jogos.dto.ScreenshotPostDTO;
import br.unitau.inf.jogos.model.Screenshot;
import br.unitau.inf.jogos.model.ScreenshotComentario;
import br.unitau.inf.jogos.model.Usuario;
import br.unitau.inf.jogos.repository.ScreenshotComentarioRepository;
import br.unitau.inf.jogos.repository.ScreenshotRepository;
import br.unitau.inf.jogos.security.AuthenticationService;

@RestController
@RequestMapping("/screenshot")
public class ScreenshotController {
	@Autowired
	private ScreenshotRepository repository;
	@Autowired
	private ScreenshotComentarioRepository comentarioRepository;
	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping
	public List<ScreenshotGetDTO> get() {
		List<Screenshot> lista = repository.findByBloqueadoFalseOrderByDataDesc();
		return ScreenshotGetDTO.convert(lista);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ScreenshotGetDTO> getById(@PathVariable Integer id) {
		ResponseEntity<ScreenshotGetDTO> ret = ResponseEntity.notFound().build();
		Optional<Screenshot> search = repository.findById(id);
		if (search.isPresent()) {
			Screenshot item = search.get();
			if (!item.isBloqueado()) {
				ret = ResponseEntity.ok(new ScreenshotGetDTO(item));
			} else {
				System.out.println("Screenshot bloquado");
				ret = new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
			}
		}
		else
			System.out.println("Screenshot nao encontrado");
		return ret;
	}

	@GetMapping("/{id}/comentarios")
	public ResponseEntity<List<ScreenshotComentarioGetDTO>> getComentarios(@PathVariable Integer id) {
		ResponseEntity<List<ScreenshotComentarioGetDTO>> ret = ResponseEntity.notFound().build();
		Optional<Screenshot> search = repository.findById(id);
		if (search.isPresent()) {
			Screenshot item = search.get();
			if (!item.isBloqueado()) {
				List<ScreenshotComentario> lista = comentarioRepository.findByScreenshot_idAndBloqueadoFalseOrderByDataDesc(id);
				ret = ResponseEntity.ok(ScreenshotComentarioGetDTO.convert(lista));		
			}
			else {
				System.out.println("Screenshot bloquado");
				ret = new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
			}
		}
		else
			System.out.println("Screenshot nao encontrado");
		return ret;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<ScreenshotGetDTO> post(@RequestBody @Valid ScreenshotPostDTO body,
			UriComponentsBuilder uriBuilder) {
		ResponseEntity<ScreenshotGetDTO> ret = ResponseEntity.badRequest().build();
		Screenshot item = body.convert();
		Usuario usuario = authenticationService.loggedUser();
		if (usuario.hasProjeto()) {
			item.setUsuario(usuario);
			item.setProjeto(usuario.getProjeto());
			repository.save(item);
			URI uri = uriBuilder.path("/comentario/{id}").buildAndExpand(item.getId()).toUri();
			ret = ResponseEntity.created(uri).body(new ScreenshotGetDTO(item));
		}
		else
			System.out.println("Usuario nao pertence a nenhum projeto");
		return ret;
	}
}