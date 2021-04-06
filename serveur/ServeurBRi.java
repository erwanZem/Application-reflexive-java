package serveur;


import java.io.*;
import java.lang.reflect.Constructor;
import java.net.*;


public class ServeurBRi implements Runnable {
	private ServerSocket listen_socket;
	private Class < ? extends ServiceServ> cl;
	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurBRi(int port,Class < ? extends ServiceServ> c) {
		this.cl=c;
		try {
			listen_socket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	//get instance d'un service avec get constructor on v�rifie que c'est bien un service 
	// Le serveur ecoute et accepte les connections.
	// pour chaque connection, il cree un ServiceInversion, 
	// qui va la traiter.
	public void run() {
		try {
			while(true) {
				Constructor<? extends ServiceServ> cons =  cl.getConstructor(Socket.class);
				ServiceServ serv = cons.newInstance(this.listen_socket.accept());
				serv.start();
			}
		}
		catch (IOException e) { 
			try {this.listen_socket.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'�coute :"+e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	// restituer les ressources --> finalize
	protected void finalize() throws Throwable {
		try {this.listen_socket.close();} catch (IOException e1) {}
	}

	// lancement du serveur
	public void lancer() {
		(new Thread(this)).start();
	}
}
