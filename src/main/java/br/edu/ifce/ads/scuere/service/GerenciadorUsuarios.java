package br.edu.ifce.ads.scuere.service;

import br.edu.ifce.ads.scuere.exceptions.UsuarioNaoEncontradoException;
import br.edu.ifce.ads.scuere.model.Usuario;
import br.edu.ifce.ads.scuere.dao.UsuarioDAO;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorUsuarios {

    private Map<String, Usuario> cacheUsuarios;
    private UsuarioDAO usuarioDAO;

    public GerenciadorUsuarios() {
        this.cacheUsuarios = new HashMap<>();
        this.usuarioDAO = new UsuarioDAO();
    }

    public void cadastrarUsuario(Usuario usuario) {
        cacheUsuarios.put(usuario.getCpf(), usuario);
        usuarioDAO.salvar(usuario);
    }

    public Usuario buscarPorCpf(String cpf) throws UsuarioNaoEncontradoException {
        if (cacheUsuarios.containsKey(cpf)) {
            System.out.println("Usuário encontrado no Cache (Memória).");
            return cacheUsuarios.get(cpf);
        }

        Usuario usuarioDB = usuarioDAO.buscarPorCpfDB(cpf);
        if (usuarioDB != null) {
            System.out.println("Usuário recuperado do Banco de Dados. Adicionando ao Cache.");
            cacheUsuarios.put(cpf, usuarioDB);
            return usuarioDB;
        }

        throw new UsuarioNaoEncontradoException(cpf);
    }
}