package br.eti.hpds.fastFurious.impressora;

import java.io.IOException;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;

/**
 * Imprime abrindo um socket TCP (porta 9100) direto na impressora.
 * Só funciona se o Spring estiver na mesma rede que a impressora.
 */
@Service
@ConditionalOnProperty(name = "impressora.modo", havingValue = "rede")
public class ImpressoraRede implements ImpressoraCupom {

    @Value("${impressora.host}") private String host;
    @Value("${impressora.porta:9100}") private int porta;

    @Override
    public void imprimir(Cupom cupom) {
        try (Socket socket = new Socket(host, porta);
             EscPos escpos = new EscPos(socket.getOutputStream())) {

            Style titulo = new Style().setBold(true)
                    .setJustification(EscPosConst.Justification.Center);

            escpos.writeLF(titulo, "FAST FURIOUS");
            escpos.writeLF("Pedido: " + cupom.numero());

            // for em vez de forEach: os métodos do escpos lançam IOException,
            // que não pode ser propagada de dentro de uma lambda
            for (Cupom.Item i : cupom.itens()) {
                escpos.writeLF(i.descricao() + "  R$ " + i.valor());
            }

            escpos.feed(3).cut(EscPos.CutMode.FULL);

        } catch (IOException e) {
            throw new IllegalStateException("Falha ao imprimir", e);
        }
    }
}
