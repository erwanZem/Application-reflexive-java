package briAma;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;
import bri.Service;
import service.ServiceAma;

public class ServiceRegistryAma{
	private static int numServStart = 1;
	private static int numServStop = 1;
	private static HashMap<Integer ,Class <? extends Service>> servicesAMAStart;
	private static HashMap<Integer ,Class <? extends Service>> servicesAMAStop;
	static {
		servicesAMAStop = new HashMap<Integer, Class<? extends Service>>();
		servicesAMAStart = new HashMap<Integer, Class<? extends Service>>();
	}
	public static void addServiceStart(Class<?> cl) throws Exception  {
		Boolean im=false;
		for(Class<?> c :cl.getInterfaces()) {
			if(c.equals(bri.Service.class)) {
				im=true;
				break;
			}
		}
		if(!im) {
			throw new Exception("la classe n'implemente pas BRi.Service\n");}
		int mod = cl.getModifiers();
		if(Modifier.isAbstract(mod)) {
			throw new Exception("la classe est abtract");}
		if(!(Modifier.isPublic(mod))) {
			throw new Exception("la classe n'est pas public");}
		Constructor<?> con= cl.getConstructor(Socket.class);
		if(!(con.getAnnotatedExceptionTypes().length==0)) {
			throw new Exception("la constructeur à des exceptions");
		}
		if(!Modifier.isPublic(con.getModifiers())) {
			throw new Exception("le constructeur n'est pas publique");
		}

		Field[] f=cl.getDeclaredFields();
		for(Field fi :f) {
			if(fi.getType().equals(Socket.class)) {
				if(!(Modifier.isPrivate(fi.getModifiers()))&&Modifier.isFinal(fi.getModifiers()))
				{		throw new Exception("attribut socket invalide");	}
			}	
		}
		Method m=null;
		try {
			m = cl.getMethod("toStringue");
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int mod1=m.getModifiers();
		if(!(Modifier.isStatic(mod1)&&Modifier.isPublic(mod1))) {

		}
		if(!(m.getAnnotatedExceptionTypes().length==0)) {

		}
		if(!(m.getReturnType().equals(String.class))) {

		}
		System.out.println("add success");
		synchronized(servicesAMAStart) {
		servicesAMAStart.put(numServStart++, (Class<? extends Service>) cl);
		}
	}

	public static void addServiceStop(Class<? extends Service> cl) throws Exception  {
			
		servicesAMAStop.put(numServStop++, cl);}



	// renvoie la classe de service (numService -1)	
	public static Class<?> getServiceClass(int numService) {
		return servicesAMAStart.get(numService);
	}
	public static void MAJ (Class<?> cl) {
		if(servicesAMAStart.containsValue(cl)) {
			int num=getKeyByValue(servicesAMAStart, cl);
			servicesAMAStart.remove(num);
			servicesAMAStart.put(num, (Class<? extends Service>) cl);
		}
	}

	// liste les activités présentes
	public static String toStringue() {
		String result = "Activités présentes :";
		//for(Class<?> c: servicesProg. ) {
		int index=1;
		Iterator it = servicesAMAStart.values().iterator();

		while (it.hasNext()) {
			result+="##"+index +" Service"+it.next().toString();
			index++;
		}
		return result;
	}

	public static void stop(int i) {
		//change le service numéro i dans la HashMap stop
		try {
			synchronized (servicesAMAStart) {
				addServiceStop(servicesAMAStart.get(i));
				servicesAMAStart.remove(i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void start(int i) {
		//change le service numéro i dans la HashMap start
		try {
			synchronized (servicesAMAStart) {
				addServiceStop(servicesAMAStop.get(i));
				servicesAMAStop.remove(i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String showStop() {
		String result = "Activités arrétées :";
		//for(Class<?> c: servicesProg. ) {
		int index=1;
		Iterator it = servicesAMAStop.values().iterator();

		while (it.hasNext()) {
			result+="##"+index +" Service"+it.getClass().getSimpleName();
			index++;
		}
		return result;
	}
	public static  boolean isRegistery(Class <? extends Service> s) {
		return servicesAMAStart.containsKey(s);}

	public static String showStart() {
		String result = "Activités démmarer :";
		//for(Class<?> c: servicesProg. ) {
		int index=1;
		Iterator it = servicesAMAStop.values().iterator();

		while (it.hasNext()) {
			result+="##"+index +" Service "+it.getClass().getSimpleName();
			index++;
		}
		return result;
	}
	public static void Désinstaller(Class <? extends Service> s) throws Exception {
		int i;
		int y;
		synchronized (servicesAMAStart) {
			i = getKeyByValue(servicesAMAStart, s);
			servicesAMAStart.remove(i);
			y = getKeyByValue(servicesAMAStop, s);
			servicesAMAStop.remove(y);
		}
		if(i==0&&y==0) {
			throw new Exception("le Service que vous voulez désinstaller n'existe pas");
		}

	}
	public static <T, E> Integer getKeyByValue(HashMap<Integer, Class<? extends Service>> servicesAMAStart2, Class<?> cl) {
		for (Entry<Integer, Class<? extends Service>> entry : servicesAMAStart2.entrySet()) {
			if (Objects.equals(cl, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}}