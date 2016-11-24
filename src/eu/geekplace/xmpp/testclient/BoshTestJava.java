package eu.geekplace.xmpp.testclient;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.bosh.BOSHConfiguration;
import org.jivesoftware.smack.bosh.XMPPBOSHConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;

public class BoshTestJava extends SmackTest<XMPPBOSHConnection> {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPBOSHConnection> test = new BoshTestJava();
		test.runTest();
	}

	@Override
	public void runTestSubclass() throws SmackException, IOException, XMPPException, InterruptedException {
		BOSHConfiguration conf = BOSHConfiguration.builder().setUsernameAndPassword(USER, PASS).setFile("/http-bind/").setHost(BOSH_SERV).setPort(7070).setServiceName(SERV).build();
		connection = new XMPPBOSHConnection(conf);
		connection.connect();
		connection.login();

		ChatManager chat = ChatManager.getInstanceFor(connection);
		chat.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat arg0, boolean arg1) {

			}
		});
		send("Hi, what's up?");
		connection.disconnect();
	}

}
