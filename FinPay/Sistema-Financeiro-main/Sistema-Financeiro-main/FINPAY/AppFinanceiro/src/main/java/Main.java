public class Main {
    public static void main(String[] args) {
        // Criação de usuários
        GerenciarUsuarios gerenciador = new GerenciarUsuarios();
        gerenciador.adicionarUsuario("Bruno de Azevedo Mendonca", "bruno", "boanoitebruno");
        gerenciador.adicionarUsuario("Arthur Vital Fontana", "vital", "boanoitebruno");
        gerenciador.adicionarUsuario("Rafael Mele Porto", "leila", "boanoitebruno");
        gerenciador.adicionarUsuario("João Henrique Nazar Tavares", "joaohenrique", "boanoitebruno");
        gerenciador.adicionarUsuario("JContel", "jcontel", "esqueci");

        new LoginScreen(gerenciador); // Executa a tela de login
    }
}
