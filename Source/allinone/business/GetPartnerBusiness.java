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
import allinone.data.ResponseCode;
import allinone.data.SuperUserEntity;
import allinone.databaseDriven.PartnerDB;
import allinone.protocol.messages.GetPartnerResponse;
import allinone.protocol.messages.LoginResponse;
import java.util.ArrayList;
import java.util.logging.Level;
import org.slf4j.Logger;
import vn.game.common.ILoggerFactory;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;

public class GetPartnerBusiness
        extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetPartnerBusiness.class);

    /* Error */
    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
         mLog.debug("[LOGIN]: Catch");

        MessageFactory msgFactory = aSession.getMessageFactory();
        GetPartnerResponse resPartner = (GetPartnerResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        resPartner.mCode = 1;
        ArrayList<SuperUserEntity> arList = null;
        PartnerDB db = new PartnerDB();
        try {
            arList = (ArrayList<SuperUserEntity>) db.getListPartner();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GetPartnerBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        resPartner.arList = arList;
        resPartner.success(arList);
        resPartner.session = aSession;
        try {
            aSession.write(resPartner);
        } catch (ServerException ex) {
            java.util.logging.Logger.getLogger(GetPartnerBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
}
