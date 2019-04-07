package com.hcs3.curso.money.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.hcs3.curso.money.api.model.Lancamento;
import com.hcs3.curso.money.api.model.Lancamento_;
import com.hcs3.curso.money.api.repository.LancamentoRepository;
import com.hcs3.curso.money.api.repository.filter.LancamentoFilter;

import antlr.StringUtils;

public class LancamentoRepositoryImpl implements LacamentoRepositoryQuery{

	@PersistenceContext
	EntityManager manager;

	@Override
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = criteriaBuilder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//Cria as Restrições
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter,criteriaBuilder,root);
		criteria.where(predicates);
		
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		return query.getResultList();
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder criteriaBuilder,
			Root<Lancamento> root) {
		
			List<Predicate> predicates = new ArrayList<>();
		
			if(lancamentoFilter.getDescricao() != null) {
				
				predicates.add(criteriaBuilder.like(
						criteriaBuilder.lower(
							root.get(Lancamento_.descricao)
						), "%" + lancamentoFilter.getDescricao().toLowerCase()+ "%"));
			}
			if(lancamentoFilter.getDataVencimentoAte() != null) {
				predicates.add(
						criteriaBuilder.lessThan(
							
							root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()
								
						)
					);
			}
			if(lancamentoFilter.getDataVencimentoDe() != null) {
				predicates.add(
					criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe())
						);
			}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	
	
}
