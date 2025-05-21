import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.Date;

public class CadastrarFinancas extends JFrame {
    private FinancasManager financasManager;
    private JTextField txtDescricao;
    private JTextField txtValor;
    private JSpinner spinnerData;
    private JComboBox<Categoria> comboCategorias = new JComboBox<>();
    private CategoriaManager categoriaManager;
    private DefaultListModel<Financas> listModel = new DefaultListModel<>();
    private JList<Financas> listaTransacoes = new JList<>(listModel);

    // JComboBox para exibir se é receita ou despesa
    JComboBox<String> tipoFinanca = new JComboBox<>(new String[] { "Receita", "Despesa" });

    public CadastrarFinancas(FinancasManager financasManager, CategoriaManager categoriaManager) {
        this.financasManager = financasManager;
        this.categoriaManager = categoriaManager;

        setTitle("Cadastrar Finança");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        // IMAGEM DA APLICAÇÃO: Tratamento de erros caso não consiga carregar a imagem que colocamos
        try {
            InputStream imgBruno = getClass().getClassLoader().getResourceAsStream("Bruno.jpeg");
            if (imgBruno != null) {
                ImageIcon iconFinanceiro = new ImageIcon(ImageIO.read(imgBruno));
                setIconImage(iconFinanceiro.getImage());
            } else {
                System.err.println("A imagem do Bruno não foi encontrada no classpath!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Descrição
        gbc.gridx = 0;
        gbc.gridy = 0; // linha 0
        add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        txtDescricao = new JTextField(15);
        add(txtDescricao, gbc);

        // Valor
        gbc.gridx = 0;
        gbc.gridy = 1; // linha 1
        add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx = 1;
        txtValor = new JTextField(15);
        add(txtValor, gbc);

        // Tipo Finança
        gbc.gridx = 0;
        gbc.gridy = 2; // linha 2
        add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        add(tipoFinanca, gbc);

        // Data
        gbc.gridx = 0;
        gbc.gridy = 3; // linha 3
        add(new JLabel("Data:"), gbc);
        gbc.gridx = 1;

        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        spinnerData = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerData, "dd/MM/yyyy");
        spinnerData.setEditor(dateEditor);
        spinnerData.setPreferredSize(new Dimension(150, 25));
        add(spinnerData, gbc);

        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 4; // linha 4
        add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        comboCategorias = new JComboBox<>();
        add(comboCategorias, gbc);

        // Botão de Cadastrar
        gbc.gridx = 0;
        gbc.gridy = 5; // linha 5
        gbc.gridwidth = 2;
        JButton btnCadastrar = new JButton("Cadastrar");
        add(btnCadastrar, gbc);

        // Ação do botão
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarFinanca();
            }
        });

        // COMENTADO !!!!! setVisible(true);
    }

    private void cadastrarFinanca() {
        String descricao = txtDescricao.getText().trim();
        String valorStr = txtValor.getText().trim();
        String tipo = (String) tipoFinanca.getSelectedItem(); // Pega o tipo de transação ("Receita" ou "Despesa")

        if (!descricao.isEmpty() && !valorStr.isEmpty() && tipo != null && comboCategorias.getSelectedItem() != null) {
            try {
                double valor = Double.parseDouble(valorStr);

                // Se for "Despesa", inverte o sinal do valor
                if (tipo.equals("Despesa")) {
                    valor = -valor;
                }

                Date data = (Date) spinnerData.getValue();
                Categoria categoriaSelecionada = (Categoria) comboCategorias.getSelectedItem();

                // Registra a transação com o valor positivo ou negativo
                financasManager.cadastrarFinanca(descricao, valor, categoriaSelecionada, data);

                atualizarListaFinancas();
                txtDescricao.setText("");
                txtValor.setText("");
                tipoFinanca.setSelectedIndex(0); // Reseta o JComboBox para "Receita"
                spinnerData.setValue(new Date()); // Reseta para data atual

                JOptionPane.showMessageDialog(null, "Finança cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Função para atualizar a lista de finanças que foram cadastradas
    private void atualizarListaFinancas() {
        listModel.clear();
        for (Financas t : financasManager.listarFinancas()) {
            listModel.addElement(t);
        }
    }

    // Função para atualizar as categorias
    public void atualizarCategorias() {
        comboCategorias.removeAllItems();
        for (Categoria c : categoriaManager.listarCategorias()) {
            comboCategorias.addItem(c);
        }
    }

    // TRABALHAR NESSA FUNÇÃO FUTURAMENTE!!!!!!
    /* private void abrirGerenciamentoCategorias() {
        CategoriaScreen categoriaScreen = new CategoriaScreen(categoriaManager, this);
        categoriaScreen.setVisible(true);
    } */
}
