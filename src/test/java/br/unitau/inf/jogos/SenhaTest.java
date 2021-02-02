package br.unitau.inf.jogos;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SenhaTest {
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("teste"));
	}
}
