package br.edu.ifce.ads.scuere.exceptions;

public class VeiculoJaCadastradoException extends Exception {
    public VeiculoJaCadastradoException(String chassi) {
        super("Erro: O veículo com chassi " + chassi + " já está cadastrado no sistema.");
    }
}