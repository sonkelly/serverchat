package allinone.business;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.SimpleException;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.ViewNameRequest;
import allinone.protocol.messages.ViewNameResponse;
import java.util.Calendar;

public class ViewNameBusiness extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(ViewNameBusiness.class);

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug(" Change viewname " + aSession.getUID());
        MessageFactory msgFactory = aSession.getMessageFactory();
        ViewNameResponse response = (ViewNameResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        ViewNameRequest request = (ViewNameRequest) aReqMsg;
        aSession.setLastAccessTime(Calendar.getInstance().getTime());
        mLog.debug(" data input " + request.viewname);

        try {
            if (request.viewname.length() < 5 || request.viewname.length() >= 20) {
                throw new SimpleException("Tài khoản phải có độ dài từ 5 đến 20 ký tự!");
            }
            String pattern = "^[a-zA-Z_0-9.@_]{5,100}$";
            if (!request.viewname.matches(pattern)) {
                throw new SimpleException("Tên hiển thị không được chứa ký tự đặc biệt!");
            }
            UserDB userDb = new UserDB();
            boolean checkUserName = userDb.userViewNameIsExist(request.viewname);
            if (checkUserName) {

                throw new SimpleException("Tên hiển thị đã tồn tại, vui lòng chọn tên hiển thị khác!");

            }
            if(aSession.getUserEntity().mUsername.equals(request.viewname)){
                 throw new SimpleException("Tên đăng nhập và tên hiển thị không được giống nhau.");
            }
            
            int result = userDb.changeViewName(aSession.getUID(), request.viewname);

            if (result == 0) {
                throw new SimpleException("Không cập nhật được tên hiển thị!");
            }

            response.setSuccess("Cập nhật tên hiển thị thành công!.",request.viewname);
            aResPkg.addMessage(response);

        } catch (Throwable t) {
            response.setFailure(t.getMessage());
            aResPkg.addMessage(response);
        }

        return 1;
    }
}
