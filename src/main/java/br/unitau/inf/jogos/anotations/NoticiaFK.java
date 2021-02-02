package br.unitau.inf.jogos.anotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.unitau.inf.jogos.validator.NoticiaFKValidator;

@Constraint(validatedBy = NoticiaFKValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoticiaFK {
	  String message() default "Noticia invalida";
	  Class<?>[] groups() default {};
	  Class<? extends Payload>[] payload() default { };
}