package eu.geekplace.xmpp.chatroom;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.geekplace.xmpp.testclient.SmackTest;

public class User2 extends SmackTest<XMPPTCPConnection> {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new User2();
		test.runTest();
	}

	@Override
	public void runTestSubclass() throws Exception {
		XMPPTCPConnectionConfiguration.Builder conf = XMPPTCPConnectionConfiguration.builder();
		conf.setServiceName(SERV);
		String name = "test02";
		conf.setUsernameAndPassword(name, name);
		conf.setSecurityMode(SecurityMode.disabled);
		// conf.setLegacySessionDisabled(true);
		conf.setCompressionEnabled(true);
		TLSUtils.acceptAllCertificates(conf);
		connection = new XMPPTCPConnection(conf.build());
		connection.connect();
		connection.login();
		ChatManager chat = ChatManager.getInstanceFor(connection);
		chat.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat arg0, boolean arg1) {
				arg0.addMessageListener(new ChatMessageListener() {
					@Override
					public void processMessage(Chat arg0, Message arg1) {
						System.out.println(arg1.getBody());
					}
				});
			}
		});
		MultiUserChat muc = JoinMultiUserChat.join(connection, name, name, "mYroomName");
		muc.addMessageListener(new MessageListener() {
			@Override
			public void processMessage(Message message) {
				System.out.println(message.getFrom() + "\t" + message.getBody());
			}
		});
		while (true) {
			// muc.sendMessage("大家好,我是test02.");
			Thread.sleep(1000);
		}
		// connection.disconnect();
	}

}
