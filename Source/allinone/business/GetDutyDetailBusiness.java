/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.util.List;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.DutyEntity;
import allinone.databaseDriven.DutyDB;
import allinone.protocol.messages.GetDutyDetailRequest;
import allinone.protocol.messages.GetDutyDetailResponse;


/**
 *
 * @author mcb
 */
public class GetDutyDetailBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetDutyDetailBusiness.class);
    
//    private static String WAP_LINK = "http://m.landsoft.vn?p=";
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {

        GetDutyDetailRequest rqBot = (GetDutyDetailRequest) aReqMsg;
        
        
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetDutyDetailResponse resWapGame = (GetDutyDetailResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try
        {
        
            DutyDB db = new DutyDB();
            List<DutyEntity> lstDuties = db.getDuties();
            boolean hasFound = false;
            int size = lstDuties.size();
            for(int i = 0; i< size; i++)
            {
                DutyEntity entity = lstDuties.get(i);
                if(entity.getDutyId() == rqBot.dutyId)
                {
                    hasFound = true;
                    resWapGame.setSuccess(entity.getDutyDetail());
                    break;
                }
            }
            
            if(!hasFound)
                resWapGame.setFailure("Không tồn tại nhiệm vụ này");
            
        }
        catch(Exception ex )
        {
            mLog.error(ex.getMessage(), ex);
            
        }
        aResPkg.addMessage(resWapGame);
        return 1;
    }
}
