package br.edu.ifce.ads.scuere.model;

import br.edu.ifce.ads.scuere.interfaces.RelatorioGerencial;
import br.edu.ifce.ads.scuere.interfaces.Tributavel;
import java.util.ArrayList;
import java.util.List;

public class ServicoOficina implements Tributavel, RelatorioGerencial {
    private String descricaoProblema;
    private double valorMaoDeObra;
    private List<Peca> pecasUsadas;

    public ServicoOficina(String descricaoProblema, double valorMaoDeObra) {
        this.descricaoProblema = descricaoProblema;
        this.valorMaoDeObra = valorMaoDeObra;
        this.pecasUsadas = new ArrayList<>();
    }

    public void adicionarPeca(Peca peca) {
        this.pecasUsadas.add(peca);
    }

    public double calcularValorTotal() {
        double totalPecas = 0;
        for (Peca p : pecasUsadas) {
            totalPecas += p.calcularValorFinal();
        }
        return valorMaoDeObra + totalPecas + calcularImposto();
    }

    @Override
    public double calcularImposto() {
        return valorMaoDeObra * 0.05;
    }

    @Override
    public String gerarDadosRelatorio() {
        return "Serviço de Oficina - " + descricaoProblema + " | Valor Total: R$ " + calcularValorTotal();
    }
}