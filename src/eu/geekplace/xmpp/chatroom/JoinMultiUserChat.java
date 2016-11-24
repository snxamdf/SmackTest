package eu.geekplace.xmpp.chatroom;

import java.util.List;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

public class JoinMultiUserChat {

	/**
	 * 加入聊天室
	 * 
	 * @param user
	 *            昵称
	 * @param password
	 *            会议室密码
	 * @param roomsName
	 *            会议室名
	 */
	public static MultiUserChat join(XMPPConnection connection, String user, String password, String roomsName) throws Exception {
		if (connection == null)
			return null;
		try {
			MultiUserChatManager mucm = MultiUserChatManager.getInstanceFor(connection);
			List<String> services = mucm.getServiceNames();
			if (services.isEmpty()) {
				throw new Exception("No MUC services found");
			}
			// 使用XMPPConnection创建一个MultiUserChat窗口
			String service = services.get(0);
			// 创建一个MultiUserChat
			MultiUserChat muc = mucm.getMultiUserChat(roomsName + "@" + service);
			// 聊天室服务将会决定要接受的历史记录数量
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxChars(10);
			// history.setSince(new Date());
			// 用户加入聊天室
			muc.join(user, password, history, 1000 * 30);
			System.out.println("MultiUserChat 会议室【" + roomsName + "】加入成功........");
			return muc;
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("MultiUserChat 会议室【" + roomsName + "】加入失败........");
			return null;
		}
	}
}
