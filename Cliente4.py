import socket

class ClienteSocket:
    def __init__(self, host='localhost', porta=4000):
        self.host = host
        self.porta = porta
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def conectar(self):
        try:
            self.socket.connect((self.host, self.porta))
            print("[Cliente] Conectado ao servidor.")
        except ConnectionRefusedError:
            print("[Cliente] Não foi possível conectar ao servidor. Verifique se ele está em execução.")
            return False
        return True

    def enviar_comando(self, comando):
        try:
            self.socket.sendall((comando + "\n").encode())
            resposta = self.socket.recv(1024).decode()
            return resposta.strip()
        except Exception as e:
            print(f"[Cliente] Erro ao enviar comando: {e}")
            return None

    def receber_mensagem(self):
        try:
            dados = self.socket.recv(1024).decode()
            return dados.strip()
        except Exception as e:
            print(f"[Cliente] Erro ao receber mensagem: {e}")
            return None

    def fechar_conexao(self):
        self.socket.close()
        print("[Cliente] Conexão encerrada.")

    def executar(self):
        if not self.conectar():
            return

       
        while True:
            mensagem = self.receber_mensagem()
            if not mensagem:
                print("[Cliente] Conexão encerrada pelo servidor.")
                return
            print(mensagem)

            if "Rejeitado" in mensagem:
                print("[Cliente] Conexão rejeitada pelo servidor.")
                self.fechar_conexao()
                return
            if "Digite seus comandos:" in mensagem:
                break  

       
        while True:
            comando = input("[Cliente] Digite um comando (ou 'sair' para encerrar): ").strip()
            if comando.lower() == 'sair':
                print("[Cliente] Encerrando conexão.")
                break
            resposta = self.enviar_comando(comando)
            if resposta is None:
                break
            print("[Servidor] " + resposta)

        self.fechar_conexao()

if __name__ == '__main__':
    cliente = ClienteSocket()
    cliente.executar()
