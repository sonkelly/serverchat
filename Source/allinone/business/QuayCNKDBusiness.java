package allinone.business;

import java.util.Random;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.HTTPPoster;
import allinone.data.MessagesID;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.ItemDB;
import allinone.databaseDriven.UserDB;
//import allinone.protocol.messages.MiniPokerSpinRequest;
//import allinone.protocol.messages.MiniPokerSpinResponse;
import allinone.protocol.messages.QuayCNKDResponse;
import java.net.URLEncoder;
import org.json.JSONObject;
import vn.game.common.MD5;
import vn.game.common.ServerException;
import vn.game.common.sendCommon;
import vn.game.room.Room;

public class QuayCNKDBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(QuayCNKDBusiness.class);
    
    private static final int[] arrBonusMoney = {0, 0, 0, 0, 2500, 2500, 5000, 5000, 10000, 10000, 25000, 50000};
    private static final int[] arrBonusTimes = {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int[] arrClient = {0, 2, 1, 3, 20, 22, 11, 13, 10, 12, 21, 23};
public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException {
        int result = -2;
        if (aSession.getUserEntity().lockRequest) {
            return result;
        }
        aSession.getUserEntity().lockRequest = true;
        mLog.debug("[VQMN] CATCH");
        MessageFactory msgFactory = aSession.getMessageFactory();
        QuayCNKDResponse response = (QuayCNKDResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        try {
            mLog.debug("[VQMNBusiness] request");
            //MiniPokerSpinRequest request = (MiniPokerSpinRequest) aReqMsg;
            //mLog.debug("[VQMNBusiness] request cuoc:" + request.cuoc);
            Room room = aSession.getRoom();
            boolean checkMoney = true;

//            int cuoc =  100;
//
//            if(cuoc < 0){
//                 response.setFailure("Dữ liệu không đúng.");
//            }    

            UserDB userDb = new UserDB();
            long moneyDB = userDb.getMoney(aSession.getUserEntity().mUid,"money");
            mLog.debug("MONEy DB"+moneyDB);
            //mLog.debug("MONEY TO request.betside : " + request.betside + "| moeny : " + money + " moneyDB:" + moneyDB + " table.gState:" + table.gState);
//            if ( cuoc > moneyDB ) {
//                response.setFailure("Bạn không đủ tiền để chơi. vui lòng nạp tiền.");
//                checkMoney = false;
//            }else{

                String signStr = aSession.getID() + "|" + aSession.getUserEntity().mUsername +"|" + "23aasd22#4@1490kdj34&^45u234";
                String sign = MD5.toMD5(signStr);

                StringBuffer url = new StringBuffer();
                url.append("").append("&userId=").append(aSession.getUserEntity().mUid);
                url.append("&userName=").append(URLEncoder.encode(aSession.getUserEntity().mUsername, "UTF-8"));

                //url.append("&cuoc=").append(cuoc);
                
                int typemoney = 1;
                if(UserDB.isRealMoney){
                    typemoney = 2;
                }

                url.append("&ip=").append(URLEncoder.encode(aSession.getIP(), "UTF-8"));
                url.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
               
                HTTPPoster p = new HTTPPoster();
                p.setParameCall(HTTPPoster.API_VQMN_SPIN);
                mLog.debug("API_VQMN_SPIN money  url.toString() " + url.toString());
                String resPost = p.callPost(url.toString());
                if (resPost != null) {

                    JSONObject json = new JSONObject(resPost);

                    if (json != null) {
                        int error = json.getInt("error");
                        String message = json.getString("msg");
                        switch (error) {
                            case 0:
                                String msg = json.getString("msg");
                              
                                String moneyWin = json.getString("moneyWin");
                                String count = json.getString("count");
                                mLog.debug("moneyWin222:"+moneyWin);
                                //response.setSuccess(smg,moneyWin);
                                response.mCode = ResponseCode.SUCCESS;
                                response.msg = msg.toString();
                                response.moneyWin = moneyWin;
                                response.count = count;
                                long getmoneyDB = userDb.getMoney(aSession.getUserEntity().mUid,"money");
                                aSession.getUserEntity().money = getmoneyDB;

                                //Send update card
                                sendCommon.sendUpdateMoney(aSession, msgFactory);
                                //sendCommon.sendHuSlotInfo(aSession, msgFactory);
                                break;
                            case 2:

                                response.setFailure(message);
                                break;
                            default:
                                response.setFailure( message);
                                break;
                        }
                    }
//                } else {
//                    response.setFailure("Lỗi hệ thống, vui lòng liên hệ ban quản trị.");
//                }
                
                      

            }
        } catch (Throwable t) {
            mLog.debug("loi roi", t);
            response.setFailure("Có lỗi xảy ra, vui lòng thử lại sau");
            aSession.getUserEntity().lockRequest = false;
            aResPkg.addMessage(response);
        } finally {
            aSession.getUserEntity().lockRequest = false;
            aResPkg.addMessage(response);
        }
        return result;
    }
//    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("[BUY_ITEM]: Catch");
//        MessageFactory msgFactory = aSession.getMessageFactory();
//        QuayCNKDResponse resBuyAvatar =
//                (QuayCNKDResponse) msgFactory.getResponseMessage(aReqMsg.getID());
//        
//        try {
////            QuayCNKDRequest rqBuyAvatar = (QuayCNKDRequest) aReqMsg;
////            ItemDB db = new ItemDB();
//            
//            Random rand = new Random(System.currentTimeMillis());
//            int ret = (int)(Math.abs(rand.nextLong() % 12));
//            
//            if(ret> 9)
//            {
//                //it means win 5 or 10 times
//                int nextRet = (int)(Math.abs(rand.nextLong() % 5));
//                if(nextRet>0)
//                {
//                    ret = (int)(Math.abs(rand.nextLong() % 4)) +2; // it fails
//                }
//                
//            }
//            
//            ItemDB db = new ItemDB();
//            UserEntity entity = db.quayCNKD(aSession.getUID(), arrBonusMoney[ret], arrBonusTimes[ret]);
//            
//            if(entity.timesQuay < -1)
//            {
//                throw new BusinessException("Bạn không đủ 5.000 Bi để quay vòng");
//            }
//            
//            //update cash
//            StringBuilder sb = new StringBuilder();
//            sb.append(arrClient[ret]).append(AIOConstants.SEPERATOR_BYTE_1);
//            sb.append(entity.timesQuay);
//            
//            
//           resBuyAvatar.mCode = ResponseCode.SUCCESS;
//           resBuyAvatar.value = sb.toString();
//                    
//        }
//        catch(BusinessException be)
//        {
//            resBuyAvatar.setFailure(be.getMessage());
//        }
//        catch (Throwable t) {
//            mLog.error(t.getMessage(), t);
//            resBuyAvatar.setFailure("Có lỗi xảy ra");
//        } finally {
//            if ((resBuyAvatar != null)) {
//                aResPkg.addMessage(resBuyAvatar);
//            }
//        }
//        return 1;
//    }
}
