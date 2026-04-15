package br.eti.hpds.fastFurious.controller;

import br.eti.hpds.fastFurious.domain.model.Produto;
import br.eti.hpds.fastFurious.domain.repository.ProdutoRepository;
import br.eti.hpds.fastFurious.service.ProdutoService;
import jakarta.validation.Valid;
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
public class ProdutoController {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
   @Autowired
   private ProdutoService produtoService;
    
    @GetMapping("/produto")
    public List<Produto> listar() {
        return produtoRepository.findAll();
    }
    
    @GetMapping("/produto/{produtoID}")
    public ResponseEntity<Produto> buscar (@PathVariable Long produtoID) {
        
        Optional<Produto> produto = produtoRepository.findById(produtoID);
        
        if(produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/produto")
    @ResponseStatus(HttpStatus.CREATED)
    public Produto adicionar(@Valid @RequestBody Produto produto){
        
        return produtoService.criar(produto);
    }
    
    @PutMapping("/produto/{produtoID}")
    public ResponseEntity<Produto> atualizar(@Valid @PathVariable Long produtoID,
            @RequestBody Produto produto) {
        
        
        if(!produtoRepository.existsById(produtoID)){
            return ResponseEntity.notFound().build();
        }
        
        produto.setId(produtoID);
        produto = produtoService.criar(produto);
        return ResponseEntity.ok(produto);
    }
    
    @DeleteMapping("/produto/{produtoID}")
    public ResponseEntity<Void> excluir(@PathVariable Long produtoID){
        
        if (!produtoRepository.existsById(produtoID)){
            return ResponseEntity.notFound().build();
        }
        
        produtoService.excluir(produtoID);
        return ResponseEntity.noContent().build();
    }
}