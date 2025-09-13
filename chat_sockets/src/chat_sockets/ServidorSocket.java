package chat_sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket {
	
	public static void main(String[] args) {

		ServerSocket servidor = null;
		try {
			System.out.println("Iniando servidor...");
			servidor = new ServerSocket(3020);
			System.out.println("Servidor Iniciado");
			
			while (true) {
				Socket cliente = servidor.accept();
				new GerenciadorDeClientes(cliente);
			}
		} catch (IOException e) {

			try {
				if (servidor != null)
					servidor.close();
			} catch (IOException e1) {
			}
			System.err.println("A porta estaa em uso ou o servidor foi fechado");
			e.printStackTrace();
		}

	}
}
