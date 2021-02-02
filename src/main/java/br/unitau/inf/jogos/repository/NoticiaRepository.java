package br.unitau.inf.jogos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unitau.inf.jogos.model.Noticia;

public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {
	List<Noticia> findByBloqueadoFalseOrderByDataDesc();
	List<Noticia> findByProjeto_idAndBloqueadoFalseOrderByDataDesc(Integer projeto_id);
	Optional<Noticia> findByIdAndBloqueadoFalse(Integer value);
}