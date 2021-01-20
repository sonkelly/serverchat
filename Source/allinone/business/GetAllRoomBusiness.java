/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.business;

import java.util.List;

import org.slf4j.Logger;

import tools.CacheGameInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.databaseDriven.RoomDB;
import allinone.protocol.messages.GetAllRoomResponse;
import allinone.room.NRoomEntity;

/**
 *
 * @author Administrator
 */
public class GetAllRoomBusiness extends AbstractBusiness {
    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetWaitingListBusiness.class);
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException {
        mLog.debug("[GET  ALL ROOM LIST]: Catch : "+aSession.getCurrentZone());

        MessageFactory msgFactory = aSession.getMessageFactory();
        GetAllRoomResponse resGetAllRoom =
        	(GetAllRoomResponse)msgFactory.getResponseMessage(aReqMsg.getID());
        resGetAllRoom.session = aSession;
        try{
//            GetAllRoomRequest req = (GetAllRoomRequest) aReqMsg;
            if(aSession.getByteProtocol()> AIOConstants.PROTOCOL_PRIMITIVE)
            {
                CacheGameInfo cache = new CacheGameInfo();
                
                if(aSession.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
                {
                    resGetAllRoom.value = cache.getPhongInfo(aSession.getCurrentZone(), false,aSession.getRealMoney());
                }
                else
                {
                    resGetAllRoom.value = cache.getPhongInfo(aSession.getCurrentZone(), aSession.isMobileDevice(),aSession.getRealMoney());
                }
                resGetAllRoom.mCode = ResponseCode.SUCCESS;
            }
            else
            {
                RoomDB db = new RoomDB();

                List<NRoomEntity> rooms = db.getRooms(aSession.getCurrentZone(),aSession.getRealMoney());
    //            aSession.setRoomLevel(req.level);

                resGetAllRoom.setSuccess(ResponseCode.SUCCESS, rooms);
            }
        } catch (Exception e) {
        	mLog.debug("Get all rooms list error:"+e.getCause());
			resGetAllRoom.setFailure(ResponseCode.FAILURE, "Không thể kết nối đến cơ sở dữ liệu");
        }
        finally{
            if ((resGetAllRoom != null)){
                aResPkg.addMessage(resGetAllRoom);
            }
        }

        return 1;
    }

}
