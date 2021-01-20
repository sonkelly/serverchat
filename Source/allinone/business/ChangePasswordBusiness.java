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
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.ChangePasswordRequest;
import allinone.protocol.messages.ChangePasswordResponse;

public class ChangePasswordBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ChangePasswordBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug(" Change Password " + aSession.getUID());
        MessageFactory msgFactory = aSession.getMessageFactory();
        ChangePasswordResponse response = (ChangePasswordResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        ChangePasswordRequest request = (ChangePasswordRequest) aReqMsg;
        request.newPassword = request.newPassword.toLowerCase();
        request.reTypePassword = request.reTypePassword.toLowerCase();
        request.oldPassword = request.oldPassword.toLowerCase();
        mLog.debug(" data input " + request.newPassword + " " + request.reTypePassword + " " + request.oldPassword);

        try {
            // validate password
            //if(request.oldPassword.equals("") ||request.newPassword.equals("") || request.reTypePassword.equals(""))
            if (request.oldPassword.equals("") || request.newPassword.equals("") || request.reTypePassword.equals("")) {
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
            UserDB userDb = new UserDB();
            aSession.getUserEntity().mPassword = aSession.getUserEntity().mPassword.toLowerCase();
            if (!aSession.getUserEntity().mPassword.equals(request.oldPassword)) {
                throw new SimpleException("Mật khẩu không đúng!");
            }
            int result = userDb.changePassword(aSession.getUID(), request.oldPassword, request.newPassword);

            if (result == 0) {
                throw new SimpleException("Mật khẩu cũ không chính xác!");
            }

            response.setSuccess("Thay đổi mật khẩu thành công. Vui lòng đăng nhập lại");
            aResPkg.addMessage(response);

        } catch (Throwable t) {
            response.setFailure(t.getMessage());
            aResPkg.addMessage(response);
        }

        return 1;
    }
}
