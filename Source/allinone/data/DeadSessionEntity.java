/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

/**
 *
 * @author mcb
 */
public class DeadSessionEntity {
    private long userId;
    private int phongId;
    
    public DeadSessionEntity(long userId, int phongId)
    {
        this.userId = userId;
        this.phongId = phongId;
                
    }

    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @return the phongId
     */
    public int getPhongId() {
        return phongId;
    }
    
}
