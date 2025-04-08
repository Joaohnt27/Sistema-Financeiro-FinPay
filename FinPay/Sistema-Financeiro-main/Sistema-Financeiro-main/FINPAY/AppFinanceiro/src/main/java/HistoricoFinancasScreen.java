import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class HistoricoFinancasScreen extends JFrame {
    private JTextField txtBuscaData;
    private JTextField txtBuscaDescricao;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboTipo;
    private CategoriaManager categoriaManager;

    public HistoricoFinancasScreen(List<Financas> financasList, CategoriaManager categoriaManager) {
        this.categoriaManager = categoriaManager;

        setTitle("Histórico de Finanças");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel do Filtro
        JPanel painelFiltro = new JPanel(new GridLayout(1, 5, 10, 10));

        // Campo de busca por data
        txtBuscaData = new JTextField(10);
        painelFiltro.add(new JLabel("Data (dd/MM/yyyy):"));
        painelFiltro.add(txtBuscaData);

        // Campo de busca por descrição
        txtBuscaDescricao = new JTextField(10);
        painelFiltro.add(new JLabel("Descrição:"));
        painelFiltro.add(txtBuscaDescricao);

        // Combo de categoria
        comboCategoria = new JComboBox<>();
        comboCategoria.addItem("Todas as Categorias");
        for (Categoria c : categoriaManager.listarCategorias()) {
            comboCategoria.addItem(c.getNome());
        }
        painelFiltro.add(new JLabel("Categoria:"));
        painelFiltro.add(comboCategoria);

        // Combo de tipo (Receita/Despesa)
        comboTipo = new JComboBox<>();
        comboTipo.addItem("Todos");
        comboTipo.addItem("Receita");
        comboTipo.addItem("Despesa");
        painelFiltro.add(new JLabel("Tipo:"));
        painelFiltro.add(comboTipo);

        // Botão de Filtrar
        JButton btnFiltrar = new JButton("Filtrar");
        painelFiltro.add(btnFiltrar);

        add(painelFiltro, BorderLayout.NORTH);

        // Model de lista para exibir as finanças filtradas
        DefaultListModel<String> listModel = new DefaultListModel<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Preencher a lista com as finanças iniciais
        for (Financas f : financasList) {
            String linha = String.format("Data: %s | Valor: R$ %.2f | Categoria: %s | Descrição: %s",
                    sdf.format(f.getData()),
                    f.getValor(),
                    f.getCategoria().getNome(),
                    f.getDescricao());
            listModel.addElement(linha);
        }

        // Criando JList para exibir as finanças
        JList<String> lista = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(lista);
        add(scrollPane, BorderLayout.CENTER);

        // Função do botão de "Filtrar"
        btnFiltrar.addActionListener(e -> {
            List<Financas> listaFiltrada = financasList;

            // Filtro por Data
            if (!txtBuscaData.getText().isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String buscaData = txtBuscaData.getText();
                    listaFiltrada = listaFiltrada.stream()
                            .filter(f -> dateFormat.format(f.getData()).equals(buscaData))
                            .collect(Collectors.toList());
                } catch (Exception ex) {
                    // Tratar erro de formato
                    JOptionPane.showMessageDialog(this, "Formato de data inválido! Use dd/MM/yyyy.");
                }
            }

            // Filtro por Descrição
            if (!txtBuscaDescricao.getText().isEmpty()) {
                String buscaDescricao = txtBuscaDescricao.getText().toLowerCase();
                listaFiltrada = listaFiltrada.stream()
                        .filter(f -> f.getDescricao().toLowerCase().contains(buscaDescricao))
                        .collect(Collectors.toList());
            }

            // Filtro por Categoria
            String categoriaSelecionada = (String) comboCategoria.getSelectedItem();
            if (categoriaSelecionada != null && !categoriaSelecionada.equals("Todas as Categorias")) {
                listaFiltrada = listaFiltrada.stream()
                        .filter(f -> f.getCategoria().getNome().equals(categoriaSelecionada))
                        .collect(Collectors.toList());
            }

            // Filtro por Tipo
            String tipoSelecionado = (String) comboTipo.getSelectedItem();
            if (tipoSelecionado != null && !tipoSelecionado.equals("Todos")) {
                if (tipoSelecionado.equals("Receita")) {
                    listaFiltrada = listaFiltrada.stream()
                            .filter(f -> f.getValor() > 0)
                            .collect(Collectors.toList());
                } else if (tipoSelecionado.equals("Despesa")) {
                    listaFiltrada = listaFiltrada.stream()
                            .filter(f -> f.getValor() < 0)
                            .collect(Collectors.toList());
                }
            }

            // Exibir os totais (Receitas, Despesas e Saldo)
            exibirTotais(listaFiltrada);

            // Atualizar a lista de resultados
            DefaultListModel<String> updatedListModel = new DefaultListModel<>();
            for (Financas f : listaFiltrada) {
                String linha = String.format("Data: %s | Valor: R$ %.2f | Categoria: %s | Descrição: %s",
                        sdf.format(f.getData()),
                        f.getValor(),
                        f.getCategoria().getNome(),
                        f.getDescricao());
                updatedListModel.addElement(linha);
            }
            lista.setModel(updatedListModel);
        });
    }

    // Função para exibir o total das receitas e despesas no período
    private void exibirTotais(List<Financas> listaFiltrada) {
        double totalReceitas = 0;
        double totalDespesas = 0;

        // Calcula o total de receitas e despesas dentro do período filtrado
        for (Financas f : listaFiltrada) {
            if (f.getValor() > 0) {
                totalReceitas += f.getValor(); // Soma as receitas (valores positivos)
            } else {
                totalDespesas += f.getValor(); // Soma as despesas (valores negativos)
            }
        }

        // Exibe o total de receitas, despesas e saldo
        String mensagem = String.format("Total no Período:\n" +
                        "Receitas: R$ %.2f\n" +
                        "Despesas: R$ %.2f\n" +
                        "Saldo: R$ %.2f\n" +
                        (totalReceitas + totalDespesas >= 0 ? "Saldo Positivo! =D" : "Saldo Negativo! :("),
                totalReceitas, totalDespesas, totalReceitas + totalDespesas);
        JOptionPane.showMessageDialog(this, mensagem, "Totais no Período", JOptionPane.INFORMATION_MESSAGE);
    }
}
