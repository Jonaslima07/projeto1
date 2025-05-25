import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Servidor {

    private static final int Porta = 4000;
    private static final int Clientes = 5;

    static final Set<String> ipsConectados = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try (ServerSocket servidorSocket = new ServerSocket(Porta)) {
            System.out.println("[Servidor] Aguardando conexões na porta " + Porta + "...");

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                String ipCliente = clienteSocket.getInetAddress().getHostAddress();
                System.out.println("[Servidor] IP do cliente: " + ipCliente);

                synchronized (ipsConectados) {
                    if (ipsConectados.contains(ipCliente)) {
                        System.out.println("[Servidor] " + ipCliente + " já está conectado. Conexão recusada.");
                        PrintStream saida = new PrintStream(clienteSocket.getOutputStream());
                        saida.println("Rejeitado!!!: IP já conectado");
                        clienteSocket.close();
                        continue;
                    }

                    if (ipsConectados.size() >= Clientes) {
                        System.out.println("[Servidor] Limite máximo de clientes atingido.");
                        PrintStream saida = new PrintStream(clienteSocket.getOutputStream());
                        saida.println("REJEITADO: Limite máximo de clientes atingido");
                        clienteSocket.close();
                        continue;
                    }

                    ipsConectados.add(ipCliente);
                    PrintStream saida = new PrintStream(clienteSocket.getOutputStream());
                    saida.println("OK");  // confirma conexão aceita

                    System.out.println("[Servidor] Cliente conectado: " + ipCliente);

                    ServidorRunnable tarefa = new ServidorRunnable(clienteSocket, ipCliente);
                    Thread threadCliente = new Thread(tarefa);
                    threadCliente.start();
                }
            }
        } catch (IOException e) {
            System.out.println("[Servidor] Erro: " + e.getMessage());
        }
    }
}
