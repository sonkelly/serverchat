package allinone.business;

import allinone.data.MessagesID;
import allinone.data.SimpleTable;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;

import allinone.protocol.messages.XocDiaFireRequest;
import allinone.protocol.messages.XocDiaFireResponse;
import allinone.server.Server;
import java.io.PrintStream;
import java.util.Calendar;
import org.slf4j.Logger;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.common.sendCommon;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.session.ISession;
import vn.game.workflow.SimpleWorkflow;

public class XocDiaFireBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(XocDiaFireBusiness.class);

    public XocDiaFireBusiness() {
    }

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException {
        int result = -2;
        if (aSession.getUserEntity().lockRequest) {
            return result;
        }
        aSession.getUserEntity().lockRequest = true;
        //mLog.debug("[XocDiaFireBusiness] CATCH");
        MessageFactory msgFactory = aSession.getMessageFactory();
        XocDiaFireResponse response = (XocDiaFireResponse) msgFactory.getResponseMessage(MessagesID.XOCDIA_VAOTIEN);

        try {
            aSession.setLastAccessTime(Calendar.getInstance().getTime());
            //mLog.debug("[XocDiaFireBusiness] request");
            XocDiaFireRequest request = (XocDiaFireRequest) aReqMsg;
            //mLog.debug("[XocDiaFireBusiness] request money:" + request.money);
            Room room = aSession.getRoom();
            long money = request.money;
            long moneyToCheck = 0L;
            boolean checkMoney = true;
            //mLog.debug("[XocDiaFireBusiness] request table:", table);
            if (room != null) {
                int zoneID = 0;
                zoneID = aSession.getCurrentZone();
                //mLog.debug("[XocDiaFireBusiness] CURRENT ZONE ID : " + zoneID + "| room : " + room.getRoomId());
                if (room != null) {
                    //mLog.debug("[XocDiaFireBusiness] " + room.getName() + "| ");

                }
            }

            UserDB userDb = new UserDB();
            long moneyDB =  userDb.getMoney(aSession.getUserEntity().mUid, aSession.getRealMoney());
            long moneySession = aSession.getUserEntity().money;
            //money money cuoc
          
            //long totalBet = player.getMutilBet();
            
            
            //mLog.debug("MONEY TO request.betside : " + request.betside + "| moeny : " + money + " moneySession:" + moneySession + " table.gState:" + table.gState +" totalBet:"+totalBet);
        } catch (Throwable t) {
            mLog.debug("loi roi", t);
            response.setFailure("Có lỗi xảy ra, vui lòng thử lại Throwable XocDiaFire");
            aSession.getUserEntity().lockRequest = false;
            aResPkg.addMessage(response);
        } finally {
            aSession.getUserEntity().lockRequest = false;
            aResPkg.addMessage(response);
        }
        return result;
    }
}
