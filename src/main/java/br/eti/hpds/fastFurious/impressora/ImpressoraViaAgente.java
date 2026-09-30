package br.eti.hpds.fastFurious.impressora;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Como o Spring está na nuvem, ele não alcança a impressora da loja.
 * Aqui ele publica o cupom num tópico WebSocket e um agente local
 * (rodando no PC do caixa) recebe e imprime.
 * Exige spring-boot-starter-websocket e a configuração do broker STOMP.
 */
@Service
@ConditionalOnProperty(name = "impressora.modo", havingValue = "agente")
public class ImpressoraViaAgente implements ImpressoraCupom {

    private final SimpMessagingTemplate ws;

    public ImpressoraViaAgente(SimpMessagingTemplate ws) { this.ws = ws; }

    @Override
    public void imprimir(Cupom cupom) {
        ws.convertAndSend("/topic/impressao/" + cupom.lojaId(), cupom);
    }
}