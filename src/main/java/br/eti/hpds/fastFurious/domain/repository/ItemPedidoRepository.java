package br.eti.hpds.fastFurious.domain.repository;

import br.eti.hpds.fastFurious.domain.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository <ItemPedido, Long>{
    
}
