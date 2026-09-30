package br.eti.hpds.fastFurious.pagamento;

/** Retorno de uma cobrança. No fluxo por API o resultado final chega depois (webhook/consulta). */
public record ResultadoPagamento(String transacaoId, String status) { }