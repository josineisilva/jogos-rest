package br.unitau.inf.jogos.dto;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import br.unitau.inf.jogos.anotations.ProjetoFK;
import br.unitau.inf.jogos.model.Comentario;
import br.unitau.inf.jogos.model.Projeto;
import br.unitau.inf.jogos.repository.ProjetoRepository;
import br.unitau.inf.jogos.repository.UsuarioRepository;

public class ComentarioPostDTO {
	@ProjetoFK
	private Integer projeto_id;
	@NotBlank
	private String texto;

	public Integer getProjeto_id() {
		return projeto_id;
	}

	public String getTexto() {
		return texto;
	}

	public Comentario convert(ProjetoRepository projetoRepository, UsuarioRepository usuarioRepository) {
		Comentario ret = new Comentario();
		ret.setTexto(texto);
		Optional<Projeto> projetoSearch = projetoRepository.findById(projeto_id);
		if (projetoSearch.isPresent()) {
			Projeto projeto = projetoSearch.get();
			ret.setProjeto(projeto);
		}
		return ret;
	}
}