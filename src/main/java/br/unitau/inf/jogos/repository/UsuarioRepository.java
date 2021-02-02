package br.unitau.inf.jogos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.unitau.inf.jogos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByResetpassToken(String token);
    List<Usuario> findByProjeto_idAndBloqueadoFalseOrderByNameAsc(Integer projeto_id);
    @Query(value = "SELECT * FROM usuario WHERE NOT bloqueado "
    		+"AND projeto_id IS NULL AND "
    		+"EXISTS ( SELECT * FROM usuarioperfil WHERE usuario_id=usuario.id "
    		+"         AND perfil_id='1') ORDER BY name",nativeQuery = true)
    List<Usuario> findFree();
    @Query(value = "SELECT COUNT(*) FROM usuario WHERE NOT bloqueado AND "
    		+"EXISTS ( SELECT * FROM usuarioperfil WHERE usuario_id=usuario.id "
    		+ "         AND perfil_id='2')",nativeQuery = true)
    long countVotantes();
    @Query("SELECT u.voto_projeto AS projeto, COUNT(*) AS total FROM Usuario AS u "
    		+"WHERE u.voto_data IS NOT NULL "
    		+"GROUP BY u.voto_projeto ORDER BY u.voto_projeto")
    List<Object[]> countVotos();
}