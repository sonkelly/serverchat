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
import allinone.data.ItemOrderEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.ItemHistoryRequest;
import allinone.protocol.messages.ItemHistoryResponse;
import java.util.Calendar;

public class ItemHistoryBusiness extends AbstractBusiness {

    private static final Logger mLog
            = LoggerContext.getLoggerFactory().getLogger(ItemHistoryBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        //mLog.debug("[Get ITEM]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        ItemHistoryResponse response = (ItemHistoryResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        ItemHistoryRequest req = (ItemHistoryRequest) aReqMsg;
        try {

            long uid = aSession.getUID();

            CacheItem cache = new CacheItem();
            int page = req.page;

            if (page <= 0) {
                page = 1;
            }
            int items_per_page = 20;
            // build query
            int offset = (page - 1) * items_per_page;
            String limit = offset + "," + items_per_page;
            //mLog.debug(" Item order history limit: " + limit);
            ArrayList<ItemOrderEntity> temp = (ArrayList<ItemOrderEntity>) cache.getItemsOrderHistory(uid, limit);

            //mLog.debug(" Item Order List size " + temp.size());

            response.setSuccess(ResponseCode.SUCCESS, temp);
            aSession.setLastAccessTime(Calendar.getInstance().getTime());
            response.session = aSession;
            aSession.write(response);

        } catch (Throwable t) {
            response.setFailure(ResponseCode.FAILURE, "Có lỗi xảy ra.");
            aResPkg.addPackage(aResPkg);
        }

        return 1;
    }
}
