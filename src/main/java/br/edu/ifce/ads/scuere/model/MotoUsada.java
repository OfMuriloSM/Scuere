package br.edu.ifce.ads.scuere.model;

public class MotoUsada extends Veiculo {

    public MotoUsada(String id, double precoBase, String chassi, String marca, String modelo, int ano, double quilometragem) {
        super(id, precoBase, chassi, marca, modelo, ano, quilometragem);
    }

    @Override
    public double calcularValorFinal() {
        // Depreciação proporcional à quilometragem (mínimo 50% do preço base)
        double fatorDepreciacao = Math.max(0.50, 1.0 - (getQuilometragem() / 500_000.0));
        return getPrecoBase() * fatorDepreciacao;
    }

    @Override
    public String gerarDadosRelatorio() {
        return "Moto Usada - Chassi: " + getChassi()
                + " | KM: " + getQuilometragem()
                + " | Valor Final: R$ " + String.format("%.2f", calcularValorFinal());
    }
}
