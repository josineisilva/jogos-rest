package br.unitau.inf.jogos.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import br.unitau.inf.jogos.model.Projeto;

public class ProjetoPostDTO {
	@NotBlank
	@Length(max = 60)
	private String nome;
	private String descricao;
	private String equipe;
	private String download;
	private String icon;
	private String onesheet;

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public String getEquipe() {
		return equipe;
	}
	
	public String getDownload() {
		return download;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public String getOnesheet() {
		return onesheet;
	}
	
	public Projeto convert() {
		Projeto ret = new Projeto();
		ret.setNome(nome);
		ret.setDescricao(descricao);
		ret.setEquipe(equipe);
		ret.setDownload(download);
		ret.setIcon(icon);
		ret.setOnesheet(onesheet);
		return ret;
	}
}