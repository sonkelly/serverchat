package allinone.business;

import java.util.Vector;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.DatabaseDriver;
import allinone.protocol.messages.GetMostPlayingResponse;

public class GetMostPlayingBusiness extends AbstractBusiness
{

    private static final Logger mLog = 
    	LoggerContext.getLoggerFactory().getLogger(GetMostPlayingBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg)
    {
        mLog.debug("[GET MOST PLAYING]: Catch");
//        aSession.getCollectInfo().append("->GetMostPlaying: ");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetMostPlayingResponse resGetMostPlayingList = (GetMostPlayingResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try
        {
            resGetMostPlayingList.session = aSession;
            long uid = aSession.getUID();
            mLog.debug("[GET MOST PLAYING]: for" + uid);
            Vector<UserEntity> richests = DatabaseDriver.getMostPlaying();
            resGetMostPlayingList.setSuccess(ResponseCode.SUCCESS, richests);
        } catch (Throwable t) {
            resGetMostPlayingList.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resGetMostPlayingList != null)){
                aResPkg.addMessage(resGetMostPlayingList);
            }
        }

        return 1;
    }
}
