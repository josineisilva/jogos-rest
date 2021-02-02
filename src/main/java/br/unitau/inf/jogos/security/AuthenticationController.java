package br.unitau.inf.jogos.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@PostMapping
	public ResponseEntity<?> autenticar(@RequestBody @Valid AuthenticationDTO dto) {
		System.out.println("Vai gerar autenticacao");
		UsernamePasswordAuthenticationToken token = dto.toToken();
		try {
			Authentication authentication = authenticationManager.authenticate(token);
			String jwt = jwtService.generate(authentication);
			return ResponseEntity.ok(new JwtDTO(jwt, "Bearer"));
		} catch (AuthenticationException e) {
			System.out.println("Falha na geracao do JWT: "+e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}