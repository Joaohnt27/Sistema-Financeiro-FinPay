import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import javax.swing.ListCellRenderer;
import java.util.stream.Collectors;

public class HistoricoFinancasScreen extends JFrame {
    private JTextField txtBuscaData;
    private JTextField txtBuscaDescricao;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboTipo;
    private CategoriaManager categoriaManager;
    private List<Financas> financasList;
    private DefaultListModel<String> listModel; 
    private JList<String> lista;

    public HistoricoFinancasScreen(List<Financas> financasList, CategoriaManager categoriaManager) {
        this.categoriaManager = categoriaManager;
        this.financasList = financasList;  // Associa a lista de finanças
        this.listModel = new DefaultListModel<>();  

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

        // Preencher a lista com as finanças iniciais
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        atualizarLista();

        // Criando JList para exibir as finanças
        lista = new JList<>(listModel);
        lista.setCellRenderer(new FinancaRenderer());
        JScrollPane scrollPane = new JScrollPane(lista);
        add(scrollPane, BorderLayout.CENTER);

        // Criando um menu Popup
        JPopupMenu menuPopup = new JPopupMenu();
        JMenuItem itemEditar = new JMenuItem("Editar");
        JMenuItem itemExcluir = new JMenuItem("Remover");

        menuPopup.add(itemEditar);
        menuPopup.add(itemExcluir);

        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { // Clique do botão direito
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = lista.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        lista.setSelectedIndex(index);
                        menuPopup.show(lista, e.getX(), e.getY());
                    }
                }
            }
        });

        // Ação no botão "Editar"
        itemEditar.addActionListener(e -> {
            int index = lista.getSelectedIndex();
            if (index >= 0) {
                editarCategoria(index);
            }
        });

        // Ação no botão "Remover"
        itemExcluir.addActionListener(e -> {
            int index = lista.getSelectedIndex();
            if (index >= 0) {
                excluirCategoria(index);
            }
        });

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

    // Função de editar do Popup
    private void editarCategoria(int index) {
        Financas selecionada = financasList.get(index);

        // Painel para inserir os novos dados
        JPanel painel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField txtDescricao = new JTextField(selecionada.getDescricao());
        JTextField txtValor = new JTextField(String.valueOf(selecionada.getValor()));
        JTextField txtCategoria = new JTextField(selecionada.getCategoria().getNome());
        JTextField txtData = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(selecionada.getData()));

        painel.add(new JLabel("Descrição:"));
        painel.add(txtDescricao);
        painel.add(new JLabel("Valor:"));
        painel.add(txtValor);
        painel.add(new JLabel("Categoria:"));
        painel.add(txtCategoria);
        painel.add(new JLabel("Data:"));
        painel.add(txtData);

        int resultado = JOptionPane.showConfirmDialog(
                this, painel, "Editar Finança", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String novaDescricao = txtDescricao.getText().trim();
                double novoValor = Double.parseDouble(txtValor.getText().replace(",", "."));
                String novaCategoria = txtCategoria.getText().trim();
                Date novaData = new SimpleDateFormat("dd/MM/yyyy").parse(txtData.getText().trim());

                selecionada.setDescricao(novaDescricao);
                selecionada.setData(novaData);
                selecionada.setValor(novoValor);
                selecionada.getCategoria().setNome(novaCategoria);

                financasList.set(index, selecionada);
                atualizarLista();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Função de remover do Popup
    private void excluirCategoria(int index) {
        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover esta transação?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            financasList.remove(index);
            listModel.remove(index);
            atualizarLista();
        }
    }

    // Atualizar a lista do histórico após edição ou remoção
    private void atualizarLista() {
        listModel.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Financas f : financasList) {
            String linha = String.format("Data: %s | Valor: R$ %.2f | Categoria: %s | Descrição: %s",
                    sdf.format(f.getData()),
                    f.getValor(),
                    f.getCategoria().getNome(),
                    f.getDescricao());
            listModel.addElement(linha);
        }
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

    // Renderer para a cor do histórico ser igual a cor da categoria
    private class FinancaRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Financas f = financasList.get(index);

            if (!isSelected) {
                Color corCategoria = f.getCategoria().getColor();
                label.setBackground(corCategoria);
                label.setOpaque(true);
            }

            return label;
        }
    }
}
