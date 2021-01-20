/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

import vn.game.session.ISession;
import allinone.protocol.messages.BoiRequest;
import allinone.protocol.messages.BoiResponse;

/**
 *
 * @author mcbvrus
 */
public class QueueBoiEntity {
    private BoiResponse boiRes;
    private BoiRequest boiRequest;
    private ISession session;
    
    public QueueBoiEntity(BoiResponse boiRes, BoiRequest boRequest, ISession session)
    {
        this.boiRes = boiRes;
        this.boiRequest = boRequest;
        this.session = session;
    }

    /**
     * @return the boiRes
     */
    public BoiResponse getBoiRes() {
        return boiRes;
    }

    /**
     * @param boiRes the boiRes to set
     */
    public void setBoiRes(BoiResponse boiRes) {
        this.boiRes = boiRes;
    }

    /**
     * @return the boiRequest
     */
    public BoiRequest getBoiRequest() {
        return boiRequest;
    }

    /**
     * @param boiRequest the boiRequest to set
     */
    public void setBoiRequest(BoiRequest boiRequest) {
        this.boiRequest = boiRequest;
    }

    /**
     * @return the session
     */
    public ISession getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(ISession session) {
        this.session = session;
    }
    
}
