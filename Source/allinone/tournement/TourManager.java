package allinone.tournement;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import vn.game.db.DBException;
import allinone.business.BusinessException;
import allinone.data.AIOConstants;
import static allinone.data.SimpleTable.mLog;
import allinone.data.UserEntity;
import allinone.data.ZoneID;
import allinone.databaseDriven.DBAdmin;
import allinone.databaseDriven.DBPoolConnection;
import allinone.databaseDriven.TourUserDB;
import allinone.databaseDriven.UserDB;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TourManager {

    ArrayList<TournementEntity> tours;

    public TourManager() {
        mLog.debug("TourManager");
        tours = new ArrayList<TournementEntity>(10);
        try {
            initTour();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() throws SQLException {

        tours.clear();
        initTour();

    }

    public void initTour() throws SQLException {

        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            AutoTourBidaDB bidaDB = new AutoTourBidaDB();
            tours = bidaDB.getTourRuning();
            if (tours.size() <= 0) {//ko tim thay thi tu dong insert them 1 tour moi
                AutoTourBidaTemplateDB bidaTemplateDB = new AutoTourBidaTemplateDB();
                TourTemplateEntity tourTmp = bidaTemplateDB.getTemplate(ZoneID.BIDA_TOUR);
                if (tourTmp != null) {
                    mLog.debug("tao tour moi ");
                    bidaDB.createTour(tourTmp);
                    this.initTour();//goi lai init tour
                } else {
                    mLog.debug("khong thay template tour");
                }
            } else {//xu ly tour dang chay
                mLog.debug("co tour roi dang cho run");
            }

            // return result;
        } catch (SQLException e1) {
            // LogLibChan.instance().alwayTrace(new Object[]{"| Thread", Long.valueOf(Thread.currentThread().getId()), "|", "SQLException:", "db admin", e1});
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                // LogLibChan.instance().alwayTrace(new Object[]{"error close"});
            }
        }

    }

    public ArrayList<TournementEntity> getToursInfo() {
        return tours;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<TournementEntity> getToursInfo(long uid)
            throws SQLException, DBException {
        ArrayList<TournementEntity> res = (ArrayList<TournementEntity>) tours
                .clone();

        for (TournementEntity t : res) {
            t.isBook = (TourUserDB.userTourIsExist(uid, t.id) > 0);
        }
        return res;
    }

    public String getTourDetail(int t) throws SQLException {
        String res = "Đây là giải đấu. Thích thì chiến";
        String query = "{ call GetTourInfo(?) }";
        Connection con = DBPoolConnection.getConnection();
        ResultSet rs = null;
        CallableStatement cs = null;
        try {

            cs = con.prepareCall(query);
            cs.clearParameters();
            cs.setInt(1, t);

            rs = cs.executeQuery();
            if (rs != null && rs.next()) {
                res = rs.getString("desc");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cs != null) {
                cs.clearParameters();
                cs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return res;
    }

    public TournementEntity getTour(int id) {
        for (TournementEntity t : tours) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }

    public long book(UserEntity u, TournementEntity tour) throws BusinessException,
            SQLException {
        long currentMoney = 0;
        if (tour != null) {
            AutouTourBidaUserDB tourUserDB = new AutouTourBidaUserDB();
            long registerId = tourUserDB.regsiterTour(u, tour);
            if (registerId > 0) {
                UserDB userDBObj = new UserDB();

                currentMoney = userDBObj.updateUserMoney(tour.money_coc, false, u.mUid,
                        "Đăng ký tham gia giải đấu " + tour.id, u.experience + 1, ZoneID.BIDA_TOUR, tour.id, tour.id, AIOConstants.PRIFIX_REALMONEY,0,0);
            }

        } else {
            throw new BusinessException("Không tìm thấy giải đấu!");
        }
        return currentMoney;

    }

    public final long convertStringDateToLong(String str) {
        String cur_date = new SimpleDateFormat("yyyy-MM-dd ").format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append(cur_date).append(str);
        long result = 0L;
        try {
            result = sdf.parse(sb.toString()).getTime() / 1000L;
        } catch (ParseException e) {
        }
        return result;
    }

}
