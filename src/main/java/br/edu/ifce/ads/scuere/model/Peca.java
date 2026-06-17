package br.edu.ifce.ads.scuere.model;

public class Peca extends ItemComerciavel {
    private String modeloCompativel;

    public Peca(String id, double precoBase, String modeloCompativel) {
        super(id, precoBase);
        this.modeloCompativel = modeloCompativel;
    }

    @Override
    public double calcularValorFinal() {
        return getPrecoBase() * 1.30;
    }

    @Override
    public String gerarDadosRelatorio() {
        return "Peça (" + modeloCompativel + ") - ID: " + getId() + " | Valor de Venda: R$ " + calcularValorFinal();
    }
}