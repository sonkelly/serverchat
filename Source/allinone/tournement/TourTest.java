/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.tournement;

import allinone.business.BusinessException;
import allinone.data.UserEntity;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.CacheUserInfo;

/**
 *
 * @author mac
 */
public class TourTest {

    public static void UserRegisterTour(TourManager mng, long uid, String isRealMoney) {
        //TourManager mng = aSession.getTourMgr();
        //TournementEntity tour = mng.getTour(tID);
        CacheUserInfo cacheUser = new CacheUserInfo();
        UserEntity user = cacheUser.getUserInfo(uid, isRealMoney);
        try {
            System.out.println("register tour uid: "+user.mUid);
            mng.book(user, mng.tours.get(0));
        } catch (BusinessException ex) {
            Logger.getLogger(TourTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TourTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
