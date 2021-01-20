package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.SimpleException;
import allinone.data.UserEntity;
import allinone.databaseDriven.PasswordLevel2DB;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.ChangePasswordLevel2Request;
import allinone.protocol.messages.ChangePasswordLevel2Response;

public class ChangePasswordLevel2Business extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChangePasswordLevel2Business.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug(" Change Password " + aSession.getUID());
        MessageFactory msgFactory = aSession.getMessageFactory();
        ChangePasswordLevel2Response response = (ChangePasswordLevel2Response) msgFactory.getResponseMessage(aReqMsg.getID());
        ChangePasswordLevel2Request request = (ChangePasswordLevel2Request) aReqMsg;
        request.newPassword = request.newPassword.toLowerCase();
        request.reTypePassword = request.reTypePassword.toLowerCase();
        request.oldPassword = request.oldPassword.toLowerCase();
        mLog.debug(" data input " + request.newPassword + " " + request.reTypePassword + " " + request.oldPassword);

        try {
            // validate password
            //if(request.oldPassword.equals("") ||request.newPassword.equals("") || request.reTypePassword.equals(""))
            if (request.newPassword.equals("") || request.reTypePassword.equals("")) {
                throw new SimpleException("Bạn cần nhập đầy đủ thông tin!");
            }

            if (!request.newPassword.equals(request.reTypePassword)) {
                throw new SimpleException("Mật khẩu nhập lại không đúng!");
            }

            if (request.newPassword.length() < 6) {
                throw new SimpleException("Mật khẩu phải lớn hơn 6 ký tự!");
            }

            if (request.newPassword.equals(request.oldPassword)) {
                throw new SimpleException("Mật khẩu mới phải khác mật khẩu cũ!");
            }

            // update password
            PasswordLevel2DB PassLevel2Obj = new PasswordLevel2DB();
           
            String strPassDB = PassLevel2Obj.getPasswordLevel2(aSession.getUID());
            mLog.debug("strPassDB:"+strPassDB);
            String msg = "Thay đổi mật khẩu câp 2 thành công";
            if(strPassDB.equals("chuathietlap")){//chua tao mk cap 2 thi tap moi
                boolean res = PassLevel2Obj.createPassLevel2(aSession.getUID(),request.newPassword);
                if(res){
                    msg = "Chúc mừng bạn tạo mật khẩu cấp 2 thành công.";
                }else{
                    msg = "Có lỗi tạo mật khẩu cấp 2, Vui lòng thử lại.";
                }
                response.setSuccess(msg);
                aResPkg.addMessage(response);
            }else if (!strPassDB.equals(request.oldPassword)) {
                //throw new SimpleException("Lỗi Đã thiết lập Mật khẩu cấp 2 Rồi, Mật khẩu cũ không chính xác!");
               
                    msg = "Lỗi Đã thiết lập Mật khẩu cấp 2 Rồi, Mật khẩu cũ không chính xác!";
               
                response.setSuccess(msg);
                aResPkg.addMessage(response);
            }else{
                //chagne  mk
                int result = PassLevel2Obj.changePassword(aSession.getUID(), request.oldPassword, request.newPassword);
                String msgChange = "Thay đổi mật khẩu câp 2 thành công";
                if (result == 0) {
                    msgChange = "Mật khẩu cũ không chính xác!";
                }
                //end change pass 

                response.setSuccess(msgChange);
                aResPkg.addMessage(response);
            }
            

        } catch (Throwable t) {
            response.setFailure(t.getMessage());
            aResPkg.addMessage(response);
        }

        return 1;
    }
}
