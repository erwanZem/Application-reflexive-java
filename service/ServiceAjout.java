package service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import bri.Service;
import briAma.ServiceRegistryAma;
import utilisateurs.Programmeur;

public class ServiceAjout implements Service {
	private Socket client;
	private static String fileDest= "tmp/";
	private Programmeur p;
	public ServiceAjout(Socket s,Programmeur p){
		this.client=s;
		this.p=p;
	}
	static {
        File dir = new File(fileDest);
        dir.mkdirs();
        fileDest = dir.getAbsolutePath()+"\\";
        
    }
	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		out.println("Bienvenue dans le Service d'ajout "+ this.p.getLogin()+",##"
				+ "Saisissez le nom du service que vous souhaitez charger sinon \"exit\" pour quitter ##Url : \""+this.p.getUrl().toString()+"\" ?##"
				+ "##(Pour l'import d'une bibliothèque, le nom de celle ci doit être le nom de la classe service principale)##>");
		String adresseFTP = this.p.getUrl().toString();
		while (true) {
			try {
				String saisie = in.readLine();
				String main = (saisie==null) ? "exit": saisie;
				if(main.contentEquals("exit"))
					break;
				out.println("Saisissez le format du fichier à charger.##"
						+ "Les formats acceptés sont:  \".zip\" et \".class\"##>");
				saisie = in.readLine();
				String ext = (saisie==null) ? "null": saisie;
				if(ext.contentEquals(".class")) {
					URL[] urls = {new URL(adresseFTP)};
					URLClassLoader classLoader = new URLClassLoader(urls);
					Class<? extends Service> s = classLoader.loadClass(main).asSubclass(Service.class);
					ServiceRegistryAma.addServiceStart(s);
					classLoader.close();
					break;
				}
				else if(ext.contentEquals(".zip")) {
					String fileDest=null;
					URL[] urls = {new URL("file:"+fileDest)};
					URLClassLoader classLoader = new URLClassLoader(urls);
					ArrayList<String> classes = decompress(new URL(adresseFTP+main+ext));
					// à partir d'ici les fichiers ont été ajouté dans le fichier tmp de l'application
					for(String classe : classes) {
						Class <?> cl = classLoader.loadClass(classe.replaceAll("/","."));
						if(classe.contentEquals(main))
							ServiceRegistryAma.addServiceStart(cl);
					}
					classLoader.close();
					break;
				}
				else
				{
					out.println("Seuls les formats \".zip\" et \".class\" sont supportés, veuillez ré-essayer##"
							+ "Quel est le nom du service à ajouter ?##>");
				}
			} catch (Exception e) {
				e.printStackTrace();
				out.println("##Erreur: Le service n'a pas pu étre ajouté.##" + e+"##Veuillez re-saisir le nom de votre classe##>");
			}
		}
		out.println("Merci d'enrichir les services disponibles de notre application !##Pressez une touche pour sortir...");
		//on vide l'input
		try {
			in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<String> decompress(URL urlFile) throws IOException {
        ArrayList<String> noms = new ArrayList<>();
        String url = urlFile.toString();
        String file = url.substring(url.lastIndexOf("/"), url.lastIndexOf("."));
        new File(fileDest+file).mkdirs();
        String extension = ".class";
        InputStream input = urlFile.openStream();
        ZipInputStream zipIn = new ZipInputStream(input);
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = fileDest + entry.getName();
            if(entry.isDirectory()) {
                File dir = new File(filePath);
                dir.mkdirs();
            }
            else {
                if(entry.getName().endsWith(extension)) {
                    noms.add(entry.getName().substring(0, entry.getName().length()-extension.length()));
                    extractFile(zipIn, filePath);
                }
            }
            entry = zipIn.getNextEntry();

        }
        return noms;
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}