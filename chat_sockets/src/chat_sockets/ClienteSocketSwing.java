package chat_sockets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClienteSocketSwing extends JFrame {
	private JTextArea taEditor = new JTextArea("Digite aqui sua mensagem: \n");
	private JTextArea taVisor = new JTextArea();
	private JList liUsuarios = new JList();
	private JScrollPane scrollTaVisor = new JScrollPane(taVisor);
	private PrintWriter escritor;
	private BufferedReader leitor;

	public ClienteSocketSwing() { // Parte da Interface
		setTitle("Chat com sockets");
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		liUsuarios.setBackground(Color.GRAY);
		taEditor.setBackground(Color.green);
		taEditor.setPreferredSize(new Dimension(600, 60));
//		taVisor.setPreferredSize(new Dimension(350, 100));
		taVisor.setEditable(false);
		liUsuarios.setPreferredSize(new Dimension(100, 140));
		add(taEditor, BorderLayout.SOUTH);
		add(scrollTaVisor, BorderLayout.CENTER);
		add(new JScrollPane(taVisor), BorderLayout.CENTER);
		add(new JScrollPane(liUsuarios), BorderLayout.WEST);
//		setSize(350, 150);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String[] usuarios = new String[] { "Mateus", "Pedro", "João" };
		preencherListaUsuarios(usuarios);

	}

	private void preencherListaUsuarios(String[] usuarios) {
		DefaultListModel modelo = new DefaultListModel();
		liUsuarios.setModel(modelo);
		for (String usuario : usuarios) {
			modelo.addElement(usuario);
		}

	}

	private void iniciarEscritor() {
		taEditor.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					if (taVisor.getText().isEmpty()) {
						return;
					}
					Object usuario = liUsuarios.getSelectedValue();
					if (usuario != null) {
						taVisor.append("Eu: ");
						taVisor.append(taEditor.getText());
						taVisor.append("\n");
						escritor.println(Comandos.MENSAGEM + usuario);
						escritor.println(taEditor.getText());
						taEditor.setText("");
						e.consume();

					} else {
						if (taVisor.getText().equalsIgnoreCase(Comandos.SAIR)) {
							System.exit(0);
						}
						JOptionPane.showMessageDialog(ClienteSocketSwing.this, "Por favor selecione um usuário.");
						return;
					}

				}

			}
		});
	}

	public static void main(String[] args) {
		ClienteSocketSwing cliente = new ClienteSocketSwing();
		cliente.iniciarChat();
		cliente.iniciarEscritor();
		cliente.iniciarLeitor();
//		cliente.atualizarListaUsuarios();

	}

	private void atualizarListaUsuarios() {
		escritor.println(Comandos.LISTA_USUARIOS);
	}

	private void iniciarLeitor() {
		try {

			while (true) {
				String mensagem = leitor.readLine();
				if (mensagem == null || mensagem.isEmpty())
					continue;
				
				if (mensagem.equals(Comandos.LISTA_USUARIOS)) {
					String[] usuarios = leitor.readLine().split(",");
					preencherListaUsuarios(usuarios);
				} else if (mensagem.equals(Comandos.LOGIN)) {
					String login = JOptionPane.showInputDialog("Qual o seu login?");
					escritor.println(login);
				} else if (mensagem.equals(Comandos.LOGIN_NEGADO)) {
					JOptionPane.showMessageDialog(ClienteSocketSwing.this, "O login é inválido");
				} else if (mensagem.equals(Comandos.LOGIN_ACEITO)) {
					atualizarListaUsuarios();
				} else {
					taVisor.append(mensagem);
					taVisor.append("\n");
					taVisor.setCaretPosition(taVisor.getDocument().getLength());
					

				}
			}

		} catch (IOException e) {
			System.out.println("Impossivel ler a mensagem do servidor");
			e.printStackTrace();
		}
	}

	private DefaultListModel getListaUsuarios() {
		return (DefaultListModel) liUsuarios.getModel();
	}

	public void iniciarChat() {
		try {
			final Socket cliente = new Socket("127.0.0.1", 9998);
			escritor = new PrintWriter(cliente.getOutputStream(), true);
			leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

		} catch (UnknownHostException e) {
			System.out.println("O endereço inserido é invalido");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("O servidor pode estar fora do ar");
			e.printStackTrace();
		}
	}
}
