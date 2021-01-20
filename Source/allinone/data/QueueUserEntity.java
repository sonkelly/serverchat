/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.session.ISession;

/**
 *
 * @author mcb
 */
public class QueueUserEntity {
    
    private AbstractRequestMessage request;
   
    
    private ISession session;
    private IResponseMessage response;

    public QueueUserEntity(AbstractRequestMessage request, ISession session, 
            IResponseMessage response)
    {
        this.request = request;
        this.session = session;
        this.response = response;
        
//        this.albumIndex = albumIndex;
                
    }
    
    public QueueUserEntity()
    {
        
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

    /**
     * @return the response
     */
    public IResponseMessage getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(IResponseMessage response) {
        this.response = response;
    }

    /**
     * @return the request
     */
    public AbstractRequestMessage getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(AbstractRequestMessage request) {
        this.request = request;
    }

    


   

    
    
}
