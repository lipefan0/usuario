package br.com.upvisibility.usuario.infrastructure.repository;

import br.com.upvisibility.usuario.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
