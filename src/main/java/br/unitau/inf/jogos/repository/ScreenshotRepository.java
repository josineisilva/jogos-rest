package br.unitau.inf.jogos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unitau.inf.jogos.model.Screenshot;

public interface ScreenshotRepository extends JpaRepository<Screenshot, Integer> {
	List<Screenshot> findByBloqueadoFalseOrderByDataDesc();
	List<Screenshot> findByProjeto_idAndBloqueadoFalseOrderByDataDesc(Integer projeto_id);
	Optional<Screenshot> findByIdAndBloqueadoFalse(Integer value);
}
