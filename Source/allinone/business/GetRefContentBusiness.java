package allinone.business;

import allinone.data.UserEntity;
import allinone.databaseDriven.ReferenceCodesDB;
import allinone.protocol.messages.GetRefContentResponse;
import allinone.server.Server;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Random;
import org.slf4j.Logger;
import vn.game.common.ILoggerFactory;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;

public class GetRefContentBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetRefContentBusiness.class);

    private char[] charArray = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'k', 'l', 'm'};

    public GetRefContentBusiness() {
    }

    public String getBarCode(int numChar, int limit) throws SQLException {
        if (limit >= 5) {
            return "";
        }
        limit++;
        Random rnd = new Random();
        String result = "";
        for (short i = 0; i < numChar; i = (short) (i + 1)) {
            result = result + charArray[rnd.nextInt(charArray.length - 1)];
            System.out.println("getBarCode : " + result);
        }
        long uid = ReferenceCodesDB.checkRefCode(result);
        if ((result.length() <= 1) || (uid != -1L)) {
            result = getBarCode(numChar, limit);
        }
        return result;
    }

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException {
        int result = -2;
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetRefContentResponse res = (GetRefContentResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        //session = aSession;
        try {
            String barCode = ReferenceCodesDB.getCodeFromUid(aSession.getUID().longValue());
            boolean insertToDB = false;
            if (barCode.length() <= 1) {
                barCode = getBarCode(6, 0);
                insertToDB = true;
            }
            if (barCode.length() > 1) {
                if (insertToDB) {
                    ReferenceCodesDB.insertRefCode(barCode, aSession.getUID().longValue());
                }
                res._dialogueContent = ("Mã giới thiệu: " + barCode + ". Tặng bạn bè " + Server.MONEY_BONUS_REFERENCE + " đồng để cùng chơi game và nhận được 10% khi người bạn mời nạp thẻ");
                //res._smsContent = (aSession. + " đang chơi game, tải tại http://choidi.xyz/?ref=" + barCode + " để cùng chơi và nhập mã \"" + barCode + "\" để được tặng " + Server.MONEY_BONUS_REFERENCE);
                res._smsContent = "";
                res._referenceCode = barCode;
                res._urlReference = ("http://xyz/?ref=" + barCode);
                res.mCode = 1;
            } else {
                res.setFailure(0, "Không sinh được mã!");
            }
            result = -1;
        } catch (Throwable t) {
            res.setFailure(0, "Có lỗi xảy ra, vui lòng thử lại lúc khác!");
            result = -1;
        } finally {
            aResPkg.addMessage(res);
        }
        return result;
    }
}
