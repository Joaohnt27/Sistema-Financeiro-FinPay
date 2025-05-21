import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Gerenciar os usuários do sistema
public class GerenciarUsuarios {

    private SessionFactory factory;

    private Usuario usuarioAtual;

    public GerenciarUsuarios() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Usuario.class).buildSessionFactory();
    }

    public void adicionarUsuario(Usuario usuario) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            session.save(usuario);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public Usuario buscarUsuario(String login) {
        Session session = factory.openSession();
        Usuario usuario = null;
        try {
            session.beginTransaction();
            usuario = session.createQuery("SELECT u FROM Usuario u LEFT JOIN FETCH u.categorias WHERE u.login = :login", Usuario.class).setParameter("login", login).uniqueResult();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return usuario;
    }

    public boolean autenticarUsuario(String login, String senha) {
        Usuario usuario = buscarUsuario(login);
        if (usuario != null && usuario.verificaSenha(senha)) {
            usuarioAtual = usuario;
            return true;
        }
        return false;
    }

    public Usuario getUsuarioAtual() {return usuarioAtual;}

    public void fechar() {
        factory.close();
    }

    public SessionFactory getSessionFactory() {
        return factory;
    }
}

