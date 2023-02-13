package br.com.food.pagamentos.service;


import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.food.pagamentos.enums.StatusEnum;
import br.com.food.pagamentos.http.PedidoClient;
import br.com.food.pagamentos.model.PagamentoModel;
import br.com.food.pagamentos.model.dto.PagamentoDTO;
import br.com.food.pagamentos.repository.PagamentoRepository;


@Service
public class PagamentoService {

	
	@Autowired
	private PagamentoRepository pagamentoRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PedidoClient pedido;
	
	public Page<PagamentoDTO> obterTodos(Pageable paginacao) {
        return pagamentoRepo
                .findAll(paginacao)
                .map(p -> modelMapper.map(p, PagamentoDTO.class));
    }
	
	public PagamentoDTO obterPorId(Long id) {
        PagamentoModel pagamento = pagamentoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }
	
	
	public PagamentoDTO criarPagamento(PagamentoDTO dto) {
		PagamentoModel pagamento = modelMapper.map(dto, PagamentoModel.class);
        pagamento.setStatus(StatusEnum.CRIADO);
        pagamentoRepo.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }
	
	public PagamentoDTO atualizarPagamento(Long id, PagamentoDTO dto) {
		PagamentoModel pagamento = modelMapper.map(dto, PagamentoModel.class);
        pagamento.setId(id);
        pagamento = pagamentoRepo.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDTO.class);
    }
	
	public void excluirPagamento(Long id) {
		pagamentoRepo.deleteById(id);
    }
	
	
	   public void confirmarPagamento(Long id){
	        Optional<PagamentoModel> pagamento = pagamentoRepo.findById(id);

	        if (!pagamento.isPresent()) {
	            throw new EntityNotFoundException();
	        }

	        pagamento.get().setStatus(StatusEnum.CONFIRMADO);
	        pagamentoRepo.save(pagamento.get());
	        pedido.atualizaPagamento(pagamento.get().getPedidoId());
	    }
	
	
}
