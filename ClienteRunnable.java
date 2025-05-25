import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClienteRunnable implements Runnable {

    private final Socket socket;

    public ClienteRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String mensagem;
            while ((mensagem = reader.readLine()) != null) {
                if ("OK".equals(mensagem)) {
                    System.out.println("[Cliente] Conectado ao servidor!");
                } else {
                    System.out.println("[Servidor] " + mensagem);
                }
            }
        } catch (IOException e) {
            System.out.println("[ClienteRunnable] Erro: " + e.getMessage());
        } finally {
            System.out.println("[ClienteRunnable] Encerrando thread de leitura.");
        }
    }
}
