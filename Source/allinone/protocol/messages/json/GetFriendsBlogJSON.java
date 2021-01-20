/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.BlogEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetFriendsBlogRequest;
import allinone.protocol.messages.GetFriendsBlogResponse;

/**
 *
 * @author mcb
 */
public class GetFriendsBlogJSON implements IMessageProtocol {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetFriendsBlogJSON.class);

    public boolean decode(Object aEncodedObj, IRequestMessage aDecodingObj) throws ServerException {
        try {
            JSONObject jsonData = (JSONObject) aEncodedObj;
            GetFriendsBlogRequest blogRequest = (GetFriendsBlogRequest)aDecodingObj;

            if(jsonData.has("page"))//for game xoc dia
            {
                    blogRequest.page = jsonData.getInt("page");
            }
            else
            {
                blogRequest.page = 1;
            }
            return true;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return true;
    }

    public Object encode(IResponseMessage aResponseMessage) throws ServerException {
        try {
            JSONObject encodingObj = new JSONObject();
            
            encodingObj.put("mid", aResponseMessage.getID());
            GetFriendsBlogResponse blogRes = (GetFriendsBlogResponse) aResponseMessage; 
            encodingObj.put("code", blogRes.mCode);
            if(blogRes.mCode == ResponseCode.FAILURE)
            {
                encodingObj.put("error_msg", blogRes.errMsg);
            }
            else
            {
            
                JSONArray blogsJarr = new JSONArray();
                int size = blogRes.blogs.size();
                CacheUserInfo cacheUser = new CacheUserInfo();
                for(int i = 0; i<size; i++ )
                {
                    BlogEntity entity = blogRes.blogs.get(i);

                    JSONObject jBlog = new JSONObject();
                    jBlog.put("friendId", entity.getUserId());
                    jBlog.put("friendName", entity.getUserName());
                    jBlog.put("postId", entity.getBlogId());
                    jBlog.put("title", entity.getTitle());
                    jBlog.put("view", entity.getViewCount());
                    jBlog.put("like", entity.getLikeCount());
                    jBlog.put("comment", entity.getCommentCount());
                    jBlog.put("upDate", entity.getUpdatedDate().getTime());

                    blogsJarr.put(jBlog);
                }
                encodingObj.put("posts", blogsJarr);
                if(blogRes.numBlog>0)
                    encodingObj.put("numPost", blogRes.numBlog);
            }
            
            
            
            
            return encodingObj;
        } catch (JSONException ex) {
            mLog.error(ex.getMessage(), ex);
        }
        
        return null;
    }
}