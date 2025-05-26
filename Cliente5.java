import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente5 {

    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 4000);
            PrintStream saida = new PrintStream(socket.getOutputStream());
            Scanner teclado = new Scanner(System.in)
        ) {
            System.out.println("[Cliente] Conectado ao servidor!");

           
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
        } catch (IOException e) {
            System.out.println("[Cliente] Erro: " + e.getMessage());
        } finally {
            System.out.println("[Cliente] Socket fechado.");
        }
    }
}
