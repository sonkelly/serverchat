/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.room;

import vn.game.session.ISession;

/**
 *
 * @author vipgame10
 */
public class PairingPlayer {

    public int countTimeOut = 10;
    public ISession session;
    public long uid;
    public boolean cancelPairing = false;

    public PairingPlayer(ISession session, long uid) {
        this.session = session;
        this.uid = uid;
    }

}
