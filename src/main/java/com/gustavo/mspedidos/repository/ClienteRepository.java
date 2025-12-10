package com.gustavo.mspedidos.repository;

import com.gustavo.mspedidos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> { }
