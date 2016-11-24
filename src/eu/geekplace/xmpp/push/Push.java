package eu.geekplace.xmpp.push;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.muc.packet.GroupChatInvitation;

import eu.geekplace.xmpp.testclient.SmackTest;

public class Push extends SmackTest<XMPPTCPConnection> {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new Push();
		test.runTest();
	}

	@Override
	public void runTestSubclass() throws SmackException, IOException, XMPPException, InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		XMPPTCPConnectionConfiguration.Builder conf = XMPPTCPConnectionConfiguration.builder();
		conf.setServiceName(SERV);
		conf.setUsernameAndPassword(USER, PASS);
		conf.setSecurityMode(SecurityMode.disabled);
		// conf.setLegacySessionDisabled(true);
		conf.setCompressionEnabled(true);
		TLSUtils.acceptAllCertificates(conf);
		connection = new XMPPTCPConnection(conf.build());
		connection.connect();
		connection.login();
		GroupChatInvitation groupChat = new GroupChatInvitation("");

		ChatManager chat = ChatManager.getInstanceFor(connection);
		chat.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat arg0, boolean arg1) {
			}
		});
		while (true) {
			send("嗨你好.");
			Thread.sleep(1000);
		}
		// connection.disconnect();
	}
}
