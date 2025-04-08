import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CategoriaManager {
    private Usuario usuario;

    public CategoriaManager(Usuario usuario) {
        this.usuario = usuario;
    }

    public void adicionarCategoria(String nome, Color color) {
        usuario.adicionarCategoria(new Categoria(nome, color));
    }

    public void editarCategoria(int index, String nome, Color color) {
        List<Categoria> categorias = listarCategorias();
        if (index >= 0 && index < categorias.size()) {
            Categoria categoria = categorias.get(index);
            categoria.setNome(nome);
            categoria.setColor(color);
        }
    }

    public List<Categoria> listarCategorias() {
        return usuario.getCategorias();
    }

    // Renderer (sem alterações)
    public static class CategoriaRenderer extends JPanel implements ListCellRenderer<Categoria> {
        private JLabel label;

        public CategoriaRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel();
            label.setFont(new Font("Arial", Font.BOLD, 14));
            add(label, BorderLayout.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Categoria> list, Categoria categoria, int index, boolean isSelected, boolean cellHasFocus) {
            if (categoria != null) {
                label.setText(categoria.toString());
                label.setForeground(Color.BLACK);
                Color colorBack = categoria.getColor() != null ? categoria.getColor() : Color.WHITE;
                setBackground(isSelected ? list.getSelectionBackground() : colorBack);
            } else {
                label.setText("");
                setBackground(Color.WHITE);
            }
            return this;
        }
    }
}
