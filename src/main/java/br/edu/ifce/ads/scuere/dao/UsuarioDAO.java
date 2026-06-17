package br.edu.ifce.ads.scuere.dao;

import br.edu.ifce.ads.scuere.model.Usuario;
import br.edu.ifce.ads.scuere.database.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios(cpf, nome, endereco) VALUES(?,?,?)";


        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, usuario.getCpf());
            pstmt.setString(2, usuario.getNome());
            pstmt.setString(3, usuario.getEndereco());

            pstmt.executeUpdate();
            System.out.println("Usuário " + usuario.getNome() + " salvo no banco de dados!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar usuário no banco: " + e.getMessage());
        }
    }

    public Usuario buscarPorCpfDB(String cpf) {
        String sql = "SELECT * FROM usuarios WHERE cpf = ?";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cpf);
            ResultSet rs = pstmt.executeQuery();

            
            if (rs.next()) {
                return new Usuario(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("endereco")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }
}