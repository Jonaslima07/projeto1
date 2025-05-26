import socket

def cliente3():
    host = '127.1.0.1'
    porta = 4000

    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((host, porta))
            print("[Cliente] Conectado ao servidor.")

            sock_file = s.makefile('r')

           
            while True:
                linha = sock_file.readline()
                if not linha:
                    break
                print(linha.strip())
                if "Digite seus comandos:" in linha:
                    break

            while True:
                comando = input("[Cliente] Digite um comando (ou 'sair' para encerrar): ").strip()
                if comando.lower() == 'sair':
                    print("[Cliente] Encerrando conexão.")
                    break

                s.sendall((comando + "\n").encode())  # **IMPORTANTE: + "\n"**

                resposta = sock_file.readline()
                if not resposta:
                    print("[Cliente] Conexão encerrada pelo servidor.")
                    break
                print("[Servidor] " + resposta.strip())

    except ConnectionRefusedError:
        print("[Cliente] Não foi possível conectar ao servidor. Verifique se ele está em execução.")
    except Exception as e:
        print(f"[Cliente] Erro: {e}")

if __name__ == '__main__':
    cliente3()
