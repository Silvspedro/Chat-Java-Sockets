package chat_sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorDeClientes extends Thread {

	private Socket cliente;
	private String nomeCliente;
	private BufferedReader leitor;
	private PrintWriter escritor;
	private static final Map<String, GerenciadorDeClientes> clientes = new HashMap<String, GerenciadorDeClientes>();

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
			leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			escritor = new PrintWriter(cliente.getOutputStream(), true);
			escritor.println("Por favor escreva seu nome");
			String msg = leitor.readLine();
			this.nomeCliente = msg;
			escritor.println("Olá " + this.nomeCliente);
			escritor.println("o que voce precisa?");
			clientes.put(this.nomeCliente, this);

			while (true) {
				msg = leitor.readLine();
				if (msg.equalsIgnoreCase("::Sair")) {
					this.cliente.close();
				} else if (msg.toLowerCase().startsWith("::msg")) {
//					clientes.get(msg.substring(5, msg.length()));
					String nomeDestinatario = msg.substring(5, msg.length());
					System.out.println("Enviado para " + nomeDestinatario);
					GerenciadorDeClientes destinatario = clientes.get(nomeDestinatario);
					if (destinatario == null) {
						escritor.println(" O cliente informado não existe");
					}else {
						escritor.println("Digite uma mensagem para: " + destinatario.getNomeCliente());
						destinatario.getEscritor().println(this.nomeCliente + "Disse: " + leitor.readLine());
					}
				}
				else {
					escritor.println(this.nomeCliente + ", Disse: " + msg);
				}

			}

		} catch (IOException e) {
			System.err.println("O cliente fechou a conexão");
		}
		super.run();
	}

	public PrintWriter getEscritor() {
		return escritor;
	}

//	public BufferedReader getLeitor() {
//		return leitor;
//	}
	
	public String getNomeCliente() {
		return nomeCliente;
	}
}
