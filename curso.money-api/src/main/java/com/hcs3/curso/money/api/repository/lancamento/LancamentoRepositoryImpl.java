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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

	@Override
	public Page<Lancamento> paginar(LancamentoFilter lancamentoFilter, Pageable page) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = criteriaBuilder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//Cria as Restrições
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter,criteriaBuilder,root);
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		
		adicionarRestricoesDePaginacao(query, page);
		
		return new PageImpl<Lancamento>(query.getResultList(), page, total(lancamentoFilter));
	}

	private long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		
		criteria.where(predicates);
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable page) {
		int paginaAtual = page.getPageNumber();
		int totalDeRegistroPorPagina = page.getPageSize();
		int registroAtual = paginaAtual*totalDeRegistroPorPagina;
		query.setFirstResult(registroAtual);
		query.setMaxResults(totalDeRegistroPorPagina);
	}
	
	
	
}
