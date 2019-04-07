package com.hcs3.curso.money.api.repository.lancamento;

import java.util.List;

import com.hcs3.curso.money.api.model.Lancamento;
import com.hcs3.curso.money.api.repository.filter.LancamentoFilter;

public interface LacamentoRepositoryQuery {

	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
	
}
