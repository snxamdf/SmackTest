package eu.geekplace.xmpp.chatroom;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.geekplace.xmpp.testclient.SmackTest;

/**
 * 测试推送
 * 
 * @author hongyanyang1
 *
 */
public class Push1 extends SmackTest<XMPPTCPConnection> {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new Push1();
		test.runTest();
	}

	@Override
	public void runTestSubclass() throws Exception {
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
		MultiUserChat muc = JoinMultiUserChat.join(connection, USER, PASS, "mYroomName");
		muc.addMessageListener(new MessageListener() {
			@Override
			public void processMessage(Message message) {
				System.out.println(message.getFrom() + " " + message.getBody());
			}
		});
		while (true) {
			muc.sendMessage("推送的消息内容内容内容内容.");
			Thread.sleep(100);
		}
	}
}
