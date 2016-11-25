package eu.geekplace.xmpp.testclient;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

public abstract class SmackTest<C extends XMPPConnection> {
	public static String SERV;
	public static String USER;
	public static String PASS;
	public static String BOSH_SERV;
	public static String OTHER_ENTITY;
	public static boolean DEBUG;

	public C connection;

	static {
		// JUL Debugger will not print any information until configured to print
		// log messages of level FINE
		SmackConfiguration.addDisabledSmackClass("org.jivesoftware.smack.debugger.JulDebugger");
		SmackConfiguration.setDefaultPacketReplyTimeout(1000 * 60 * 5);
		SmackConfiguration.DEBUG = true;
	}

	public final void runTest() throws Exception {
		loadProperties();
		System.out.println("Using Smack Version: " + SmackConfiguration.getVersion());
		SmackConfiguration.DEBUG = DEBUG;
		runTestSubclass();
		System.out.println("Test run without Exception");
	}

	public abstract void runTestSubclass() throws Exception;

	static void loadProperties() throws IOException {
		// File propertiesFile = findPropertiesFile();
		Properties properties = new Properties();
		properties.setProperty("serv", "07d335610935");
		properties.setProperty("user", "admin");
		properties.setProperty("pass", "admin");
		properties.setProperty("boshserv", "201512080143-");
		properties.setProperty("otherentity", "test01@201512080143-");
		properties.setProperty("debug", "false");
		// try (FileInputStream in = new FileInputStream(propertiesFile)) {
		// properties.load(in);
		// }
		SERV = properties.getProperty("serv");
		USER = properties.getProperty("user");
		PASS = properties.getProperty("pass");
		BOSH_SERV = properties.getProperty("boshserv");
		OTHER_ENTITY = properties.getProperty("otherentity");
		String debug = properties.getProperty("debug");
		if (debug != null) {
			DEBUG = Boolean.valueOf(debug);
		}
	}

	public void send(String message) throws NotConnectedException, InterruptedException {
		Message m = new Message(OTHER_ENTITY);
		m.setType(Type.groupchat);
		m.setBody(message);
		// connection.sendPacket(m);
		connection.sendStanza(m);
	}

	public static File findPropertiesFile() throws IOException {
		List<String> possibleLocations = new LinkedList<String>();
		possibleLocations.add("example.properties");
		String userHome = System.getProperty("user.home");
		System.out.println(userHome);
		if (userHome != null) {
			possibleLocations.add(userHome + "/.config/smack-examples/properties");
		}
		for (String possibleLocation : possibleLocations) {
			File res = new File(possibleLocation);
			if (res.isFile())
				return res;
		}
		throw new IOException("Could not find properties file: Searched locations");
	}

	protected final void printlnFmt(Object o, Object... args) {
		if (o != null && args != null && args.length > 0) {
			String s = o.toString();
			for (int i = 0; i < args.length; i++) {
				String item = args[i] == null ? "" : args[i].toString();
				if (s.contains("{" + i + "}")) {
					s = s.replace("{" + i + "}", item);
				} else {
					s += " " + item;
				}
			}
			System.out.println(s);
		}
	}

	protected final void println(Object... args) {
		StringBuffer sb = new StringBuffer();
		for (Object str : args) {
			sb.append(str).append(" ");
		}
		System.out.println(sb.toString());
	}
}
