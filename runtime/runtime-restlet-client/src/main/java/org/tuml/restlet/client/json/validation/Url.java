package org.tuml.restlet.client.json.validation;

public class Url implements TumlValidation {
	private String protocol;
	private String host;
	private int port;
	private String regexp;
	private String flag;

	public Url(String protocol, String host, int port, String regexp, String flag) {
		super();
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.regexp = regexp;
		this.flag = flag;
	}

}
