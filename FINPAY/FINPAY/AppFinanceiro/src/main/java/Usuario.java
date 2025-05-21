import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;


@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // Atributos para o usuário
    private String nome;
    private String login;
    private String senha;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
    private List<Financas> financas = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
    private List<Categoria> categorias = new ArrayList<>();

    public Usuario() {}

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
