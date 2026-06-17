package br.edu.ifce.ads.scuere.dao;

import br.edu.ifce.ads.scuere.database.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PecaDAO {

    public void criarTabelaPecas() {
        String sql = "CREATE TABLE IF NOT EXISTS pecas (" +
                     "  id TEXT PRIMARY KEY," +
                     "  compatibilidade TEXT," +
                     "  preco REAL" +
                     ");";
        try (Connection conn = ConexaoDB.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de peças: " + e.getMessage());
        }
    }

    public void salvar(String id, String compatibilidade, double preco) {
        String sql = "INSERT OR IGNORE INTO pecas(id, compatibilidade, preco) VALUES(?,?,?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, compatibilidade);
            ps.setDouble(3, preco);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar peça: " + e.getMessage());
        }
    }

    /** Retorna lista no formato "ID (Compatibilidade)" para o ComboBox */
    public List<String> listarTodos() {
        List<String> items = new ArrayList<>();
        String sql = "SELECT id, compatibilidade FROM pecas ORDER BY id";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(rs.getString("id") + " (" + rs.getString("compatibilidade") + ")");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar peças: " + e.getMessage());
        }
        return items;
    }
}
