import org.hibernate.SessionFactory;
import java.util.Date;
import java.util.List;

public class FinancasManager {
    private Usuario usuario;
    private SessionFactory sessionFactory;

    public FinancasManager(Usuario usuario, SessionFactory sessionFactory) {
        this.usuario = usuario;
        this.sessionFactory = sessionFactory;
    }

    public void cadastrarFinanca(String descricao, double valor, Categoria categoria, Date data) {
        Financas novaFinanca = new Financas(descricao, valor, categoria, data, usuario);
        usuario.adicionarFinanca(novaFinanca);
    }

    public List<Financas> listarFinancas() {
        return usuario.getFinancas();
    }

    public void excluirFinanca(int index) {
        List<Financas> financas = usuario.getFinancas();
        if(index >= 0 && index < financas.size()) {
            Financas fin = financas.get(index);
            financas.remove(index);
        }
    }

    public void editarFinanca(int index, Financas editado) {
        List<Financas> financas = usuario.getFinancas();
        if(index >= 0 && index < financas.size()) {
            Financas original = financas.get(index);
            original.setDescricao(editado.getDescricao());
            original.setValor(editado.getValor());
            original.setCategoria(editado.getCategoria());
            original.setData(editado.getData());
        }
    }
}
