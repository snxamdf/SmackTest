package eu.geekplace.xmpp.pubsub;

import java.util.Date;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.SimplePayload;

import eu.geekplace.xmpp.testclient.SmackTest;

public class PubSubPush extends SmackTest<XMPPTCPConnection> {
	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new PubSubPush();
		test.runTest();
	}

	@Override
	public void runTestSubclass() throws Exception {
		try {
			XMPPTCPConnectionConfiguration.Builder conf = XMPPTCPConnectionConfiguration.builder();
			conf.setServiceName(SERV);
			conf.setUsernameAndPassword("admin", "admin");
			conf.setSecurityMode(SecurityMode.disabled);
			conf.setCompressionEnabled(true);
			TLSUtils.acceptAllCertificates(conf);
			connection = new XMPPTCPConnection(conf.build());
			connection.connect();
			connection.login();

			PubSubManager manager = new PubSubManager(connection);
			String nodeId = "zyf_test2";
			LeafNode myNode = null;
			try {
				myNode = manager.getNode(nodeId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (myNode == null) {
				myNode = manager.createNode(nodeId);
			}
			String msg = "发布的内容的内容的内容的内容的内容.时间" + new Date().toGMTString();
			SimplePayload payload = new SimplePayload("message", "pubsub:test:message", "<message>" + msg + "</message>");
			PayloadItem<SimplePayload> item = new PayloadItem<SimplePayload>(null, payload);
			System.out.println("-----publish-----------");
			while (true) {
				myNode.publish(item);
				Thread.sleep(1000);
			}

		} catch (Exception E) {
			E.printStackTrace();
		}
	}
}
