package br.edu.ifce.ads.scuere.model;

public abstract class Veiculo extends ItemComerciavel {
    private String chassi;
    private String marca;
    private String modelo;
    private int ano;
    private double quilometragem;

    public Veiculo(String id, double precoBase, String chassi, String marca, String modelo, int ano, double quilometragem) {
        super(id, precoBase);
        this.chassi = chassi;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        setQuilometragem(quilometragem);
    }

    public double getQuilometragem() { return quilometragem; }

    public void setQuilometragem(double quilometragem) {
        if (quilometragem < 0) {
            throw new IllegalArgumentException("A quilometragem não pode ser negativa.");
        }
        this.quilometragem = quilometragem;
    }

    public String getChassi() { return chassi; }
    public void setChassi(String chassi) { this.chassi = chassi; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
}