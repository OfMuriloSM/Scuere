package br.edu.ifce.ads.scuere.factory;

import br.edu.ifce.ads.scuere.model.ItemComerciavel;
import br.edu.ifce.ads.scuere.model.MotoNova;
import br.edu.ifce.ads.scuere.model.Peca;

public class ItemFactory {

    public static ItemComerciavel criarItem(String tipo, String id, double precoBase, String[] dadosExtras) {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("O tipo de item não pode ser nulo.");
        }

        return switch (tipo.toUpperCase()) {
            case "MOTONOVA" -> new MotoNova(id, precoBase, dadosExtras[0], dadosExtras[1], dadosExtras[2], Integer.parseInt(dadosExtras[3]));
            case "PECA" -> new Peca(id, precoBase, dadosExtras[0]);
            default -> throw new IllegalArgumentException("Tipo de item desconhecido: " + tipo);
        };
    }
}