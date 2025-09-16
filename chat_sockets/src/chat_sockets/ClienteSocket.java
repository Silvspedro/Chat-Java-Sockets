package chat_sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteSocket {

	public static void main(String[] args) {
		try {
			final Socket cliente = new Socket("127.0.0.1", 9998);

			cliente.getInputStream();

			new Thread() {

				public void run() {
					try {
						BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

						while (true) {
							String mensagem = leitor.readLine();
							if(mensagem == null || mensagem.isEmpty())
								continue;
							System.out.println("O servidor disse: " + mensagem);
						}

					} catch (IOException e) {
						System.out.println("Impossivel ler a mensagem do servidor");
						e.printStackTrace();
					}
				}
			}.start();

			PrintWriter escritor = new PrintWriter(cliente.getOutputStream(), true);
			BufferedReader leitorTerminal = new BufferedReader(new InputStreamReader(System.in));
			String mensagemTerminal = "" ;
			while ( true ) {
				mensagemTerminal  = leitorTerminal.readLine();
				if(mensagemTerminal == null || mensagemTerminal.length() == 0) {
					continue;
				}
				escritor.println(mensagemTerminal);
				if (mensagemTerminal.equalsIgnoreCase("::Sair")) {
					System.exit(0);
				}
			}
			
			
		} catch (UnknownHostException e) {
			System.out.println("O endereço inserido é invalido");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("O servidor pode estar fora do ar");
			e.printStackTrace();
		}
	}
}
