package eu.geekplace.xmpp.testclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;

public class TCPXEP198Test extends SmackTest<XMPPTCPConnection> {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new TCPXEP198Test();
		test.runTest();
	}

	@Override
	public void runTestSubclass() throws KeyManagementException, NoSuchAlgorithmException, SmackException, IOException, XMPPException, InterruptedException {
		XMPPTCPConnectionConfiguration.Builder conf = XMPPTCPConnectionConfiguration.builder();
		conf.setServiceName(SERV);
		conf.setUsernameAndPassword(USER, PASS);
		conf.setSecurityMode(SecurityMode.disabled);
		// conf.setLegacySessionDisabled(true);
		conf.setCompressionEnabled(true);
		TLSUtils.acceptAllCertificates(conf);
		connection = new XMPPTCPConnection(conf.build());
		connection.setUseStreamManagement(true);

		connection.connect();

		connection.login();

		send("你好,有什么事吗?");

		connection.instantShutdown();

		send("你好,有什么事吗?我立刻关闭");

		// Reconnect with xep198
		connection.connect();

		send("你好,有什么事吗?我在恢复");

		connection.disconnect();
	}
}
