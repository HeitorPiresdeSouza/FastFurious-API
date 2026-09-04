package br.eti.hpds.fastFurious.service;

import br.eti.hpds.fastFurious.domain.model.Pedido;
import br.eti.hpds.fastFurious.domain.model.StatusPedido;
import br.eti.hpds.fastFurious.domain.repository.PedidoRepository;
import br.eti.hpds.fastFurious.exceptionhandler.ProblemaException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    public Optional<Pedido> atualizarStatus(Long id, StatusPedido novoStatus) {

        Optional<Pedido> optPedido = pedidoRepository.findById(id);

        if (optPedido.isEmpty()) {
            return optPedido;
        }

        Pedido pedidoAntigo = optPedido.get();

        // ABERTO PRONTO ENTREGUE CANCELADO 
        if (novoStatus == StatusPedido.PRONTO && pedidoAntigo.getStatus() == StatusPedido.ABERTO) {
            // Só pode mudar para pronto quando EM ABERTO
            pedidoAntigo.setStatus(StatusPedido.PRONTO);
            pedidoAntigo.setDataPronto(LocalDateTime.now());

        } else if (novoStatus == StatusPedido.ENTREGUE && pedidoAntigo.getStatus() == StatusPedido.PRONTO) {
            // Só pode mudar para entregue quando EM PRONTO
            pedidoAntigo.setStatus(StatusPedido.ENTREGUE);
            pedidoAntigo.setDataEntregue(LocalDateTime.now());

        } else if (novoStatus == StatusPedido.CANCELADO && pedidoAntigo.getStatus() != StatusPedido.ENTREGUE) {
            // Só pode mudar para cancelado se não estiver EM ENTREGUE
            pedidoAntigo.setStatus(StatusPedido.CANCELADO);
            pedidoAntigo.setDataCancelado(LocalDateTime.now());

        } else {
            throw new RuntimeException("Status " + novoStatus + " não pode ser aplicado em " + pedidoAntigo.getStatus().name());
        }

        optPedido = Optional.of(pedidoRepository.save(pedidoAntigo));
        return optPedido;
    }

    public void excluir(Long pedidoID) {
        pedidoRepository.deleteById(pedidoID);
    }

//    public Pedido criar(Pedido pedido) {
//        pedido.setDataAbertura(LocalDateTime.now());
//        pedido.setStatus(StatusPedido.ABERTO);
//        return pedidoRepository.save(pedido);
//    }
//      
    public Pedido criar(Pedido pedido) {
        pedido.setDataAbertura(LocalDateTime.now());
        pedido.setStatus(StatusPedido.ABERTO);

        // Vínculo bidirecional: resolve o TransientPropertyValueException
        if (pedido.getListaItens() != null) {
            pedido.getListaItens().forEach(item -> item.setPedido(pedido));
        }

        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> salvar(Pedido pedidoNovo) {

        Optional<Pedido> optPedidoExistente = pedidoRepository.findById(pedidoNovo.getId());
        
        if (!optPedidoExistente.isPresent()) {
            return Optional.empty();
        }

        Pedido pedidoExistente = optPedidoExistente.get();
        
        return Optional.of(pedidoRepository.save(pedidoExistente));

    }

}
