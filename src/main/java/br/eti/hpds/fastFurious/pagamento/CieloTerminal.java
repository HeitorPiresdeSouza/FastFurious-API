package br.eti.hpds.fastFurious.pagamento;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Integração Remota da Cielo (Order Manager).
 * Fluxo: criar pedido (POST /orders) -> liberar para pagamento (PUT ?operation=PLACE)
 * -> cliente paga na maquininha -> consultar/receber notificação até status PAID.
 */
@Service
public class CieloTerminal implements PagamentoTerminal {

    private final RestClient http;

    public CieloTerminal(RestClient.Builder builder,
                         // Sandbox: https://api.cielo.com.br/sandbox-lio/order-management/v1
                         @Value("${cielo.base-url:https://api.cielo.com.br/order-management/v1}") String baseUrl,
                         @Value("${cielo.client-id}") String clientId,
                         @Value("${cielo.access-token}") String token,
                         @Value("${cielo.merchant-id}") String merchantId) {
        this.http = builder
                .baseUrl(baseUrl)
                .defaultHeader("client-id", clientId)
                .defaultHeader("access-token", token)
                .defaultHeader("merchant-id", merchantId)
                .build();
    }

    @Override
    public ResultadoPagamento cobrar(BigDecimal valor, TipoPagamento tipo, String referencia) {
        long centavos = valor.movePointRight(2).longValueExact(); // R$ 10,00 -> 1000

        Map<String, Object> pedido = new HashMap<>();
        pedido.put("number", referencia);          // id do pedido no SEU sistema
        pedido.put("reference", "PED-" + referencia);
        pedido.put("status", "DRAFT");             // padrão da API; PLACE é feito em seguida
        pedido.put("price", centavos);             // número, em centavos (como no exemplo da Cielo)
        pedido.put("items", List.of(Map.of(
                "sku", referencia,
                "name", "Pedido " + referencia,
                "unit_price", centavos,
                "quantity", 1,
                "unit_of_measure", "EACH")));
        pedido.put("transactions", List.of());     // obrigatório no corpo, mas vazio na criação

        // payment_code define a forma de pagamento já na criação do pedido.
        // Sem ele, o operador escolhe na maquininha. Só funciona nos terminais da Nova Smart
        // (L300 V4, L400, DX800); na L300 V3 (LIO) payment_code e installments são ignorados.
        String paymentCode = paymentCode(tipo);
        if (paymentCode != null) {
            pedido.put("payment_code", paymentCode);
        }
        // installments: "0" = à vista. Débito e crédito à vista usam 0.
        if (tipo == TipoPagamento.DEBITO || tipo == TipoPagamento.CREDITO) {
            pedido.put("installments", "0");
        }

        // 1) Cria o pedido (resposta 201). Assumo que o JSON traz o "id" (UUID): a doc
        //    não mostrou o exemplo de resposta, então confira no "Try It" do sandbox.
        Map<String, Object> criado = http.post().uri("/orders").body(pedido)
                .retrieve().body(new ParameterizedTypeReference<Map<String, Object>>() {});
        String id = String.valueOf(criado.get("id"));

        // 2) PLACE: libera o pedido para pagamento, exibindo-o na Cielo Smart.
        http.put().uri("/orders/{id}?operation=PLACE", id).retrieve().toBodilessEntity();

        return new ResultadoPagamento(id, "PENDENTE");
    }


    /**
     * Traduz o seu enum para o payment_code da Cielo.
     * ATENÇÃO: a documentação de Entidades não lista os valores aceitos. Os códigos abaixo
     * seguem o padrão do SDK da Cielo LIO e PRECISAM ser confirmados na página
     * "Criar um pedido". Ajuste os cases aos nomes do seu enum.
     * Retornar null deixa a escolha para o operador na maquininha.
     */
    private String paymentCode(TipoPagamento tipo) {
        if (tipo == null) return null;
        return switch (tipo) {
            case DEBITO  -> "DEBITO_AVISTA";
            case CREDITO -> "CREDITO_AVISTA";   // à vista; parcelado seria outro código
            case PIX     -> "PIX";
        };
    }

    /** Consulta o pedido pelo seu número. Use para confirmar PAID sem confiar só em notificação. */
    public String consultarPedido(String numero) {
        return http.get().uri("/orders?number={n}", numero).retrieve().body(String.class);
    }

    @Override
    public void cancelar(String transacaoId) {
        // O endpoint "Alterar status" só aceita PLACE, PAY e CLOSE: não cancela.
        // Falta descobrir com a Cielo como cancelar/estornar.
        throw new UnsupportedOperationException("Cancelamento ainda não implementado");
    }
}