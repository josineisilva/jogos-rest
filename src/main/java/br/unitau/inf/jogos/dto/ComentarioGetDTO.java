package br.unitau.inf.jogos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.unitau.inf.jogos.model.Comentario;
import br.unitau.inf.jogos.model.Projeto;
import br.unitau.inf.jogos.model.Usuario;

public class ComentarioGetDTO {
    private Integer id;
    private Integer projeto_id;
    private LocalDateTime data = LocalDateTime.now();
    private String texto;
    private Integer usuario_id;
    
	public ComentarioGetDTO(Comentario obj) {
		this.id = obj.getId();
		this.data = obj.getData();
		this.texto = obj.getTexto();
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
	
	public String getTexto() {
		return texto;
	}
	
	public Integer getUsuario_id() {
		return usuario_id;
	}
	
	public static List<ComentarioGetDTO> convert(List<Comentario> lista) {
		return lista.stream().map(ComentarioGetDTO::new).collect(Collectors.toList());
	}
}