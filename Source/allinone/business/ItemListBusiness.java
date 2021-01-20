package allinone.business;

import java.util.ArrayList;

import org.slf4j.Logger;

import tools.CacheItem;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ItemEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.ItemListRequest;
import allinone.protocol.messages.ItemListResponse;
import vn.game.common.SessionUtils;

public class ItemListBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(ItemListBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        mLog.debug("[Get ITEM]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        ItemListResponse response = (ItemListResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        ItemListRequest request = (ItemListRequest) aReqMsg;
        try {
            if (!SessionUtils.checkSessionDB(aSession)) {
                return 0;
            }
        } catch (Exception e) {

        }
        try {

            int cardSize = 0;
            int itemSize = 0;
            ArrayList<ItemEntity> temp = null;

            CacheItem cache = new CacheItem();
            mLog.debug("itemType:" + request.itemType);
            //if (request.itemType <= 0 || request.itemType > 2) {

            // get card list type = 1
            ArrayList<ItemEntity> cardList = (ArrayList<ItemEntity>) cache.getItemsByType(1);
            cardSize = cardList.size();

            // get item list type = 2
            ArrayList<ItemEntity> itemList = (ArrayList<ItemEntity>) cache.getItemsByType(2);
            itemSize = itemList.size();

            if (request.itemType == 1 || request.itemType == 0) {
                request.itemType = 1;
                temp = cardList;

            } else if (request.itemType == 2) {
                request.itemType = 2;
                temp = itemList;
            }

            //}
//            else {
//                temp = (ArrayList<ItemEntity>) cache.getItemsByType(request.itemType);
//                mLog.debug(" Item List size " + temp.size());
//                if (request.itemType == 1) {
//                    cardSize = temp.size();
//                }
//                if (request.itemType == 2) {
//                    itemSize = temp.size();
//                }
//            }
            response.setSuccess(ResponseCode.SUCCESS, temp);

            response.itemType = request.itemType;
            response.cardSize = cardSize;
            response.itemSize = itemSize;

            response.session = aSession;
            aSession.write(response);

        } catch (Throwable t) {
            response.setFailure(ResponseCode.FAILURE, "Có lỗi xảy ra.");
            aResPkg.addPackage(aResPkg);
        }

        return 1;
    }
}
