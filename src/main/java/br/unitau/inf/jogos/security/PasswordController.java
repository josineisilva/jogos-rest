package br.unitau.inf.jogos.security;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.unitau.inf.jogos.model.Usuario;
import br.unitau.inf.jogos.repository.UsuarioRepository;

@Controller
public class PasswordController {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("/password/read")
	public String read(@RequestParam String token, Model model) {
		String ret = "passwordmessage";
		model.addAttribute("token", token);
		Optional<Usuario> search = usuarioRepository.findByResetpassToken(token);
		if (search.isPresent()) {
			Usuario usuario = search.get();
			if (LocalDateTime.now().isBefore(usuario.getResetpassLimit())) {
				ret = "passwordform";
			} else
				model.addAttribute("error", "Requisição expirada");
		} else
			model.addAttribute("error", "Requisição inválida");
		return ret;
	}

	@PostMapping("/password/reset")
	@Transactional
	public String processResetPassword(HttpServletRequest request, Model model) {
		String ret = "passwordform";
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	    String confirm = request.getParameter("confirm");
	    model.addAttribute("token", token);
		Optional<Usuario> search = usuarioRepository.findByResetpassToken(token);
		if (search.isPresent()) {
			Usuario usuario = search.get();
			if (LocalDateTime.now().isBefore(usuario.getResetpassLimit())) {
				if (password.equals(confirm)) {
					BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			        String encodedPassword = passwordEncoder.encode(password);
			        usuario.setPassword(encodedPassword);			         
			        usuario.setResetpassToken(null);
			        usuario.setResetpassLimit(null);
			        usuario.setResetpassForce(false);
			        model.addAttribute("success", "Senha alterada com sucesso");
			        ret = "passwordmessage";
				}
				else
					model.addAttribute("error", "Senhas nao conferem");
			} else
				model.addAttribute("error", "Requisição expirada");
		} else
			model.addAttribute("error", "Requisição inválida");
	    return ret;
	}
}