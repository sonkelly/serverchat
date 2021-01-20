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
import allinone.data.EventEntity;
import allinone.data.MessagesID;
import allinone.data.QueueEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetListEventResponse;
import allinone.protocol.messages.SendImageResponse;
import allinone.queue.data.CommonQueue;

public class GetListEventBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetListEventBusiness.class);
        
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
		GetListEventResponse resBoc = (GetListEventResponse) msgFactory.getResponseMessage(aReqMsg.getID());
		try {
//                    List<EventEntity> lstEvents = EventDB.getEvents(aSession.getUserEntity().partnerId);
					CacheEventInfo cacheEvent = new CacheEventInfo();
					List<EventEntity> lstEvents = cacheEvent.getEventList();
                    int size = lstEvents.size();
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i< size; i++)
                    {
                        EventEntity entity = lstEvents.get(i);
                        sb.append(entity.getEventId()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(entity.getTittle()).append(AIOConstants.SEPERATOR_BYTE_1);
                        sb.append(entity.isThumb()?"1":"0").append(AIOConstants.SEPERATOR_BYTE_2);                        
                        if(entity.isThumb())
                        {
                            StringBuilder thumb = new StringBuilder();
                            thumb.append(entity.getEventId()).append(AIOConstants.SEPERATOR_BYTE_1);
                            thumb.append("1").append(AIOConstants.SEPERATOR_BYTE_1);
                            thumb.append(entity.getThumbDetail());
                            insertQueue(msgFactory, thumb.toString(), aSession);
                        }
                    }                    
                    if(size>0)
                    sb.deleteCharAt(sb.length() -1);
                    resBoc.mCode = ResponseCode.SUCCESS;
                    resBoc.value = sb.toString();
                        
		} finally {
			aResPkg.addMessage(resBoc);
		}
		return 1;
	}

	
}
