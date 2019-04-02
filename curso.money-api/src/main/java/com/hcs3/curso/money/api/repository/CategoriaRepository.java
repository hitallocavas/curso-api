package com.hcs3.curso.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcs3.curso.money.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
