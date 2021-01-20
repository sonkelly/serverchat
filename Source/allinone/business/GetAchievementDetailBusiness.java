package allinone.business;

import java.util.List;

import org.slf4j.Logger;

import tools.CacheEventInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.EventPlayerEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetAchievementDetailRequest;
import allinone.protocol.messages.GetAchievementDetailResponse;

public class GetAchievementDetailBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetAchievementDetailBusiness.class);
        
       

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {

		
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetAchievementDetailResponse resBoc = (GetAchievementDetailResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
                    GetAchievementDetailRequest rqEvent = (GetAchievementDetailRequest) aReqMsg;
                    StringBuilder sb = new StringBuilder();
                    if(rqEvent.achievementId >0)
                    {
                        CacheEventInfo cache = new CacheEventInfo();
                        List<EventPlayerEntity> lstPlayers = cache.getEventPlayer(rqEvent.achievementId);
                        int playingSize = lstPlayers.size();
                        for(int i = 0; i< playingSize; i++)
                        {
                            EventPlayerEntity playerEntity = lstPlayers.get(i);
                            sb.append(playerEntity.getUsrEntity().mUid).append(AIOConstants.SEPERATOR_BYTE_1);
                            sb.append(playerEntity.getUsrEntity().mUsername).append(" - ").
                                    append(playerEntity.point).append(" lần").append(AIOConstants.SEPERATOR_BYTE_1);
                            sb.append(playerEntity.getDescription()).append(AIOConstants.SEPERATOR_BYTE_2);
//                            sb.append("1").append(AIOConstants.SEPERATOR_BYTE_1);
//                            sb.append("kenshin1").append(AIOConstants.SEPERATOR_BYTE_1);
//                            sb.append("0").append(AIOConstants.SEPERATOR_BYTE_2);
//                            sb.append("2").append(AIOConstants.SEPERATOR_BYTE_1);
//                            sb.append("kenshin2").append(AIOConstants.SEPERATOR_BYTE_1);
//                            sb.append("0");
                        }
                        
                        if(playingSize>0)
                        {
                            sb.deleteCharAt(sb.length()-1);
                        }
                    }
                            
//                    List<EventEntity> lstEvents = EventDB.getEvents();
//                    int size = lstEvents.size();
//                    
//                    StringBuilder sb = new StringBuilder();
//                    for(int i = 0; i< size; i++)
//                    {
//                        EventEntity entity = lstEvents.get(i);
//                        
//                        if(entity.getEventId() == rqEvent.eventId)
//                        {
//                            sb.append(entity.getEventId()).append(AIOConstants.SEPERATOR_BYTE_1);
//                            sb.append(entity.getContent());
//                            
//                            resBoc.mCode = ResponseCode.SUCCESS;
//                            resBoc.value = sb.toString();
//                        
//                            if(entity.isDetailImage())
//                            {
//                                StringBuilder thumb = new StringBuilder();
//                                thumb.append(entity.getEventId()).append(AIOConstants.SEPERATOR_BYTE_1);
//                                thumb.append("0").append(AIOConstants.SEPERATOR_BYTE_1);
//                                thumb.append(entity.getPicDetail());
//                                insertQueue(msgFactory, thumb.toString(), aSession);
//                            }
//                            return 1;
//                        }
//                    }
                    
                    resBoc.mCode = ResponseCode.SUCCESS;
                    resBoc.value = sb.toString();
                        
		} 
                catch(Exception ex)
                {
                    mLog.error(ex.getMessage(), ex);
                    resBoc.setFailure("co loi ket noi voi co so du lieu");
                }
                finally {
			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

	
}
