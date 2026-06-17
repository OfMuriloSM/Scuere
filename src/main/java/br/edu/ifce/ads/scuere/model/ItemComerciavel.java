package br.edu.ifce.ads.scuere.model;

import br.edu.ifce.ads.scuere.interfaces.RelatorioGerencial;

public abstract class ItemComerciavel implements RelatorioGerencial {
    private String id;
    private double precoBase;

    public ItemComerciavel(String id, double precoBase) {
        this.id = id;
        setPrecoBase(precoBase);
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public double getPrecoBase() { return precoBase; }

    public void setPrecoBase(double precoBase) {
        if (precoBase < 0) {
            throw new IllegalArgumentException("O preço base não pode ser negativo."); // Uso de exceção nativa [cite: 16]
        }
        this.precoBase = precoBase;
    }

    public abstract double calcularValorFinal();
}