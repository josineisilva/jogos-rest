package br.unitau.inf.jogos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unitau.inf.jogos.model.NoticiaComentario;

public interface NoticiaComentarioRepository extends JpaRepository<NoticiaComentario, Integer> {
	List<NoticiaComentario> findByBloqueadoFalseOrderByDataDesc();
	List<NoticiaComentario> findByNoticia_idAndBloqueadoFalseOrderByDataDesc(Integer noticia_id);
}
