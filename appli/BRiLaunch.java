package appli;



import serveur.ServeurBRi;



public class BRiLaunch {
	private final static int PORT_PROG = 4001;
	private final static int PORT_AMA = 3001;

	public static void main(String[] args) {


		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
		new Thread(new ServeurBRi(PORT_AMA, service.ServiceAma.class)).start();
		new Thread(new ServeurBRi(PORT_PROG,  service.ServicePro.class)).start();
	}		

	public static int getPortProg() {
		return PORT_PROG;
	}
	public static int getPortAma() {
		return PORT_AMA;
	}
}
