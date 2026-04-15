package br.eti.hpds.fastFurious.domain.repository;

import br.eti.hpds.fastFurious.domain.model.Produto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository <Produto, Long> {
    
    List<Produto> findByName (String name);
    List<Produto> findByNameContaining (String name);
    
}
