/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.business;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import tools.CacheWallInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.AuditFollowEntity;
import allinone.data.MessagesID;
import allinone.data.QueueImageEntity;
import allinone.data.ResponseCode;
import allinone.data.UserEntity;
import allinone.protocol.messages.GetFollowRequest;
import allinone.protocol.messages.GetFollowResponse;
import allinone.protocol.messages.GetSocialAvatarResponse;
import allinone.queue.data.ImageQueue;

/**
 * 
 * @author Dinhpv
 */
public class GetFollowBusiness extends AbstractBusiness {

	private static final Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetFollowBusiness.class);
        //private static final int PAGE_SIZE = 10;
        
	public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
			IResponsePackage aResPkg) throws ServerException {

		int rtn = PROCESS_FAILURE;
		mLog.debug("[Get Follow] : Catch");
//		WallDB db = new WallDB();
		MessageFactory msgFactory = aSession.getMessageFactory();

		GetFollowResponse resFinds = (GetFollowResponse) msgFactory
				.getResponseMessage(aReqMsg.getID());
		try {
			GetFollowRequest rqRegister = (GetFollowRequest) aReqMsg;
                        CacheWallInfo cache = new CacheWallInfo();
                        List<AuditFollowEntity> lstUsers =  cache.getFollow(aSession.getUID());

                        StringBuilder sb = new StringBuilder();
                        int lstUserSize = lstUsers.size();
                        if(lstUsers != null && lstUserSize>0)
                        {
//                            int maxPageSize = 0;
                            int fromIndex = rqRegister.pageIndex * AIOConstants.PAGE_10_DEFAULT_SIZE;
                            int toIndex = (rqRegister.pageIndex+1) * AIOConstants.PAGE_10_DEFAULT_SIZE;
                            if(toIndex>lstUserSize)
                            {
                                toIndex = lstUserSize;
                            }
                            List<UserEntity> diffUsers = new ArrayList<UserEntity>();
                            
    //                        int userSize = lstUsers.size();
                            for(int i = fromIndex; i< toIndex; i++)
                            {
                                AuditFollowEntity entity = lstUsers.get(i);
                                int userSize = diffUsers.size();
                                int index = userSize;
                                for(int j = 0; j< userSize; j++)
                                {
                                    if(diffUsers.get(j).mUid== entity.getUser().mUid)
                                    {
                                        index = j; 
                                        break;
                                    }
                                }

                                if(index == userSize || userSize == 0)
                                {
                                    diffUsers.add(entity.getUser());
                                }

                                sb.append(Integer.toString(index)).append(AIOConstants.SEPERATOR_BYTE_1);
                                
                                sb.append(Long.toString(entity.getFollowDate().getTime())).append(AIOConstants.SEPERATOR_BYTE_2);
                                
                            }
                            
                            int userSize = diffUsers.size();

                            if(sb.length()>0)
                            {
                                sb.deleteCharAt(sb.length() -1);
                                
                                sb.append(AIOConstants.SEPERATOR_BYTE_3);
                                
                                
                                for(int i = 0; i< userSize; i++)
                                {
                                    UserEntity entity = diffUsers.get(i);
                                    sb.append(Long.toString(entity.mUid)).append(AIOConstants.SEPERATOR_BYTE_1);
                                    sb.append(entity.mUsername).append(AIOConstants.SEPERATOR_BYTE_1);
                                    sb.append(entity.mIsMale?"1":"0").append(AIOConstants.SEPERATOR_BYTE_1);
                                    sb.append(Long.toString(entity.avFileId)).append(AIOConstants.SEPERATOR_BYTE_2);
                                }
                                
                                sb.deleteCharAt(sb.length() -1);
                                if(rqRegister.pageIndex == 0)
                                {
                                    
                                    int countPage = (int)Math.ceil((float)lstUserSize/(float)AIOConstants.PAGE_10_DEFAULT_SIZE);
                                    sb.append(AIOConstants.SEPERATOR_BYTE_3);
                                    sb.append(rqRegister.pageIndex).append(AIOConstants.SEPERATOR_BYTE_1);
                                    sb.append(countPage);
                                }
                                
                            }
                            
                            resFinds.setSuccess(sb.toString());
                            aSession.write(resFinds);
                            rtn = PROCESS_OK;
                            UUID imgRequest = UUID.randomUUID();
                            aSession.setImageRequest(imgRequest);
                            long currentTime = System.currentTimeMillis();
                            
                            for(int i = 0; i< userSize; i++)
                            {
                                UserEntity entity = diffUsers.get(i);
                                
                                if(entity.avFileId>0)
                                {
                                    //put into queue
                                    GetSocialAvatarResponse queueAvar = (GetSocialAvatarResponse)msgFactory.getResponseMessage(MessagesID.GET_SOCIAL_AVATAR);
                                    queueAvar.mCode = ResponseCode.SUCCESS;
                    //                QueueImageEntity imgEntity = new QueueImageEntity(entity.getFileId(), false, aSession, queueAlbum, rqAlb.albumId);
                                    QueueImageEntity imgEntity = new QueueImageEntity(entity.avFileId, true,
                                            aSession, queueAvar);

                                    imgEntity.setUserId(entity.mUid);
                                    imgEntity.setRequestImgId(imgRequest);
                                    imgEntity.setRequestTime(currentTime);
                                    ImageQueue imgQueue = new ImageQueue();
                                    imgQueue.insertImage(imgEntity);

                                }
                            }
                            
                        }
                        else
                        {
                            resFinds.setSuccess(sb.toString());
                            aSession.write(resFinds);
                            rtn = PROCESS_OK;
                        }
                      
//			rtn = PROCESS_OK;
		} 
                
                            
                catch (Throwable t) {
			resFinds.setFailure("Có lỗi xảy ra!");
			rtn = PROCESS_OK;
                        aResPkg.addMessage(resFinds);
			mLog.error("Process message " + aReqMsg.getID() + " error.", t);
		}
		return rtn;
	}
}
