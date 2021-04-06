package briPro;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import utilisateurs.Programmeur;

public  class  ProgrammeurBRI {
	private static  HashMap <String, Programmeur> Programmeurs;
	static {
		Programmeurs=new HashMap<String, Programmeur>();
		try {
			Programmeurs.put("root", new Programmeur("root", "root", new URL("ftp://localhost:2121/classes/root/")));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private ProgrammeurBRI() {

	}
	public static void addProgrammeur (Programmeur p){
		Programmeurs.putIfAbsent(p.getLogin(), p);
	}
	public static Programmeur IsInscr (String user,String mpd) throws Exception {
		Programmeur p;
		try {
			p=Programmeurs.get(user);
		}
		catch (Exception e){
			throw new Exception("utilisateur inexistant");
		}
		if(p.getPwd().contentEquals(mpd))
			return p;
		else 
			throw new Exception("Mauvais mot de passe");
	}
	public static Programmeur newProgrammeur (String user, String mdp, String url) throws Exception {
		Programmeur p;
		if(Programmeurs.containsValue(user))
			throw new Exception("Utilisateur déjà inscrit");
		if(mdp.isEmpty())
			throw new Exception("Mot de passe vide, veuillez au moins mettre un caractère");
		if(url.isEmpty())
			throw new Exception("Lien du serveur ftp vide, veuillez le remplir correctement");
		if (!url.contains("ftp"))
			throw new Exception("L'adresse de votre serveur ftp doit contenir \"ftp\" dans votre url");
		if (!url.contains(user))
			throw new Exception("L'adresse de votre serveur doit contenir votre Login donc "+user);
		try {
			p = new Programmeur(user,mdp,new URL(url));
			addProgrammeur(p);
		} 
		catch(Exception e) {
			throw new Exception("Echec de l'incription");
		}
		return p;
	}
}
