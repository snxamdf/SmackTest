package eu.geekplace.xmpp.group;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import eu.geekplace.xmpp.chatroom.CreateRoom;
import eu.geekplace.xmpp.testclient.SmackTest;

public class CreateGroup extends SmackTest<XMPPTCPConnection> {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new CreateRoom();
		test.runTest();
	}

	@Override
	public void runTestSubclass() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
