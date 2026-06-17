package br.edu.ifce.ads.scuere;

import br.edu.ifce.ads.scuere.dao.PecaDAO;
import br.edu.ifce.ads.scuere.dao.VeiculoDAO;
import br.edu.ifce.ads.scuere.database.ConexaoDB;
import br.edu.ifce.ads.scuere.exceptions.UsuarioNaoEncontradoException;
import br.edu.ifce.ads.scuere.factory.ItemFactory;
import br.edu.ifce.ads.scuere.model.ItemComerciavel;
import br.edu.ifce.ads.scuere.model.Peca;
import br.edu.ifce.ads.scuere.model.ServicoOficina;
import br.edu.ifce.ads.scuere.model.Usuario;
import br.edu.ifce.ads.scuere.model.Veiculo;
import br.edu.ifce.ads.scuere.service.GerenciadorUsuarios;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class ScuereController {

    // --- Aba Venda ---
    @FXML private TextField campoCpfVenda;
    @FXML private ComboBox<String> comboIdItemVenda;
    @FXML private ComboBox<String> comboTipoVenda;
    @FXML private Label labelMensagemVenda;

    // --- Aba Estoque (Motos) ---
    @FXML private TextField cadIdChassi, cadMarca, cadModelo, cadAno, cadPreco, cadKm;
    @FXML private Label labelMensagemMoto;

    // --- Aba Estoque (Peças) ---
    @FXML private TextField cadIdPeca, cadCompatibilidade, cadPrecoPeca;
    @FXML private Label labelMensagemPeca;

    // --- Aba Oficina ---
    @FXML private TextArea campoDescricaoServico;
    @FXML private TextField campoValorMaoObra, campoPecasUsadas;
    @FXML private Label labelMensagemOficina;

    // --- Aba Clientes ---
    @FXML private TextField cadCpfCliente;
    @FXML private TextField cadNomeCliente;
    @FXML private TextField cadEnderecoCliente;
    @FXML private Label labelMensagemCliente;

    // --- Dashboard ---
    @FXML private Label labelCaixaHoje;
    @FXML private Label labelCaixaOntem;
    @FXML private Label labelDespesasMes;
    @FXML private TextField campoDespesa, campoValorDespesa;
    @FXML private Label labelMensagemFinanceiro;

    private GerenciadorUsuarios gerenciadorUsuarios;
    private VeiculoDAO veiculoDAO;
    private PecaDAO pecaDAO;
    private double totalCaixaHoje = 0.0;
    private double totalDespesasMes = 0.0;

    public void initialize() {
        gerenciadorUsuarios = new GerenciadorUsuarios();
        veiculoDAO = new VeiculoDAO();
        pecaDAO = new PecaDAO();

        veiculoDAO.criarTabelaVeiculos();
        pecaDAO.criarTabelaPecas();

        // Dados financeiros persistidos
        String hoje    = LocalDate.now().toString();
        String ontem   = LocalDate.now().minusDays(1).toString();
        String mesAtual = hoje.substring(0, 7);

        totalCaixaHoje   = ConexaoDB.buscarTotalDia(hoje);
        totalDespesasMes = ConexaoDB.buscarDespesasMes(mesAtual);
        double totalOntem = ConexaoDB.buscarTotalDia(ontem);

        labelCaixaOntem.setText(String.format("R$ %.2f", totalOntem));
        labelDespesasMes.setText(String.format("R$ %.2f", totalDespesasMes));

        iniciarThreadDoCaixa();

        // Categorias disponíveis
        comboTipoVenda.setItems(FXCollections.observableArrayList("Moto Nova", "Moto Usada", "Peças"));

        // Listener: ao escolher a categoria, carrega itens do banco filtrados
        comboTipoVenda.setOnAction(e -> atualizarListaItens());

        // Começa sem itens no combo de seleção — o usuário escolhe a categoria primeiro
        comboIdItemVenda.setPromptText("Selecione a categoria primeiro");
    }

    /** Consulta o banco e popula o ComboBox de itens conforme a categoria escolhida. */
    private void atualizarListaItens() {
        String categoria = comboTipoVenda.getValue();
        if (categoria == null) return;

        comboIdItemVenda.getItems().clear();
        comboIdItemVenda.setValue(null);

        List<String> itens = switch (categoria) {
            case "Moto Nova"  -> veiculoDAO.listarPorTipo("MotoNova");
            case "Moto Usada" -> veiculoDAO.listarPorTipo("MotoUsada");
            case "Peças"      -> pecaDAO.listarTodos();
            default -> List.of();
        };

        if (itens.isEmpty()) {
            comboIdItemVenda.setPromptText("Nenhum item cadastrado nesta categoria");
        } else {
            comboIdItemVenda.setItems(FXCollections.observableArrayList(itens));
            comboIdItemVenda.setPromptText("Selecione o item disponível");
        }
    }

    @FXML
    protected void realizarVendaCompleta() {
        String cpf = campoCpfVenda.getText();
        String tipoUI = comboTipoVenda.getValue();
        String idItemSelecionado = comboIdItemVenda.getValue();

        if (cpf.isEmpty() || tipoUI == null || idItemSelecionado == null) {
            mostrarErro(labelMensagemVenda, "Preencha todos os campos da venda.");
            return;
        }

        String tipoFactory = switch (tipoUI) {
            case "Moto Nova"  -> "MotoNova";
            case "Moto Usada" -> "MotoUsada";
            case "Peças"      -> "Peca";
            default -> "";
        };

        // Extrai apenas o ID/chassi antes do parênteses: "9BW1234 (CB500)" → "9BW1234"
        String idReal = idItemSelecionado.split(" ")[0];

        try {
            Usuario cliente = gerenciadorUsuarios.buscarPorCpf(cpf);
            String[] dadosExtras = {idReal, "Marca Genérica", "Modelo Genérico", "2026", "5000"};
            ItemComerciavel itemVendido = ItemFactory.criarItem(tipoFactory, idReal, 25000.0, dadosExtras);

            double valorFinal = itemVendido.calcularValorFinal();
            totalCaixaHoje += valorFinal;
            ConexaoDB.salvarTotalDia(LocalDate.now().toString(), totalCaixaHoje);

            mostrarSucesso(labelMensagemVenda,
                    "Venda aprovada para " + cliente.getNome() + "! Valor: R$ " + String.format("%.2f", valorFinal));
            campoCpfVenda.clear();
            comboIdItemVenda.setValue(null);

        } catch (UsuarioNaoEncontradoException e) {
            mostrarErro(labelMensagemVenda, e.getMessage());
        } catch (Exception e) {
            mostrarErro(labelMensagemVenda, "Erro na venda: " + e.getMessage());
        }
    }

    @FXML
    protected void cadastrarMoto() {
        if (cadIdChassi.getText().isEmpty() || cadPreco.getText().isEmpty() || cadKm.getText().isEmpty()) {
            mostrarErro(labelMensagemMoto, "Chassi, Preço e KM são obrigatórios.");
            return;
        }

        try {
            double preco = Double.parseDouble(cadPreco.getText());
            double km    = Double.parseDouble(cadKm.getText());
            int ano = cadAno.getText().isEmpty() ? 2026 : Integer.parseInt(cadAno.getText());

            String tipoFactory = (km == 0) ? "MotoNova" : "MotoUsada";
            String tipoAviso   = (km == 0) ? "Moto 0 KM" : "Moto Usada (" + (int) km + " km)";

            String[] dados = {
                cadIdChassi.getText(), cadMarca.getText(), cadModelo.getText(),
                String.valueOf(ano), String.valueOf(km)
            };
            ItemComerciavel novoItem = ItemFactory.criarItem(tipoFactory, cadIdChassi.getText(), preco, dados);

            if (novoItem instanceof Veiculo v) {
                veiculoDAO.salvar(v, tipoFactory);
            }

            // Se o filtro de venda estiver na mesma categoria, recarrega a lista
            String categoriaAtual = comboTipoVenda.getValue();
            if ((km == 0 && "Moto Nova".equals(categoriaAtual)) ||
                (km > 0  && "Moto Usada".equals(categoriaAtual))) {
                atualizarListaItens();
            }

            mostrarSucesso(labelMensagemMoto, tipoAviso + " registrada com sucesso!");
            cadIdChassi.clear(); cadMarca.clear(); cadModelo.clear();
            cadAno.clear(); cadPreco.clear(); cadKm.clear();

        } catch (NumberFormatException e) {
            mostrarErro(labelMensagemMoto, "Ano, Preço e KM precisam ser números válidos.");
        }
    }

    @FXML
    protected void cadastrarPeca() {
        if (cadIdPeca.getText().isEmpty() || cadPrecoPeca.getText().isEmpty()) {
            mostrarErro(labelMensagemPeca, "ID da Peça e Preço são obrigatórios.");
            return;
        }

        try {
            double preco = Double.parseDouble(cadPrecoPeca.getText());
            String compatibilidade = cadCompatibilidade.getText().isEmpty()
                    ? "Universal" : cadCompatibilidade.getText();

            pecaDAO.salvar(cadIdPeca.getText(), compatibilidade, preco);

            // Se o filtro estiver em "Peças", recarrega a lista imediatamente
            if ("Peças".equals(comboTipoVenda.getValue())) {
                atualizarListaItens();
            }

            mostrarSucesso(labelMensagemPeca, "Peça \"" + cadIdPeca.getText() + "\" adicionada ao estoque.");
            cadIdPeca.clear(); cadCompatibilidade.clear(); cadPrecoPeca.clear();

        } catch (NumberFormatException e) {
            mostrarErro(labelMensagemPeca, "O preço deve ser um número válido.");
        }
    }

    @FXML
    protected void lancarServico() {
        if (campoDescricaoServico.getText().isEmpty() || campoValorMaoObra.getText().isEmpty()) {
            mostrarErro(labelMensagemOficina, "Descrição e Valor da mão de obra são obrigatórios.");
            return;
        }

        try {
            double maoDeObra = Double.parseDouble(campoValorMaoObra.getText());
            ServicoOficina servico = new ServicoOficina(campoDescricaoServico.getText(), maoDeObra);

            if (!campoPecasUsadas.getText().isEmpty()) {
                for (String p : campoPecasUsadas.getText().split(",")) {
                    servico.adicionarPeca(new Peca(p.trim(), 100.0, "Genérica"));
                }
            }

            double valorTotal = servico.calcularValorTotal();
            totalCaixaHoje += valorTotal;
            ConexaoDB.salvarTotalDia(LocalDate.now().toString(), totalCaixaHoje);

            mostrarSucesso(labelMensagemOficina,
                    "Nota Fiscal Gerada! Total: R$ " + String.format("%.2f", valorTotal));
            campoDescricaoServico.clear(); campoValorMaoObra.clear(); campoPecasUsadas.clear();

        } catch (NumberFormatException e) {
            mostrarErro(labelMensagemOficina, "O valor da mão de obra deve ser numérico.");
        }
    }

    @FXML
    protected void cadastrarCliente() {
        String cpf = cadCpfCliente.getText();
        String nome = cadNomeCliente.getText();
        String endereco = cadEnderecoCliente.getText();

        if (cpf.isEmpty() || nome.isEmpty() || endereco.isEmpty()) {
            mostrarErro(labelMensagemCliente, "Todos os campos são obrigatórios para o cadastro.");
            return;
        }

        try {
            // Usa a classe Usuario que modelamos lá na Fase 2
            Usuario novoCliente = new Usuario(cpf, nome, endereco);

            // O gerenciador salva no Map (memória) e no SQLite (banco) de uma vez só
            gerenciadorUsuarios.cadastrarUsuario(novoCliente);

            mostrarSucesso(labelMensagemCliente, "Cliente " + nome + " cadastrado com sucesso!");

            // Limpa os campos após salvar
            cadCpfCliente.clear();
            cadNomeCliente.clear();
            cadEnderecoCliente.clear();

        } catch (Exception e) {
            mostrarErro(labelMensagemCliente, "Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    @FXML
    protected void registrarDespesa() {
        String descricao = campoDespesa.getText().trim();
        String valorStr  = campoValorDespesa.getText().trim();

        if (descricao.isEmpty() || valorStr.isEmpty()) {
            mostrarErro(labelMensagemFinanceiro, "Preencha a descrição e o valor da despesa.");
            return;
        }

        try {
            double valor = Double.parseDouble(valorStr);
            ConexaoDB.salvarDespesa(LocalDate.now().toString(), descricao, valor);
            totalDespesasMes += valor;
            labelDespesasMes.setText(String.format("R$ %.2f", totalDespesasMes));
            mostrarAlerta(labelMensagemFinanceiro,
                    "Despesa de R$ " + String.format("%.2f", valor) + " registrada.");
            campoDespesa.clear(); campoValorDespesa.clear();
        } catch (NumberFormatException e) {
            mostrarErro(labelMensagemFinanceiro, "O valor deve ser numérico.");
        }
    }

    private void iniciarThreadDoCaixa() {
        Thread threadCaixa = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1500);
                    final double snapshot = totalCaixaHoje;
                    Platform.runLater(() ->
                        labelCaixaHoje.setText(String.format("R$ %.2f", snapshot))
                    );
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        threadCaixa.setDaemon(true);
        threadCaixa.start();
    }

    private void mostrarSucesso(Label label, String mensagem) {
        label.setText(mensagem);
        label.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
    }

    private void mostrarErro(Label label, String mensagem) {
        label.setText(mensagem);
        label.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
    }

    private void mostrarAlerta(Label label, String mensagem) {
        label.setText(mensagem);
        label.setStyle("-fx-text-fill: #f59e0b; -fx-font-weight: bold;");
    }
}
