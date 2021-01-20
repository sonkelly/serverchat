package allinone.business;

import java.util.regex.Pattern;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.UpdatePhoneRequest;
import allinone.protocol.messages.UpdatePhoneResponse;

public class UpdatePhoneBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(UpdatePhoneBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) throws ServerException {
		int rtn = PROCESS_FAILURE;		
		mLog.debug("[UPDATE USER] : PHONENUMBER ");
		MessageFactory msgFactory = aSession.getMessageFactory();
		UpdatePhoneResponse res = (UpdatePhoneResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        res.session = aSession;
        
		try {			
			long userId = aSession.getUID();
			UpdatePhoneRequest rq = (UpdatePhoneRequest) aReqMsg;
			UserDB userDb = new UserDB();

			 // validate phone number bat dau bang so 0, 10 - 11 so
            String phoneExp = "^0[0-9]{9,10}$";
			if(rq.phone == null || "".equals(rq.phone)) {
				res.setFailure(ResponseCode.FAILURE, "Vui lòng nhập số điện thoại!");
			} else if(!rq.phone.matches(phoneExp)) {
				res.setFailure(ResponseCode.FAILURE, "Số điện thoại không chính xác!");
			} else {				
				
				int result = 0;				
				String errorMessage = "";
				
				mLog.debug(" username " + rq.userName);
				
				if(!"".equals(rq.userName)) {
					
					 Pattern p = Pattern.compile("^[1-9]\\d{0,}+$");
			         String pattern = "^[a-zA-Z_0-9.@_]{5,100}$";

			        // validate user name
					if(rq.userName.length() < 5) {
						result = 4;
						errorMessage = "Tài khoản phải nhiều hơn 4 ký tự, không có khoảng trống, không được toàn chữ số!";
					} else if (checkInvalidName(rq.userName)) {
						result = 4;
						errorMessage = "Tên tài khoản không hợp lệ!";						
					} else if (p.matcher(rq.userName).matches()) {
						result = 4;
						errorMessage = "Tài khoản không được toàn chữ số (trừ bắt đầu bằng 0)!";												
					} else if (!rq.userName.matches(pattern)) {
						result = 4;
						errorMessage = "Tài khoản không được chứa ký tự đặc biệt!";		
					} else {
						mLog.debug(" Update phone and username ");
						result = userDb.updatePhoneNumberAndUserName(userId, rq.phone, rq.userName);							
					}
					
				} else {
					result = userDb.updatePhoneNumber(userId, rq.phone);															
				}
				
				if(result == 1) {
					res.setSuccess(ResponseCode.SUCCESS, "Cập nhật thành công!");							
					
					// reset user cached
		            UserEntity entity = userDb.getUserInfo(userId,aSession.getRealMoney());
		            if (entity != null) 
		            {
		                CacheUserInfo cacheUser = new CacheUserInfo();
		                cacheUser.updateCacheUserInfo(entity);
		            }    					
		            
				} else if (result == 0) {
					res.setFailure(ResponseCode.FAILURE, "Số điện thoại đã được sử dụng quá 3 lần!");					
				} else if (result == 2) {
					res.setFailure(ResponseCode.FAILURE, "Tài khoản đã tồn tại, vui lòng đăng ký tài khoản khác!");										
				} else if (result == 4) {
					res.setFailure(ResponseCode.FAILURE, errorMessage);										
				}
			}
			
			rtn = PROCESS_OK;
			
		} catch (Throwable t) {
			res.setFailure(ResponseCode.FAILURE,"Dữ liệu bạn nhập không chính xác!");
			rtn = PROCESS_OK;
		
		} finally {
			aResPkg.addMessage(res);
		}
		
		return rtn;
	}
	
	  private boolean checkInvalidName(String name) {
	    	String[] samples = {"admjn","admin","quanly","tuquy","hethong","fuck","guest","facebook"};
	        int len = samples.length;
	        for(int i = 0; i< len; i++) {
	            String sample = samples[i].toLowerCase();
	            if(name.toLowerCase().contains(sample)) return true;
	        }
	        return false;
	    }
	
	
}
