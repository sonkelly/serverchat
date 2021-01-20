/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;



import java.util.List;

import tools.CacheFriendsInfo;
import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.MessageEntity;
import allinone.data.ResponseCode;
import allinone.data.SocialFriendEntity;
import allinone.protocol.messages.EnterSocialAreaResponse;

/**
 *
 * @author mcb
 */
public class EnterSocialAreaBusiness extends AbstractBusiness {

    private static final org.slf4j.Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(EnterSocialAreaBusiness.class);
    
    
    
    
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
//        mLog.debug("get albums");
        MessageFactory msgFactory = aSession.getMessageFactory();
        EnterSocialAreaResponse resSocial = (EnterSocialAreaResponse) msgFactory.getResponseMessage(aReqMsg.getID());
//        Connection con = null;
        try {
//            con = DBPoolConnection.getConnection();
//            MessageDB db = new MessageDB(con);
//            List<PrivateChatEntity> lstChats = db.getPrvChat(aSession.getUID());
            
//            int numMessage = db.getNumMessage(aSession.getUID());
            
//            FriendDB fDb = new FriendDB(con);
            CacheUserInfo cacheUser = new CacheUserInfo();
            MessageEntity msgEntity = cacheUser.getMessage(aSession.getUID());
            int numMessage = msgEntity.getUnReadMessage();
            aSession.setCurrPosition(null);
            
//            int numRequestFriends = fDb.getNumRequestFriends(aSession.getUID());
            CacheFriendsInfo cacheFriend = new CacheFriendsInfo();
            //List<SocialFriendEntity> lstFriends = cacheFriend.getSocialFriendRequests(aSession.getUID());
            
            StringBuilder sb = new StringBuilder();
//            List<UserEntity> lstUsers = new ArrayList<UserEntity>();
//            
//            int chatSize = lstChats.size();
//            for(int i = 0; i< chatSize; i++)
//            {
//                PrivateChatEntity entity = lstChats.get(i);
//                
//                int uidSize = lstUsers.size();
//                
//                int index = uidSize;
//                for(int j = 0; j< uidSize; j++)
//                {
//                    if(lstUsers.get(j).mUid== entity.getSrcId())
//                    {
//                        index = j;
//                        break;
//                    }
//                }
//
//                if(index == uidSize || uidSize == 0) //it 'snew uid so we add it
//                {
//                    UserEntity usrEntity = new UserEntity();
//                    usrEntity.mUid = entity.getSrcId();
//                    usrEntity.mUsername = entity.getSrcName();
//                    
//                    lstUsers.add(usrEntity);
//                }
//
//                sb.append(index).append(AIOConstants.SEPERATOR_BYTE_1);
//                sb.append(entity.getContent()).append(AIOConstants.SEPERATOR_BYTE_2);
//                
//            }
//            
//            if(chatSize>0)
//            {
//                sb.deleteCharAt(sb.length() -1);
//                
//            }
//            
//            sb.append(AIOConstants.SEPERATOR_BYTE_3);
//            
//            int userSize = lstUsers.size();
//            for(int i = 0; i< userSize; i++)
//            {
//                UserEntity entity = lstUsers.get(i);
//                sb.append(Long.toString(entity.mUid)).append(AIOConstants.SEPERATOR_BYTE_1);
//                sb.append(entity.mUsername).append(AIOConstants.SEPERATOR_BYTE_2);
//            }
//            
//            if(userSize>0)
//            {
//                sb.deleteCharAt(sb.length() -1);
//            }
//            
//            sb.append(AIOConstants.SEPERATOR_BYTE_3);
//            
            sb.append(Integer.toString(numMessage));
            sb.append(AIOConstants.SEPERATOR_BYTE_1);
            //sb.append(Integer.toString(lstFriends.size()));
            
            resSocial.value = sb.toString();
            resSocial.mCode = ResponseCode.SUCCESS;       
            
            aSession.write(resSocial);
            
//            aSession.setmGroup(null);
            
//            if (aSession.getGroup() != null) {
//                VivuDisappearResponse disA =
//                        (VivuDisappearResponse) msgFactory.getResponseMessage(MessagesID.ViVuDisappear);
//                disA.setSuccess(aSession.getUID());
//                aSession.getGroup().broadcast(disA, false, aSession.getUID());
//                aSession.getGroup().left(aSession);
//            }
            
            
            
//            //send online play in queue
//            GetOnlineMemberResponse onlineRes = (EnterChatRoomResponse) msgFactory.getResponseMessage(MessagesID.GET_ONLINE_MEMBER);
        } 
        catch (ServerException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }
        finally
        {
//            if(con != null)
//            {
//                try {
//                    con.close();
//                } catch (SQLException ex) {
//                    mLog.error(ex.getMessage(), ex);
//                }
//            }
        }
                   
            

            
            
//        }
//        catch(BusinessException ex)
//        {
//            mLog.warn(ex.getMessage());
//            resalbum.setFailure(ex.getMessage());
//            
//        }
             
        return 1;
    }
}
