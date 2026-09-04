package br.eti.hpds.fastFurious.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime dataAbertura;
    
    private LocalDateTime dataCancelado;
    
    private LocalDateTime dataPronto;
    
    private LocalDateTime dataEntregue;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ItemPedido> listaItens;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoConsumo consumo;

    public Pedido() {
    }

    public Pedido(Long id, LocalDateTime dataAbertura, LocalDateTime dataCancelado, LocalDateTime dataPronto, LocalDateTime dataEntregue, List<ItemPedido> listaItens, StatusPedido status, TipoConsumo consumo) {
        this.id = id;
        this.dataAbertura = dataAbertura;
        this.dataCancelado = dataCancelado;
        this.dataPronto = dataPronto;
        this.dataEntregue = dataEntregue;
        this.listaItens = listaItens;
        this.status = status;
        this.consumo = consumo;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataCancelado() {
        return dataCancelado;
    }

    public void setDataCancelado(LocalDateTime dataCancelado) {
        this.dataCancelado = dataCancelado;
    }


    public List<ItemPedido> getListaItens() {
        return listaItens;
    }

    public void setListaItens(List<ItemPedido> listaItens) {
        this.listaItens = listaItens;
    }


    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDateTime getDataPronto() {
        return dataPronto;
    }

    public void setDataPronto(LocalDateTime dataPronto) {
        this.dataPronto = dataPronto;
    }

    public LocalDateTime getDataEntregue() {
        return dataEntregue;
    }

    public void setDataEntregue(LocalDateTime dataEntregue) {
        this.dataEntregue = dataEntregue;
    }

    public TipoConsumo getConsumo() {
        return consumo;
    }

    public void setConsumo(TipoConsumo consumo) {
        this.consumo = consumo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.dataAbertura);
        hash = 37 * hash + Objects.hashCode(this.dataCancelado);
        hash = 37 * hash + Objects.hashCode(this.dataPronto);
        hash = 37 * hash + Objects.hashCode(this.dataEntregue);
        hash = 37 * hash + Objects.hashCode(this.listaItens);
        hash = 37 * hash + Objects.hashCode(this.status);
        hash = 37 * hash + Objects.hashCode(this.consumo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pedido other = (Pedido) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dataAbertura, other.dataAbertura)) {
            return false;
        }
        if (!Objects.equals(this.dataCancelado, other.dataCancelado)) {
            return false;
        }
        if (!Objects.equals(this.dataPronto, other.dataPronto)) {
            return false;
        }
        if (!Objects.equals(this.dataEntregue, other.dataEntregue)) {
            return false;
        }
        if (!Objects.equals(this.listaItens, other.listaItens)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        return this.consumo == other.consumo;
    }    
}
