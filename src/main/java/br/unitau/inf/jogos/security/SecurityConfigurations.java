package br.unitau.inf.jogos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.unitau.inf.jogos.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	// Configuracoes de autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// Configuracoes de autorizacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().mvcMatchers(HttpMethod.POST, "/auth").permitAll()
				.mvcMatchers(HttpMethod.POST, "/auth/*").permitAll()
				.mvcMatchers(HttpMethod.GET, "/projeto").permitAll()
				.mvcMatchers(HttpMethod.GET, "/projeto/*").permitAll()
				.mvcMatchers(HttpMethod.GET, "/projeto/*/comentarios").permitAll()
				.mvcMatchers(HttpMethod.GET, "/projeto/*/noticias").permitAll()
				.mvcMatchers(HttpMethod.GET, "/projeto/*/screenshots").permitAll()
				.mvcMatchers(HttpMethod.GET, "/projeto/*/usuarios").permitAll()
				.mvcMatchers(HttpMethod.POST, "/projeto").hasRole("DESENVOLVEDOR")	
				.mvcMatchers(HttpMethod.PUT, "/projeto/adduser/*").hasRole("DESENVOLVEDOR")
				.mvcMatchers(HttpMethod.PUT, "/projeto/removeuser/*").hasRole("DESENVOLVEDOR")
				.mvcMatchers(HttpMethod.PUT, "/projeto").hasRole("DESENVOLVEDOR")
				.mvcMatchers(HttpMethod.GET, "/comentario").authenticated()
				.mvcMatchers(HttpMethod.GET, "/comentario/*").permitAll()
				.mvcMatchers(HttpMethod.POST, "/screenshot").hasRole("DESENVOLVEDOR")
				.mvcMatchers(HttpMethod.GET, "/screenshot").authenticated()
				.mvcMatchers(HttpMethod.GET, "/screenshot/*").permitAll()
				.mvcMatchers(HttpMethod.GET, "/screenshot/*/comentarios").permitAll()
				.mvcMatchers(HttpMethod.GET, "/screenshotcomentario").authenticated()
				.mvcMatchers(HttpMethod.GET, "/screenshotcomentario/*").permitAll()
				.mvcMatchers(HttpMethod.POST, "/noticia").hasRole("DESENVOLVEDOR")
				.mvcMatchers(HttpMethod.GET, "/noticia").authenticated()
				.mvcMatchers(HttpMethod.GET, "/noticia/*").permitAll()
				.mvcMatchers(HttpMethod.GET, "/noticia/*/comentarios").permitAll()
				.mvcMatchers(HttpMethod.GET, "/noticiacomentario").authenticated()
				.mvcMatchers(HttpMethod.GET, "/noticiacomentario/*").permitAll()
				.mvcMatchers(HttpMethod.GET, "/usuario/free").hasRole("DESENVOLVEDOR")
				.mvcMatchers(HttpMethod.GET, "/usuario/*").permitAll()
				.mvcMatchers(HttpMethod.GET, "/voto/resultados").permitAll()
				.mvcMatchers(HttpMethod.GET, "/voto").hasRole("USUARIO")
				.mvcMatchers(HttpMethod.POST, "/voto/*").hasRole("USUARIO")
				.mvcMatchers(HttpMethod.DELETE, "/voto").hasRole("USUARIO")
				.mvcMatchers(HttpMethod.POST, "/password/forgot").permitAll()
				.mvcMatchers(HttpMethod.GET, "/password/read").permitAll()
				.mvcMatchers(HttpMethod.POST, "/password/reset").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(new AuthenticationFilter(jwtService, usuarioRepository),
						UsernamePasswordAuthenticationFilter.class);
	}

	// Configuracoes de recursos estaticos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**",
				"/swagger-resources/**");
	}
}