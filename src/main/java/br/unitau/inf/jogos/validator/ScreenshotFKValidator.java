package br.unitau.inf.jogos.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.unitau.inf.jogos.anotations.ScreenshotFK;
import br.unitau.inf.jogos.model.Screenshot;
import br.unitau.inf.jogos.repository.ScreenshotRepository;

@Component
public class ScreenshotFKValidator implements ConstraintValidator<ScreenshotFK, Integer> {
	@Autowired
	private ScreenshotRepository repository;
	
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		Optional<Screenshot> search = repository.findByIdAndBloqueadoFalse(value);
		if (search.isPresent()) {
			return true;
		}
		return false;
	}
}