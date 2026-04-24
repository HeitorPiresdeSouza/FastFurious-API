package br.eti.hpds.fastFurious.domain.dto;

import br.eti.hpds.fastFurious.domain.model.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizaStatusDTO(
        
        @NotNull (message = "Status é obrigatório")
        StatusPedido status
        ) {

}
