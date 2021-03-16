package br.unitau.inf.jogos.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthenticationDTO {
    private String email;
    private String password;

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

    public UsernamePasswordAuthenticationToken toToken() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}