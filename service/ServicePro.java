package service;


import java.io.*;
import java.lang.reflect.Constructor;
import java.net.*;

import bri.Service;
import briPro.ProgrammeurBRI;
import serveur.ServiceServ;
import briPro.MenuPro;
import utilisateurs.Programmeur;


public class ServicePro extends ServiceServ {

	private Socket client;
	private Programmeur p;
	public ServicePro(Socket socket) {
		client = socket;
	}
	//Demande De connexion
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println("Bonjour Monsieur le programmeur ,j'espere que vous allez bien programmer##Avez vous un compte?(O ou N) ");

			boolean f=true;

			while(f) {
				char c=in.readLine().substring(0).charAt(0);
			
				switch (c) {
				case 'O' :{
					String username,pass;
					out.println("Quel est ton nom ?");
					while (true) {
						username = in.readLine();
						out.println("quel est ton mot de passe");
						pass = in.readLine();
						try {
							this.p = ProgrammeurBRI.IsInscr(username, pass);
							f = false;
							break;
						} catch (Exception e) {
							out.println(e + "##Nous n'avons pas reussi à vous connecter, veuillez ressayer");
						}

					}
					break;
				}
				case 'N' :
				{
					String username,pass,url;
					while (true) {
						out.println("Quel est ton nom ?");
						username = in.readLine();
						out.println("Quel est ton mot de passe :");
						pass = in.readLine();
						out.println("Veuillez indiquer le lien de votre serveur ftp :");
						url = in.readLine();
						try {
							this.p = ProgrammeurBRI.newProgrammeur(username, pass, url);
							f = false;
							break;
						} catch (Exception e) {
							out.println(e + "##Nous n'avons pas reussi à vous inscrire, veuillez ressayer");
						}

					}
					break;
				}
				// faire le code d'inscription
				default :
					out.println("ecrivez O ou N en majuscule ");

				}
			} 
			System.out.println("ici avant a la co");
			out.println(MenuPro.toStringue()+"##Tapez le numéro de service désiré :");
			System.out.println("ici apres a la co");
			while (true) {
				int choix = Integer.parseInt(in.readLine());
				Service s = null;
				// instancier le service numéro "choix" en lui passant la socket "client"
				try {
					Class<?> cl = MenuPro.getServiceClass(choix);
					Constructor<?> cons = cl.getConstructor(Socket.class, Programmeur.class);// avec un programmeur pour unifier 
					s = (Service) cons.newInstance(client, p);
					s.run();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					out.println("Ce Service est indisponible pour le moment , nos programmeur de talent( tel que audran.exe) est sur le coup");
				} 
			}
		}
		catch (IOException e) {
			//Fin du service
		}
		try {client.close();} catch (IOException e2) {}
	}

	protected void finalize() throws Throwable {
		client.close(); 
	}

	// lancement du service


}
