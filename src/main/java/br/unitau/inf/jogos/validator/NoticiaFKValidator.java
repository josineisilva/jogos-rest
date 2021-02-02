package br.unitau.inf.jogos.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.unitau.inf.jogos.anotations.NoticiaFK;
import br.unitau.inf.jogos.model.Noticia;
import br.unitau.inf.jogos.repository.NoticiaRepository;

@Component
public class NoticiaFKValidator implements ConstraintValidator<NoticiaFK, Integer> {
	@Autowired
	private NoticiaRepository repository;
	
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		Optional<Noticia> search = repository.findByIdAndBloqueadoFalse(value);
		if (search.isPresent()) {
			return true;
		}
		return false;
	}
}