package br.unitau.inf.jogos.dto;

import javax.validation.constraints.NotBlank;

import br.unitau.inf.jogos.model.Noticia;

public class NoticiaPostDTO {
	@NotBlank
	private String texto;

	public String getTexto() {
		return texto;
	}

	public Noticia convert() {
		Noticia ret = new Noticia();
		ret.setTexto(texto);
		return ret;
	}
}