package br.unitau.inf.jogos.dto;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import br.unitau.inf.jogos.anotations.ScreenshotFK;
import br.unitau.inf.jogos.model.Screenshot;
import br.unitau.inf.jogos.model.ScreenshotComentario;
import br.unitau.inf.jogos.repository.ScreenshotRepository;
import br.unitau.inf.jogos.repository.UsuarioRepository;

public class ScreenshotComentarioPostDTO {
	@ScreenshotFK
	private Integer screenshot_id;
	@NotBlank
	private String texto;

	public Integer getScreenshot_id() {
		return screenshot_id;
	}

	public String getTexto() {
		return texto;
	}

	public ScreenshotComentario convert(ScreenshotRepository screenshotRepository, UsuarioRepository usuarioRepository) {
		ScreenshotComentario ret = new ScreenshotComentario();
		ret.setTexto(texto);
		Optional<Screenshot> screenshotSearch = screenshotRepository.findByIdAndBloqueadoFalse(screenshot_id);
		if (screenshotSearch.isPresent()) {
			Screenshot screenshot = screenshotSearch.get();
			ret.setScreenshot(screenshot);
		}
		return ret;
	}
}