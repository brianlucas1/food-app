package br.com.food.pagamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.food.pagamentos.model.PagamentoModel;

public interface PagamentoRepository extends JpaRepository<PagamentoModel, Long>{

	
	
}
