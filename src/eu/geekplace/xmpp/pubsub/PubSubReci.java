package eu.geekplace.xmpp.pubsub;

import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.Node;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;

import eu.geekplace.xmpp.testclient.SmackTest;

public class PubSubReci extends SmackTest<XMPPTCPConnection> {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new PubSubReci();
		test.runTest();
	}

	@Override
	public void runTestSubclass() throws Exception {
		XMPPTCPConnectionConfiguration.Builder conf = XMPPTCPConnectionConfiguration.builder();
		conf.setServiceName(SERV);
		conf.setUsernameAndPassword("test01", "test01");
		conf.setSecurityMode(SecurityMode.disabled);
		conf.setCompressionEnabled(true);
		TLSUtils.acceptAllCertificates(conf);
		connection = new XMPPTCPConnection(conf.build());
		connection.connect();
		connection.login();

		String nodeId = "zyf_test2";
		PubSubManager manager = new PubSubManager(connection);
		Node eventNode = manager.getNode(nodeId);
		eventNode.addItemEventListener(new ItemEventListener<PayloadItem>() {
			public void handlePublishedItems(ItemPublishEvent evt) {
				List<PayloadItem> lists = evt.getItems();
				for (PayloadItem item : lists) {
					println(item.getPayload().toXML(), item.getId());
				}
			}
		});
		eventNode.subscribe(connection.getUser());
		while (true)
			;
	}
}
