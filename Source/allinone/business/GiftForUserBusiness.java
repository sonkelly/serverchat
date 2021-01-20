/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import allinone.data.HTTPPoster;
import allinone.data.ResponseCode;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.databaseDriven.InfoDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.GiftForUserRequest;
import allinone.protocol.messages.GiftForUserResponse;
import java.net.URLEncoder;
import org.json.JSONObject;
import vn.game.common.MD5;
import vn.game.common.sendCommon;

/**
 *
 * @author Vostro 3450
 */
public class GiftForUserBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(GiftForUserBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        MessageFactory msgFactory = aSession.getMessageFactory();
        GiftForUserResponse resGift = (GiftForUserResponse) msgFactory
                .getResponseMessage(aReqMsg.getID());
        try {
            GiftForUserRequest rq = (GiftForUserRequest) aReqMsg;
            if ((rq.objectID == null) || (rq.objectID.equals(""))) {
                resGift.setFailure("Mã gift code không đúng");

            } else {
                long uid = aSession.getUID();
                String uname = aSession.getUserName();
                String signStr = uid + "|" + uname + "|" + "23aasd22#4@1490kdj34&^45u234";
                String sign = MD5.toMD5(signStr);

                StringBuffer url = new StringBuffer();
                url.append("?userId=").append(uid);
                url.append("&userName=").append(URLEncoder.encode(uname, "UTF-8"));
                url.append("&cardCode=").append(URLEncoder.encode(rq.objectID, "UTF-8"));
                url.append("&ip=").append(URLEncoder.encode(aSession.getIP(), "UTF-8"));
                url.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
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
                                resGift.setSuccess(message);
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

                                resGift.setFailure(message);
                                break;
                            default:
                                resGift.setFailure(message);
                                break;
                        }
                    }
                } else {
                    resGift.setFailure("Lỗi hệ thống, vui lòng liên hệ ban quản trị.");
                }
            }
            //resBoc.setSuccess(String.valueOf(InfoDB.giftForUser(aSession.getUID(), rq.objectID, rq.type)));
        } catch (Throwable e) {
            resGift.setFailure(e.getMessage());
        } finally {
            aResPkg.addMessage(resGift);
        }
        return 1;
    }
}
