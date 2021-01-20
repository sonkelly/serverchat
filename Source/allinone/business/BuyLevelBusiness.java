package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.databaseDriven.DatabaseDriver;
import allinone.databaseDriven.InfoDB;
import allinone.protocol.messages.BuyLevelRequest;
import allinone.protocol.messages.BuyLevelResponse;

public class BuyLevelBusiness extends AbstractBusiness {

    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(BuyLevelBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[BUY-LEVEL]: Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        BuyLevelResponse resBuyLevel =
                (BuyLevelResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            BuyLevelRequest rqBuyLevel = (BuyLevelRequest) aReqMsg;
            long uid = rqBuyLevel.uid;
            int currLevel = DatabaseDriver.getUserLevel(uid);
            if (currLevel == 15) {
                resBuyLevel.setFailure(ResponseCode.FAILURE, "Bạn đã ở trên đỉnh, không thể lên hơn được nữa");
            } else {
                InfoDB infoDb = new InfoDB();
                long money = infoDb.getMoneyForUpdateLevel(currLevel + 1);
                long cashU = DatabaseDriver.getUserMoney(uid);
                if (cashU >= money) {
                    DatabaseDriver.updateLevel(uid);
                    long newMoney = infoDb.getMoneyForUpdateLevel(currLevel + 1);
                    resBuyLevel.setSuccess(ResponseCode.SUCCESS, cashU - money, currLevel + 2, newMoney);
                } else {
                    resBuyLevel.setFailure(ResponseCode.FAILURE, "Bạn không đủ tiền để nâng cấp.");
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            resBuyLevel.setFailure(ResponseCode.FAILURE, "Có lỗi xảy ra khi bạn nâng cấp");
        } finally {
            if ((resBuyLevel != null)) {
                aResPkg.addMessage(resBuyLevel);
            }
        }
        return 1;
    }
}
