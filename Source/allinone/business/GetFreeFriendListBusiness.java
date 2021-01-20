package allinone.business;

import java.util.Vector;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.session.ISession;
import vn.game.session.SessionManager;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetFreeFriendListRequest;
import allinone.protocol.messages.GetFreeFriendListResponse;

public class GetFreeFriendListBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(GetFreeFriendListBusiness.class);

   @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();

        GetFreeFriendListResponse resGetFreeFriendList = (GetFreeFriendListResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {

            GetFreeFriendListRequest rqGet = (GetFreeFriendListRequest) aReqMsg;
        
            int type = rqGet.type;

            SessionManager manager = aSession.getManager();
            Vector<UserEntity> res = new Vector<UserEntity>();

            Room room = aSession.getRoom();

            long minimumMoneyJoin = 0;

            if (room != null) {
                minimumMoneyJoin = room.getAttactmentData().firstCashBet;
            }
         
            if (type == 1) {//dung cho bida khi choi cung friend
                mLog.debug("vao getListFriend type:" + type);
                res = manager.getListFriend(50, type, aSession.getCurrentZone(), minimumMoneyJoin);
            } else {//dung cho moi choi binh thuong
                res = manager.dumpFreeFriend(50, type, aSession.getCurrentZone(), minimumMoneyJoin, aSession);
            }

            resGetFreeFriendList.setSuccess(ResponseCode.SUCCESS, res);
            resGetFreeFriendList.setSession(aSession);
            
        } catch (Throwable t) {
            resGetFreeFriendList.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resGetFreeFriendList != null)) {
                aResPkg.addMessage(resGetFreeFriendList);
            }
        }

        return 1;
    }
}
