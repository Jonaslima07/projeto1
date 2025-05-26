import socket

def cliente3():
    host = 'localhost'
    porta = 4000

    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((host, porta))

            sock_file = s.makefile('r')

            while True:
                linha = sock_file.readline()
                if not linha:
                    print("[Cliente] Conexão encerrada pelo servidor.")
                    return
                print(linha.strip())

               
                if linha.lower().startswith("rejeitado"):
                    print("[Cliente] Conexão rejeitada pelo servidor.")
                    return

                if "Digite seus comandos:" in linha:
                    break

            
            print("[Cliente] Conectado ao servidor.")

            while True:
                comando = input("[Cliente] Digite um comando (ou 'sair' para encerrar): ").strip()
                if comando.lower() == 'sair':
                    print("[Cliente] Encerrando conexão.")
                    break

                s.sendall((comando + "\n").encode())

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
