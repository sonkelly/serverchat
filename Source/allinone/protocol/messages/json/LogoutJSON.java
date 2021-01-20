/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.protocol.messages.json;

import vn.game.common.ServerException;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;

/**
 *
 * @author Dinhpv
 */
public class LogoutJSON implements IMessageProtocol {

    public boolean decode(Object paramObject, IRequestMessage paramIRequestMessage) throws ServerException {
        return true;
    }

    public Object encode(IResponseMessage paramIResponseMessage) throws ServerException {
        return true;
    }
}
