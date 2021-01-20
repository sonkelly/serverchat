package allinone.business;

import java.util.List;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.TopEventResponse;

public class TopEventBusiness extends AbstractBusiness
{

    private static final Logger mLog = 
    	LoggerContext.getLoggerFactory().getLogger(TopEventBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg)
    {
        // process's status
        mLog.debug("[GET TOP EVENT PLAYER]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        TopEventResponse resEventPlayer = (TopEventResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try
        {
            long uid = aSession.getUID();
            mLog.debug("[GET TOP EVENT PLAYER]: for" + uid);
            
            UserDB db = new UserDB();
            
            List<UserEntity> players = db.getTopEvent(uid);
            resEventPlayer.setSuccess(ResponseCode.SUCCESS, players);
            resEventPlayer.fromDate = "29-04-2012";
            resEventPlayer.toDate = "05-05-2012";
            resEventPlayer.title ="Event VIPGAME chảo mừng ngày 30/4 và 1/5(chỉ áp dụng cho người chơi đã active)";
            
        } catch (Throwable t) {
            resEventPlayer.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resEventPlayer != null)) {
                aResPkg.addMessage(resEventPlayer);
            }
        }
        return 1;
    }
}
