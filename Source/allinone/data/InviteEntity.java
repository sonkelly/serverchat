/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

/**
 *
 * @author mcbvrus
 */
public class InviteEntity {
    private int gameId;
    private int inviteId;
    private long betMoney;
    
    public InviteEntity(int gameId, int inviteId, long betMoney)
    {
        this.gameId = gameId;
        this.inviteId = inviteId;
        this.betMoney = betMoney;
    }

    /**
     * @return the gameId
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * @return the inviteId
     */
    public int getInviteId() {
        return inviteId;
    }

    /**
     * @return the betMoney
     */
    public long getBetMoney() {
        return betMoney;
    }
    
    
}
