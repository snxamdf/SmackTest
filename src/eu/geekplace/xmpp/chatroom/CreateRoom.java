package eu.geekplace.xmpp.chatroom;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;

import eu.geekplace.xmpp.testclient.SmackTest;

/**
 * 创建聊天室房间
 * 
 * @author hongyanyang1
 *
 */
public class CreateRoom extends SmackTest<XMPPTCPConnection> {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new CreateRoom();
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
		MultiUserChat muc = CreateRoom.createRoom(connection, "mYroomName1", null);
		muc.addMessageListener(new MessageListener() {
			@Override
			public void processMessage(Message message) {
				println(message.getFrom(), ":", message.getBody());
			}
		});
	}

	/**
	 * 创建房间
	 * 
	 * @param roomName
	 *            房间名称
	 * @throws Exception
	 */
	public static MultiUserChat createRoom(XMPPConnection connection, String roomName, String password) throws Exception {
		if (connection == null)
			return null;
		MultiUserChat muc = null;
		try {

			MultiUserChatManager mucm = MultiUserChatManager.getInstanceFor(connection);
			List<String> services = mucm.getServiceNames();
			if (services.isEmpty()) {
				throw new Exception("No MUC services found");
			}
			String service = services.get(0);
			// 创建一个MultiUserChat
			muc = mucm.getMultiUserChat(roomName + "@" + service);
			// 创建聊天室 ---如果创建过了就不能创建了... 这个第一次创建时也不需要调用
			// muc.create(roomName);
			// 获得聊天室的配置表单
			Form form = muc.getConfigurationForm();
			// 根据原始表单创建一个要提交的新表单。
			Form submitForm = form.createAnswerForm();
			// 向要提交的表单添加默认答复
			List<FormField> fields = form.getFields();
			for (FormField field : fields) {
				System.out.println(field.getVariable());
				if (!FormField.FORM_TYPE.equals(field.getType()) && field.getVariable() != null) {
					// 设置默认值作为答复
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// 设置聊天室的新拥有者
			List<String> owners = new ArrayList<String>();
			owners.add(connection.getUser());// 用户JID
			// submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// 设置聊天室是持久聊天室，即将要被保存下来
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// 房间仅对成员开放
			submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// 允许占有者邀请其他人
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			if (!"".equals(password) && password != null) {
				// 进入是否需要密码
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
				// 设置进入密码
				submitForm.setAnswer("muc#roomconfig_roomsecret", password);
			}
			// 能够发现占有者真实 JID 的角色
			// submitForm.setAnswer("muc#roomconfig_whois", "anyone");
			// 登录房间对话
			// submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			// 仅允许注册的昵称登录
			// submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			// 允许使用者修改昵称
			// submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
			// 允许用户注册房间
			// submitForm.setAnswer("x-muc#roomconfig_registration", false);
			// 发送已完成的表单（有默认值）到服务器来配置聊天室
			muc.sendConfigurationForm(submitForm);
		} catch (XMPPException e) {
			e.printStackTrace();
			return null;
		}
		return muc;
	}

}
