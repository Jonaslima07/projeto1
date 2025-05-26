import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 4000);
            PrintStream saida = new PrintStream(socket.getOutputStream());
            Scanner teclado = new Scanner(System.in);

            ClienteRunnable clienteRunnable = new ClienteRunnable(socket);
            Thread threadLeitura = new Thread(clienteRunnable);
            threadLeitura.start();

            String mensagem;
            while (true) {
                mensagem = teclado.nextLine();
                saida.println(mensagem);
                if (mensagem.equalsIgnoreCase("/sair")) {
                    System.out.println("[Cliente] Desconectando...");
                    break;
                }
            }

            saida.close();
            teclado.close();
            
        } catch (IOException e) {
            System.out.println("[Cliente] Erro: " + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("[Cliente] Erro ao fechar socket: " + e.getMessage());
                }
            }
            System.out.println("[Cliente] Socket fechado.");
        }
    }
}
