package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;

import bri.Service;
import briAma.ServiceRegistryAma;
import utilisateurs.Programmeur;

public class ServiceDemarrer implements Service {
	private Socket client;
	private  Programmeur p;
	public ServiceDemarrer(Socket s,Programmeur p){
		this.client = s;
		this.p=p;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {

			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter (client.getOutputStream(), true);
			out.println("Bonjour"+p.getLogin()+"##"+ServiceRegistryAma.showStop()+"##Tapez le numéro de service que vous souhaitez démarrer (\"exit\" pour quitter) :##>");
			while (true) {
				try {
					String choix = in.readLine();
					if(choix.contentEquals("exit"))
						break;
					Class<?> cl = ServiceRegistryAma.getServiceClass(Integer.parseInt(choix));
					ServiceRegistryAma.start(Integer.parseInt(choix));
					out.println("Votre service a bien été démarrer !");
					break;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					out.println("Nous n'avons pas réussi a redémarrer votre service##"+e+"Veuillez ressayer:##>");
				} 
			} 
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
