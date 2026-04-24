package br.eti.hpds.fastFurious.domain.repository;

import br.eti.hpds.fastFurious.domain.model.Pedido;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    
    List<Pedido> findByDataAbertura (LocalDateTime dataAbertura);
    List<Pedido> findByCpf (String cpf);
}
