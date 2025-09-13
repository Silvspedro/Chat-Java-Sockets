package chat_sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GerenciadorDeClientes extends Thread{

	private Socket cliente;
	private String nomeCliente;

	public GerenciadorDeClientes(Socket cliente) {
		this.cliente = cliente;
		start();
		
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		try {
			BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			PrintWriter escritor = new PrintWriter(cliente.getOutputStream(), true);
			escritor.println("Por favor escreva seu nome");
			String msg = leitor.readLine();
			this.nomeCliente = msg;
			escritor.println("Olá " + this.nomeCliente);
			escritor.println("o que voce precisa?");			
			
			while (true) {
				msg = leitor.readLine();
				if (msg.equalsIgnoreCase("::Sair")) {
					this.cliente.close();
				}else {
					escritor.println(this.nomeCliente + ", Disse: " + msg);
				}
				
			}
			
		} catch (IOException e) {
			System.err.println("O cliente fechou a conexão");
		}
		super.run();
	}
}
