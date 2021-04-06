package utilisateurs;

import java.net.URL;

public class Programmeur {
	private String login;
	private String pwd;
	private URL Url;
	public Programmeur(String l,String p,URL url) {
		this.login=l;
		this.pwd=p;
		this.setUrl(url);
	}
	public String getLogin() {
		return login;
	}
	public String getPwd() {
		return pwd;
	}
	public String getUrl() {
		return Url.toString();
	}
	public void setUrl(URL url) {
		this.Url = url;
	}
}
