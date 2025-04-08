import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.InputStream;

public class CategoriaScreen extends JFrame {
    private CategoriaManager categoriaManager;
    private CadastrarFinancas cadastrarFinancas;
    private DefaultListModel<Categoria> listModel = new DefaultListModel<>();
    private JList<Categoria> listaCategorias = new JList<>(listModel);
    private JTextField campoNome = new JTextField(15);
    private Color color = Color.WHITE;

    public CategoriaScreen(CategoriaManager categoriaManager, CadastrarFinancas cadastrarFinancas) {
        this.categoriaManager = categoriaManager;
        this.cadastrarFinancas = cadastrarFinancas;

        setTitle("Gerenciar Categorias"); // Nome da aba
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

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

        JPanel panelTop = new JPanel();

        //Criando o campo de inserção do nome
        panelTop.add(new JLabel("Nome:"));
        panelTop.add(campoNome);

        //Criando os botões de cor e Adicionar
        JButton btnCorSelect = new JButton("Cor");
        btnCorSelect.addActionListener(e -> escolherCor());
        panelTop.add(btnCorSelect);
        JButton btnAdicionar = new JButton("Adicionar");
        panelTop.add(btnAdicionar);
        btnAdicionar.setMnemonic(KeyEvent.VK_S);

        listaCategorias.setCellRenderer(new CategoriaManager.CategoriaRenderer());
        JScrollPane scrollPane = new JScrollPane(listaCategorias);

        //Criando os botões das funções principais
        JPanel panelBottom = new JPanel();
        JButton btnEdit = new JButton("Editar");
        panelBottom.add(btnEdit);
        JButton btnRemover = new JButton("Remover");
        panelBottom.add(btnRemover);

        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        //adicionando eventos nos botões
        btnAdicionar.addActionListener(e -> adicionarCategoria());
        btnRemover.addActionListener(e -> removerCategoria());
        btnEdit.addActionListener(e -> editarCategoria());
        getRootPane().setDefaultButton(btnAdicionar);

        atualizarLista();
    }

    // Função de adicionar uma categoria
    private void adicionarCategoria() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um nome para a categoria", //Mensagem de erro caso o usuario n preencher o campo "Nome"
                    "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        categoriaManager.adicionarCategoria(nome, color);
        atualizarLista();
        listaCategorias.repaint();

        // Verifica se cadastrarFinancas não é nulo antes de chamar a função
        if (cadastrarFinancas != null) {
            cadastrarFinancas.atualizarCategorias();
        } else {
            System.err.println("Erro: cadastrarFinancas está nulo! No cadastrar categoria!");
        }

        campoNome.setText("");
    }

    // Função para escolher a cor das categorias
    private void escolherCor() {
        Color novaCor = JColorChooser.showDialog(this, "Escolha a cor da Categoria!", color);
        if (novaCor != null) {
            color = novaCor;
        }
    }

    // Função de editar categoria, podendo editar nome e a cor
    private void editarCategoria() {
        Categoria selecionada = listaCategorias.getSelectedValue();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para editar!", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(2,2,5,5));
        JTextField novoNome = new JTextField(selecionada.getNome());
        JButton btnNovaCor = new JButton("Escolher Cor");

        final Color[] novaCor = {selecionada.getColor()};
        btnNovaCor.addActionListener(e -> {
            Color corEscolhida = JColorChooser.showDialog(this, "Escolha a nova cor!", novaCor[0]);
            if (corEscolhida != null) {
                novaCor[0] = corEscolhida;
            }
        });

        panel.add(new JLabel("Nome:"));
        panel.add(novoNome);
        panel.add(btnNovaCor);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Editar Categoria", JOptionPane.OK_CANCEL_OPTION);
        if (resultado == JOptionPane.OK_OPTION) {
            String nome = novoNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um nome para a Categoria!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int index = listaCategorias.getSelectedIndex();
            categoriaManager.editarCategoria(index, nome, novaCor[0]);
            atualizarLista();
            cadastrarFinancas.atualizarCategorias();
        }
    }

    // Função de remover uma categoria criada
    private void removerCategoria() {
        Categoria selecionada = listaCategorias.getSelectedValue();
        if (selecionada != null) {
            if (selecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma categoria para remover!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //mensagem de confirmação, de acordo com a quinta heurística de Nielsen
            int confirmacao = JOptionPane.showConfirmDialog(this, "Realmente deseja remover a categoria" + selecionada.getNome() + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                categoriaManager.listarCategorias().remove(selecionada);
                atualizarLista();
            }

            if (cadastrarFinancas != null) {
                cadastrarFinancas.atualizarCategorias();
            } else {
                System.err.println("Erro: cadastrarFinancas está nulo! No remover categoria!");
            }
        }
    }

    // Função para atualizar a lista
    private void atualizarLista() {
        listModel.clear();
        for (Categoria c : categoriaManager.listarCategorias()) {
            listModel.addElement(c);
        }
    }
}
