package br.unitau.inf.jogos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.unitau.inf.jogos.model.Screenshot;
import br.unitau.inf.jogos.model.ScreenshotComentario;
import br.unitau.inf.jogos.model.Usuario;

public class ScreenshotComentarioGetDTO {
    private Integer id;
    private Integer Screenshot_id;
    private LocalDateTime data = LocalDateTime.now();
    private String texto;
    private Integer usuario_id;
    
	public ScreenshotComentarioGetDTO(ScreenshotComentario obj) {
		this.id = obj.getId();
		this.data = obj.getData();
		this.texto = obj.getTexto();
		Screenshot Screenshot = obj.getScreenshot();
		if ( Screenshot != null )
			this.Screenshot_id = Screenshot.getId();
		Usuario usuario = obj.getUsuario();
		if ( usuario != null )
			this.usuario_id = usuario.getId();
	}
    
	public Integer getId() {
		return id;
	}
	
	public Integer getScreenshot_id() {
		return Screenshot_id;
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
	
	public static List<ScreenshotComentarioGetDTO> convert(List<ScreenshotComentario> lista) {
		return lista.stream().map(ScreenshotComentarioGetDTO::new).collect(Collectors.toList());
	}
}