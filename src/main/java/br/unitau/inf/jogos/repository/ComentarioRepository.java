package br.unitau.inf.jogos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unitau.inf.jogos.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
	List<Comentario> findByBloqueadoFalseOrderByDataDesc();
	List<Comentario> findByProjeto_idAndBloqueadoFalseOrderByDataDesc(Integer projeto_id);
}