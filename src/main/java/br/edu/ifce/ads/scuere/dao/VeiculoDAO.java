package br.edu.ifce.ads.scuere.dao;

import br.edu.ifce.ads.scuere.model.Veiculo;
import br.edu.ifce.ads.scuere.database.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public void criarTabelaVeiculos() {
        String sql = "CREATE TABLE IF NOT EXISTS veiculos (" +
                "chassi TEXT PRIMARY KEY, " +
                "marca TEXT, modelo TEXT, ano INTEGER, " +
                "quilometragem REAL, preco REAL, tipo TEXT" +
                ");";
        try (Connection conn = ConexaoDB.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de veículos: " + e.getMessage());
        }
    }

    public void salvar(Veiculo veiculo, String tipoVeiculo) {
        String sql = "INSERT OR IGNORE INTO veiculos(chassi, marca, modelo, ano, quilometragem, preco, tipo) " +
                     "VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, veiculo.getChassi());
            ps.setString(2, veiculo.getMarca());
            ps.setString(3, veiculo.getModelo());
            ps.setInt(4, veiculo.getAno());
            ps.setDouble(5, veiculo.getQuilometragem());
            ps.setDouble(6, veiculo.getPrecoBase());
            ps.setString(7, tipoVeiculo);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar veículo: " + e.getMessage());
        }
    }

    /**
     * Retorna lista no formato "CHASSI (Modelo)" filtrada por tipo (MotoNova / MotoUsada).
     */
    public List<String> listarPorTipo(String tipo) {
        List<String> items = new ArrayList<>();
        String sql = "SELECT chassi, modelo FROM veiculos WHERE tipo = ? ORDER BY chassi";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(rs.getString("chassi") + " (" + rs.getString("modelo") + ")");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar veículos: " + e.getMessage());
        }
        return items;
    }
}
