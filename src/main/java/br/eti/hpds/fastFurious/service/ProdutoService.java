package br.eti.hpds.fastFurious.service;

import br.eti.hpds.fastFurious.domain.model.Produto;
import br.eti.hpds.fastFurious.domain.repository.ProdutoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    
    public Produto criar (Produto produto) {
        return produtoRepository.save(produto);
    }
    
    public void excluir (Long produtoID){
        produtoRepository.deleteById(produtoID);
    }
}
