package br.unitau.inf.jogos.dto;

import javax.validation.constraints.NotBlank;

import br.unitau.inf.jogos.model.Screenshot;

public class ScreenshotPostDTO {
	@NotBlank
	private String descricao;
	@NotBlank
	private String imagem;

	public String getDescricao() {
		return descricao;
	}
	
	public String getImagem() {
		return imagem;
	}

	public Screenshot convert() {
		Screenshot ret = new Screenshot();
		ret.setDescricao(descricao);
		ret.setImagem(imagem);
		return ret;
	}
}