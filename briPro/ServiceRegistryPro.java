package briPro;

import java.util.HashMap;
import java.util.Iterator;

import bri.Service;
import service.*;
import utilisateurs.Programmeur;

public class ServiceRegistryPro {
	private static final int numServ = 1;
	// cette classe est un registre de services
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique



	//private static List<Class<?>> servicesProg;
	private static HashMap<Integer ,Class<? extends Service>> servicesProg;

	static {
		servicesProg = new HashMap<Integer, Class<? extends Service>>();
		//servicesClass =new ArrayList<Class<?>> () ;
		ServiceAjout.class.getClassLoader();
		//Programmeurs=new HashMap<String, String>();
		ServiceAjout.class.getClassLoader();
		ServiceDemarrer.class.getClassLoader();
		ServiceAdresse.class.getClassLoader();
		ServiceMAj.class.getClassLoader();
		ServiceStop.class.getClassLoader();
		ServiceDesinstaller.class.getClassLoader();
		addService();
	}
	public static void addService()  {
		int i = 0;
		servicesProg.put(i, ServiceAjout.class);
		servicesProg.put(i++, ServiceDemarrer.class);
		servicesProg.put(i++, ServiceAdresse.class);
		servicesProg.put(i++, ServiceMAj.class);
		servicesProg.put(i++, ServiceStop.class);
		servicesProg.put(i++, ServiceDesinstaller.class);
	}

	// renvoie la classe de service (numService -1)	
	public static Class<?> getServiceClass(int numService) {
		synchronized(servicesProg) {
		return servicesProg.get(numService-1);}
	}

	// liste les activités présentes
	public static String toStringue() {
		String result = "Activités présentes :";
		//for(Class<?> c: servicesProg. ) {
		int index=1;
		Iterator it = servicesProg.values().iterator();

		while (it.hasNext()) {
			result+="##"+index +" Service"+it.getClass().getSimpleName();
			index++;
		}
		return result;
	}

}
