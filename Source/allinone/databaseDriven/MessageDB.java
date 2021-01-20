/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.databaseDriven;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import allinone.data.Message;
import allinone.data.PrivateChatEntity;

/**
 *
 * @author mcb
 */
public class MessageDB {
    private static final String  SOURCE_ID_PARAM = "sourceId";
    private static final String  USER_ID_PARAM = "userId";
    
    
    private static final String  CONTENT_PARAM = "content";
    private static final String  TITLE_PARAM = "title";
    private static final String  DEST_ID_PARAM = "destId";
    private static final String  MESSAGE_ID_PARAM = "messageId";
    private static final String  TYPE_PARAM = "type";
    
    
    
//    private static final String  SYSTEM_OBJECT_ID_PARAM = "systemObjectId";
//    
//    private static final String  COMMENT_ID_PARAM = "commentId";
    private static Connection conn;
    public MessageDB()
    {
        
    }
    
    
    public MessageDB(Connection conn)
    {
        this.conn = conn;
    }
    
    
    public void insertMessage(long userId, String content, long destUid, String title) throws SQLException
    {
        String query = "{call uspInsertMessage(?,?, ?, ?)} ";
        Connection conn = DBPoolConnection.getConnection();

        try {

             CallableStatement cs = conn.prepareCall(query);
             
             cs.setLong(SOURCE_ID_PARAM, userId);
             cs.setString(CONTENT_PARAM, content);
             cs.setLong(DEST_ID_PARAM, destUid);
             cs.setString(TITLE_PARAM, title);

             cs.execute();
        }
        finally
        {
            conn.close();
        }
        
    }
    
    
    public  List<Message> receiveMessage(long id) throws Exception {
		List<Message> res = new ArrayList<Message>();
		String query = "{ call uspGetMxhMessage(?) }";
                String UID_PARAM = "userId";
                
                Connection conn = DBPoolConnection.getConnection();
                
                try
                {
                    CallableStatement cs = conn.prepareCall(query);
                    cs.setLong(UID_PARAM, id);
                    ResultSet rs = cs.executeQuery();
                    if (rs != null) {
                            while (rs.next()) {
                                    long messID = rs.getLong("messageID");
                                    long dID = rs.getLong("destID");
                                    long sID = rs.getLong("sourceID");
                                    String detail = rs.getString("content");
                                    String title = rs.getString("title");
                                    String sourceName = rs.getString("name");
                                    //String destName = rs.getString("DestName");
                                    boolean isRead = rs.getBoolean("isRead");
                                    long avFileId = rs.getLong("avatarFileId");
                                    
                                    
                                    Date date = rs.getTimestamp("datecreated");
                                    Message m = new Message(messID, sID, dID, detail, title, date,
                                                    sourceName, "");
                                    
                                    m.isRead = isRead;
                                    m.avatarFileId = avFileId;
                                    res.add(m);

                            }
                            
                            rs.close();
                    }

                    
                    
                    cs.clearParameters();

                    cs.close();
                }
                finally
                {
                    conn.close();
                }

		return res;

	}
    
        public  void deleteMessage(int type, long messageId, long userId) throws SQLException  {
		
		String query = "{ call uspDeleteMxhMessage(?, ?, ?) }";
                
                
                Connection conn = DBPoolConnection.getConnection();
                
                try
                {
                    CallableStatement cs = conn.prepareCall(query);
                    cs.setInt(TYPE_PARAM, type);
                    cs.setLong(MESSAGE_ID_PARAM, messageId);
                    cs.setLong(USER_ID_PARAM, userId);
                    cs.execute();
                    cs.close();
                }
                finally
                {
                    conn.close();
                }


	}
    
   
    
