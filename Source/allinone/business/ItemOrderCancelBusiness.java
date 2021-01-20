package allinone.business;

import allinone.data.HTTPPoster;
import tools.CacheItem;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.ItemDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.ItemOrderCancelRequest;
import allinone.protocol.messages.ItemOrderCancelResponse;
import java.net.URLEncoder;
import org.json.JSONObject;
import vn.game.common.MD5;
import vn.game.common.sendCommon;

public class ItemOrderCancelBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog = LoggerContext.getLoggerFactory().getLogger(ReadMessageBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[ReadMessage] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        ItemOrderCancelResponse response = (ItemOrderCancelResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        response.session = aSession;

        try {
            long userId = aSession.getUID();
            String username = aSession.getUserName();
            double money = aSession.getUserEntity().money;

            ItemOrderCancelRequest req = (ItemOrderCancelRequest) aReqMsg;

            long itemOrderId = req.itemOrderId;
            mLog.debug("Cancel Order Item " + itemOrderId);
            //edit by zep
            String signStr = userId + "|" + username + "|" + money + "|" + "23aasd22#4@1490kdj34&^45u234";
            //String sign = MD5.toMD5(signStr);
            String sign = MD5.toMD5(signStr);

            StringBuffer url = new StringBuffer();
            url.append("").append("partnerId=1");
            url.append("&serviceId=").append(1);
            url.append("&userId=").append(userId);
            url.append("&userName=").append(URLEncoder.encode(username, "UTF-8"));
            url.append("&itemid=").append(itemOrderId);
            url.append("&ip=").append(URLEncoder.encode(aSession.getIP(), "UTF-8"));
            url.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
            url.append("&typeMoney=").append(2);
            HTTPPoster p = new HTTPPoster();
            p.setParameCall(HTTPPoster.CANCEL_ORDER_ITEM);
            String res = p.callPost(url.toString());
            if (res != null) {
                //System.out.println("res" + res);

                JSONObject json = new JSONObject(res);

                if (json != null) {
                    int error = json.getInt("error");
                    String message = json.getString("msg");
                    if (error == 0) {
                        response.setSuccess(ResponseCode.SUCCESS, message);
                        UserDB userDb = new UserDB();
                        long moneyDB = userDb.getMoney(aSession.getUserEntity().mUid, aSession.getRealMoney());
                        if (aSession.getIsRealMoney()) {
                            aSession.getUserEntity().realmoney = moneyDB;
                        } else {
                            aSession.getUserEntity().money = moneyDB;
                        }

                        //Send update card
                        sendCommon.sendUpdateMoney(aSession, msgFactory);
                    } else {
                        response.setSuccess(ResponseCode.SUCCESS, message);
                    }
                }
            } else {
                response.setSuccess(ResponseCode.SUCCESS, "Lỗi hệ thống, vui lòng liên hệ ban quản trị.");
            }
//end edit by zep
            // order
//            int result = ItemDB.cancelOrderItem(userId, itemOrderId);
//            
//            if(result == 1) {
//            	response.setSuccess(ResponseCode.FAILURE, "Không thể hoàn trả");
//            } else {
//                response.setSuccess(ResponseCode.SUCCESS, "Hoàn trả thành công");
//                // update item history cached
//                CacheItem cache = new CacheItem();
//                cache.updateItemsOrderHistory(userId);
//            }

        } catch (Exception e) {
            mLog.error(e.getMessage(), e);
            response.setFailure(ResponseCode.FAILURE, "Có lỗi hủy đơn hàng, vui lòng thử lại sau.");
        } finally {
            if ((response != null)) {
                aResPkg.addMessage(response);
            }
        }
        return 1;
    }

}
