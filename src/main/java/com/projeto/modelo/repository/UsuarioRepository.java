package com.projeto.modelo.repository;



import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.UsuarioStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    
    @Query("SELECT u FROM Usuario u WHERE " +
           "(:status IS NULL OR u.status = :status) AND " +
           "(LOWER(u.nome) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.cargo) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Usuario> buscarPorTermoEStatus(@Param("search") String search, @Param("status") UsuarioStatus status, Pageable pageable);
    
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailAndCodigoTrocaSenha(String email, Integer codigo);

    Optional<Usuario> findByEmailAuthsoft(String emailAuthsoft);

    Optional<Usuario> findByEmailAndStatus(String email, UsuarioStatus status);

}
