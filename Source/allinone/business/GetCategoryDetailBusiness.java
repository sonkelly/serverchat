/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;


import java.util.List;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.NewsCategoryEntity;
import allinone.data.QueueNewsEntity;
import allinone.databaseDriven.NewsCategoryDB;
import allinone.protocol.messages.GetCategoryDetailRequest;
import allinone.protocol.messages.GetCategoryDetailResponse;
import allinone.queue.data.GetNewsQueue;




/**
 *
 * @author mcb
 */
public class GetCategoryDetailBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetCategoryDetailBusiness.class);
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("get category detail");
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetCategoryDetailResponse resCat = (GetCategoryDetailResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            
            GetCategoryDetailRequest rqAlb = (GetCategoryDetailRequest) aReqMsg;
            
//            AlbumDB db = new AlbumDB();
            List<NewsCategoryEntity> lstCategories = NewsCategoryDB.getNewsCategory();
            
            NewsCategoryEntity catEntity = null;
            int categorySize = lstCategories.size();
            for(int i = 0; i< categorySize; i++)
            {
                NewsCategoryEntity entity = lstCategories.get(i);
                if(entity.getCategoryId() == rqAlb.categoryId)
                {
                    catEntity = entity;
                    break;
                }
            }
            
            if(catEntity == null)
            {
                throw new BusinessException("Chọn sai mục tin tức");
            }
            
            catEntity.setQueryPage(rqAlb.pageIndex+1);
//            resCat = null;
            GetCategoryDetailResponse queueCat = (GetCategoryDetailResponse) msgFactory.getResponseMessage(aReqMsg.getID());
            
            QueueNewsEntity queueEntity = new QueueNewsEntity(catEntity, aSession, queueCat);
            GetNewsQueue queue = new GetNewsQueue();
            queue.insertNews(queueEntity);
            
            resCat = null;
            
            
            
//            CacheNewsInfo cache  = new CacheNewsInfo();
//            resCat.value = cache.getCategoryDetail(catEntity, rqAlb.pageIndex + 1);
//
//            resCat.mCode = ResponseCode.SUCCESS;  
            
        } 
        catch(BusinessException ex)
        {
            //mLog.warn(ex.getMessage());
            resCat.setFailure(ex.getMessage());
        }
        finally
        {
            if (resCat!= null)
            {
                try {
                    aSession.write(resCat);
                } catch (ServerException ex) {
                    mLog.error(ex.getMessage(), ex);
                }
                        
            }
        }        
        return 1;
    }
}
