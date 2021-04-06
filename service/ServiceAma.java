package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;

import bri.Service;
import briAma.ServiceRegistryAma;
import serveur.ServiceServ;

public class ServiceAma  extends ServiceServ {
	
	private Socket client;

	public ServiceAma(Socket socket) {
		client = socket;

	}
	
	//Instance du ServiceAma
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter (client.getOutputStream (), true);
			out.println(ServiceRegistryAma.toStringue()+"##Tapez le numéro de service désiré :");
			int choix = Integer.parseInt(in.readLine());
			Service s=null;
			// instancier le service numéro "choix" en lui passant la socket "client"
			try {
				Class<?> cl = ServiceRegistryAma.getServiceClass(choix);
				Constructor<?> c =cl.getConstructor(Socket.class);
				s=(Service) c.newInstance(client);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// invoquer run() pour cette instance ou la lancer dans un thread à part 
			s.run();

		}
		catch (IOException e) {
			//Fin du service
		}

		try {client.close();} catch (IOException e2) {}
	}

	protected void finalize() throws Throwable {
		client.close(); 
	}
}
	//e
	