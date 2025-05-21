import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

public class FinanceiroScreen extends JFrame {

    private GerenciarUsuarios gerenciarUsuarios;
    private CategoriaManager categoriaManager;
    private CadastrarFinancas cadastrarFinancas;
    private FinancasManager financasManager;

    public FinanceiroScreen(GerenciarUsuarios gerenciarUsuarios) {
        Usuario usuarioAtual = gerenciarUsuarios.getUsuarioAtual();
        this.gerenciarUsuarios = gerenciarUsuarios;
        this.financasManager = new FinancasManager(usuarioAtual, gerenciarUsuarios.getSessionFactory());
        this.categoriaManager = new CategoriaManager(usuarioAtual, gerenciarUsuarios.getSessionFactory());
        this.cadastrarFinancas = null; // Inicializa como nulo e só cria quando necessário

        setTitle("FinPay - Dashboard");
        setSize(750, 530);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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

        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Responsável por organizar o layout na vertical
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza todo o conteúdo

        // Texto do topo da tela
        JLabel tituloFinanceiro = new JLabel(
                gerenciarUsuarios.getUsuarioAtual().getNome() + ", bem-vindo ao dashboard do FinPay!",
                SwingConstants.CENTER
        );
        tituloFinanceiro.setFont(new Font("Arial", Font.BOLD, 20));
        tituloFinanceiro.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o título
        mainPanel.add(tituloFinanceiro);

        // Painel para a imagem do meio da tela
        JLabel imagemLabel = new JLabel();
        try {
            ImageIcon imagem = new ImageIcon(getClass().getClassLoader().getResource("Finpay.png")); // Substitua "image.png" pelo nome do arquivo da imagem
            imagemLabel.setIcon(imagem);
            imagemLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza a imagem
            mainPanel.add(imagemLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Painel para os botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15)); // Centraliza os botões

        JButton btnAdicionarFinanca = new JButton("Adicionar Finança");
        btnAdicionarFinanca.addActionListener(e -> {
            if (cadastrarFinancas == null) {
                cadastrarFinancas = new CadastrarFinancas(financasManager, categoriaManager);
            }
            cadastrarFinancas.setVisible(true);
        });

        JButton btnHistoricoFinanca = new JButton("Histórico de Finanças");
        btnHistoricoFinanca.addActionListener(e -> {
            List<Financas> lista = financasManager.listarFinancas();
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma finança cadastrada ainda.", "Histórico", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Passando o categoriaManager junto com a lista de finanças
                HistoricoFinancasScreen historicoScreen = new HistoricoFinancasScreen(lista, categoriaManager);
                historicoScreen.setVisible(true);
            }
        });

        JButton btnGerenciarCategoria = new JButton("Gerenciar Categorias");
        btnGerenciarCategoria.addActionListener(e -> {
            System.out.println("Abrindo CategoriaScreen...");
            if (cadastrarFinancas == null) {
                cadastrarFinancas = new CadastrarFinancas(financasManager, categoriaManager);
            }
            CategoriaScreen categoriaScreen = new CategoriaScreen(categoriaManager, cadastrarFinancas);
            categoriaScreen.setVisible(true);
        });

        painelBotoes.add(btnAdicionarFinanca);
        painelBotoes.add(btnHistoricoFinanca);
        painelBotoes.add(btnGerenciarCategoria);

        JButton btnExibirTotalFinancas = new JButton("Exibir Total de Finanças");
        btnExibirTotalFinancas.addActionListener(e -> exibirTotalFinancas());
        painelBotoes.add(btnExibirTotalFinancas); // Adiciona o botão no painel de botões

        // Adiciona o painel de botões ao painel principal
        mainPanel.add(painelBotoes);

        // Painel inferior para o botão "Sair" e o rodapé
        JPanel painelInferior = new JPanel();
        painelInferior.setLayout(new BoxLayout(painelInferior, BoxLayout.Y_AXIS));

        // Botão "Sair"
        JButton btnSair = new JButton("Sair");
        btnSair.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o botão
        btnSair.addActionListener(e -> desejaSair());


        // Rodapé com os nomes dos desenvolvedores
        JLabel rodape = new JLabel("@2025 - Desenvolvido por: Arthur Vital Fontana, João Henrique Nazar Tavares e Rafael Mele Porto", SwingConstants.CENTER);
        rodape.setFont(new Font("Arial", Font.BOLD, 10));
        rodape.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o rodapé

        // Adicionando o botão e o rodapé ao painel inferior
        painelInferior.add(btnSair);
        painelInferior.add(Box.createVerticalStrut(5)); // Espaço entre botão e rodapé
        painelInferior.add(rodape);

        mainPanel.add(painelInferior); // Adiciona o painel inferior no painel principal

        add(mainPanel); // Adiciona o painel principal ao JFrame

        setVisible(true);
    }

    // Função para exibir o total das finanças
    private void exibirTotalFinancas() {
        double totalReceitas = 0;
        double totalDespesas = 0;

        // Calcula o total de receitas e despesas
        for (Financas f : financasManager.listarFinancas()) {
            if (f.getValor() > 0) {
                totalReceitas += f.getValor(); // Soma as receitas (valores positivos)
            } else {
                totalDespesas += f.getValor(); // Soma as despesas (valores negativos)
            }
        }

        // Calcula o saldo (receitas - despesas)
        double saldo = totalReceitas + totalDespesas; // Lembre-se que despesas são negativas, então soma com totalDespesas

        // Exibe o total em uma mensagem de diálogo
        String mensagem = String.format("Total de Finanças:\n" +
                        "Receitas: R$ %.2f\n" +
                        "Despesas: R$ %.2f\n" +
                        "Saldo: R$ %.2f\n" +
                        (saldo >= 0 ? "Saldo Positivo! =D" : "Saldo Negativo! :("),
                totalReceitas, totalDespesas, saldo);
        JOptionPane.showMessageDialog(this, mensagem, "Resumo das Finanças", JOptionPane.INFORMATION_MESSAGE);
    }

    // Função para exibir uma caixa de confirmação de saída (troca de usuário)
    private void desejaSair() {
        JDialog desejaSair = new JDialog(this, "Trocar usuário", true);
        desejaSair.setSize(300, 150);
        desejaSair.setLocationRelativeTo(this);
        desejaSair.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblMsgSair = new JLabel("Deseja realmente sair do seu usuário?", SwingConstants.CENTER);
        lblMsgSair.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnSim = new JButton("Sim");
        JButton btnNao = new JButton("Não");

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelBotoes.add(btnSim);
        painelBotoes.add(btnNao);

        desejaSair.add(lblMsgSair, gbc);
        gbc.gridy = 1;
        desejaSair.add(painelBotoes, gbc);

        btnSim.addActionListener(e -> {
            desejaSair.dispose();
            dispose();
            new LoginScreen(gerenciarUsuarios);
        });

        btnNao.addActionListener(e -> desejaSair.dispose());

        desejaSair.setVisible(true);
    }
}
