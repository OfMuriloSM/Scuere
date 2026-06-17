package br.edu.ifce.ads.scuere.model;

import br.edu.ifce.ads.scuere.interfaces.Tributavel;

public class MotoNova extends Veiculo implements Tributavel {

    public MotoNova(String id, double precoBase, String chassi, String marca, String modelo, int ano) {
        super(id, precoBase, chassi, marca, modelo, ano, 0.0); // Moto nova sempre tem 0 km
    }

    @Override
    public double calcularValorFinal() {
        return getPrecoBase() + calcularImposto();
    }

    @Override
    public double calcularImposto() {
        return getPrecoBase() * 0.15; // 15% de imposto para motos novas
    }

    @Override
    public String gerarDadosRelatorio() {
        return "Moto Nova - Chassi: " + getChassi() + " | Valor Final: R$ " + calcularValorFinal();
    }

    
}