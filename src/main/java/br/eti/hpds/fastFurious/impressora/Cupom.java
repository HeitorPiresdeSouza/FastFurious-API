package br.eti.hpds.fastFurious.impressora;

import java.math.BigDecimal;
import java.util.List;

public record Cupom(String lojaId, String numero, List<Item> itens) {
    public record Item(String descricao, BigDecimal valor) { }
}