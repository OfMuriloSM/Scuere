package br.edu.ifce.ads.scuere.service;

import br.edu.ifce.ads.scuere.exceptions.UsuarioNaoEncontradoException;
import br.edu.ifce.ads.scuere.model.Usuario;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorUsuarios {

    private Map<String, Usuario> cacheUsuarios;

    public GerenciadorUsuarios() {
        this.cacheUsuarios = new HashMap<>();
    }

    public void cadastrarUsuario(Usuario usuario) {
        cacheUsuarios.put(usuario.getCpf(), usuario);
    }

    public Usuario buscarPorCpf(String cpf) throws UsuarioNaoEncontradoException {
        Usuario usuario = cacheUsuarios.get(cpf);

        if (usuario == null) {
            throw new UsuarioNaoEncontradoException(cpf);
        }

        return usuario;
    }
}