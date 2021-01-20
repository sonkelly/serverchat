/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.DutyEntity;
import allinone.databaseDriven.DutyDB;
import allinone.protocol.messages.DoneDutyRequest;
import allinone.protocol.messages.DoneDutyResponse;


/**
 *
 * @author mcb
 */
public class DoneDutyBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(DoneDutyBusiness.class);
    
//    private static String WAP_LINK = "http://m.landsoft.vn?p=";
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {

        DoneDutyRequest rqBot = (DoneDutyRequest) aReqMsg;
        
        
        MessageFactory msgFactory = aSession.getMessageFactory();
        DoneDutyResponse resDoneDuty = (DoneDutyResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        try
        {
        
            DutyDB db = new DutyDB();
            List<DutyEntity> lstDuties = db.getDuties();
            DutyEntity dutyEntity = null;
            int size = lstDuties.size();
            for(int i = 0; i< size; i++)
            {
                DutyEntity entity = lstDuties.get(i);
                if(entity.getDutyId() == rqBot.dutyId)
                {
                    dutyEntity = entity;
//                    resDoneDuty.setSuccess(entity.getDutyDetail());
                    break;
                }
            }
            
            if(dutyEntity==  null)
                throw new BusinessException("Không tồn tại nhiệm vụ này");
            
            SimpleDateFormat sdf = new SimpleDateFormat(dutyEntity.getDateFomat());
            Date dtNow = new Date();
            String dtNowString = sdf.format(dtNow);
            if(!dtNowString.matches(dutyEntity.getDtDuty()))
            {
                if(rqBot.dutyId == 1)
                {
                    throw new BusinessException("Đã hết hoặc chưa đến giờ báo danh");
                }
                else
                {
                    throw new BusinessException("Đã hết giờ làm nhiệm vụ hoặc chưa đến giờ");
                }
            }
            
//            if(rqBot.dutyId == 1)// bao danh
//            {
//                
                int ret = db.doneDuty(rqBot.dutyId, aSession.getUID());

                if(ret == -2)
                    throw new BusinessException("Bạn đã báo danh rồi");
            
                if(ret == -3)
                    throw new BusinessException("Đã đủ số người báo danh nhận giải. Lần sau bạn hãy đến báo danh sớm hơn");
                
                resDoneDuty.setSuccess("Bạn báo danh thành công và được thưởng 3.000 Bi vào tài khoản");
                
                
                
//            }
            

            
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
