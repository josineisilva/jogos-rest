package br.unitau.inf.jogos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unitau.inf.jogos.model.ScreenshotComentario;

public interface ScreenshotComentarioRepository extends JpaRepository<ScreenshotComentario, Integer> {
	List<ScreenshotComentario> findByBloqueadoFalseOrderByDataDesc();
	List<ScreenshotComentario> findByScreenshot_idAndBloqueadoFalseOrderByDataDesc(Integer Screenshot_id);
}
