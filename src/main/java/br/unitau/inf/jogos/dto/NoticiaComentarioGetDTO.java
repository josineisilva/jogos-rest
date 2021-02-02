package br.unitau.inf.jogos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.unitau.inf.jogos.model.Noticia;
import br.unitau.inf.jogos.model.NoticiaComentario;
import br.unitau.inf.jogos.model.Usuario;

public class NoticiaComentarioGetDTO {
    private Integer id;
    private Integer noticia_id;
    private LocalDateTime data = LocalDateTime.now();
    private String texto;
    private Integer usuario_id;
    
	public NoticiaComentarioGetDTO(NoticiaComentario obj) {
		this.id = obj.getId();
		this.data = obj.getData();
		this.texto = obj.getTexto();
		Noticia noticia = obj.getNoticia();
		if ( noticia != null )
			this.noticia_id = noticia.getId();
		Usuario usuario = obj.getUsuario();
		if ( usuario != null )
			this.usuario_id = usuario.getId();
	}
    
	public Integer getId() {
		return id;
	}
	
	public Integer getNoticia_id() {
		return noticia_id;
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
	
	public static List<NoticiaComentarioGetDTO> convert(List<NoticiaComentario> lista) {
		return lista.stream().map(NoticiaComentarioGetDTO::new).collect(Collectors.toList());
	}
}