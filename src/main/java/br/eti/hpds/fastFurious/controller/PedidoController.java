package br.eti.hpds.fastFurious.controller;

import br.eti.hpds.fastFurious.domain.dto.AtualizaStatusDTO;
import br.eti.hpds.fastFurious.domain.model.Pedido;
import br.eti.hpds.fastFurious.domain.model.StatusPedido;
import br.eti.hpds.fastFurious.domain.repository.PedidoRepository;
import br.eti.hpds.fastFurious.service.PedidoService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/pedido")
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/pedido/{pedidoID}")
    public ResponseEntity<Pedido> buscarById(@PathVariable Long pedidoID) {

        Optional<Pedido> pedido = pedidoRepository.findById(pedidoID);

        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/pedido")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido criar(@Valid @RequestBody Pedido pedido) {

        return pedidoService.criar(pedido);
    }

    @PutMapping("/pedido/atualizaStatus/{pedidoID}")
    public ResponseEntity<Pedido> atualizarStatus(@Valid @PathVariable Long pedidoID,
            @RequestBody AtualizaStatusDTO atualizaStatusDTO) {

        Optional<Pedido> optPedido = pedidoService.atualizarStatus(pedidoID, atualizaStatusDTO.status());

        if (optPedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optPedido.get());

    }

    @PutMapping("/pedido/{pedidoID}")
    public ResponseEntity<Pedido> atualizar(@Valid @PathVariable Long pedidoID,
            @RequestBody Pedido pedido) {

        if (!pedidoRepository.existsById(pedidoID)) {
            return ResponseEntity.notFound().build();
        }

        pedido.setId(pedidoID);
        Optional<Pedido> optPedido = pedidoService.salvar(pedido);
        if (optPedido.isPresent()) {
            return ResponseEntity.ok(optPedido.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/pedido/{pedidoID}")
    public ResponseEntity<Void> excluir(@PathVariable Long pedidoID) {
        if (!pedidoRepository.existsById(pedidoID)) {
            return ResponseEntity.notFound().build();
        }

        pedidoService.excluir(pedidoID);
        return ResponseEntity.noContent().build();
    }

}
