package br.unitau.inf.jogos.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.unitau.inf.jogos.dto.ComentarioGetDTO;
import br.unitau.inf.jogos.dto.NoticiaGetDTO;
import br.unitau.inf.jogos.dto.ProjetoPostDTO;
import br.unitau.inf.jogos.dto.ScreenshotGetDTO;
import br.unitau.inf.jogos.dto.UsuarioGetDTO;
import br.unitau.inf.jogos.model.Comentario;
import br.unitau.inf.jogos.model.Noticia;
import br.unitau.inf.jogos.model.Projeto;
import br.unitau.inf.jogos.model.Screenshot;
import br.unitau.inf.jogos.model.Usuario;
import br.unitau.inf.jogos.repository.ComentarioRepository;
import br.unitau.inf.jogos.repository.NoticiaRepository;
import br.unitau.inf.jogos.repository.ProjetoRepository;
import br.unitau.inf.jogos.repository.ScreenshotRepository;
import br.unitau.inf.jogos.repository.UsuarioRepository;
import br.unitau.inf.jogos.security.AuthenticationService;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {
	@Autowired
	private ProjetoRepository repository;
	@Autowired
	private ComentarioRepository comentarioRepository;
	@Autowired
	private NoticiaRepository noticiaRepository;
	@Autowired
	private ScreenshotRepository screenshotRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping
	public List<Projeto> get() {
		return repository.findAll(Sort.by("id").ascending());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Projeto> getById(@PathVariable Integer id) {
		ResponseEntity<Projeto> ret = ResponseEntity.notFound().build();
		Optional<Projeto> search = repository.findById(id);
		if (search.isPresent()) {
			ret = ResponseEntity.ok(search.get());
		} else
			System.out.println("Projeto nao contrado");
		return ret;
	}

	@GetMapping("/{id}/comentarios")
	public ResponseEntity<List<ComentarioGetDTO>> getComentarios(@PathVariable Integer id) {
		ResponseEntity<List<ComentarioGetDTO>> ret = ResponseEntity.notFound().build();
		Optional<Projeto> search = repository.findById(id);
		if (search.isPresent()) {
			List<Comentario> lista = comentarioRepository.findByProjeto_idAndBloqueadoFalseOrderByDataDesc(id);
			ret = ResponseEntity.ok(ComentarioGetDTO.convert(lista));
		} else
			System.out.println("Projeto nao contrado");
		return ret;
	}

	@GetMapping("/{id}/noticias")
	public ResponseEntity<List<NoticiaGetDTO>> getNoticias(@PathVariable Integer id) {
		ResponseEntity<List<NoticiaGetDTO>> ret = ResponseEntity.notFound().build();
		Optional<Projeto> search = repository.findById(id);
		if (search.isPresent()) {
			List<Noticia> lista = noticiaRepository.findByProjeto_idAndBloqueadoFalseOrderByDataDesc(id);
			ret = ResponseEntity.ok(NoticiaGetDTO.convert(lista));
		} else
			System.out.println("Projeto nao contrado");
		return ret;
	}

	@GetMapping("/{id}/screenshots")
	public ResponseEntity<List<ScreenshotGetDTO>> getScreenshots(@PathVariable Integer id) {
		ResponseEntity<List<ScreenshotGetDTO>> ret = ResponseEntity.notFound().build();
		Optional<Projeto> search = repository.findById(id);
		if (search.isPresent()) {
			List<Screenshot> lista = screenshotRepository.findByProjeto_idAndBloqueadoFalseOrderByDataDesc(id);
			ret = ResponseEntity.ok(ScreenshotGetDTO.convert(lista));
		} else
			System.out.println("Projeto nao contrado");
		return ret;
	}

	@GetMapping("/{id}/usuarios")
	public ResponseEntity<List<UsuarioGetDTO>> getUsuarios(@PathVariable Integer id) {
		ResponseEntity<List<UsuarioGetDTO>> ret = ResponseEntity.notFound().build();
		Optional<Projeto> search = repository.findById(id);
		if (search.isPresent()) {
			List<Usuario> lista = usuarioRepository.findByProjeto_idAndBloqueadoFalseOrderByNameAsc(id);
			ret = ResponseEntity.ok(UsuarioGetDTO.convert(lista));
		} else
			System.out.println("Projeto nao contrado");
		return ret;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<Projeto> post(@RequestBody @Valid ProjetoPostDTO body, UriComponentsBuilder uriBuilder) {
		ResponseEntity<Projeto> ret = ResponseEntity.badRequest().build();
		Projeto item = body.convert();
		Usuario usuario = usuarioRepository.findById(authenticationService.loggedUser().getId()).get();
		if (!usuario.hasProjeto()) {
			repository.save(item);
			usuario.setProjeto(item);
			URI uri = uriBuilder.path("/projeto/{id}").buildAndExpand(item.getId()).toUri();
			ret = ResponseEntity.created(uri).body(item);
		} else
			System.out.println("Usuario ja esta em um projeto");
		return ret;
	}

	@PutMapping
	@Transactional
	public ResponseEntity<Projeto> put(@RequestBody @Valid ProjetoPostDTO body, UriComponentsBuilder uriBuilder) {
		ResponseEntity<Projeto> ret = ResponseEntity.badRequest().build();
		Projeto novo = body.convert();
		Usuario usuario = authenticationService.loggedUser();
		if (usuario.hasProjeto()) {
			Integer id = usuario.getProjeto().getId();
			Projeto projeto = repository.findById(id).get();
			projeto.setNome(novo.getNome());
			projeto.setDescricao(novo.getDescricao());
			projeto.setEquipe(novo.getEquipe());
			projeto.setIcon(novo.getIcon());
			projeto.setOnesheet(novo.getOnesheet());
			ret = ResponseEntity.ok(projeto);
		} else
			System.out.println("Usuario nao esta em nenhum projeto");
		return ret;
	}

	@PutMapping("/adduser/{id}")
	@Transactional
	public ResponseEntity<?> addUser(@PathVariable Integer id) {
		ResponseEntity<?> ret = ResponseEntity.badRequest().build();
		Usuario logged = authenticationService.loggedUser();
		Projeto projeto = logged.getProjeto();
		if (projeto != null) {
			Optional<Usuario> search = usuarioRepository.findById(id);
			if (search.isPresent()) {
				Usuario usuario = usuarioRepository.findById(search.get().getId()).get();
				if (!usuario.isBloqueado()) {
					if (usuario.isDesenvolvedor()) {
						if (!usuario.hasProjeto()) {
							usuario.setProjeto(projeto);
							ret = ResponseEntity.ok("Usuario adicionado ao projeto");
						} else
							System.out.println("Usuario ja esta em um projeto");
					} else
						System.out.println("Usuario nao e desenvolvedor");
				} else {
					System.out.println("Usuario bloquado");
					ret = new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
				}
			} else {
				System.out.println("Usuario nao encontrado");
				ret = ResponseEntity.notFound().build();
			}
		} else
			System.out.println("Usuario nao esta em nenhum projeto");
		return ret;
	}

	@PutMapping("/removeuser/{id}")
	@Transactional
	public ResponseEntity<?> removeUser(@PathVariable Integer id) {
		ResponseEntity<?> ret = ResponseEntity.badRequest().build();
		Usuario logged = authenticationService.loggedUser();
		Projeto projeto = logged.getProjeto();
		if (projeto != null) {
			Optional<Usuario> search = usuarioRepository.findById(id);
			if (search.isPresent()) {
				Usuario usuario = usuarioRepository.findById(search.get().getId()).get();
				if (usuario.checkProjeto(projeto.getId())) {
					usuario.setProjeto(null);
					ret = ResponseEntity.ok("Usuario removido ao projeto");
				} else
					System.out.println("Usuario nao esta no projeto");
			} else {
				System.out.println("Usuario nao encontrado");
				ret = ResponseEntity.notFound().build();
			}
		} else
			System.out.println("Usuario nao esta em nenhum projeto");
		return ret;
	}
}