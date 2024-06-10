package com.ntt.client.repository;

import java.util.Optional;
import com.ntt.client.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteRepository extends JpaRepository<Cliente,Long> {

    Optional<Cliente> findByIdentificacion(String identification);

    void deleteByIdentificacion(String identification);

    boolean existsByIdentificacion(String identification);

}
