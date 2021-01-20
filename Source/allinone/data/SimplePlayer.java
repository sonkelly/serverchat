package allinone.data;

import allinone.databaseDriven.UserDB;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vn.game.common.ServerException;
import vn.game.session.ISession;

public class SimplePlayer implements Serializable{

    public int level;
    public String avatarID;
    public String username;
    public String viewname;//add by zep
    public boolean isStop;
    public long id;
    public long moneyForBet;   //NamNT bet money. There 2 types of money: default money and bet money
    public boolean isWin;
    public boolean isGiveUp;
    public long cash;
    public long currentMatchID;
    public transient ISession currentOwner;
    public transient ISession currentSession;
    public boolean isReady = false;
    public int index;
    public int pos;
    public boolean isOut = false;
    public int registOut = 0;
    private int experience;
    protected long wonMoney;
    private long lastActivated;
    public boolean isMonitor = false; // like isObserve of phom
    private long betOther; //for bet together game 3 cay
    private long multiBetMoney;
    private boolean chan; //for game xocDia;

    private boolean confirmBetOther = false;

    protected boolean showHand = false;

    private long betChan;
    private long betLe;
    private String isRealMoney = "realmoney";
    public static final int POINT_WINNER = 3;
    public static final int POINT_LO0SE = 0;
    public static final int POINT_WINNER_NOT_GET_MONEY = 2;
    public static final int POINT_LOOSE_NOT_MINUS_MONEY = 1;

    public int countTimeoutPlayFlag = 0;

    public int lastIndex = -1;
    public boolean isLevelUp = false;
    public boolean notEnoughMoney() {
        if (cash < moneyForBet) {
            return true;
        }
        return false;
    }

    public boolean notEnoughMoney(int times) {
        if (cash < moneyForBet * times) {
            return true;
        }
        return false;
    }

    public boolean takeNotEnoughMoney(long money) {
        if (money < moneyForBet) {
            return true;
        }
        return false;
    }

    public void write(Object obj) {
        try {
            this.currentSession.write(obj);
        } catch (ServerException e) {
            // TODO: handle exception
        }
    }

    public static int countExperience(boolean isWin, long money, int zoneId) {

        if (zoneId == ZoneID.POKER || zoneId == ZoneID.NXITO || zoneId == ZoneID.NEW_BA_CAY|| zoneId == ZoneID.XOC_DIA) {
            if (money == 0) {
                return 1;
            }

            if (isWin) {
                return 2;
            } else {
                return 0;
            }

        }

        if (!isWin) {
            return 0;
        }

        if (money == 0) {
            return 2;
        }

        if (isWin && money > 0) {
            return 3;
        }

        if (isWin && money < 0) {
            return 1;
        }

        return 0;

    }
    public static int getLevel(int exp,int zoneid){
        int defaultExp = 10;
        int[] ratioArray = {2, 10, 30, 50, 100, 300, 500, 750, 1000, 1500};
        int expLevel = 0;
        List<Integer> expBounds = new ArrayList<Integer>();
        for (int i = 1; i <= 100; i++) {
            if (1 <= i && i < 6) {
                expLevel += i * defaultExp;
            } else if (6 <= i && i < 10) {
                expLevel += i * ratioArray[0] * defaultExp;
            } else if (10 <= i && i < 20) {
                expLevel += i * ratioArray[1] * defaultExp;
            } else if (20 <= i && i < 30) {
                expLevel += i * ratioArray[2] * defaultExp;
            } else if (30 <= i && i < 40) {
                expLevel += i * ratioArray[3] * defaultExp;
            } else if (40 <= i && i < 50) {
                expLevel += i * ratioArray[4] * defaultExp;
            } else if (50 <= i && i < 60) {
                expLevel += i * ratioArray[5] * defaultExp;
            } else if (60 <= i && i < 70) {
                expLevel += i * ratioArray[6] * defaultExp;
            } else if (70 <= i && i < 80) {
                expLevel += i * ratioArray[7] * defaultExp;
            } else if (80 <= i && i < 90) {
                expLevel += i * ratioArray[8] * defaultExp;
            } else if (90 <= i && i <= 99) {
                expLevel += i * ratioArray[9] * defaultExp;
            } else if (i == 100) {
                expLevel += i * 2000 * defaultExp;
            }
            expBounds.add(expLevel);
        }
        for (int i = 0; i < expBounds.size(); i++) {
            if (exp < expBounds.get(i)) {
                return i+1;
            }
        }
        return 1;
        
//        int[] EXP_BOUNDS = {30, 45, 60, 75, 90, 105, 120, 150, 180, 210, 240, 270, 330, 390, 450, 510, 600, 690, 780, 900, 1050, 1275, 1500, 1700, 2000, 2500, 3000, 3500, 4000};
//              
//        for (int i = 0; i < EXP_BOUNDS.length; i++) {
//            if (exp < EXP_BOUNDS[i]) {
//                return i+1;
//            }
//        }
//        
//        double level = ((exp - 4000) / 500 + 30)/10;
//        if(zoneid == ZoneID.XOC_DIA){
//            level = ((exp - 4000) / 500 + 30)/30;
//        }
//        if(level <= 0){
//            level = 1;
//        }
//        return (int) level;
    }
    
