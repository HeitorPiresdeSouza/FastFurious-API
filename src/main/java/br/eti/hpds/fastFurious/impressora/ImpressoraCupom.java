package br.eti.hpds.fastFurious.impressora;

/** Abstração da impressora. Troque a implementação sem mexer no resto do sistema. */
public interface ImpressoraCupom {
    void imprimir(Cupom cupom);
}