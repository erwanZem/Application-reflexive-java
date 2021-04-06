package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import bri.Service;
import briAma.ServiceRegistryAma;
import utilisateurs.Programmeur;

public class ServiceDesinstaller implements Service{
	private Socket client;
	private Programmeur p;
	public ServiceDesinstaller(Socket c, Programmeur p) {
		this.client=c;
		this.p=p;
	}

	@Override
	public void run() {
		PrintWriter out=null;
		BufferedReader in=null;
		String adresseftp=p.getUrl();
		try {
			in= new BufferedReader (new InputStreamReader(client.getInputStream()));
			out = new PrintWriter (client.getOutputStream (), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.println("bonjour"+p.getLogin()+"##Tapez le nom du service que vous voulez désinstaller (\"exit\" pour quitter):##>");
		while(true) {
			String choix;
			try {
				choix = in.readLine();
				if(choix.equalsIgnoreCase("exit")) {
					break;
				}
				URL[] urls = {new URL(adresseftp)};
				URLClassLoader classLoader = new URLClassLoader(urls);
				Class<? extends Service> s = classLoader.loadClass(choix).asSubclass(Service.class);
				ServiceRegistryAma.Désinstaller(s);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				out.println(e+"##Veuillez ressayer##>");
			}
		}

	}

}
