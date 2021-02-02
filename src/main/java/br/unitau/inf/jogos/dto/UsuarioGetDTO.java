package br.unitau.inf.jogos.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.unitau.inf.jogos.model.Usuario;

public class UsuarioGetDTO {
	private Integer id;
	private String name;
	private boolean voto;

	public UsuarioGetDTO(Usuario obj) {
		this.id = obj.getId();
		this.name = obj.getName();
		this.voto = obj.hasVoto();
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public boolean getVoto() {
		return voto;
	}
	
	public static List<UsuarioGetDTO> convert(List<Usuario> lista) {
		return lista.stream().map(UsuarioGetDTO::new).collect(Collectors.toList());
	}
}