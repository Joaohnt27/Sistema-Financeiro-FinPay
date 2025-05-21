import java.awt.Color;
import jakarta.persistence.*;
import org.hibernate.SessionFactory;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private String colorHex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    private SessionFactory sessionFactory;

    public Categoria() {}

    public Categoria( String nome, Color color, Usuario usuario) {
        this.nome = nome;
        this.setColor(color);
        this.usuario = usuario;
    }

    public long getId() { return id; }

    public String getNome() {
        return nome;
    }

    public Color getColor() {
        if (colorHex == null) {
            return Color.WHITE;
        }
        return Color.decode(colorHex);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setColor(Color color) {
        if (color == null) {
            color = Color.WHITE;
        }
        this.colorHex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return nome;
    }
}
