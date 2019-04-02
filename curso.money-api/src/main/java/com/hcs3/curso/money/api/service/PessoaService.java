package com.hcs3.curso.money.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.hcs3.curso.money.api.model.Pessoa;
import com.hcs3.curso.money.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = this.pessoaRepository.findOne(codigo);
		if(pessoaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		else {
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
		return this.pessoaRepository.save(pessoaSalva);
		
		}
		
	}
	

}
