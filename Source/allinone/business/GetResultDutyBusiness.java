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
import allinone.data.AIOConstants;
import allinone.data.AuditDutyEntity;
import allinone.data.DutyEntity;
import allinone.databaseDriven.DutyDB;
import allinone.protocol.messages.GetResultDutyRequest;
import allinone.protocol.messages.GetResultDutyResponse;



/**
 *
 * @author mcb
 */
public class GetResultDutyBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(GetResultDutyBusiness.class);
    
//    private static String WAP_LINK = "http://m.landsoft.vn?p=";
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {

        GetResultDutyRequest rqDuty = (GetResultDutyRequest) aReqMsg;
        
        
        MessageFactory msgFactory = aSession.getMessageFactory();
        GetResultDutyResponse resDoneDuty = (GetResultDutyResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try
        {
        
            DutyDB db = new DutyDB();
            List<DutyEntity> lstDuties = db.getDuties();
            DutyEntity dutyEntity = null;
            int size = lstDuties.size();
            for(int i = 0; i< size; i++)
            {
                DutyEntity entity = lstDuties.get(i);
                if(entity.getDutyId() == rqDuty.dutyId)
                {
                    dutyEntity = entity;
//                    resDoneDuty.setSuccess(entity.getDutyDetail());
                    break;
                }
            }
            
            if(dutyEntity==  null)
                throw new BusinessException("Không tồn tại nhiệm vụ này");
            
            List<AuditDutyEntity> lstAudits = db.getAuditDuty(rqDuty.dutyId);
            int auditSize = lstAudits.size();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i< auditSize; i++)
            {
                AuditDutyEntity entity = lstAudits.get(i);
                sb.append(Long.toString(entity.getUsrEntity().mUid)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(entity.getUsrEntity().mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Long.toString(entity.getUsrEntity().avFileId)).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Long.toString(entity.getAuditDate().getTime())).append(AIOConstants.SEPERATOR_BYTE_1);
                sb.append(Integer.toString(entity.getBonusMoney())).append(AIOConstants.SEPERATOR_BYTE_2);
            }
            
            if(auditSize>0)
            {
                sb.deleteCharAt(sb.length() -1);
            }
            
               
            resDoneDuty.setSuccess(sb.toString());
                
                
                
            
            

            
        }
        catch(BusinessException be)
        {
            resDoneDuty.setFailure(be.getMessage());
        }
        
        catch(Exception ex )
        {
            resDoneDuty.setFailure("Co loi xay ra");
            mLog.error(ex.getMessage(), ex);
            
        }
        
        aResPkg.addMessage(resDoneDuty);
        return 1;
    }
}
