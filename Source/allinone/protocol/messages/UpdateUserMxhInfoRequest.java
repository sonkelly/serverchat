/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package allinone.protocol.messages;

import java.util.Date;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author Dinhpv
 */
public class UpdateUserMxhInfoRequest extends AbstractRequestMessage
{
    public boolean sex;
    public int cityId;
    public String address;
    public int jobId;
    public Date birthday;
    public String hobby;
    public String nickSkype;
    public String nickYahoo;
    public String phoneNumber;
    public long avatarFileId;
    public String status;
    public int characterId;
    
    public IRequestMessage createNew()
    {
        return new UpdateUserMxhInfoRequest();
    }
}
