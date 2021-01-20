/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.server;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.workflow.SimpleWorkflow;
import allinone.chat.data.ChatRoomZone;


/**
 * 
 * @author binh_lethanh
 */
public class Server {

	private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(SimpleWorkflow.class);
	public static double REAL_GOT_MONEY = 0.95;
	public static String MONEY_TYPE = "Xu";

	private static SimpleWorkflow worker;
	public static int numberOnline = 0;
	public static boolean isICOM = false;
	public static boolean isCachedID = true;
        public static long MONEY_BONUS_REFERENCE = 500L;
        public static boolean isTaiXiu = false;
        public static boolean isBaoTri = false;
        public static int lengthPhienTX = 13;
        public static int portWebsocket = 9090;
        public static String isNodeBB = "";
        public static String basePath = "/home/GameServer";
        public static boolean isBida = true;
        public static String SMS_ACTIVE = "Vui lòng kích hoạt TK nhận ngay 5.000 Xu (TK đầu tiên của số điện thoại) và thêm Xu mỗi ngày khi số tiền < 500 Xu (phí 1500/sms)";
	public static boolean isLogBidaMongo = false;
	public static String ipMongoDb = "localhost";
        public static ChannelFutureListener CLOSE = new ChannelFutureListener() {
		@Override
		public void operationComplete(ChannelFuture future) {
			future.getChannel().close();
		}
	};

	public static ChannelFutureListener NONE = new ChannelFutureListener() {
		@Override
		public void operationComplete(ChannelFuture future) {
		}
	};

	/**
	 * A {@link ChannelFutureListener} that closes the {@link Channel} when the
	 * operation ended up with a failure or cancellation rather than a success.
	 */
	public static ChannelFutureListener CLOSE_ON_FAILURE = new ChannelFutureListener() {
		@Override
		public void operationComplete(ChannelFuture future) {
			if (!future.isSuccess()) {
				future.getChannel().close();
			}
		}
	};

	public static void main(String[] args) throws ServerException {
		try {
			worker = new SimpleWorkflow();
			worker.start();
		} catch (ServerException ex) {
			mLog.error(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static void changeCachedIP() {
		isCachedID = (isCachedID ? false : true);
	}

	public static SimpleWorkflow getWorker() {
		return worker;
	}

	public static ChatRoomZone getChatRoomZone() {
		return worker.getChatZone();
	}
}
