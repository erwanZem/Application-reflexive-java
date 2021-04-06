package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import bri.Service;
import utilisateurs.Programmeur;

public class ServiceAdresse implements Service {
	private Socket client;
	private Programmeur p;
	public ServiceAdresse(Socket s,Programmeur p){
		this.client=s;
		this.p=p;
	}
	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter (client.getOutputStream (), true);
			out.println("Bonjour "+p.getLogin()+"##Quel est la nouvelle adresse de votre serveur ftp(avec \\\"exit\\\" vous pouvez quitter) ?##>");
			boolean t=true;
			String url =null;
			while (t) {
				url = in.readLine();
				if(url.contentEquals("exit"))
					break;
				out.println("voulez vraiment remplacer votre adresse actuelle "+p.getUrl()+" par cette nouvelle adresse(o ou n)##"+url);
				char a=in.readLine().charAt(0);
				switch(a) {
				case'o' :{
					t=false;
					break;}
				case'O' :{
					t=false;
					break;}
				case 'n':{
					out.println("Quel est la nouvelle adresse de votre serveur ftpO");
					break;
				}
				case 'N':{
					out.println("Quel est la nouvelle adresse de votre serveur ftpO");
					break;}
				default:{
					out.println("veuillez saisire o ou n ");
					break;
				} 
				}
			}
			if (!url.contains("ftp"))
				throw new Exception("L'adresse de votre serveur ftp doit contenir \"ftp\" dans votre url");
			if (!url.contains(p.getLogin()))
				throw new Exception("L'adresse de votre serveur doit contenir votre Login donc "+p.getLogin());
			try {
				p.setUrl(new URL(url));
				out.println("adresse ftp changer avec succes##"+p.getUrl());

				//ftp...
				//adresse ftp point vers le package du programmeur, 
			} catch(Exception e1) {
				throw new Exception("Impossible de changer votre lien de votre serveur ftp");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
