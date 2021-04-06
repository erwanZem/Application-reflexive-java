package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import bri.Service;
import briAma.ServiceRegistryAma;
import utilisateurs.Programmeur;

public class ServiceMAj implements Service{
	private Socket client;
	private Programmeur p;
	public ServiceMAj(Socket cl,Programmeur p){
		this.client=cl;
		this.p=p;
	}
	@Override
	public void run() {
		PrintWriter out=null;
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream()));
			out = new PrintWriter (client.getOutputStream (), true);
			out.println("bonjour "+p.getLogin()+"##Quel Service voulez mettre a jour(avec \"exit\" vous pouvez quitter) ?##>");
			URLClassLoader urlcl = null;
			while (true) {
				try {
					String choix = in.readLine();
					if(choix.contentEquals("exit"))
						break;
					String fileDirString = p.getUrl().toString();
					URL fileDir= new URL(fileDirString);
					URL[] TabU =  {fileDir};
					urlcl = new URLClassLoader(TabU);
					Class<? extends Service> cl  = urlcl.loadClass(choix).asSubclass(Service.class);
					ServiceRegistryAma.MAJ(cl);
					out.println("Le service " +choix+ " a bien été mis a jour");
					urlcl.close();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					out.println("Le Service Séléctionner n'a pas pu être mis a jour##"+"a cause de: "+e+"##veuillez ressayer##>");
				}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}