    public  String readMessage(long messageId) throws Exception {
		
            String query = "{ call uspReadMxhMessage(?) }";
            Connection conn = DBPoolConnection.getConnection();
            String ret = "";
            try
            {
                CallableStatement cs = conn.prepareCall(query);
                cs.setLong(MESSAGE_ID_PARAM, messageId);
                ResultSet rs = cs.executeQuery();
                if (rs != null && rs.next()) {
                    ret = rs.getString("content");
                    rs.close();
                }

                cs.close();
            }
            finally
            {
                conn.close();
            }

            return ret;

    }
    
    
    public List<PrivateChatEntity> getPrvChat(long id) throws Exception {
        List<PrivateChatEntity> res = new ArrayList<PrivateChatEntity>();
        String query = "{ call uspGetPrivateChat(?) }";


        Connection con = null;
        
        if(this.conn == null)
            con = DBPoolConnection.getConnection();
        else
            con = this.conn;

        try {
            CallableStatement cs = con.prepareCall(query);
            cs.setLong(USER_ID_PARAM, id);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {

                    long sID = rs.getLong("sourceID");
                    String detail = rs.getString("content");
                    String sourceName = rs.getString("name");
                    Date date = rs.getDate("datecreated");
                    
                    PrivateChatEntity chatEntity = new PrivateChatEntity(sID, sourceName, date, detail);
                    res.add(chatEntity);

                }

                rs.close();
            }



            cs.clearParameters();

            cs.close();
        } finally {
            if(this.conn == null)
                con.close();
        }

        return res;

    }
    
    
    public  int getNumMessage(long userId) throws Exception {
	int ret = 0;	
        String query = "{ call uspGetNumMess(?) }";

        Connection con = null;
        
        if(this.conn == null)
            con = DBPoolConnection.getConnection();
        else
            con = this.conn;

        try
        {
            CallableStatement cs = con.prepareCall(query);
            cs.setLong(USER_ID_PARAM, userId);
            ResultSet rs = cs.executeQuery();
            if (rs != null && rs.next()) {
                ret = rs.getInt("count");
                rs.close();
            }

            cs.clearParameters();

            cs.close();
        }
        finally
        {
             if(this.conn == null)
                con.close();
        }

        return ret;

    }
    
    
    
    
//    public List<CommentEntity> getComment(int systemObjectId, long systemObjectRecordId) throws SQLException
//    {
//        List<CommentEntity> lstComments = new ArrayList<CommentEntity>();
//        Connection con = DBPoolConnection.getConnection();
//        
//        
//        String query = "{ call uspGetComment(?,?) }";
//        try
//        {
//            CallableStatement cs = con.prepareCall(query);
//            
//            cs.setInt(SYSTEM_OBJECT_ID_PARAM, systemObjectId);
//            cs.setLong(DEST_ID_PARAM, systemObjectRecordId);
//            
//            ResultSet rs = cs.executeQuery();
//            if(rs != null)
//            {
//                while(rs.next())
//                {
//                    
//                    String content = rs.getString("content");
//                    
//                    long commentId = rs.getLong("commentId");
//                    long userId = rs.getLong("userId");
//                    String userName = rs.getString("name");
//                    long avatarFileId = rs.getLong("avatarFileId");
//                    
//                            
//                    
//                    Date updatedDate = rs.getTimestamp("updatedDate");
//                    Date createdDate = rs.getTimestamp("createdDate");
//                    CommentEntity entity  = new CommentEntity(commentId, content, 
//                            userId, userName, avatarFileId, createdDate, updatedDate, systemObjectId, systemObjectRecordId);
//                    lstComments.add(entity);
//                    
//                }
//                
//                
//                rs.close();
//                cs.close();
//            }
//        }
//        finally
//        {
//            con.close();
//        }
//        
//        return lstComments;
//    }
//    
//    
//    public void addLike(long userId, long objectId, int systemObjectId) throws SQLException
//    {
//        String query = "{call uspInsertLike(?, ?, ?)} ";
//        Connection conn = DBPoolConnection.getConnection();
//
//        try {
//
//             CallableStatement cs = conn.prepareCall(query);
//             
//             cs.setLong(SOURCE_ID_PARAM, userId);
//             
//             cs.setLong(DEST_ID_PARAM, objectId);
//             cs.setInt(SYSTEM_OBJECT_ID_PARAM, systemObjectId);
//             cs.execute();
//        }
//        finally
//        {
//            conn.close();
//        }
//        
//    }
}