    public static int getLevelUpMoney(int level) {
        List<Integer> moneyArray = new ArrayList<Integer>();
        int number = 0;
        int count = 0;
        for (int i = 1; i <= 100; i++) {
            if (i == 1 || i == 10 || i == 20 || i == 30 || i == 40 || i == 50 || i == 60 || i == 70 || i == 80 || i == 90){
                count = 0;
            }
            if (1 <= i && i < 6) {
                number = 20000 + count * 5000;
            } else if (6 <= i && i < 20) {
                number = 50000 + count * 5000;
            } else if (20 <= i && i < 30) {
                number = 100000 + count * 10000;
            } else if (30 <= i && i < 40) {
                number = 200000 + count * 10000;
            } else if (40 <= i && i < 50) {
                number = 400000 + count * 20000;
            } else if (50 <= i && i < 60) {
                number = 1000000 + count * 20000;
            } else if (60 <= i && i < 70) {
                number = 1500000 + count * 50000;
            } else if (70 <= i && i < 80) {
                number = 2000000 + count * 50000;
            } else if (80 <= i && i < 90) {
                number = 2500000 + count * 50000;
            } else if (90 <= i && i <= 99) {
                number = 3000000 + count * 100000;
            } else if (i == 100) {
                number = 5000000;
            }
            count++;
            moneyArray.add(number);
        }
        int money = moneyArray.get(level - 1);
        return money;
    }
    
    public void setLevelUp() throws SQLException {
        UserDB userDb = new UserDB();
        UserEntity user = userDb.getUserInfo(this.id, "realmoney");
        this.experience = user.experience;
        if (user.level > this.level) {
            this.level = user.level;
            this.isLevelUp = true;
        } else {
            this.isLevelUp = false;
        }
    }
        
//    public void setCurrentOwner(ISession currentOwner) {
//        this.currentOwner = currentOwner;
//    }
    /**
     * @return the experience
     */
    public int getExperience() {
        return experience;
    }

    /**
     * @param experience the experience to set
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    public long moneyLost(long money_) {
        if (this.cash <= 0) {
            return 0;
        } else if (this.cash < money_) {
            return this.cash;
        } else {
            return money_;
        }
    }

    /**
     * @return the wonMoney
     */
    public long getWonMoney() {
        return wonMoney;
    }

    /**
     * @param wonMoney the wonMoney to set
     */
    public void setWonMoney(long wonMoney) {
        this.wonMoney = wonMoney;
    }

    /**
     * @return the lastActivated
     */
    public long getLastActivated() {
        return lastActivated;
    }

    /**
     * @param lastActivated the lastActivated to set
     */
    public void setLastActivated(long lastActivated) {
        this.lastActivated = lastActivated;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public SimplePlayer clone() {
        SimplePlayer player = new SimplePlayer();
        player.id = this.id;
        player.currentSession = this.currentSession;
        player.isOut = this.isOut;
        player.registOut = this.registOut;
       

        return player;
    }

    /**
     * @return the betOther
     */
    public long getBetOther() {
        return betOther;
    }

    /**
     * @param betOther the betOther to set
     */
    public void setBetOther(long betOther) {
        this.betOther = betOther;
    }

    /**
     * @return the multiBetMoney
     */
    public long getMultiBetMoney() {
        return multiBetMoney;
    }

    /**
     * @param multiBetMoney the multiBetMoney to set
     */
    public void setMultiBetMoney(long multiBetMoney) {
        this.multiBetMoney = multiBetMoney;
    }

    /**
     * @return the showHand
     */
    public boolean isShowHand() {
        return showHand;
    }

    /**
     * @param showHand the showHand to set
     */
    public void setShowHand(boolean showHand) {
        this.showHand = showHand;
    }

    /**
     * @return the confirmBetOther
     */
    public boolean isConfirmBetOther() {
        return confirmBetOther;
    }

    /**
     * @param confirmBetOther the confirmBetOther to set
     */
    public void setConfirmBetOther(boolean confirmBetOther) {
        this.confirmBetOther = confirmBetOther;
    }

    /**
     * @return the chan
     */
    public boolean isChan() {
        return chan;
    }

    /**
     * @param chan the chan to set
     */
    public void setChan(boolean chan) {
        this.chan = chan;
    }

    /**
     * @return the betChan
     */
    public long getBetChan() {
        return betChan;
    }

    /**
     * @param betChan the betChan to set
     */
    public void setBetChan(long betChan) {
        this.betChan = betChan;
    }

    /**
     * @return the betLe
     */
    public long getBetLe() {
        return betLe;
    }

    /**
     * @param betLe the betLe to set
     */
    public void setBetLe(long betLe) {
        this.betLe = betLe;
    }
    ///
    public void setRealMoney(String money) {
        this.isRealMoney = money;
    }
    public String getRealMoney() {
        return isRealMoney;
    }
    public long getCurrMoney(String money) {
        this.isRealMoney = money;
        UserDB userDb = new UserDB();
        long moneyDB = userDb.getMoney(this.id, this.isRealMoney);
        return moneyDB;
    }
}
