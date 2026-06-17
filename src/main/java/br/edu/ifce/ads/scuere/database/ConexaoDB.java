package br.edu.ifce.ads.scuere.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {

    // Caminho absoluto na home do usuário — evita o bug de "banco sumindo" entre execuções
    private static final String URL =
            "jdbc:sqlite:" + System.getProperty("user.home") + "/scuere.db";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco: " + e.getMessage());
            return null;
        }
    }

    public static void criarTabelas() {
        String sqlUsuarios =
            "CREATE TABLE IF NOT EXISTS usuarios (" +
            "  cpf TEXT PRIMARY KEY," +
            "  nome TEXT NOT NULL," +
            "  endereco TEXT" +
            ");";

        String sqlFinanceiro =
            "CREATE TABLE IF NOT EXISTS financeiro (" +
            "  data TEXT PRIMARY KEY," +
            "  total REAL DEFAULT 0" +
            ");";

        String sqlDespesas =
            "CREATE TABLE IF NOT EXISTS despesas (" +
            "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  data TEXT NOT NULL," +
            "  descricao TEXT," +
            "  valor REAL NOT NULL" +
            ");";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlFinanceiro);
            stmt.execute(sqlDespesas);
            System.out.println("Tabelas verificadas/criadas com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    // ---- Helpers financeiros ----

    public static double buscarTotalDia(String data) {
        String sql = "SELECT total FROM financeiro WHERE data = ?";
        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.out.println("Erro ao buscar total do dia: " + e.getMessage());
        }
        return 0.0;
    }

    public static void salvarTotalDia(String data, double total) {
        String sql =
            "INSERT INTO financeiro(data, total) VALUES(?,?) " +
            "ON CONFLICT(data) DO UPDATE SET total = excluded.total";
        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data);
            ps.setDouble(2, total);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar total do dia: " + e.getMessage());
        }
    }

    public static void salvarDespesa(String data, String descricao, double valor) {
        String sql = "INSERT INTO despesas(data, descricao, valor) VALUES(?,?,?)";
        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data);
            ps.setString(2, descricao);
            ps.setDouble(3, valor);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar despesa: " + e.getMessage());
        }
    }

    public static double buscarDespesasMes(String anoMes) {
        // anoMes no formato "2026-06"
        String sql = "SELECT COALESCE(SUM(valor), 0) FROM despesas WHERE data LIKE ?";
        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, anoMes + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("Erro ao buscar despesas do mês: " + e.getMessage());
        }
        return 0.0;
    }
}
