package allinone.protocol.messages.json;

import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.AIOConstants;
import allinone.data.GioiThieuEntity;
import allinone.data.ResponseCode;
import allinone.databaseDriven.FriendDB;
import allinone.protocol.messages.RegisterRequest21;
import allinone.protocol.messages.RegisterResponse21;

public class RegisterJSON21 implements IMessageProtocol {

	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(
			RegisterJSON21.class);

	public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj)
			throws ServerException {
            try {
                JSONObject jsonData = (JSONObject) aEncodedObj;
                RegisterRequest21 register = (RegisterRequest21) aDecodingObj;
                if(jsonData.has("v"))
                {
                    String[] arrValues = jsonData.getString("v").split(AIOConstants.SEPERATOR_BYTE_1);
                    
                    register.mUsername = arrValues[0];
                    register.mPassword = arrValues[1];
                    register.partnerId = Integer.parseInt(arrValues[2]);
                    register.protocol = Integer.parseInt(arrValues[3]);
                    if(arrValues.length>4)
                    {
                        
                        register.isMale = arrValues[4].equals("1");
                       
                        if(arrValues.length>5)
                        {
                            register.isMxh = arrValues[6].equals("1");
                             
                            register.linkGuiTang = arrValues[5];
                            //parse info
                            
                            
                            
                            try
                            {
                                if (arrValues.length>7)
                                {
                                    String refCode = arrValues[7];
                                    int deviceType = Integer.parseInt(arrValues[8]);
                                    register.refCode = refCode;
                                    register.deviceType = deviceType;
                                     if(arrValues.length>9)
                                    {
                                        register.clientSessionId = arrValues[9];
                                    }
                                    
                                }
                                long refGioiThieuId = Long.parseLong(register.linkGuiTang);
                                
                                FriendDB db = new FriendDB();
                              //  GioiThieuEntity gioiThieuEntity =  db.getRefGioithieu(refGioiThieuId);
                                
//                                byte[] arrGuiTang = Base64.decode(register.linkGuiTang);
//                                byte[] byteGuiTang = Utils.getCipherDecrypt().doFinal(arrGuiTang);
//                                String str = new String(byteGuiTang);
//                                String[] args = str.split("&");
//                                register.gioiThieuUid = gioiThieuEntity.getUserId();
//                                register.phone = gioiThieuEntity.getPhoneOrMail();
//                                register.partnerId = gioiThieuEntity.getPartnerId();
//                                if(!(register.partnerId == AIOConstants.XOMTUI_PARTNER_ID || register.partnerId == AIOConstants.VIPGAME_PARTNER_ID))
//                                {
//                                    register.partnerId = AIOConstants.MEDIA_MXH_ID;
//                                }
                                
                                
                            }
                            catch(Exception ex)
                            {
                                
                                mLog.debug(ex.getMessage());
                            }
                        }
                        
                    }
                    
                    
                    return true;
                }
                //don't support old version
                
//                register.mUsername = jsonData.getString("username");
//                register.mPassword = jsonData.getString("password");
//                if(jsonData.has("key")){
//                        register.key = jsonData.getString("key");
//                }else {
//                        register.key = "";
//                }
//
//                if(jsonData.has("partnerid"))
//                {
//                    register.partnerId = jsonData.getInt("partnerid");
//                }
//                else
//                {
//                    register.partnerId = 0; //default is vipcom
//                }
//                
//                if(jsonData.has("protocol"))
//                {
//                    register.protocol = jsonData.getInt("protocol");
//                }
//
//                try {
//                        register.isMale = jsonData.getBoolean("male");
//                        register.mAge = jsonData.getInt("age");
//                        register.mail = jsonData.getString("mail");
//                        register.phone = jsonData.getString("phone");
//                } catch (Exception e) {
//                        register.isMale = true;
//                        register.mAge = 20;
//                        register.mail = "";
//                        register.phone = "";
//                }
//                if (jsonData.has("cp"))
//                        register.cp = jsonData.getString("cp");

                return false;
            } catch (Throwable t) {
                    mLog.error("[DECODER] " + aDecodingObj.getID(), t);
                    return false;
            }
	}

	public Object encode(IResponseMessage aResponseMessage)
			throws ServerException {
		try {
			JSONObject encodingObj = new JSONObject();
                        RegisterResponse21 register = (RegisterResponse21) aResponseMessage;
                        if(register.session != null && register.session.getByteProtocol()> AIOConstants.PROTOCOL_ADVERTISING)
                        {
                            StringBuilder sb = new StringBuilder();
                            sb.append(Integer.toString(aResponseMessage.getID())).append(AIOConstants.SEPERATOR_BYTE_1);
                            sb.append(Integer.toString(register.mCode)).append(AIOConstants.SEPERATOR_NEW_MID);
                            if (register.mCode == ResponseCode.FAILURE) {
                                     sb.append(register.mErrorMsg);
                            }
                            sb.append(register.values);
                            
                            encodingObj.put("v", sb.toString());
                            return encodingObj;
                        }
                        
			// put response data into json object
//			System.out.println("mid " + aResponseMessage.getID());
			encodingObj.put("mid", aResponseMessage.getID());
			
			encodingObj.put("code", register.mCode);
			if (register.mCode == ResponseCode.FAILURE) {
				encodingObj.put("error_msg", register.mErrorMsg);
			} else if (register.mCode == ResponseCode.SUCCESS) {
				encodingObj.put("uid", register.mUid);
				encodingObj.put("money", register.money);
				encodingObj.put("avatar", register.avatarID);
				encodingObj.put("level", register.level);
			}
			// response encoded obj
			return encodingObj;
		} catch (Throwable t) {
			mLog.error("[ENCODER] " + aResponseMessage.getID(), t);
			return null;
		}
	}
}
