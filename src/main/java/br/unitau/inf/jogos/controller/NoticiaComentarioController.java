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
import br.unitau.inf.jogos.dto.NoticiaComentarioPostDTO;
import br.unitau.inf.jogos.model.Noticia;
import br.unitau.inf.jogos.model.NoticiaComentario;
import br.unitau.inf.jogos.model.Usuario;
import br.unitau.inf.jogos.repository.NoticiaComentarioRepository;
import br.unitau.inf.jogos.repository.NoticiaRepository;
import br.unitau.inf.jogos.repository.UsuarioRepository;
import br.unitau.inf.jogos.security.AuthenticationService;

@RestController
@RequestMapping("/noticiacomentario")
public class NoticiaComentarioController {
	@Autowired
	private NoticiaComentarioRepository repository;
	@Autowired
	private NoticiaRepository noticiaRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping
	public List<NoticiaComentarioGetDTO> get() {
		List<NoticiaComentario> lista = repository.findByBloqueadoFalseOrderByDataDesc();
		return NoticiaComentarioGetDTO.convert(lista);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NoticiaComentarioGetDTO> getById(@PathVariable Integer id) {
		ResponseEntity<NoticiaComentarioGetDTO> ret = ResponseEntity.notFound().build();
		Optional<NoticiaComentario> search = repository.findById(id);
		if (search.isPresent()) {
			NoticiaComentario item = search.get();
			if (!item.isBloqueado()) {
				ret = ResponseEntity.ok(new NoticiaComentarioGetDTO(item));
			} else {
				System.out.println("Comentario da noticia bloquado");
				ret = new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
			}
		}
		else
			System.out.println("Comentario da noticia nao encontrado");
		return ret;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<NoticiaComentarioGetDTO> post(@RequestBody @Valid NoticiaComentarioPostDTO body,
			UriComponentsBuilder uriBuilder) {
		ResponseEntity<NoticiaComentarioGetDTO> ret = ResponseEntity.badRequest().build();
		NoticiaComentario item = body.convert(noticiaRepository, usuarioRepository);
		Noticia noticia = item.getNoticia();
		if (noticia != null) {
			if (!noticia.isBloqueado()) {
				Usuario usuario = authenticationService.loggedUser();
				item.setUsuario(usuario);
				repository.save(item);
				URI uri = uriBuilder.path("/noticiacomentario/{id}").buildAndExpand(item.getId()).toUri();
				ret = ResponseEntity.created(uri).body(new NoticiaComentarioGetDTO(item));
			}
			else
				System.out.println("Noticia bloquada");
		}
		else
			System.out.println("Noticia nao encontrada");
		return ret;
	}
}