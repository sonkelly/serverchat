/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import allinone.data.UserEntity;
import java.util.List;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponseMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.session.ISession;

/**
 *
 * @author Zeple
 */
public class AddUserManagerBusiness extends AbstractBusiness {

    public String mErrorMsg;
    public List<UserEntity> players;
    public String title;
    public String fromDate;
    public String toDate;

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg,
            IResponsePackage aResPkg) {
        return 1;
    }
}
