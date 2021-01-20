package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.protocol.messages.BuyItemResponse;

public class BuyItemBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(BuyItemBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[BUY_ITEM]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        BuyItemResponse resBuyAvatar =
                (BuyItemResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        
//        try {
//            BuyItemRequest rqBuyAvatar = (BuyItemRequest) aReqMsg;
//            ItemDB db = new ItemDB();
//            int ret = db.buyItem(aSession.getUID(), rqBuyAvatar.itemId);
//            
//            if (ret == -1) {
//                resBuyAvatar.setFailure("Không tồn tại người chơi");
//            } else if(ret == -2){
//                resBuyAvatar.setFailure("Bạn không có đủ tiền mua đồ");
//            }
//            else
//            {
//                resBuyAvatar.mCode = ResponseCode.SUCCESS;
//                CacheUserInfo cacheUserInfo = new CacheUserInfo();
//                cacheUserInfo.deleteFullCacheUser(aSession.getUserEntity());
//            }
//                        
//                    
//        } catch (Throwable t) {
//            mLog.error(t.getMessage(), t);
//            resBuyAvatar.setFailure("Có lỗi xảy ra");
//        } finally {
//            if ((resBuyAvatar != null)) {
//                aResPkg.addMessage(resBuyAvatar);
//            }
//        }
        
        return 1;
    }
}
