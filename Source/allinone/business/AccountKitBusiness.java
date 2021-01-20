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
import allinone.data.ResponseCode;
import allinone.databaseDriven.ItemDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.AccountKitRequest;
import allinone.protocol.messages.AccountKitResponse;
import allinone.protocol.messages.ItemOrderRequest;
import allinone.protocol.messages.ItemOrderResponse;
import java.net.URLEncoder;
import org.json.JSONObject;
import vn.game.common.MD5;
import vn.game.common.sendCommon;

public class AccountKitBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog = LoggerContext.getLoggerFactory().getLogger(AccountKitBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[AccountKitBusiness] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        AccountKitResponse response = (AccountKitResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        response.session = aSession;

        try {
            long userId = aSession.getUID();
            String username = aSession.getUserName();
            boolean isActive = aSession.getUserEntity().isActive;
            if(isActive){
                response.setFailure(ResponseCode.FAILURE, "Tài khoản đã kích hoạt rồi.");
                return 0;
            }

            AccountKitRequest req = (AccountKitRequest) aReqMsg;

            String phone = req.phone;
            mLog.debug("phone: " + phone);
            //edit by zep
            String signStr = userId + "|" + username + "|" + phone + "|" + "23aasd22#4@1490kdj34&^45u234";
            String sign = MD5.toMD5(signStr);

            StringBuffer url = new StringBuffer();
            url.append("").append("phone=" + phone);
            url.append("&uid=").append(userId);
            url.append("&name=").append(URLEncoder.encode(username, "UTF-8"));
         
            
            url.append("&ip=").append(URLEncoder.encode(aSession.getIP(), "UTF-8"));
            url.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
            url.append("&typeMoney=").append(2);
           
            HTTPPoster p = new HTTPPoster();
            p.setParameCall(HTTPPoster.API_ACCOUNT_KIT);
            String res = p.callPost(url.toString());
            mLog.debug("phone res: " + res);
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
                            aSession.getUserEntity().money = moneyDB;

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
