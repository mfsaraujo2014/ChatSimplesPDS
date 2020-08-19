package Chat.java;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Cliente {
	JFrame janela_chat=null;
	JButton btn_enviar=null;
	JTextField txt_mensagem=null;
	JTextArea area_chat=null;
	JPanel conteudo_areachat=null;
	JPanel conteudo_btntxt=null;
	JScrollPane scroll=null;
	Socket socket=null;
	BufferedReader leitor=null;
	PrintWriter escritor=null;
	
	public Cliente() {
		FazerInterface();
	}
	
	public void FazerInterface() {
		janela_chat = new JFrame("Cliente");
		btn_enviar = new JButton("Enviar");
		txt_mensagem = new JTextField(4);
		area_chat = new JTextArea(20,24);
		scroll = new JScrollPane(area_chat);
		conteudo_areachat = new JPanel();
		conteudo_areachat.setLayout(new GridLayout(1,1));
		conteudo_areachat.add(scroll);
		conteudo_btntxt = new JPanel();
		conteudo_btntxt.setLayout(new GridLayout(1,2));
		conteudo_btntxt.add(txt_mensagem);
		conteudo_btntxt.add(btn_enviar);
		janela_chat.setLayout(new BorderLayout());
		janela_chat.add(conteudo_areachat, BorderLayout.NORTH);
		janela_chat.add(conteudo_btntxt, BorderLayout.SOUTH);
		janela_chat.setSize(600, 420);
		janela_chat.setVisible(true);
		janela_chat.setResizable(false);
		janela_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Thread principal = new Thread(new Runnable() {
			public void run() {
				try {
				socket = new Socket("localhost", 9000);
				ler();
				escrever();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		principal.start();
	}
	
	public void ler() {
		Thread ler_fio = new Thread(new Runnable() {
			public void run() {
				try {
					leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						area_chat.append("---------------------Conexão estabelecida!---------------------"+"\n");
						area_chat.append("----------------Para sair do chat digite 'exit'----------------"+"\n");
						area_chat.append("---Para enviar uma mensagem clique com o mouse em 'Enviar'---" + "\n" );
							while(true) {
								String mensagem_recebida = leitor.readLine();
								area_chat.append("Servidor:"+mensagem_recebida+"\n");
							}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		ler_fio.start();
	}
	
	public void escrever() {
		Thread escrever_fio = new Thread(new Runnable() {
			public void run() {
				try {
					escritor = new PrintWriter(socket.getOutputStream(),true);
					btn_enviar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e){
							String enviar_mensagem = txt_mensagem.getText();
							escritor.println(enviar_mensagem);
							txt_mensagem.setText("");
							area_chat.append("Cliente:"+enviar_mensagem+"\n");
							if(enviar_mensagem.equalsIgnoreCase("exit")) {
								System.exit(0);
							}
							}
					});
				} catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		escrever_fio.start();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Cliente();
	}

}
