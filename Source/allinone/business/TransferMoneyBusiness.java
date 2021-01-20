/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

/**
 *
 * @author Zeple
 */
import allinone.data.HTTPPoster;
import allinone.data.QueueEntity;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.TransferMoneyRequest;
import allinone.protocol.messages.TransferMoneyResponse;
import allinone.queue.data.CommonQueue;
import java.net.URLEncoder;
import org.json.JSONObject;
import vn.game.protocol.MessageFactory;
import org.slf4j.Logger;

import vn.game.common.ILoggerFactory;
import vn.game.common.LoggerContext;
import vn.game.common.MD5;
import vn.game.common.sendCommon;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.room.Room;
import vn.game.session.ISession;

public class TransferMoneyBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(TransferMoneyBusiness.class);

    public TransferMoneyBusiness() {
    }

    @Override
    public int handleMessage(ISession aSession, IRequestMessage request, IResponsePackage aResPkg) {
        mLog.debug("[TransferMoneyBusiness]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        TransferMoneyResponse res = (TransferMoneyResponse) msgFactory.getResponseMessage(request.getID());
        TransferMoneyRequest req = (TransferMoneyRequest) request;
        try {
            Room room = aSession.getRoom();
            if (room == null) {
                QueueEntity queeueTransferMoeny = new QueueEntity(aSession, res);
                CommonQueue queue = new CommonQueue();
                queue.insertQueue(queeueTransferMoeny);

                //if ((aSession.getUserEntity().money > 10000L) && (req.money >= 5000L)) {
                if (req.money < aSession.getUserEntity().money) {
                    UserEntity desUser = null;
                    UserDB userObj = new UserDB();
                    //System.out.println("desName:" + req.desName);
                    if (req.desName != null) {
                        desUser = userObj.getUserInfoByViewName(req.desName, aSession.getRealMoney());
                    } else {
                        desUser = userObj.getUserInfo(req.desID, aSession.getRealMoney());
                    }
                    if (desUser != null) {

                        String signStr = aSession.getID() + "|" + aSession.getUserEntity().mUsername + "|" + req.money + "|" + "23aasd22#4@1490kdj34&^45u234";
                        String sign = MD5.toMD5(signStr);

                        StringBuffer url = new StringBuffer();
                        url.append("").append("&userId=").append(aSession.getUserEntity().mUid);
                        url.append("&userName=").append(URLEncoder.encode(aSession.getUserEntity().mUsername, "UTF-8"));
                        url.append("&idReceive=").append(desUser.mUid);
                        url.append("&money=").append(req.money);
                        url.append("&pass=").append(req.pass);
                        url.append("&phone=").append(req.phone);
                        url.append("&checkpass=").append(1);
                        int typemoney = 1;
                        if (UserDB.isRealMoney) {
                            typemoney = 2;
                        }

                        url.append("&ip=").append(URLEncoder.encode(aSession.getIP(), "UTF-8"));
                        url.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
                        url.append("&typeMoney=").append(typemoney);
                        HTTPPoster p = new HTTPPoster();
                        p.setParameCall(HTTPPoster.API_TRANFER_MONEY);
                        mLog.debug("tranfer money  url.toString() " + url.toString());
                        String resPost = p.callPost(url.toString());
                        if (resPost != null) {

                            JSONObject json = new JSONObject(resPost);

                            if (json != null) {
                                int error = json.getInt("error");
                                String message = json.getString("msg");
                                switch (error) {
                                    case 0:

                                        UserDB userDb = new UserDB();
                                        long moneyDB = userDb.getMoney(aSession.getUserEntity().mUid, aSession.getRealMoney());
                                        if (aSession.getIsRealMoney()) {
                                            aSession.getUserEntity().realmoney = moneyDB;
                                        } else {
                                            aSession.getUserEntity().money = moneyDB;
                                        }
                                        res.success(message, moneyDB);
                                        //Send update card
                                        sendCommon.sendUpdateMoney(aSession, msgFactory);
                                        break;
                                    case 2:

                                        res.setFailure(message);
                                        break;
                                    default:
                                        res.setFailure(message);
                                        break;
                                }
                            }
                        } else {
                            res.setFailure("Lỗi hệ thống, vui lòng liên hệ ban quản trị.");
                        }
//                            if (getUserEntityisPartner) {
//                                curMoney = UserDB.transferMoney(money, getUserEntitymUid, mUid);
//                                if (curMoney != -1L) {
//                                    res.success("Chuyển tiền thành công cho " + mUsername);
//                                } else {
//                                    res.setFailure("Lỗi hệ thống!");
//                                }
//                            } else {
//                                mLog.debug("[Finded Destination user]: " + mUsername + "| " + mUid);
//
//                                if (UserDB.checkSuperUser(mUid)) {
//                                    curMoney = UserDB.transferMoney(money, getUserEntitymUid, mUid);
//                                    if (curMoney != -1L) {
//                                        res.success("Chuyển tiền thành công cho " + mUsername);
//                                    } else {
//                                        res.setFailure("Lỗi hệ thống!");
//                                    }
//                                } else {
//                                    res.setFailure("Người chơi bạn chuyển không phải là nhà phân phối !");
//                                }
//                            }
                    } else {
                        res.setFailure("Không tìm thấy người chơi !");
                    }
                } else {
                    res.setFailure("Số tiền bạn chuyển nhiều hơn số tiền bạn có !");
                }

            } else {
                res.setFailure("Không thể chuyển tiền trong bàn chơi !");
            }
        } catch (Exception e) {
            res.setFailure("Phiên đăng nhập hết hạn, vui lòng đăng nhập lại !");
        } finally {
            aResPkg.addMessage(res);
        }
        return 0;
    }
}
