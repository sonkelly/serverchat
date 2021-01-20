package allinone.business;

import tools.CacheItem;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.HTTPPoster;
import allinone.data.QueueEntity;
import allinone.data.ResponseCode;
import allinone.databaseDriven.ItemDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.ItemOrderRequest;
import allinone.protocol.messages.ItemOrderResponse;
import allinone.queue.data.CommonQueue;
import java.net.URLEncoder;
import org.json.JSONObject;
import vn.game.common.MD5;
import vn.game.common.sendCommon;

public class ItemOrderBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog = LoggerContext.getLoggerFactory().getLogger(ReadMessageBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[ReadMessage] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        ItemOrderResponse response = (ItemOrderResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        response.session = aSession;

        try {
            long userId = aSession.getUID();
            String username = aSession.getUserName();
            double money = aSession.getUserEntity().money;

            QueueEntity queeueItemOrder = new QueueEntity(aSession, response);
            CommonQueue queue = new CommonQueue();
            queue.insertQueue(queeueItemOrder);

            ItemOrderRequest req = (ItemOrderRequest) aReqMsg;

            long itemId = req.itemId;
            String pass = req.pass;
            mLog.debug("Order Item " + itemId + " pass:" + pass);
            //edit by zep
            String signStr = userId + "|" + username + "|" + money + "|" + "23aasd22#4@1490kdj34&^45u234";
            String sign = MD5.toMD5(signStr);

            StringBuffer url = new StringBuffer();
            url.append("").append("itemid=" + itemId);
            url.append("&userId=").append(userId);
            url.append("&userName=").append(URLEncoder.encode(username, "UTF-8"));
            if (UserDB.isRealMoney) {
                url.append("&realmoney=").append(money);
            } else {
                url.append("&money=").append(money);
            }

            url.append("&ip=").append(URLEncoder.encode(aSession.getIP(), "UTF-8"));
            url.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
            url.append("&typeMoney=").append(2);
            url.append("&checkpass=").append(1);
            url.append("&pass=").append(pass);
            HTTPPoster p = new HTTPPoster();
            p.setParameCall(HTTPPoster.ORDER_ITEM);
            String res = p.callPost(url.toString());
            if (res != null) {

                JSONObject json = new JSONObject(res);

                if (json != null) {
                    int error = json.getInt("error");
                    String message = json.getString("msg");
                    switch (error) {
                        case 0:
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
                            break;
                        case 2:
                            response.errorCode = 1;
                            response.setFailure(ResponseCode.FAILURE, message);
                            break;
                        default:
                            response.setFailure(ResponseCode.FAILURE, message);
                            break;
                    }
                }
            } else {
                response.setFailure(ResponseCode.FAILURE, "Lỗi hệ thống, vui lòng liên hệ ban quản trị.");
            }
//end edit by zep

        } catch (Exception e) {
            mLog.error(e.getMessage(), e);
            response.setFailure(ResponseCode.FAILURE, "Lỗi giao dịch vui lòng thử lại sau ít phút.");
        } finally {
            if ((response != null)) {
                aResPkg.addMessage(response);
            }
        }
        return 1;
    }

}
