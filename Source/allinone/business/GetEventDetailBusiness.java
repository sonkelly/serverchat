package allinone.business;

import java.util.List;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.EventEntity;
import allinone.data.MessagesID;
import allinone.data.QueueEntity;
import allinone.data.ResponseCode;
import allinone.databaseDriven.EventDB;
import allinone.protocol.messages.GetEventDetailRequest;
import allinone.protocol.messages.GetEventDetailResponse;
import allinone.protocol.messages.SendImageResponse;
import allinone.queue.data.CommonQueue;

public class GetEventDetailBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetEventDetailBusiness.class);
        
        private void insertQueue(MessageFactory msgFactory, String value, ISession aSession)
        {
            SendImageResponse res = (SendImageResponse)msgFactory.getResponseMessage(MessagesID.SEND_IMAGE_EVENT);
            QueueEntity entity = new QueueEntity(aSession, res);
            res.setSuccess(ResponseCode.SUCCESS);
            
            res.value = value;
            
            CommonQueue queue = new CommonQueue();
            queue.insertQueue(entity);

            
        }

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) {

		
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetEventDetailResponse resBoc = (GetEventDetailResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
                    GetEventDetailRequest rqEvent = (GetEventDetailRequest) aReqMsg;
                    
                    List<EventEntity> lstEvents = EventDB.getEvents(aSession.getUserEntity().partnerId);
                    int size = lstEvents.size();
                    
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i< size; i++)
                    {
                        EventEntity entity = lstEvents.get(i);
                        
                        if(entity.getEventId() == rqEvent.eventId)
                        {
                            sb.append(entity.getEventId()).append(AIOConstants.SEPERATOR_BYTE_1);
                            sb.append(entity.getContent());
                            /*if(entity.isConcurrent())
                            {
                            
//                            if(rqEvent.eventId == 2) //Vua phom 29/11 den 02/12
//                            {
//                                CacheUserInfo cacheUser = new CacheUserInfo();
                                CacheEventInfo cache = new CacheEventInfo();
                                List<EventPlayerEntity> lstPlayers = cache.getEventPlayer(rqEvent.eventId);
                                int playerSize = lstPlayers.size();
                                if(playerSize>0)
                                {
                                    sb.append(" \n ****************************");
                                    sb.append(" \n ******Bảng thành tích*******");
                                    sb.append(" \n ****************************\n");
                                }
                                
                                for(int j = 0; j< playerSize; j++)
                                {
                                    EventPlayerEntity player = lstPlayers.get(j);
                                    sb.append("\n ").append(player.getUsrEntity().mUsername).append(" ").append(player.getDescription());
                                     sb.append(" \n ...............................");
                                }
                            }  */
                                
//                            }
                            
                            resBoc.mCode = ResponseCode.SUCCESS;
                            resBoc.value = sb.toString();
                            
                            if(entity.isDetailImage())
                            {
                                StringBuilder thumb = new StringBuilder();
                                thumb.append(entity.getEventId()).append(AIOConstants.SEPERATOR_BYTE_1);
                                thumb.append("0").append(AIOConstants.SEPERATOR_BYTE_1);
                                thumb.append(entity.getPicDetail());
                                insertQueue(msgFactory, thumb.toString(), aSession);
                            }
                            
                            
                            return 1;
                        }
                    }
                    
                    resBoc.mCode = ResponseCode.FAILURE;
                    resBoc.value = "Không tồn tại event này";
                        
		} finally {
			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

	
}
