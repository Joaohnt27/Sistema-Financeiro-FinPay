import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.awt.event.KeyEvent;

public class LoginScreen extends JFrame {

    private GerenciarUsuarios gerenciadorUsuarios;

    public LoginScreen(GerenciarUsuarios gerenciadorUsuarios) {
        this.gerenciadorUsuarios = gerenciadorUsuarios; // Inicializa o gerenciador de usuários

        setTitle("FinPay"); // Título da tela
        setSize(600, 450); // Tamanho da aplicação
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha a aplicação ao sair
        setLocationRelativeTo(null); // Centraliza a janela na tela

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

        // Configurações da tela
        setLayout(new BorderLayout()); // Define o layout principal

        // Textos que serão exibidos na tela, sem as configurações de GridBagConstraints
        JLabel tituloLogin = new JLabel("Bem-vindo ao FinPay, seu aliado nas finanças!", SwingConstants.CENTER);
        tituloLogin.setFont(new Font("Arial", Font.BOLD, 20));
        add(tituloLogin, BorderLayout.NORTH); // Adiciona o título no topo da tela

        JLabel rodapeLogin = new JLabel("@2025 - Desenvolvido por: Arthur Vital Fontana, João Henrique Nazar Tavares e Rafael Mele Porto", SwingConstants.CENTER);
        rodapeLogin.setFont(new Font("Arial", Font.BOLD, 10));
        add(rodapeLogin, BorderLayout.SOUTH); // Adiciona o rodapé na parte inferior da tela

        // Componentes de login que aparecem na tela, com as configurações de GridBagConstraints
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os componentes

        // Componentes da tela
        JLabel lblUsuario = new JLabel("Usuário:");
        JTextField txtUsuario = new JTextField(15);
        JLabel lblSenha = new JLabel("Senha:");
        JPasswordField txtSenha = new JPasswordField(15);
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setMnemonic(KeyEvent.VK_S);

        // Posicionamento dos componentes
        // Texto "Usuário:"
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsuario, gbc);

        // Caixa de texto da aba "Usuário"
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);

        // Texto "Senha"
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblSenha, gbc);

        // Caixa de texto da aba "Senha"
        gbc.gridx = 1;
        panel.add(txtSenha, gbc);

        // Botão de "LOGIN"
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        // Evento do botão "LOGIN"
        btnLogin.addActionListener(event -> {
            String usuario = txtUsuario.getText();
            String senha = new String(txtSenha.getPassword());

            if (gerenciadorUsuarios.autenticarUsuario(usuario, senha)) {
                JOptionPane.showMessageDialog(null, "Seja bem-vindo, " + gerenciadorUsuarios.getUsuarioAtual().getNome() + "!");

                dispose(); // Fecha a tela de login

                // Abrir a tela principal do sistema
                new FinanceiroScreen(gerenciadorUsuarios).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Torna o botão ENTER padrão ao pressionar
        getRootPane().setDefaultButton(btnLogin);

        // Criar botão "Registrar"
        JButton btnRegistrar = new JButton("REGISTRAR-SE");

        // Evento do botão "Registrar"
        btnRegistrar.addActionListener(event -> new RegistroScreen(gerenciadorUsuarios));

        // Posicionar o botão "Registrar" na tela
        gbc.gridy = 3;
        panel.add(btnRegistrar, gbc);

        add(panel);
        setVisible(true); // Tornar a tela visível
    }
}
