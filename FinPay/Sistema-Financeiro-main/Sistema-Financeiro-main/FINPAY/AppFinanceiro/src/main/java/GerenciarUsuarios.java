import java.util.HashMap;
import java.util.Map;

// Gerenciar os usuários do sistema
public class GerenciarUsuarios {
    // Mapa que armazena os usuários que foram cadastrados, onde a chave é o login e o valor é o objeto Usuario.
    private Map<String, Usuario> usuarios = new HashMap<>();
    // Armazena o usuário que está utilizando o sistema
    private Usuario usuarioAtual;

    // Adiciona um novo usuário ao sistema
    public void adicionarUsuario(String nome, String login, String senha) {
        usuarios.put(login, new Usuario(nome, login, senha));
    }

    // Autenticar um usuário com base no login e senha fornecidos
    public boolean autenticarUsuario(String login, String senha) {
        Usuario usuario = usuarios.get(login); // Busca o usuário pelo login

        // verifica se o usuário existe e se a senha está correta
        if (usuario != null && usuario.verificaSenha(senha)) {
            usuarioAtual = usuario; // define o usuário que foi autenticado como o usuário atual
            return true; // Login realizado
        }
        return false;
    }

    // Obter o usuário que está atualmente autenticado
    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }
}
