package allinone.business;

import org.slf4j.Logger;

import tools.CacheGameInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.MessagesID;
import allinone.data.QueueEntity;
import allinone.data.ResponseCode;
import allinone.data.ZoneID;
import allinone.databaseDriven.RoomDB;
import allinone.protocol.messages.EnterZoneRequest;
import allinone.protocol.messages.EnterZoneResponse;
import allinone.protocol.messages.VivuDisappearResponse;
import allinone.protocol.messages.ZoneCacheResponse;
import allinone.queue.data.CommonQueue;
import java.util.Calendar;

public class EnterZoneBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChatBusiness.class);
    private static final int CURRENT_ZONE_CACHE_VERSION = 1;
    private static final int NOT_USED_CACHE_VERSION = -1;
    
    private void sendZoneCacheInfo(ISession aSession, MessageFactory msgFactory, int zoneId, int timeout)
    {
        StringBuilder sb = new StringBuilder();
        ZoneCacheResponse cacheRes = (ZoneCacheResponse) msgFactory.getResponseMessage(MessagesID.ZONE_CACHE);
        cacheRes.mCode = ResponseCode.SUCCESS;
        cacheRes.gameInfo = RoomDB.getGameInfo(zoneId);
        sb.append(Integer.toString(CURRENT_ZONE_CACHE_VERSION)).append(AIOConstants.SEPERATOR_BYTE_3).append(cacheRes.gameInfo);
        sb.append(AIOConstants.SEPERATOR_BYTE_3).append(timeout);
        cacheRes.gameInfo = sb.toString();
        
        QueueEntity gameInfoEntity = new QueueEntity(aSession, cacheRes);
        CommonQueue queue = new CommonQueue();
        queue.insertQueue(gameInfoEntity);
        
        
    }
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        EnterZoneResponse resEnter = (EnterZoneResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        aSession.setLastAccessTime(Calendar.getInstance().getTime());
        mLog.debug("[ENTER ZONE]: Catch");
        String zoneName = "#";
        try {
            EnterZoneRequest rqEnter = (EnterZoneRequest) aReqMsg;
            int zoneID = rqEnter.zoneID;
            zoneName = ZoneID.getZoneName(zoneID);
            mLog.debug("Zone name = " + zoneName);
            aSession.setCurrentZone(zoneID);
            resEnter.timeout = ZoneID.getTimeout(zoneID);
            resEnter.session = aSession;
            aSession.setCurrPosition(null);
//            aSession.getCollectInfo().append("->EnterZone: ").append(zoneName);
            if(aSession.getByteProtocol()> AIOConstants.PROTOCOL_PRIMITIVE)
            {
                aSession.setChatRoom(0);
                CacheGameInfo cache = new CacheGameInfo();
               
                if( rqEnter.cacheVersion > NOT_USED_CACHE_VERSION)
                {
                    if(rqEnter.cacheVersion != CURRENT_ZONE_CACHE_VERSION)
                    {
                        sendZoneCacheInfo(aSession, msgFactory, zoneID, resEnter.timeout);
                        
                    }
                    
                }
                
                if(aSession.getByteProtocol() < AIOConstants.PROTOCOL_MODIFY_MID)
                {
                    resEnter.value = cache.getPhongInfo(zoneID, aSession.isMobileDevice(),aSession.getRealMoney());
                }
                else
                {
                    resEnter.value = cache.getPhongInfo(zoneID, false,aSession.getRealMoney());
                }
                
            }
            else
            {
                RoomDB db = new RoomDB();
                resEnter.lstRooms = db.getRooms(zoneID,aSession.getRealMoney());
            }
            
            
            resEnter.setSuccess(ResponseCode.SUCCESS);
//            DatabaseDriver.updateUserZone(aSession.getUID(),zoneID);
            aSession.setPhongID(0);
            aResPkg.addMessage(resEnter);
            
            if(aSession.isMXHDevice())
            {
                if (aSession.getGroup() != null) {
                    VivuDisappearResponse disA =
                            (VivuDisappearResponse) msgFactory.getResponseMessage(MessagesID.ViVuDisappear);
                    disA.setSuccess(aSession.getUID());
                    aSession.getGroup().broadcast(disA, false, aSession.getUID());
                    aSession.getGroup().left(aSession);
                }
                
            }
        } catch (Throwable t) {
            resEnter.setFailure(ResponseCode.FAILURE, "Hiện tại không vào được game " + zoneName + " này!");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            aResPkg.addMessage(resEnter);
        }
        return 1;
    }
}
