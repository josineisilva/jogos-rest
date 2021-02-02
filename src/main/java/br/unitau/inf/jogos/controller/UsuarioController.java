package br.unitau.inf.jogos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unitau.inf.jogos.dto.UsuarioGetDTO;
import br.unitau.inf.jogos.model.Usuario;
import br.unitau.inf.jogos.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	@Autowired
	private UsuarioRepository repository;

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioGetDTO> getById(@PathVariable Integer id) {
		ResponseEntity<UsuarioGetDTO> ret = ResponseEntity.notFound().build();
		Optional<Usuario> search = repository.findById(id);
		if (search.isPresent()) {
			Usuario item = search.get();
			if (!item.isBloqueado()) {
				ret = ResponseEntity.ok(new UsuarioGetDTO(item));
			} else {
				System.out.println("Usuario bloqueado");
				ret = new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
			}
		} else
			System.out.println("Usuario nao encontrado");
		return ret;
	}

	@GetMapping("/free")
	public ResponseEntity<List<UsuarioGetDTO>> free() {
		ResponseEntity<List<UsuarioGetDTO>> ret = ResponseEntity.badRequest().build();
		List<Usuario> lista = repository.findFree();
		ret = ResponseEntity.ok(UsuarioGetDTO.convert(lista));
		return ret;
	}
}