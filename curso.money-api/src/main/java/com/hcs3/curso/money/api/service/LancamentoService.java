package com.hcs3.curso.money.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcs3.curso.money.api.model.Lancamento;
import com.hcs3.curso.money.api.repository.LancamentoRepository;
import com.hcs3.curso.money.api.repository.PessoaRepository;
import com.hcs3.curso.money.api.service.exceptions.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	
	public Lancamento salvar(Lancamento lancamento) {
		
		if(lancamento.getPessoa().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return this.lancamentoRepository.save(lancamento);
		
		
	}
	
}
