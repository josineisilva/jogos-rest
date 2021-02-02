package br.unitau.inf.jogos.dto;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import br.unitau.inf.jogos.anotations.NoticiaFK;
import br.unitau.inf.jogos.model.Noticia;
import br.unitau.inf.jogos.model.NoticiaComentario;
import br.unitau.inf.jogos.repository.NoticiaRepository;
import br.unitau.inf.jogos.repository.UsuarioRepository;

public class NoticiaComentarioPostDTO {
	@NoticiaFK
	private Integer noticia_id;
	@NotBlank
	private String texto;

	public Integer getNoticia_id() {
		return noticia_id;
	}

	public String getTexto() {
		return texto;
	}

	public NoticiaComentario convert(NoticiaRepository noticiaRepository, UsuarioRepository usuarioRepository) {
		NoticiaComentario ret = new NoticiaComentario();
		ret.setTexto(texto);
		Optional<Noticia> noticiaSearch = noticiaRepository.findByIdAndBloqueadoFalse(noticia_id);
		if (noticiaSearch.isPresent()) {
			Noticia noticia = noticiaSearch.get();
			ret.setNoticia(noticia);
		}
		return ret;
	}
}