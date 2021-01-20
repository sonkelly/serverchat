/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

/**
 *
 * @author mcb
 */
public class AlertUserEntity {
//    private long advertisingId;

    private String content;
    //private Date userId;
    private long userId;
    private int type = 1;
    public int disconnect = 0;

    public AlertUserEntity(String content, long userId, int _type) {
        this.userId = userId;
        this.content = content;
        this.type = _type;
    }

    public AlertUserEntity(String content, long userId, int _type, int _disconnect) {
        this.userId = userId;
        this.content = content;
        this.type = _type;
        this.disconnect = _disconnect;
        //this.setContent(content);
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

}
