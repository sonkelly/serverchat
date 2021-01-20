package allinone.business;

import java.util.Hashtable;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetRoomMoneyResponse;

public class GetRoomMoneyBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetRoomMoneyBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
    	
        mLog.debug("[GET ROOM MONEY LIST]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetRoomMoneyResponse resGetRoomMoneyList = (GetRoomMoneyResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            
            long uid = aSession.getUID();
            mLog.debug("[GET ROOM MONEY LIST]:" + uid);
            //TODO NamNT why we need room money list
            Hashtable<Integer, Long> list = new Hashtable<Integer, Long>();//DatabaseDriver.getRoomMoneyList();
            resGetRoomMoneyList.setSuccess(ResponseCode.SUCCESS, list);

        } catch (Throwable t) {
            resGetRoomMoneyList.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resGetRoomMoneyList != null) ) {
                aResPkg.addMessage(resGetRoomMoneyList);
            }
        }

        return 1;
    }
}
