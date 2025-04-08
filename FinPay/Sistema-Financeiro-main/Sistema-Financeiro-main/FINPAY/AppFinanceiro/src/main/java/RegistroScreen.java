import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class RegistroScreen extends JFrame {

    public RegistroScreen(GerenciarUsuarios gerenciadorUsuarios) {
        setTitle("Registrar Usuário");
        setSize(400, 300);
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

        // Layout do painel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Espaçamento entre os componentes
        gbc.gridx = 0; // Define a coluna sempre como 0
        gbc.anchor = GridBagConstraints.WEST; // Alinha os textos à esquerda

        // Espaço para "Nome"
        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField(15);
        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(lblNome, gbc);
        gbc.gridx = 1;
        panel.add(txtNome, gbc);

        // Espaço para "Nome de Usuário"
        JLabel lblUsuario = new JLabel("Nome de Usuário:");
        JTextField txtUsuario = new JTextField(15);
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(lblUsuario, gbc);
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);

        // Espaço para "Senha"
        JLabel lblSenha = new JLabel("Senha:");
        JPasswordField txtSenha = new JPasswordField(15);
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(lblSenha, gbc);
        gbc.gridx = 1;
        panel.add(txtSenha, gbc);

        // Botão Cadastrar
        JButton btnRegistrar = new JButton("Cadastrar");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Ocupa duas colunas
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnRegistrar, gbc);

        // Botão Voltar
        JButton btnVoltar = new JButton("Voltar");
        gbc.gridy = 4;
        panel.add(btnVoltar, gbc);

        // Método do botão registrar
        btnRegistrar.addActionListener(e -> {
            String nome = txtNome.getText();  // Nome do usuário
            String usuario = txtUsuario.getText(); // Login do usuário
            String senha = new String(txtSenha.getPassword()); // Senha

            if (!nome.isEmpty() && !usuario.isEmpty() && !senha.isEmpty()) {
                gerenciadorUsuarios.adicionarUsuario(nome, usuario, senha); // Agora passa 3 parâmetros
                JOptionPane.showMessageDialog(null, "Usuário registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Fecha a tela de registro
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Evento do botão "Voltar"
        btnVoltar.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }
}
