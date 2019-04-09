package com.hcs3.curso.money.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcs3.curso.money.api.model.Lancamento;
import com.hcs3.curso.money.api.repository.filter.LancamentoFilter;
import com.hcs3.curso.money.api.repository.lancamento.LacamentoRepositoryQuery;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LacamentoRepositoryQuery{


}
