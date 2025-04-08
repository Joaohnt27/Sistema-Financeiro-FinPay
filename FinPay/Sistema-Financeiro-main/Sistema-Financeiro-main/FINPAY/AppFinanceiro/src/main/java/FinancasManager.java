import java.util.Date;
import java.util.List;

public class FinancasManager {
    private Usuario usuario;

    public FinancasManager(Usuario usuario) {
        this.usuario = usuario;
    }

    public void cadastrarFinanca(String descricao, double valor, Categoria categoria, Date data) {
        usuario.adicionarFinanca(new Financas(descricao, valor, categoria, data));
    }

    public List<Financas> listarFinancas() {
        return usuario.getFinancas();
    }
}
