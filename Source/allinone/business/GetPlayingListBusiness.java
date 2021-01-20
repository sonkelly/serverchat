package allinone.business;
import java.util.Vector;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.RoomEntity;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetPlayingListRequest;
import allinone.protocol.messages.GetPlayingListResponse;

public class GetPlayingListBusiness extends AbstractBusiness
{

    private static final Logger mLog = 
    	LoggerContext.getLoggerFactory().getLogger(GetPlayingListBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg)
    {
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetPlayingListResponse resGetPlayingList = (GetPlayingListResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        mLog.debug("[GET PLAYING ROOM LIST]: Catch");
        try
        {
            GetPlayingListRequest rqGetWaitingList = (GetPlayingListRequest) aReqMsg;
            Zone zone = aSession.findZone(aSession.getCurrentZone());
            int numPlayingRoom = zone.getNumPlaylingRoom();
            Vector<RoomEntity> playingRooms = zone.dumpPlayingRooms(rqGetWaitingList.mOffset, rqGetWaitingList.mLength, aSession.getCurrentZone());
            resGetPlayingList.setSuccess(ResponseCode.SUCCESS, numPlayingRoom, playingRooms);
        } catch (Throwable t) {
            resGetPlayingList.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu ");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally {
            if ((resGetPlayingList != null)) {
                aResPkg.addMessage(resGetPlayingList);
            }
        }

        return 1;
    }

}
