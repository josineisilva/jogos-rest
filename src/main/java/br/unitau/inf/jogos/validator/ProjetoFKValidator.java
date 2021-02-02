package br.unitau.inf.jogos.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.unitau.inf.jogos.anotations.ProjetoFK;
import br.unitau.inf.jogos.model.Projeto;
import br.unitau.inf.jogos.repository.ProjetoRepository;

@Component
public class ProjetoFKValidator implements ConstraintValidator<ProjetoFK, Integer> {
	@Autowired
	private ProjetoRepository repository;
	
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		Optional<Projeto> search = repository.findById(value);
		if (search.isPresent()) {
			return true;
		}
		return false;
	}
}