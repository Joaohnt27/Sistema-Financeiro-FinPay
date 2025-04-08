import java.awt.Color;

public class Categoria {
    private String nome;
    private Color color;

    public Categoria( String nome, Color color) {
        this.nome = nome;
        this.color = (color != null) ? color : Color.WHITE; //Cor padrão caso não houver mudança
    }

    public String getNome() {
        return nome;
    }

    public Color getColor() {
        return color;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return nome;
    }
}
