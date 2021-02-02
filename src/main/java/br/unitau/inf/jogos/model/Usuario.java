package br.unitau.inf.jogos.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario implements UserDetails {
    private static final long serialVersionUID = 1L;
	
    @Id     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String secret;
    private LocalDateTime voto_data;
    private Integer voto_projeto;
    @Column(name = "resetpass_token")
    private String resetpassToken;
    @Column(name = "resetpass_limit")
    private LocalDateTime resetpassLimit;
    @Column(name = "resetpass_force")
    private boolean resetpassForce;
    private boolean bloqueado = false;
    @ManyToOne
    private Projeto projeto;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="usuarioperfil", joinColumns=
    {@JoinColumn(name="usuario_id")},inverseJoinColumns=
    {@JoinColumn(name="perfil_id")})
    private List<Perfil> perfis = new ArrayList<>();	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public LocalDateTime getVoto_data() {
		return voto_data;
	}
	public void setVoto_data(LocalDateTime voto_data) {
		this.voto_data = voto_data;
	}
	
	public Integer getVoto_projeto() {
		return voto_projeto;
	}
	public void setVoto_projeto(Integer voto_projeto) {
		this.voto_projeto = voto_projeto;
	}
	
	public String getResetpassToken() {
		return resetpassToken;
	}
	public void setResetpassToken(String resetpassToken) {
		this.resetpassToken = resetpassToken;
	}
	
	public LocalDateTime getResetpassLimit() {
		return resetpassLimit;
	}
	public void setResetpassLimit(LocalDateTime resetpassLimit) {
		this.resetpassLimit = resetpassLimit;
	}
	
	public boolean isResetpassForce() {
		return resetpassForce;
	}
	public void setResetpassForce(boolean resetpassForce) {
		this.resetpassForce = resetpassForce;
	}
	
	public boolean isBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	public Projeto getProjeto() {
		return projeto;
	}
	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}
	
	public List<Perfil> getPerfis() {
		return perfis;
	}
	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}
	
	public boolean hasProjeto() {
		return (projeto != null);
	}
	
	public boolean hasVoto() {
		return (voto_data != null);
	}
	
	public boolean checkProjeto(Integer projeto_id) {
		boolean ret = false;
		if ( projeto != null ) {
			if ( projeto.getId() == projeto_id) {
				ret = true;
			}
		}
		return  ret;
	}
	
	public boolean isDesenvolvedor() {
		boolean ret = false;
		for( Perfil perfil : getPerfis() ) {
			if ( perfil.getId() == Perfil.values.DESENVOLVEDOR.ordinal() ) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public String jwtSecret() {
		String ret;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		BigInteger hash = new BigInteger(1, md.digest(this.email.getBytes()));
		ret = hash.toString(16)+String.valueOf(this.id)+this.secret;
		return ret;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis;
	}
	
	@Override
	public String getUsername() {
		return this.email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return !isBloqueado();
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return !isResetpassForce();
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
}