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

import br.unitau.inf.jogos.dto.NoticiaComentarioGetDTO;
import br.unitau.inf.jogos.dto.NoticiaGetDTO;
import br.unitau.inf.jogos.dto.NoticiaPostDTO;
import br.unitau.inf.jogos.model.Noticia;
import br.unitau.inf.jogos.model.NoticiaComentario;
import br.unitau.inf.jogos.model.Usuario;
import br.unitau.inf.jogos.repository.NoticiaComentarioRepository;
import br.unitau.inf.jogos.repository.NoticiaRepository;
import br.unitau.inf.jogos.security.AuthenticationService;

@RestController
@RequestMapping("/noticia")
public class NoticiaController {
	@Autowired
	private NoticiaRepository repository;
	@Autowired
	private NoticiaComentarioRepository comentarioRepository;
	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping
	public List<NoticiaGetDTO> get() {
		List<Noticia> lista = repository.findByBloqueadoFalseOrderByDataDesc();
		return NoticiaGetDTO.convert(lista);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NoticiaGetDTO> getById(@PathVariable Integer id) {
		ResponseEntity<NoticiaGetDTO> ret = ResponseEntity.notFound().build();
		Optional<Noticia> search = repository.findById(id);
		if (search.isPresent()) {
			Noticia item = search.get();
			if (!item.isBloqueado()) {
				ret = ResponseEntity.ok(new NoticiaGetDTO(item));
			} else {
				System.out.println("Noticia bloqueada");
				ret = new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
			}
		}
		else
			System.out.println("Noticia nao encontrada");
		return ret;
	}

	@GetMapping("/{id}/comentarios")
	public ResponseEntity<List<NoticiaComentarioGetDTO>> getComentarios(@PathVariable Integer id) {
		ResponseEntity<List<NoticiaComentarioGetDTO>> ret = ResponseEntity.notFound().build();
		Optional<Noticia> search = repository.findById(id);
		if (search.isPresent()) {
			Noticia item = search.get();
			if (!item.isBloqueado()) {
				List<NoticiaComentario> lista = comentarioRepository.findByNoticia_idAndBloqueadoFalseOrderByDataDesc(id);
				ret = ResponseEntity.ok(NoticiaComentarioGetDTO.convert(lista));		
			}
			else {
				System.out.println("Noticia bloqueada");
				ret = new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
			}
		}
		else
			System.out.println("Noticia nao encontrada");
		return ret;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<NoticiaGetDTO> post(@RequestBody @Valid NoticiaPostDTO body,
			UriComponentsBuilder uriBuilder) {
		ResponseEntity<NoticiaGetDTO> ret = ResponseEntity.badRequest().build();
		Noticia item = body.convert();
		Usuario usuario = authenticationService.loggedUser();
		if (usuario.hasProjeto()) {
			item.setUsuario(usuario);
			item.setProjeto(usuario.getProjeto());
			repository.save(item);
			URI uri = uriBuilder.path("/comentario/{id}").buildAndExpand(item.getId()).toUri();
			ret = ResponseEntity.created(uri).body(new NoticiaGetDTO(item));
		}
		else
			System.out.println("Usuario nao pertence a nenhum projeto");
		return ret;
	}
}