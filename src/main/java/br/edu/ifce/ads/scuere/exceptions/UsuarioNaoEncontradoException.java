package br.edu.ifce.ads.scuere.exceptions;

public class UsuarioNaoEncontradoException extends Exception {
    public UsuarioNaoEncontradoException(String cpf) {
        super("Aviso: Nenhum usuário encontrado com o CPF: " + cpf + ". Redirecionando para cadastro...");
    }
}