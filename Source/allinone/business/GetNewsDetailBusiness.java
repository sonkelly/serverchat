/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;


import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.NewsDetailEntity;
import allinone.data.QueueNewsEntity;
import allinone.protocol.messages.GetNewsDetailRequest;
import allinone.protocol.messages.GetNewsDetailResponse;
import allinone.queue.data.GetNewsQueue;




/**
 *
 * @author mcb
 */
public class GetNewsDetailBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetNewsDetailBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get news category");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetNewsDetailResponse resNews = (GetNewsDetailResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetNewsDetailRequest rqAlb = (GetNewsDetailRequest) aReqMsg;
            
            NewsDetailEntity entity = new NewsDetailEntity(rqAlb.newsId, null);
            entity.setPageindex(rqAlb.pageIndex +1);
            entity.setCategoryId(rqAlb.categoryId);
            GetNewsDetailResponse queueNews = (GetNewsDetailResponse) msgFactory.getResponseMessage(aReqMsg.getID());
            
            QueueNewsEntity queueEntity = new QueueNewsEntity(entity, aSession, queueNews);
            GetNewsQueue queue = new GetNewsQueue();
            queue.insertNews(queueEntity);
            resNews = null;
        } 
        
        finally
        {
            if (resNews!= null)
            {
                try {
                    aSession.write(resNews);
                } catch (ServerException ex) {
                    mLog.error(ex.getMessage(), ex);
                }
                        
            }
        }         
        return 1;
    }
}
