package br.eti.hpds.fastFurious.pagamento;

import java.math.BigDecimal;

/**
 * Abstração da maquininha. O resto do sistema só conhece esta interface,
 * então trocar de adquirente significa criar outra implementação.
 */
public interface PagamentoTerminal {
    ResultadoPagamento cobrar(BigDecimal valor, TipoPagamento tipo, String referencia);
    void cancelar(String transacaoId);
}