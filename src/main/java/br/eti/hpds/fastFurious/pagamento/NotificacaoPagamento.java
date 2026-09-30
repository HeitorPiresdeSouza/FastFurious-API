package br.eti.hpds.fastFurious.pagamento;

/** Corpo do webhook. Ajuste os campos ao formato real da adquirente. */
public record NotificacaoPagamento(String referencia, String status) { }