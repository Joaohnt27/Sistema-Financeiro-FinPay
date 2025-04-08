import java.util.Date;

public class Financas {
    private String descricao;
    private double valor;
    private Categoria categoria;
    private Date data;

    public Financas(String descricao, double valor, Categoria categoria, Date data) {
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Date getData() {
        return data;
    }

    // Mexer futuramente
    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "(" + categoria.getNome() + ") " + descricao + " - R$ " + valor;
    }
}
