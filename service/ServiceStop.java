package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;

import bri.Service;
import briAma.ServiceRegistryAma;
import utilisateurs.Programmeur;

public class ServiceStop implements Service {
	private Socket client;
	private Programmeur p;
	public ServiceStop(Socket s,Programmeur p){
		this.client = s;
		this.p=p;
	}

	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader (new InputStreamReader(client.getInputStream()));
			out = new PrintWriter (client.getOutputStream(), true);


			out.println("bonjour "+p.getLogin()+"##"+ServiceRegistryAma.showStart()+"##Tapez le numéro de service que vous souhaitez arrêter(\"exit\" pour quitter) :##>");
			while(true) {
				try {
					String choix = in.readLine();
					if(choix.contentEquals("exit"))
						break;
					ServiceRegistryAma.stop(Integer.parseInt(choix));
					out.println("Votre service a bien été arrêter !");
				} catch (Exception e) {

					out.println("Nous n'avons pas pu stopper votre Service ##" + e +"veulliez ressayer##>");
				} 
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
}