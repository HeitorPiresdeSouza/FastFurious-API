package br.eti.hpds.fastFurious.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoConsumo {
    LOCAL, LEVAR, ENTREGA;
    
    @JsonCreator
    public static TipoConsumo fromString(String valor) {
        for (TipoConsumo tipo : TipoConsumo.values()) {
            if (tipo.name().equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de consumo inválido: " + valor);
    }
}
