package allinone.business;


import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.protocol.messages.FindRoomByOwnerRequest;
import allinone.protocol.messages.FindRoomByOwnerResponse;

public class FindRoomByOwnerBusiness extends AbstractBusiness
{

    private static final Logger mLog = 
    	LoggerContext.getLoggerFactory().getLogger(FindRoomByOwnerBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg)
    {
        mLog.debug("[FIND ROOM]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        FindRoomByOwnerResponse resFindRoom = (FindRoomByOwnerResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try
        {
            FindRoomByOwnerRequest rqFind = (FindRoomByOwnerRequest) aReqMsg;
            String username = rqFind.roomOwner;
            Zone zone = aSession.findZone(aSession.getCurrentZone());
            Room room = zone.findRoomByOwner(username);
            if(room != null){
            	resFindRoom.setSuccess(ResponseCode.SUCCESS, room.dumpRoom());
            } else {
            	resFindRoom.setFailure(ResponseCode.FAILURE, "Không tìm được bàn");
            }
        } catch (Throwable t){
            resFindRoom.setFailure(ResponseCode.FAILURE, "Không tìm được bàn");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
        } finally{
            if ((resFindRoom != null) ){
                aResPkg.addMessage(resFindRoom);
            }
        }

        return 1;
    }
}
