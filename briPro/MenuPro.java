package briPro;

import java.util.HashMap;
import java.util.Iterator;

import service.*;

public class MenuPro {
	private static  int numServ  = 0;
	//private static List<Class<?>> servicesProg;
	private static HashMap<Integer ,Class<? >> servicesProg;
	static {
		servicesProg = new HashMap<Integer, Class<?>>();
		//servicesClass =new ArrayList<Class<?>> () ;
		ServiceAjout.class.getClassLoader();
		ServiceMAj.class.getClassLoader();
		ServiceAdresse.class.getClassLoader();
		ServiceDemarrer.class.getClassLoader();
		ServiceStop.class.getClassLoader();
		addService();
	}
	public static void addService()  {
		servicesProg.put(numServ++,ServiceAjout.class);
		servicesProg.put(numServ++,ServiceMAj.class);
		servicesProg.put(numServ++,ServiceAdresse.class);
		servicesProg.put(numServ++,ServiceDemarrer.class);
		servicesProg.put(numServ++,ServiceStop.class);
	}	
	// renvoie la classe de service (numService -1)	
	public static Class<?> getServiceClass(int numService) {
		System.out.println(servicesProg.get(numService));
		return servicesProg.get(numService);
	}
	// liste les activités présentes
	public static String toStringue() {

		String result = "Activités présentes :";
		//for(Class<?> c: servicesProg. ) {
		int index=0;
		Iterator it = servicesProg.values().iterator();
		System.out.println(servicesProg);
		while (it.hasNext()) {

			result+="##"+index +" Service :"+it.next().toString();
			index++;
		}
		return result;

	}

}
