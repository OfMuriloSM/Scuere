package br.edu.ifce.ads.scuere.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {
    private static final String URL = "jdbc:sqlite:scuere.db";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco: " + e.getMessage());
            return null;
        }
    }

    public static void criarTabelas() {
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "cpf TEXT PRIMARY KEY," +
                "nome TEXT NOT NULL," +
                "endereco TEXT" +
                ");";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsuario);
            System.out.println("Tabelas verificadas/criadas com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}