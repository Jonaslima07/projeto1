
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServidorRunnable implements Runnable {

    private final Socket socket;
    private final String clienteIdentificador;

    public ServidorRunnable(Socket socket, String clienteIdentificador) {
        this.socket = socket;
        this.clienteIdentificador = clienteIdentificador;
    }

    @Override
    public void run() {
        String ipCliente = socket.getInetAddress().getHostAddress();

        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream())); PrintStream saida = new PrintStream(socket.getOutputStream())) {
            saida.println("Menu:");
            saida.println("comando:1 - Data atual");
            saida.println("comando:2 - Hora atual");
            saida.println("comando:3 - Informações como: IP e porta do servidor, IP do cliente, mensagem se está ativo.");
            saida.println("comando:4 - Lista de IPs conectados ao servidor");
            saida.println("Digite seus comandos:");

            String mensagem;
            while ((mensagem = entrada.readLine()) != null) {
                System.out.println("[Servidor] Recebido de " + clienteIdentificador + ": " + mensagem);

                switch (mensagem.toLowerCase()) {
                    case "comando:1":
                    case "1":
                        saida.println("Data atual: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                        break;
                    case "comando:2":
                    case "2":
                        saida.println("Hora atual: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        break;
                    case "comando:3":
                    case "3":
                        saida.println("Servidor IP: " + socket.getLocalAddress().getHostAddress()
                                + ", Porta: " + socket.getLocalPort()
                                + ", Cliente IP: " + ipCliente
                                + " - Conexão ativa.");
                        break;
                    case "comando:4":
                    case "4":
                        saida.println("IPs conectados: " + Servidor.ipsConectados);
                        break;
                    default:
                        saida.println("Comando não reconhecido: " + mensagem);
                }

            }
        } catch (IOException e) {
            System.out.println("[ServidorThread] Conexão encerrada com " + clienteIdentificador);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("[ServidorThread] Erro ao fechar socket: " + e.getMessage());
            }
            Servidor.ipsConectados.remove(clienteIdentificador);
            System.out.println("[Servidor] Cliente " + clienteIdentificador + " desconectado.");
        }
    }
}
