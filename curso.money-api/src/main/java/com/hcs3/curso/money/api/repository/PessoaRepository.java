package com.hcs3.curso.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcs3.curso.money.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
