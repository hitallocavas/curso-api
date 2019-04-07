package com.hcs3.curso.money.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hcs3.curso.money.api.eventos.RecursoCriadoEvent;
import com.hcs3.curso.money.api.exceptionhandler.Erro;
import com.hcs3.curso.money.api.model.Lancamento;
import com.hcs3.curso.money.api.repository.LancamentoRepository;
import com.hcs3.curso.money.api.repository.filter.LancamentoFilter;
import com.hcs3.curso.money.api.service.LancamentoService;
import com.hcs3.curso.money.api.service.exceptions.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LacamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;
	
	
	@GetMapping
	public List<Lancamento> listar(LancamentoFilter lancamentoFilter){
		return lancamentoRepository.filtrar(lancamentoFilter);
		
	}
	
	@GetMapping("/filtrar")
	public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter){
		return lancamentoRepository.filtrar(lancamentoFilter);
		
	}
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<Object> remover(@PathVariable Long codigo){
		Lancamento lancamentoToRemove = this.getLancamento(codigo);
		this.lancamentoRepository.delete(lancamentoToRemove);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lancamento removido com sucesso");
	}
	
	
	@GetMapping("/{codigo}")
	public Lancamento getLancamento(@PathVariable Long codigo){
		return this.lancamentoRepository.findOne(codigo);
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response){
		
		Lancamento lancamentoSalvo = this.lancamentoService.salvar(lancamento);
		this.publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
		
	}
	
	@ExceptionHandler({PessoaInexistenteOuInativaException.class})
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
		String mensagemUsuario = messageSource.getMessage("pessoa.inativa-ou-inexistente", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario,mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
	
	
	
}
