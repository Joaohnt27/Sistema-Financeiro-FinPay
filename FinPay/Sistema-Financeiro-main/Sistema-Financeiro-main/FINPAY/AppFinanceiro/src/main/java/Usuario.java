import java.util.ArrayList;
import java.util.List;

public class Usuario {
    // Atributos para o usuário
    private String nome;
    private String login;
    private String senha;

    private List<Financas> financas = new ArrayList<>();
    private List<Categoria> categorias = new ArrayList<>();

    // Inicializando um novo usuário
    public Usuario(String nome, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    // Obter o nome do usuário
    public String getNome() {
        return nome;
    }

    // Obter o login do usuário
    public String getLogin() {
        return login;
    }

    // Método para verificação de senha
    public boolean verificaSenha(String senha) {
        return this.senha.equals(senha);
    }

    // Obter finanças de cada usuário - TRABALHAR NISSO AINDA
    public List<Financas> getFinancas() {
        return financas;
    }

    // Função para adicionar finanças por usuário
    public void adicionarFinanca(Financas f) {
        financas.add(f);
    }

    // Obter categorias de cada usuário - TRABALHAR NISSO AINDA
    public List<Categoria> getCategorias() {
        return categorias;
    }

    // Função de adicionar categoria por usuários
    public void adicionarCategoria(Categoria c) {
        categorias.add(c);
    }
}
