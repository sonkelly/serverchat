package allinone.business;

import allinone.data.HTTPPoster;
import java.sql.SQLException;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.CheckGiftCodeRequest;
import allinone.protocol.messages.CheckGiftCodeResponse;
import java.net.URLEncoder;
import org.json.JSONObject;
import vn.game.common.MD5;
import vn.game.common.sendCommon;

public class CheckGiftCodeBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(CheckGiftCodeBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {

        MessageFactory msgFactory = aSession.getMessageFactory();
        CheckGiftCodeResponse resBoc = (CheckGiftCodeResponse) msgFactory
                .getResponseMessage(aReqMsg.getID());
        try {
            CheckGiftCodeRequest rqEvent = (CheckGiftCodeRequest) aReqMsg;
//                    UserDB db = new UserDB();
//                    int type = 0;
//                    if(aSession.isMXHDevice())
//                        type = 1;
//                    else if(!aSession.isMobileDevice())
//                        type = 2;
//                    
//                    int ret = db.useGiftcode(aSession.getUID(), rqEvent.giffCode, type);
//                    if(ret < 1)
//                        throw new BusinessException("Không tồn tại hoặc đã bị sử dụng");
//                    
//                    resBoc.setSuccess(Integer.toString(ret));
            int partnerId = 10;
            String signStr = "";

            signStr = aSession.getUID() + "|" + aSession.getUserName() + "|" + partnerId + "|" + rqEvent.giffCode + "|" + aSession.getIP() + "|" + "23aasd22#4@1490kdj34&^45u234";
            String sign = MD5.toMD5(signStr);

            StringBuffer url = new StringBuffer();
            url.append("").append("?partnerId=" + partnerId);
            url.append("&userId=").append(aSession.getUID().toString());
            url.append("&userName=").append(aSession.getUserName());
            url.append("&cardCode=").append(rqEvent.giffCode);
            url.append("&ip=").append(aSession.getIP());
            url.append("&sign=").append(sign);
            HTTPPoster p = new HTTPPoster();
            p.setParameCall(HTTPPoster.API_GIFT_CODE);
            mLog.debug("gift code url.toString() " + url.toString());
            String resPost = p.callGet(url.toString());
            if (resPost != null) {

                JSONObject json = new JSONObject(resPost);

                if (json != null) {
                    int error = json.getInt("error");
                    String message = json.getString("msg");
                    switch (error) {
                        case 0:
                            resBoc.setSuccess(message);
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

                            resBoc.setFailure(message);
                            break;
                        default:
                            resBoc.setFailure(message);
                            break;
                    }
                }
            } else {
                resBoc.setFailure("Lỗi hệ thống, vui lòng liên hệ ban quản trị.");
            }

        } catch (Exception be) {
            resBoc.setFailure(be.getMessage());
        } finally {
            aResPkg.addMessage(resBoc);
        }
        return 1;
    }

}
