package allinone.business;

import allinone.protocol.messages.CancelShowHandRequest;
import allinone.protocol.messages.CancelShowHandResponse;
import java.util.logging.Level;
import org.slf4j.Logger;
import vn.com.landsoft.common.ILoggerFactory;
import vn.com.landsoft.common.LoggerContext;
import vn.com.landsoft.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.session.ISession;
import vn.game.workflow.SimpleWorkflow;

public class CancelShowHandBusiness
        extends AbstractBusiness {

    public CancelShowHandBusiness() {
    }

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CancelShowHandBusiness.class);

    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[BET] : Catch  ; " + aSession.getUserName());
        MessageFactory msgFactory = aSession.getMessageFactory();
        CancelShowHandResponse resCancel = (CancelShowHandResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try {
            CancelShowHandRequest rqCancel = (CancelShowHandRequest) aReqMsg;
            Room room = aSession.getRoom();

            if (room == null) {
                throw new BusinessException("Bạn cần tham gia vào một trận trước khi chơi.");
            }

            switch (aSession.getCurrentZone()) {
                case 12:
//        BauCuaTomCaTable table = (BauCuaTomCaTable)room.getAttactmentData();
//        
//        table.cancelShowHand(players);

            }

        } //    catch (BauCuaTomCaException ex)
        //    {
        //      resCancel.setFailure(ex.getMessage());
        //      
        //      mLog.debug("Invalid " + ex.getMessage());
        //      try
        //      {
        //        aSession.write(resCancel);
        //
        //      }
        //      catch (ServerException localServerException) {}
        //
        //    }
        //    catch (BusinessException ex)
        //    {
        //      mLog.error("Process message " + aReqMsg.getID() + " error." + ex.getMessage());
        //      resCancel.setFailure(ex.getMessage());
        //      
        //      mLog.error("Invalid " + ex.getMessage());
        //      try
        //      {
        //        aSession.write(resCancel);
        //
        //      }
        //      catch (ServerException localServerException1) {}
        //    }
        catch (Throwable t) {
            mLog.error("Process message " + aReqMsg.getID() + " error.", t);
            try {
                aSession.write(resCancel);
            } catch (vn.game.common.ServerException ex) {
                java.util.logging.Logger.getLogger(CancelShowHandBusiness.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 1;
    }
}
