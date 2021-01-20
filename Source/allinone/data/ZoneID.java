/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

/**
 *
 * @author binh_lethanh
 */
public class ZoneID {

    public static final int BACAY = 1;
    public static final int CARO = 53;
    public static final int PHOM = 4;
    public static final int COTUONG = 52;
    public static final int TIENLEN = 5;
    public static final int XOC_DIA = 10;
    public static final int XOC_DIA_ALL = 22;
    public static final int NEW_BA_CAY = 11;
    public static final int BAU_CUA_TOM_CA = 12;
    public static final int STAR_WAR = 20;
    public static final int LINE_ONLINE = 30;
    public static final int GEM_ONLINE = 32;
    public static final int TANK_ONLINE = 35;
    public static final int DHBC = 33;
    public static final int TRO_CHOI_AM_NHAC = 36;
    public static final int XITO = 7;
    public static final int PIKACHU = 31;
    public static final int CHAN = 34;
    public static final int AILATRIEUPHU = 3;
    public static final int LIENG = 9;
    public static final int SAM = 37;
    public static final int NXITO = 13;
//    public static final int NEW_PIKA = 8;
    public static final int BIDA = 8;
    public static final int BIDA_FOUR = 84;
    public static final int BIDA_TOUR = 842;
    public static final int BIDA_PHOM = 86;
    public static final int BINH = 14;
    public static final int POKER = 15;
    public static final int TAIXIU = 54;
    public static final int XO_SO = 16;
    public static final int FOOTBALL = 44;
    public static final int SOCCERSTART = 45;
    public static final int HEADBALL = 46;
    
    public static final int DRAGGER = 47;
    public static final int SHOOTS = 48;
    public static final int MINI_FOOTBALL = 49;

    public static String getZoneName(int zoneID) {
        switch (zoneID) {

            case BACAY:
                return "Ba Cây";

            case LIENG:
                return "Liêng";

            case PHOM:
                return "Phỏm";

            case TIENLEN:
                return "Tiến lên miền nam";

            case SAM:
                return "Sâm";

            case NEW_BA_CAY:
                return "Ba Cây";

            case XITO:
                return "Xì tố";

            case NXITO:
                return "Xì tố";

            case BINH:
                return "Mậu Binh";

            case POKER:
                return "Poker";
            case TAIXIU:
                return "Tài Xỉu";
            case XOC_DIA:
                return "Xóc Đĩa";
            case BAU_CUA_TOM_CA:
                return "Bầu Cua";
            case BIDA:
                return "Bida";
            case FOOTBALL:
                return "Football";
            case SOCCERSTART:
                return "Soccer Start";
            case HEADBALL:
                return "Head Ball";    
            case DRAGGER:
                return "Dragger";    
            case MINI_FOOTBALL:
                return "Mini Football";    
            default:
                return "Game";
        }
    }

    public static int getTimeout(int zoneID) {
        switch (zoneID) {

            case PHOM:
                return 30;

            case TIENLEN:
                return 30;

            case SAM:
                return 30;

            case NEW_BA_CAY:
                return 15;

            case LIENG:
                return 20;

            case BINH:
                return 20;

            case POKER:
                return 20;

            case XITO:
                return 15;

            case NXITO:
                return 15;
            case TAIXIU:
                return 60;
            case XOC_DIA:
                return 30;
            case BIDA:
                return 30;
            case FOOTBALL:
                return 15;
            case SOCCERSTART:
                return 15;
            case HEADBALL:
                return 15;    
            case DRAGGER:
                return 15;    
            default:
                return 20;
        }
    }
}
