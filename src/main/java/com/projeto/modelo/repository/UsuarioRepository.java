package com.projeto.modelo.repository;



import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.UsuarioStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailAndCodigoTrocaSenha(String email, Integer codigo);

    Optional<Usuario> findByEmailAuthsoft(String emailAuthsoft);

    Optional<Usuario> findByEmailAndStatus(String email, UsuarioStatus status);

}
