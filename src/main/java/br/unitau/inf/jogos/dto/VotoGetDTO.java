package br.unitau.inf.jogos.dto;

import java.time.LocalDateTime;

import br.unitau.inf.jogos.model.Usuario;

public class VotoGetDTO {
	private LocalDateTime data;
	private Integer projeto;

	public VotoGetDTO(Usuario obj) {
		this.data = obj.getVoto_data();
		this.projeto = obj.getVoto_projeto();
	}

	public LocalDateTime getData() {
		return data;
	}

	public Integer getProjeto() {
		return projeto;
	}
}
