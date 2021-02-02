package br.unitau.inf.jogos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.unitau.inf.jogos.model.Screenshot;
import br.unitau.inf.jogos.model.Projeto;
import br.unitau.inf.jogos.model.Usuario;

public class ScreenshotGetDTO {
    private Integer id;
    private Integer projeto_id;
    private LocalDateTime data = LocalDateTime.now();
    private String descricao;
    private String imagem;
    private Integer usuario_id;
    
	public ScreenshotGetDTO(Screenshot obj) {
		this.id = obj.getId();
		this.data = obj.getData();
		this.descricao = obj.getDescricao();
		this.imagem = obj.getImagem();
		Projeto projeto = obj.getProjeto();
		if ( projeto != null )
			this.projeto_id = projeto.getId();
		Usuario usuario = obj.getUsuario();
		if ( usuario != null )
			this.usuario_id = usuario.getId();
	}
    
	public Integer getId() {
		return id;
	}
	
	public Integer getProjeto_id() {
		return projeto_id;
	}
	
	public LocalDateTime getData() {
		return data;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getImagem() {
		return imagem;
	}
	
	public Integer getUsuario_id() {
		return usuario_id;
	}
	
	public static List<ScreenshotGetDTO> convert(List<Screenshot> lista) {
		return lista.stream().map(ScreenshotGetDTO::new).collect(Collectors.toList());
	}
}