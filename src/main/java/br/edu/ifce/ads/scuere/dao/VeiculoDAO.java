package br.edu.ifce.ads.scuere.dao;

import br.edu.ifce.ads.scuere.model.Veiculo;
import br.edu.ifce.ads.scuere.database.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
        String sql = "INSERT INTO veiculos(chassi, marca, modelo, ano, quilometragem, preco, tipo) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, veiculo.getChassi());
            pstmt.setString(2, veiculo.getMarca());
            pstmt.setString(3, veiculo.getModelo());
            pstmt.setInt(4, veiculo.getAno());
            pstmt.setDouble(5, veiculo.getQuilometragem());
            pstmt.setDouble(6, veiculo.getPrecoBase());
            pstmt.setString(7, tipoVeiculo);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao salvar veículo: " + e.getMessage());
        }
    }
}