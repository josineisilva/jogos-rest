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
import br.unitau.inf.jogos.dto.ScreenshotComentarioPostDTO;
import br.unitau.inf.jogos.model.Screenshot;
import br.unitau.inf.jogos.model.ScreenshotComentario;
import br.unitau.inf.jogos.model.Usuario;
import br.unitau.inf.jogos.repository.ScreenshotComentarioRepository;
import br.unitau.inf.jogos.repository.ScreenshotRepository;
import br.unitau.inf.jogos.repository.UsuarioRepository;
import br.unitau.inf.jogos.security.AuthenticationService;

@RestController
@RequestMapping("/screenshotcomentario")
public class ScreenshotComentarioController {
	@Autowired
	private ScreenshotComentarioRepository repository;
	@Autowired
	private ScreenshotRepository screenshotRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping
	public List<ScreenshotComentarioGetDTO> get() {
		List<ScreenshotComentario> lista = repository.findByBloqueadoFalseOrderByDataDesc();
		return ScreenshotComentarioGetDTO.convert(lista);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ScreenshotComentarioGetDTO> getById(@PathVariable Integer id) {
		ResponseEntity<ScreenshotComentarioGetDTO> ret = ResponseEntity.notFound().build();
		Optional<ScreenshotComentario> search = repository.findById(id);
		if (search.isPresent()) {
			ScreenshotComentario item = search.get();
			if (!item.isBloqueado()) {
				ret = ResponseEntity.ok(new ScreenshotComentarioGetDTO(item));
			} else {
				System.out.println("Comentario do screenshot bloquado");
				ret = new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
			}
		}
		else
			System.out.println("Comentario do screenshot nao encontrado");
		return ret;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<ScreenshotComentarioGetDTO> post(@RequestBody @Valid ScreenshotComentarioPostDTO body,
			UriComponentsBuilder uriBuilder) {
		ResponseEntity<ScreenshotComentarioGetDTO> ret = ResponseEntity.badRequest().build();
		ScreenshotComentario item = body.convert(screenshotRepository, usuarioRepository);
		Screenshot screenshot = item.getScreenshot();
		if (screenshot != null) {
			if (!screenshot.isBloqueado()) {
				Usuario usuario = authenticationService.loggedUser();
				item.setUsuario(usuario);
				repository.save(item);
				URI uri = uriBuilder.path("/screenshotcomentario/{id}").buildAndExpand(item.getId()).toUri();
				ret = ResponseEntity.created(uri).body(new ScreenshotComentarioGetDTO(item));
			}
			else
				System.out.println("Screenshot bloquado");
		}
		else
			System.out.println("Screenshot nao encontrado");
		return ret;
	}
}