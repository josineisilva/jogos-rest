package br.unitau.inf.jogos.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.unitau.inf.jogos.model.Usuario;
import br.unitau.inf.jogos.repository.UsuarioRepository;

public class AuthenticationFilter extends OncePerRequestFilter {
	private JwtService jwtService;
	private UsuarioRepository repository;

	public AuthenticationFilter(JwtService jwtService, UsuarioRepository repository) {
		this.jwtService = jwtService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = getJwt(request);
		if (jwtService.validate(jwt)) {
			authenticate(jwt);
		}
		filterChain.doFilter(request, response);
	}

	private String getJwt(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (authorization == null || authorization.isEmpty() || !authorization.startsWith("Bearer ")) {
			return null;
		}
		return authorization.substring(7, authorization.length());
	}

	private void authenticate(String jwt) {
		Integer usuario_id = jwtService.autenticate(jwt);
		Usuario usuario = repository.findById(usuario_id).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null,
				usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}