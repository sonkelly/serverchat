package allinone.business;

import allinone.data.UserEntity;
import allinone.databaseDriven.RefGioiThieuDB;
import allinone.databaseDriven.ReferenceCodesDB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.UpdateReferenceRequest;
import allinone.protocol.messages.UpdateReferenceResponse;
import allinone.server.Server;
import org.slf4j.Logger;
import tools.CacheUserInfo;
import vn.game.common.ILoggerFactory;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;

public class UpdateReferenceBusiness extends AbstractBusiness
{
  private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UpdateReferenceBusiness.class);
  
  public UpdateReferenceBusiness() {}
  
  public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) throws ServerException { 
      int result = -2;
    mLog.info("[UPDATE USER] : UPDATE REFERENCE USER NEWWWWWWWWWWWWWWWWWW");
    MessageFactory msgFactory = aSession.getMessageFactory();
    UpdateReferenceResponse res = (UpdateReferenceResponse)msgFactory.getResponseMessage(aReqMsg.getID());
//    session = aSession;
//    try {
//      long userId = aSession.getUID().longValue();
//      UpdateReferenceRequest rq = (UpdateReferenceRequest)aReqMsg;
//      
//      long uidRef = ReferenceCodesDB.checkRefCode(refrenceCode);
//      if (uidRef != -1L) {
//        if (uidRef != aSession.getUID().longValue()) {
//          mLog.info("[UPDATE USER] : UPDATE REFERENCE USER  " + aSession.getUID() + "| " + getUserEntitydevice + "| " + getUserEntityip + "| " + aSession.getOnlyIP());
//          if (RefGioiThieuDB.checkCanReference(aSession.getUID().longValue(), getUserEntitydevice, getUserEntityip)) {
//            if (RefGioiThieuDB.updateReference(aSession.getUID().longValue(), uidRef, getUserEntitydevice, getUserEntityip)) {
//              mLog.info("[INSERT DONE] : UPDATE REFERENCE USER " + aSession.getOnlyIP());
//              mLog.info("Nhập mã giới thiệu thành công, bạn được tặng " + Server.MONEY_BONUS_REFERENCE + " đồng!");
//              res.setSuccess(1, "Nhập mã giới thiệu thành công, bạn được tặng " + Server.MONEY_BONUS_REFERENCE + " đồng!");
//              
//              UserDB userDb = new UserDB();
//              long newmoney = userDb.updateUserMoneyForBonusNew(aSession.getUID().longValue(), Server.MONEY_BONUS_REFERENCE, "Nhap ma gioi thieu [" + refrenceCode + "] co uid la " + uidRef);
//              mLog.info("[UPDATE MONEY BONUS DONE] : UPDATE REFERENCE USER " + aSession.getOnlyIP());
//              newMoney = newmoney;
//              
//              UserEntity entity = UserDB.getUserInfo(userId);
//              money = newmoney;
//              if (entity != null) {
//                CacheUserInfo cacheUser = new CacheUserInfo();
//                cacheUser.updateCacheUserInfo(entity);
//              }
//              mLog.info("[UPDATE MONEY ENTITY DONE] : UPDATE REFERENCE USER " + aSession.getOnlyIP());
//              ReferenceCodesDB.updateRefCode(refrenceCode);
//              mLog.info("[UPDATE USED DONE] : UPDATE REFERENCE USER " + aSession.getOnlyIP());
//            } else {
//              mLog.info("Không thành công, vui lòng nhập lại mã giới thiệu!");
//              res.setFailure(0, "Không thành công, vui lòng nhập lại mã giới thiệu!");
//            }
//          } else {
//            mLog.info("Bạn không thể nhập quá mã giới thiệu nhiều lần!");
//            res.setFailure(0, "Bạn không thể nhập quá mã giới thiệu nhiều lần!");
//          }
//        } else {
//          mLog.info("Bạn không thể nhập quá mã giới thiệu nhiều lần!");
//          res.setFailure(0, "Bạn không thể nhập mã của chính bạn!");
//        }
//      } else {
//        res.setFailure(0, "Mã giới thiệu [" + refrenceCode + "] không tồn tại!");
//      }
//      result = -1;
//    } catch (Throwable t) {
//      mLog.info("EXCEPTION WHEN UPDATE REFRECENCE : " + t.getMessage());
//      res.setFailure(0, "Có lỗi xảy ra, vui lòng thử lại lúc khác!");
//      result = -1;
//    } finally {
//      aResPkg.addMessage(res);
//    }
    return result;
  }
}
