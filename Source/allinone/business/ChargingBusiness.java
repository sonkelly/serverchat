package allinone.business;

import java.util.ArrayList;

import org.slf4j.Logger;

import tools.CacheEventInfo;
import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.ResponseCode;
import allinone.data.RevenueInfo;
import allinone.data.UserEntity;
import allinone.protocol.messages.ChargingResponse;
import java.util.Calendar;

public class ChargingBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChargingBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        MessageFactory msgFactory = aSession.getMessageFactory();
        ChargingResponse resCharg = (ChargingResponse) msgFactory.getResponseMessage(aReqMsg.getID());

        mLog.debug("[GET CHARGING]: Catch");
        aSession.setLastAccessTime(Calendar.getInstance().getTime());
        try {
            CacheUserInfo cache = new CacheUserInfo();
            UserEntity entity = cache.getUserInfo(aSession.getUID(),aSession.getRealMoney());
            resCharg.session = aSession;
//            int partnerId = entity.partnerId;
//
//            mLog.debug(" Chaging partnerId " + partnerId);

            CacheEventInfo cacheEvent = new CacheEventInfo();
            ArrayList<RevenueInfo> temp = (ArrayList<RevenueInfo>) cacheEvent.getRevenue();

            StringBuilder sb = new StringBuilder();
            int tempSize = temp.size();
            mLog.debug(" Revenue size " + tempSize);

            if (tempSize == 0) {
                resCharg.setFailure(ResponseCode.FAILURE, "Hệ thống thanh toán đang bảo trì");
                aResPkg.addMessage(resCharg);
                return 1;
            }

            boolean chargingFlag = false;
            for (int i = 0; i < tempSize; i++) {
                RevenueInfo info = temp.get(i);

                chargingFlag = true;
                sb.append(info.type).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(info.title).append(AIOConstants.SEPERATOR_BYTE_1);
                if (info.type == 1) {//loai sms
                    String serviceCode = info.serviceCode.replace("uid", "" + entity.mUid); //info.serviceCode + " " + entity.mUid;
                    sb.append(serviceCode).append(AIOConstants.SEPERATOR_BYTE_1);
                } else {//the cao
                    sb.append(info.serviceCode).append(AIOConstants.SEPERATOR_BYTE_1);
                }
                sb.append(info.vnd).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(info.cash).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(info.telNumber).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(info.rate).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(info.image);

                if (i <= tempSize - 1) {
                    sb.append(AIOConstants.SEPERATOR_BYTE_2);
                }
            }

            if (chargingFlag) {
                //sb.deleteCharAt(sb.length() - 1);
                resCharg.setSuccess(ResponseCode.SUCCESS, sb.toString());
                aSession.write(resCharg);
            } else {
                resCharg.setFailure(ResponseCode.FAILURE, "Hệ thống thanh toán đang bảo trì");
                aResPkg.addMessage(resCharg);
                return 1;
            }
        } catch (Throwable t) {
            resCharg.setFailure(ResponseCode.FAILURE, "Hệ thống thanh toán đang bảo trì");
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            aResPkg.addMessage(resCharg);
        }

        return 1;
    }
}
