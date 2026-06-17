package br.edu.ifce.ads.scuere.exceptions;

public class EstoqueInsuficienteException extends Exception {
    public EstoqueInsuficienteException(String idItem) {
        super("Operação negada: O item " + idItem + " não possui unidades disponíveis no estoque.");
    }
}