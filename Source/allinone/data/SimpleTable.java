package allinone.data;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.slf4j.Logger;

import tools.CacheMatch;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.db.DBException;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IResponseMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.room.Room;
import vn.game.room.RoomEntity;
import vn.game.room.Zone;
import vn.game.room.ZoneManager;
import vn.game.session.ISession;
import allinone.business.BusinessException;
import allinone.databaseDriven.UserDB;
import allinone.protocol.messages.CancelRequest;
import allinone.protocol.messages.CancelResponse;
import allinone.protocol.messages.ChatResponse;
import allinone.protocol.messages.EndMatchResponse;
import allinone.protocol.messages.LineEatResponse;
import allinone.protocol.messages.MissionResponse;
import allinone.protocol.messages.OutResponse;
import allinone.protocol.messages.ReadyResponse;
import allinone.server.Server;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class SimpleTable {

    private Room room;
    public int maximumPlayer;
    public boolean isPlaying = false;
    public boolean isGiveUp = false;

    public int level;
    public long matchID;
    public long matchIDAuto;

    public int matchNum = 0;
    public String matchPlayingList = "";
    protected StringBuilder logDetail = new StringBuilder();
    public ISession ownerSession;
    public long firstCashBet;    //NamNT default setup money of table
    public SimplePlayer owner;
    public String name;
    protected int currentTimeOut;
    protected int timeOutTotalTable;
    public static final String NEW_LINE = System.getProperty("line.separator");
    protected StringBuilder outCodeSB = new StringBuilder();
    public static int AUTO_KICKOUT_TIMEOUT = 20000;
    public static int AUTO_KICKOUT_TIMEOUT_MAX_5P = (60 * 1000) * 1;//300000 * 10;//5 phut ko thao tac auto out
    public static int AUTO_KICKOUT_OWNER_TIMEOUT = 30000;
    protected static int SLEEP_BEETWEEN_MATCH_TIMEOUT = 10000;
    public static int AUTO_KICKOUT_OWNER_TIMEOUT2 = 10000;
    protected int currentIndexPlayer;
    protected final double REAL_GOT_MONEY = Server.REAL_GOT_MONEY;
    protected final String NONE_EXISTS_PLAYER = "Không tồn tại người chơi này";
    protected final String NOT_ALLOW_OWNER_MONEY_BET = "Số tiền vượt quá số tiền có thể thua của chủ bàn";
    protected final String NOT_ALLOW_USER_MONEY_BET = "Số tiền vượt quá số tiền bạn đang có";
    protected final String NOT_ALLOW_OWNER_BET = "Chủ bàn không được phép đặt cược";
    protected final String NOT_ALLOW_OWNER_BET_OTHER = "Chủ bàn không được phép chơi cận biên";
    protected final String REQUIRED_MONEY = "Tiền đặt cược ít hơn tiền bàn";
    protected final String NOT_PLAYING_TABLE = "Ván chơi đã kết thúc";
    protected final String DONE_BET = "Bạn đã đặt cược rồi";
    public static final int LOG_TYPE_GAME_START = 10000;
    protected final int TIMES = 4;
    public boolean dontWantAnyUser = false;
    public boolean isKickoutBot = false;
    protected boolean isTryKickOut = true;
    public static final Logger mLog = LoggerContext.getLoggerFactory()
            .getLogger(SimpleTable.class);
    public allinone.logger.Logger newLog;
    private long ownerId;
    protected SimplePlayer botPlayer;
    protected static final int MAX_RETRY_BOT = 10;
    public int tableIndex;
    public int phantram = 10;

    public static final int MONEY_TIME_PHOM = 4;
    public static final int MONEY_TIME_TMLN = 10;
    public static final int MONEY_TIME_SAM = 10;
    public static final int MONEY_TIME_BINH = 10;
    public static final int MONEY_TIME_MIN_LIENG = 1;
    public static final int MONEY_TIME_MAX_LIENG = 10;
    public static final int MONEY_TIME_XOCDIA = 10;
    public static final int MONEY_TIME_XOCDIA_MIN = 1;
    public static final int MONEY_MIN_BIDA = 6;
    public static final int MONEY_MIN_BIDA_PHOM = 2;

    public static final int MONEY_TIME_MINIMUM_POKER = 10;
    public static final int MONEY_TIME_MIN_POKER = 25;
    public static final int MONEY_TIME_MAX_POKER = 50;

    public static final int MONEY_TIME_MINIMUM_XITO = 10;
    public static final int MONEY_TIME_MIN_XITO = 25;
    public static final int MONEY_TIME_MAX_XITO = 50;

    public static final int AUTO_START_READY_GAME = 8000; // client 8s
    public static final int AUTO_START_READY_GAME_BIDA = 3000; // bida
    public static final int AUTO_START_GAME = 13000;
    public static final int AUTO_BEGIN_START_GAME = 6000; // client 5s
    public static final int AUTO_BEGIN_START_GAME_BIDA = 3000; // client 5s

    public static byte[] zonesGame = {ZoneID.TIENLEN, ZoneID.PHOM, ZoneID.NEW_BA_CAY, ZoneID.LIENG, ZoneID.NXITO, ZoneID.SAM, ZoneID.BINH, ZoneID.POKER, ZoneID.XOC_DIA, ZoneID.BAU_CUA_TOM_CA, ZoneID.BIDA, ZoneID.BIDA_FOUR, ZoneID.BIDA_PHOM, ZoneID.COTUONG};

    public long lastStartActive;
    public long lastReadyStartActive;

    public boolean autoFlayFlag = false;
    public boolean tableKickOut = false;

    public List<SimplePlayer> viewings = new ArrayList<SimplePlayer>();

    //add by zep dung cho an hu
    public long isUserAnHu = 0;
    public long moneyAnHu = 0;

    public String isRealMoney = "realmoney";

    public int getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }
    public long phongID;

    public void setPhongID(long phongID) {
        this.phongID = phongID;
    }

    public long getPhongID() {
        return phongID;
    }

    public void setRealMoney(String realmoeny) {
        this.isRealMoney = realmoeny;
    }

    public String getRealMoney() {
        return isRealMoney;
    }

    protected long lastActivated; //for auto process(timeout)

    public static SimpleTable findUserInTable(long uid) {

        SimpleTable table = null;

        ZoneManager zManager = Server.getWorker().getZoneManager();

        Enumeration<Zone> zones = zManager.getZones();

        while ((zones != null) && (zones.hasMoreElements())) {
            try {
                Zone zone = (Zone) zones.nextElement();

                Vector<RoomEntity> rooms = zone.dumpRooms(zone.getZoneId());

                for (int j = 0; j < rooms.size(); j++) {

                    try {

                        table = (SimpleTable) rooms.get(j).mAttactmentData;

                        SimplePlayer player = table.findPlayer(uid);

                        if (player != null) {
                            return table;
                        }

                    } catch (Exception ex) {
                        mLog.error("Error find table for user ", ex.getMessage());
                    }

                }

            } catch (Exception ex) {
                mLog.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

    public boolean hasBotUser() {
        try {
            List<? extends SimplePlayer> lstPlaying = getNewPlayings();
            int playingSize = lstPlaying.size();
            UserDB db = new UserDB();
            for (int i = 0; i < playingSize; i++) {
                SimplePlayer player = lstPlaying.get(i);
                if (db.checkBotUser(player.id)) {
                    return true;
                }
            }
        } catch (Exception ex) {
        }

        return false;

    }

    protected void resetBotCheck() {
        botPlayer = null;
    }

    public boolean isPlayerPlaying(long uid) throws SimpleException {

        if (!isPlaying) {
            return false;
        }

        List<? extends SimplePlayer> playings = getNewPlayings();

        if (playings == null) {
            return false;
        }

        int playingSize = playings.size();

        for (int i = 0; i < playingSize; i++) {
            SimplePlayer player = playings.get(i);
            if (player.id == uid) {
                return true;
            }
        }
        return false;
    }

    public SimplePlayer findPlayer(long uid) throws SimpleException {

        List<? extends SimplePlayer> playings = getNewPlayings();
        List<? extends SimplePlayer> waitings = getNewWaitings();

        if (playings == null) {
            return null;
        }

        int playingSize = playings.size();
        for (int i = 0; i < playingSize; i++) {
            SimplePlayer player = playings.get(i);
            if (player.id == uid) {
                return player;
            }
        }

        if (waitings != null) {
            int waitingSize = waitings.size();
            for (int i = 0; i < waitingSize; i++) {
                SimplePlayer player = waitings.get(i);
                if (player.id == uid) {
                    return player;
                }
            }

        }

        return null;
    }

    public void removePlayerById(long id) {
        List<? extends SimplePlayer> playings = getNewPlayings();
        List<? extends SimplePlayer> waitings = getNewWaitings();

        synchronized (playings) {
            for (int i = 0; i < playings.size(); i++) {
                SimplePlayer removePlayer = playings.get(i);
                if (removePlayer.id == id) {
                    playings.remove(removePlayer);
                    return;
                }
            }
        }
        synchronized (waitings) {
            for (int i = 0; i < waitings.size(); i++) {
                SimplePlayer removePlayer = waitings.get(i);
                if (removePlayer.id == id) {
                    waitings.remove(removePlayer);
                    return;
                }
            }
        }

    }

    public void removePlayer(SimplePlayer player) {
        List<? extends SimplePlayer> playings = getNewPlayings();
        List<? extends SimplePlayer> waitings = getNewWaitings();

        synchronized (playings) {
            for (int i = 0; i < playings.size(); i++) {
                SimplePlayer removePlayer = playings.get(i);
                if (removePlayer.id == player.id) {
                    playings.remove(removePlayer);
                    return;
                }
            }
        }
        synchronized (waitings) {
            for (int i = 0; i < waitings.size(); i++) {
                SimplePlayer removePlayer = waitings.get(i);
                if (removePlayer.id == player.id) {
                    waitings.remove(removePlayer);
                    return;
                }
            }
        }
    }

    //NamNT override get current player for another game later
    public SimplePlayer getCurrPlayer() {
        return null;
    }

    public FileWriter outFile_code;// = new FileWriter(args[0]);
    public PrintWriter out_code = null;// = new PrintWriter(outFile);
    public FileWriter outFile;// = new FileWriter(args[0]);
    public PrintWriter out;// = new PrintWriter(outFile);

    public long getMatchID() {
        return matchID;
    }

    public void setMatchID(long matchID) {
        this.matchID = matchID;
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    public void setIsPlaying(boolean b) {
        this.isPlaying = b;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaximumPlayer() {
        return maximumPlayer;
    }

    public void setMaximumPlayer(int max) {
        maximumPlayer = max;
    }

    public long getMinBet() {
        return firstCashBet;
    }

    public long getMatchIDAuto() {
        return matchIDAuto;
    }

    public void destroy() {
        saveLogToFile();
        try {
            if (out_code != null) {
                out_code.println("Room destroy!");
                out.println("Room destroy!");

                out.close();
                outFile.close();
                out_code.close();
                outFile_code.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFullTable() {
        return true;
    }

    public void setMatchIDAuto(long matchIDAuto) {
        this.matchIDAuto = matchIDAuto;
    }

    public void setOwnerSession(ISession ownerSession) {
        this.ownerSession = ownerSession;
    }

    public ISession getOwnerSession() {
        return ownerSession;
    }
    public String logdir = "none_log";

    protected void saveLogToFile() {

        try {
            new File("logs/" + logdir).mkdirs();

        } catch (Exception e) {
            //e.printStackTrace();
        }

        if (matchID == 0) {
            return;
        }

        try {
            System.out.println("save log to file came here");
            String str = "logs/" + logdir + "/match_" + matchID + ".txt";
            File f = new File(str);
            boolean append = false;
            if (f.exists()) {
                append = true;
            }
            FileWriter outF = new FileWriter("logs/" + logdir + "/match_" + matchID + ".txt", append);
            PrintWriter writer = new PrintWriter(outF);
            writer.println(getOutCodeSB().toString());
            writer.flush();
            writer.close();
            outF.close();
            outCodeSB.setLength(0);
            logDetail.setLength(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initLogFile() {
        try {
            File dir1 = new File(".");

            boolean success = (new File("logs/" + logdir)).mkdirs();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (matchID == 0) {
            return;
        }
        try {

            String str = "logs/" + logdir + "/match_" + matchID + ".txt";
            File f = new File(str);
            boolean append = false;
            if (f.exists()) {
                append = true;
            }

            outFile = new FileWriter("logs/" + logdir + "/match_" + matchID + ".txt", append);
            out = new PrintWriter(outFile);

            outFile_code = new FileWriter("logs/" + logdir + "/match_" + matchID + "_code.txt", append);
            out_code = new PrintWriter(outFile_code);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void doTimeout() //should be abstract method
    {
        System.out.println("Vao day de");
    }

    public void doTimeoutNotPlay() //should be abstract method
    {
        System.out.println("Vao day doTimeoutNotPlay");
    }

    /**
     * @return the currentTimeOut
     */
    public int getCurrentTimeOut() {
        return currentTimeOut;
    }

    /**
     * @param currentTimeOut the currentTimeOut to set
     */
    public void setCurrentTimeOut(int currentTimeOut) {
        this.currentTimeOut = currentTimeOut;
    }

    public int getTimeAllTable() {
        return timeOutTotalTable;
    }

    /**
     * @param currentTimeOut the currentTimeOut to set
     */
    public void setTimeAllTable(int currentTimeOut) {
        this.timeOutTotalTable = currentTimeOut;
    }

    /**
     * @return the ownerId
     */
    public long getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    protected List<SimplePlayer> removeNotEnoughMoney() {
        return null;
    }

    public void cancel(List<? extends SimplePlayer> players) {
        try {
            MessageFactory msgFactory = owner.currentSession.getMessageFactory();
            for (int i = 0; i < players.size(); i++) {
                SimplePlayer player = players.get(i);
                if (player.id != owner.id) {
                    try {
                        CancelResponse removeRes = (CancelResponse) msgFactory.getResponseMessage(MessagesID.MATCH_CANCEL);
                        removeRes.setSuccess(ResponseCode.SUCCESS, players.get(i).id);
                    } catch (Exception ex) {
                        mLog.error("cancel session error", ex);
                    }
                }
            }

            CancelResponse removeRes = (CancelResponse) msgFactory.getResponseMessage(MessagesID.MATCH_CANCEL);
            removeRes.setSuccess(ResponseCode.SUCCESS, owner.id);

        } catch (Exception ex) {
            mLog.error("cancel before delete invalid room", ex);

//            outCodeSB.append("cancel before delete invalid room").append(NEW_LINE);
        }
    }

    private void sendMsg(SimplePlayer player, IResponseMessage response) {
        try {

            //check make sure don't send to session if it 's in another room
            if (player.currentSession == null) {
                return;
            }

            Room playerRoom = player.currentSession.getRoom();

            if (playerRoom == null
                    || (playerRoom != null && playerRoom.getAttactmentData().matchID == this.matchID)) {
                if (
                        response instanceof ReadyResponse || response instanceof EndMatchResponse
                        || response instanceof CancelResponse
                        || response instanceof OutResponse
                        || response instanceof ChatResponse || response instanceof LineEatResponse
                        || response instanceof MissionResponse) {

//                if (this instanceof PhomTable || this instanceof  TienLenTable)
                    if (response instanceof MissionResponse) {
                        int a = 1;
                    }
                    response = response.clone(player.currentSession);
//                else
//                    response.setSession(player.currentSession);

                } else {
                    response.setSession(player.currentSession);
                }
                if (player.currentSession != null) {
                    player.currentSession.write(response);
                }
            }
        } catch (Exception ex) {
        } catch (Throwable ex) {
        }
    }

    public void broadcastMsg(IResponseMessage msg, List<? extends SimplePlayer> playings,
            List<? extends SimplePlayer> waitings, SimplePlayer sender, boolean isSendMe) {

        try {

            boolean isSentMe = false;

            if (isSendMe && sender != null && sender.currentSession != null && !sender.isOut) {
                isSentMe = true;
                sendMsg(sender, msg);//send owner
            }

            for (int i = 0; i < playings.size(); i++) {
                SimplePlayer player = playings.get(i);
                if ((!player.isOut || (player.isOut && player.registOut == 1))
                        && ((sender == null) || (sender != null && (player.id != sender.id || (isSendMe && !isSentMe)))))//have you sent it?
                {
                    sendMsg(player, msg);
                }
            }

            for (int i = 0; i < waitings.size(); i++) {
                SimplePlayer player = waitings.get(i);
                if (!player.isOut
                        && ((sender == null) || (sender != null && (player.id != sender.id || (isSendMe && !isSentMe))))) {
                    sendMsg(player, msg);
                }
            }

            for (int i = 0; i < viewings.size(); i++) {
                SimplePlayer player = viewings.get(i);

//            	mLog.debug(" Viewings ID " + player.id);
                if (player != null && player.currentSession != null && player.currentSession.isLoggedIn() && !player.currentSession.isExpired()) {
                    boolean sendFlag = true;
                    // check remove
                    for (int j = 0; j < playings.size(); j++) {
                        SimplePlayer playing = playings.get(j);
                        if (playing.id == player.id) {
                            sendFlag = false;
                        }
                    }

                    for (int k = 0; k < waitings.size(); k++) {
                        SimplePlayer waiting = waitings.get(k);
                        if (waiting.id == player.id) {
                            sendFlag = false;
                        }
                    }

                    if (sendFlag && ((sender == null) || (sender != null && player.id != sender.id))) {
                        sendMsg(player, msg);
                    }
                }
            }

//            outCodeSB.append("End send broadcast to room with midId: ").append(msg.getID()).append(NEW_LINE);
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

    }

    public void removePlayerNullSession(long uid) {

        SimplePlayer removedPlayer = null;

        try {
            removedPlayer = this.findPlayer(uid);
        } catch (SimpleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (removedPlayer == null) {
            return;
        }

        this.removePlayer(removedPlayer);

        //send another people to inform this not enough money event
        ISession session = this.getNotNullSession();

        if (session != null) {
            CancelResponse removeRes = (CancelResponse) this.getNotNullSession().getMessageFactory().getResponseMessage(MessagesID.MATCH_CANCEL);
            removeRes.setSuccess(ResponseCode.SUCCESS, removedPlayer.id);
            removedPlayer.isOut = true;

            if (getNewPlayings() != null) {
                broadcastMsg(removeRes, getNewPlayings(), getNewWaitings(), removedPlayer, false);
            }
        }

        if (removedPlayer.currentSession != null) {
            room.left(removedPlayer.currentSession);
            removedPlayer.currentSession.setRoom(null);
            removedPlayer.currentSession.setLoggedIn(false);
            removedPlayer.currentSession.setUIDNull();
            removedPlayer.currentSession.close();
        }
    }

    public void removeNotEnoughMoney(Room room) {
        long newOwnerId = ownerId;

        List<SimplePlayer> removedPlayers = this.removeNotEnoughMoney();

        if (removedPlayers == null) {
            return;
        }

        if (newOwnerId == this.getOwnerId()) {
            newOwnerId = 0;
        } else {
            newOwnerId = this.getOwnerId();
        }

        //System.out.println("new ownerId "+ newOwnerId);
        for (int i = 0; i < removedPlayers.size(); i++) {
            SimplePlayer removedPlayer = removedPlayers.get(i);
            //kick out this user
            MessageFactory msgFactory = removedPlayer.currentSession.getMessageFactory();
            OutResponse outRes = (OutResponse) msgFactory.getResponseMessage(MessagesID.OUT);
            outRes.session = removedPlayer.currentSession;

            outRes.setSuccess(ResponseCode.SUCCESS, removedPlayer.id, "Bạn không đủ tiền chơi game này", "", (int) this.getMatchID());

            try {
                removedPlayer.currentSession.write(outRes);
            } catch (ServerException ex) {
                mLog.error("Send out message error", ex.getMessage());
            }

//            mLog.debug(" Out khoi phong " + removedPlayer.id);
            //send another people to inform this not enough money event
            CancelResponse removeRes = (CancelResponse) msgFactory.getResponseMessage(MessagesID.MATCH_CANCEL);
            removeRes.setSuccess(ResponseCode.SUCCESS, removedPlayer.id);
            removeRes.newOwner = newOwnerId;
            removedPlayer.isOut = true;

            if (getNewPlayings() != null) {
                broadcastMsg(removeRes, getNewPlayings(), getNewWaitings(), removedPlayer, false);
            }

            room.left(removedPlayer.currentSession);
            this.removePlayer(removedPlayer);

            if (removedPlayer.currentSession != null) {
                removedPlayer.currentSession.setRoom(null);
            }
        }
    }

    public void cancelAllViewer() {

        for (int i = 0; i < this.viewings.size(); i++) {
            try {
                SimplePlayer player = this.viewings.get(i);
                if (player != null && player.currentSession != null) {
                    MessageFactory msgFactory = player.currentSession.getMessageFactory();
                    CancelResponse response = (CancelResponse) msgFactory.getResponseMessage(MessagesID.MATCH_CANCEL);

                    response.setSuccess(ResponseCode.SUCCESS, player.id);
                    response.setSession(player.currentSession);
                    response.zone_id = player.currentSession.getCurrentZone();

                    Room room = player.currentSession.leftRoom(matchID);
                    if (room != null) {
                        room.left(player.currentSession);
                    }
                    player.currentSession.setRoom(null);
                    player.currentSession.write(response);
                }
            } catch (ServerException e) {
                mLog.error(e.getMessage(), e);
            } catch (Exception e) {
                mLog.error(e.getMessage(), e);
            }
        }

        this.viewings.clear();
    }

    public void cancelAllWaitings() {

        for (int i = 0; i < getNewWaitings().size(); i++) {
            try {
                SimplePlayer player = getNewWaitings().get(i);
                if (player != null && player.currentSession != null) {
                    MessageFactory msgFactory = player.currentSession.getMessageFactory();
                    CancelResponse response = (CancelResponse) msgFactory.getResponseMessage(MessagesID.MATCH_CANCEL);

                    response.setSuccess(ResponseCode.SUCCESS, player.id);
                    response.setSession(player.currentSession);
                    response.zone_id = player.currentSession.getCurrentZone();

                    Room room = player.currentSession.leftRoom(matchID);
                    if (room != null) {
                        room.left(player.currentSession);
                    }
                    player.currentSession.setRoom(null);
                    player.currentSession.write(response);
                }
            } catch (ServerException e) {
                mLog.error(e.getMessage(), e);
            } catch (Exception e) {
                mLog.error(e.getMessage(), e);
            }
        }

        this.getNewWaitings().clear();
    }

    public void kickTimeout(Room room) {
    }

    public void kickTimeout(Room room, SimplePlayer removedPlayer, long newOwnerId) {
        MessageFactory msgFactory = removedPlayer.currentSession.getMessageFactory();
        if (msgFactory == null) {
            if (owner == null) {
                mLog.error("auto delete room");
//                outCodeSB.append("auto delete room");
                this.destroy();
                room.allLeft();

            }
            msgFactory = owner.currentSession.getMessageFactory();
        }

        OutResponse outRes = (OutResponse) msgFactory.getResponseMessage(MessagesID.OUT);
        String msg = "Bạn chưa bấm sẵn sàng";
        if (newOwnerId != 0) {
            msg = "Bạn chưa bấm bắt đầu";
        }
        outRes.setSuccess(ResponseCode.SUCCESS, removedPlayer.id, msg, "", (int) this.getMatchID());
        outRes.type = 1;
        if (removedPlayer == null || removedPlayer.currentSession == null) {
            String errorDescription = "kick timeout error null player or session";
            mLog.error(errorDescription);
//            outCodeSB.append(errorDescription).append(NEW_LINE);
            return;
        }
        try {
            removedPlayer.currentSession.write(outRes);
        } catch (ServerException ex) {
            //mLog.error("Out tienlen player error", ex);
        }

        CancelResponse removeRes = (CancelResponse) msgFactory.getResponseMessage(MessagesID.MATCH_CANCEL);
        removeRes.setSuccess(ResponseCode.SUCCESS, removedPlayer.id);
        removeRes.newOwner = newOwnerId;
        try {
            removedPlayer.currentSession.leftRoom(matchID);
        } catch (Exception ex) {
        }
        room.left(removedPlayer.currentSession);
        //room.broadcastMessage(removeRes, removedPlayer.currentSession, true);

    }

    public void autoStartGame(SimplePlayer player) {
        /*ISession session ;
	
         session = player.currentSession;
         IResponsePackage responsePkg = session.getDirectMessages();
                
         MessageFactory msgFactory = player.currentSession.getMessageFactory();
         RestartRequest restartRequest = (RestartRequest) msgFactory
         .getRequestMessage(MessagesID.MATCH_RESTART);
        
         restartRequest.mMatchId = matchID;
         restartRequest.money = firstCashBet;
         restartRequest.uid = player.id;
         try {
         IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_RESTART);
         business.handleMessage(player.currentSession, restartRequest, responsePkg);
         } catch (ServerException se) {
         }*/
    }

    //NamNT override set ready for every game  -- we should rename this method later
    public void playerReady(long uid, boolean r) {
        try {

            SimplePlayer player = findPlayer(uid);

            if (player != null) {
                player.setReady(r);
                long now = System.currentTimeMillis();
                player.setLastActivated(now);
                owner.setLastActivated(now);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

//    protected void sendReadyMessage(SimplePlayer player, IResponseMessage msg)
//    {
//        
//    }
    //NamNT override set ready for every game  -- we should rename this method later
    public void playerReadyWithBroadcast(long uid, boolean r, boolean countDown) {
        try {

            SimplePlayer player = findPlayer(uid);

            if (player != null) {
                player.setReady(r);
                long now = System.currentTimeMillis();
                player.setLastActivated(now);
                owner.setLastActivated(now);

                MessageFactory msgFactory = player.currentSession.getMessageFactory();
                if (msgFactory == null) {
//                    if(owner == null)
//                    {
//                        mLog.error("auto delete room");
//                        outCodeSB.append("auto delete room").append(NEW_LINE);
////                        this.destroy();
//                        room.allLeft();
//
//                    }
//                    msgFactory = owner.currentSession.getMessageFactory();
                    ISession session = getNotNullSession();
                    msgFactory = session.getMessageFactory();

                }

                //send ready json to room
                ReadyResponse readyMsg = (ReadyResponse) msgFactory.getResponseMessage(MessagesID.MATCH_READY);
                readyMsg.setSuccess(ResponseCode.SUCCESS, uid, r);

                if (countDown) {
                    readyMsg.startCount = 1;
                }

                broadcastMsg(readyMsg, this.getNewPlayings(), this.getNewWaitings(), player, true);

                lastaActive = System.currentTimeMillis();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    /**
     *
     * Do kick-out
     */
    public long kickOutTime = 15000;
    public long lastaActive;

    public boolean isAllReady() {

        int len = getNewPlayings().size();
        if (len < 2) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            SimplePlayer p = getNewPlayings().get(i);
            if (!p.isReady && p.id != owner.id) {
                return false;
            }

        }
        return true;
    }

    public void updateIsOut() {
        for (int i = 0; i < getNewPlayings().size(); i++) {
            SimplePlayer p = getNewPlayings().get(i);
            //System.out.println("p.registOut:"+p.registOut);
            if (p.registOut == 1) {
                p.isOut = true;
            }
        }
    }

    public void updateIsTimeOutPlay(int number) {
        for (int i = 0; i < getNewPlayings().size(); i++) {
            SimplePlayer p = getNewPlayings().get(i);
            if (p.countTimeoutPlayFlag >= number) {
                p.registOut = 1;
            }
//           	p.countTimeoutPlayFlag = 0;
        }
    }

    public void updateIsTimeOutPlay() {
        for (int i = 0; i < getNewPlayings().size(); i++) {
            SimplePlayer p = getNewPlayings().get(i);
            if (p.countTimeoutPlayFlag >= 3) {
                p.registOut = 1;
            }
//        	p.countTimeoutPlayFlag = 0;
        }
    }

    public void updateIsNullSession() {

        // check validate session
        if (getNewPlayings().size() > 0 && this.getNotNullSession() == null) {
            mLog.debug(" Kill talbe all player session is null ");
            this.room.allLeft();
            this.destroy();
            return;
        }

        // neu trong playing list - thi van sau quit
        for (int i = 0; i < getNewPlayings().size(); i++) {
            SimplePlayer p = getNewPlayings().get(i);
            if (p.currentSession == null || p.currentSession.isExpired() || p.currentSession.isClosed() || !p.currentSession.isLoggedIn()) {
                //mLog.debug("Player session timeout next turn OUT " + p.username + " " + p.id);
                p.registOut = 1;
            }
        }

        // neu trong waitting list thi thuc hien thoat khoi ban luon 
        for (int i = 0; i < getNewWaitings().size(); i++) {
            SimplePlayer p = getNewWaitings().get(i);
            if (p.currentSession == null || p.currentSession.isExpired() || p.currentSession.isClosed() || !p.currentSession.isLoggedIn()) {
                mLog.debug("Player session timeout waitting Cancel " + p.username + " " + p.id);
                removePlayerNullSession(p.id);
            }
        }
    }

    public void autoCancelPlayer(SimplePlayer player) throws ServerException, JSONException, BusinessException {

    }

    public void doAutoKickOut() {
        //mLog.debug("doAutoKickOut isPlaying:" + isPlaying + " this.getNewPlayings().size():" + this.getNewPlayings().size());
        if (!isPlaying && this.getNewPlayings().size() > 1) {
            mLog.debug("Auto Kick out owner!");
            autoKickOwner();
        }

        this.lastStartActive = System.currentTimeMillis();
    }

    public void doAutoReadyStart() {

        // removew onwer 
        try {
            this.removeOwner();
        } catch (JSONException e) {
            mLog.error(" removeOwner " + e.getMessage());
        } catch (BusinessException e) {
            mLog.error(" removeOwner " + e.getMessage());
        } catch (ServerException e) {
            mLog.error(" removeOwner " + e.getMessage());
        }

        int playingSize = this.getNewPlayings().size();

        if (playingSize <= 1 && this.getRoom().getZoneID() != ZoneID.XOC_DIA) {//edit by zep
            mLog.debug(" So nguoi choi " + playingSize);
            return;
        }

//        mLog.debug("Auto Start game!");        
        ISession session = null;
        for (int i = 0; i < this.getNewPlayings().size(); i++) {
            SimplePlayer player = this.getNewPlayings().get(i);
            if (player.cash > 0 && player.currentSession != null) {
                session = player.currentSession;
                break;
            }
        }

        if (session == null) {
            mLog.debug(" Session IS NULL ");
            return;
        }
    }

    public void doAutoStart() {
//        mLog.debug("doAutoStart");
        // validate before start
        try {
            checkEnoughMoney();
        } catch (JSONException e) {
            mLog.error(" Error enough money " + e.getMessage());
        } catch (BusinessException e) {
            mLog.error(" Error enough money " + e.getMessage());
        } catch (ServerException e) {
            mLog.error(" Error enough money " + e.getMessage());
        } catch (Exception e) {
            mLog.error(" Error enough money " + e.getMessage());
        }

        int playingSize = this.getNewPlayings().size();

//        if(playingSize <= 1) {        	
//            mLog.debug(" So nguoi choi " + playingSize);        
//        	this.lastStartActive = 0;
//        	return;        	
//        }
//edit by zep
        if (playingSize <= 1 && this.getRoom().getZoneID() != ZoneID.XOC_DIA) {
            mLog.debug(" So nguoi choi " + playingSize);
            this.lastStartActive = 0;
            return;
        }

//        mLog.debug("Auto Start game!");        
        ISession session = null;

//        if(owner == null || owner.currentSession == null) {
        for (int i = 0; i < this.getNewPlayings().size(); i++) {
            SimplePlayer player = this.getNewPlayings().get(i);
            if (player.cash > 0 && player.currentSession != null && player.currentSession.getMessageFactory() != null) {
//                	owner   = player; 
//                	ownerSession= player.currentSession;
                session = player.currentSession;
                break;
            }
        }
//        } else {
//        	session = owner.currentSession;
//        }

        if (session == null) {
            mLog.debug(" Session IS NULL ");
            this.lastStartActive = 0;
            return;
        }

       

        this.lastStartActive = System.currentTimeMillis();
    }

    private void autoKickOwner() {
        mLog.debug(" autoKickOwner ");
        ISession session;

        session = owner.currentSession;
        IResponsePackage responsePkg = session.getDirectMessages();

        MessageFactory msgFactory = owner.currentSession.getMessageFactory();
        CancelRequest restartRequest = (CancelRequest) msgFactory
                .getRequestMessage(MessagesID.MATCH_CANCEL);
        restartRequest.mMatchId = matchID;
        try {
            IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
            business.handleMessage(owner.currentSession, restartRequest, responsePkg);
        } catch (ServerException se) {
            mLog.error(se.getMessage(), se);
        }
    }

    /**
     * @return the outCodeSB
     */
    public StringBuilder getOutCodeSB() {
        return outCodeSB;
    }

    /**
     * @param outCodeSB the outCodeSB to set
     */
    public void setOutCodeSB(StringBuilder outCodeSB) {
        this.outCodeSB = outCodeSB;
    }

    protected boolean updateUserCash(UserDB userDb, SimplePlayer winner,
            SimplePlayer loser, double betMoney, String desc) throws DBException, SQLException {

        boolean havingMinusBalance = false;
        int logtypeId = LOG_TYPE_GAME_START;
        try {
            logtypeId += ownerSession.getCurrentZone();
        } catch (Exception ex) {
            mLog.error("updateUserCash", ex);
        }
        if (loser.cash == 0) {
            loser.cash -= betMoney;//avoid to save to db if lose doesn't have cash
        } else {
            loser.cash = userDb.updateUserMoney((long) betMoney,
                    false, loser.id, desc + winner.id,
                    0, logtypeId, matchID, matchNum, loser.getRealMoney(), getMinBet(), (owner != null ? owner.id : 0));
        }

        if (loser.cash < 0) {
            betMoney += loser.cash;
            havingMinusBalance = true;
            loser.cash = 0;
        }

        long wonMoney = (long) (betMoney * REAL_GOT_MONEY);
        loser.setWonMoney((long) -betMoney + loser.getWonMoney());
        winner.setWonMoney(wonMoney + winner.getWonMoney());

        //save winner
        winner.cash = userDb.updateUserMoney(wonMoney,
                true, winner.id, desc + loser.id,
                1, logtypeId, matchID, matchNum, winner.getRealMoney(), getMinBet(), (owner != null ? owner.id : 0));

//        outCodeSB.append("user: ").append(loser.username).append("lost money: ").append(loser.getWonMoney()).append(NEW_LINE);
//        outCodeSB.append("user: ").append(loser.username).append("won money: ").append(winner.getWonMoney()).append(NEW_LINE);
        return havingMinusBalance;
    }

    protected boolean updateUserCash(UserDB userDb, SimplePlayer player, boolean isWin,
            double betMoney, String desc) throws DBException, SQLException {

        boolean havingMinusBalance = false;
        int logtypeId = LOG_TYPE_GAME_START;
        try {
            logtypeId += ownerSession.getCurrentZone();
        } catch (Exception ex) {
            mLog.error("updateUserCash", ex);
        }
        long money = 0;
        if (isWin) {
            money = (long) (betMoney * REAL_GOT_MONEY);
        } else {
            money = (long) betMoney;
        }
        player.setWonMoney(money);
        player.cash = userDb.updateUserMoney(money,
                isWin, player.id, desc,
                0, logtypeId, matchID, matchNum, this.getRealMoney(), getMinBet(), (owner != null ? owner.id : 0));

        if (player.cash < 0) {
            havingMinusBalance = true;
            player.cash = 0;
        }
        return havingMinusBalance;
    }

    public int getTableSize() {
        return 0;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    //we should overried this method for every game
    public boolean newContainPlayer(long uid) {
        return false;
    }

    protected void join(SimplePlayer player) throws BusinessException {
        //override to input in playing and waiting player
    }

    public List<? extends SimplePlayer> getNewPlayings() {
        return null;
    }

    public List<? extends SimplePlayer> getNewWaitings() {
        return null;
    }


    public ISession getNotNullSession() {
        return null;
    }

    public boolean checkEnoughMoney(long money) {
        return money < this.firstCashBet;
    }

    public void resetViewingList() {

        List<SimplePlayer> newViewingList = new ArrayList<SimplePlayer>();

        for (int i = 0; i < this.viewings.size(); i++) {
            SimplePlayer player = this.viewings.get(i);
            // loai bo nhung player bi null hoac session da bi huy
            if (player != null && player.currentSession != null && player.currentSession.isLoggedIn() && !player.currentSession.isExpired()) {
                newViewingList.add(player);
            }
        }

        this.viewings = newViewingList;
    }

    public void removeGuest(SimplePlayer player) {
        for (int i = 0; i < this.viewings.size(); i++) {
            SimplePlayer removePlayer = this.viewings.get(i);
            if (removePlayer.id == player.id) {
                this.viewings.remove(i);
                break;
            }
        }
    }

    public boolean cancelGuest(long uid) {

        mLog.debug(" Cancel Guest " + uid);
        SimplePlayer player = this.findGuest(uid);

        if (player == null) {
            return false;
        }

        mLog.debug(" Cancel Guest " + player.username);

        CancelResponse resMatchCancel;
        MessageFactory msgFactory = getNotNullSession().getMessageFactory();
        resMatchCancel = (CancelResponse) msgFactory.getResponseMessage(MessagesID.MATCH_CANCEL);
        resMatchCancel.setSuccess(ResponseCode.SUCCESS, player.id);

        try {
            player.currentSession.write(resMatchCancel);
        } catch (ServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.removeGuest(player);

        return true;
    }

    public SimplePlayer findGuest(long uid) {
//    	mLog.debug(" Viewings size " + size);
        for (int i = 0; i < this.viewings.size(); i++) {
            SimplePlayer player = this.viewings.get(i);
//            mLog.debug(" Viewings ID " + player.id);
            if (player.id == uid) {
                return player;
            }
        }
        return null;
    }

    public void resetAllReady() {
        List<? extends SimplePlayer> playings = getNewPlayings();
        List<? extends SimplePlayer> waitings = getNewWaitings();

        int len = playings.size();
        for (int i = 0; i < len; i++) {
            SimplePlayer p = playings.get(i);
            p.isReady = false;
        }
        len = waitings.size();
        for (int i = 0; i < len; i++) {
            SimplePlayer p = waitings.get(i);
            p.isReady = false;
        }
    }

    public void changeSetting(long money) {
        List<? extends SimplePlayer> playings = getNewPlayings();
        List<? extends SimplePlayer> waitings = getNewWaitings();
        if (playings != null) {
            int playingSize = playings.size();
            for (int i = 0; i < playingSize; i++) {
                SimplePlayer player = playings.get(i);
                if (player != null) {
                    //this is an old version we should send cancel message to him
                    player.moneyForBet = money;
                }

            }
        } else {
            return;
        }

        if (waitings != null) {
            int waitingSize = waitings.size();
            for (int i = 0; i < waitingSize; i++) {
                SimplePlayer player = waitings.get(i);
                if (player != null) {
                    //this is an old version we should send cancel message to him
                    player.moneyForBet = money;
                }
            }
        }
    }

    public void removeTable(Room room) {

        int size = 0;

        int playingSize = this.getNewPlayings().size();

        for (int i = 0; i < playingSize; i++) {
            if (!this.getNewPlayings().get(i).isOut) {
                size++;
            }
        }
    }

    public static long getTakeMoney(long cash, long takeMoney, long minimumMoney, long minMoney, long maxMoney) throws BusinessException {

        long getMoney = 0;

        if (takeMoney == 0) {
            if (cash > maxMoney && maxMoney != -1) {
                getMoney = maxMoney;
            } else {
                getMoney = cash;
            }
        } else {
            getMoney = takeMoney;
        }

        return getMoney;
    }

    public long validateMoney(long cash, long takeMoney, long minimumMoney, long minMoney, long maxMoney) throws BusinessException {

        if (takeMoney == 0) {
            if (cash > maxMoney && maxMoney != -1) {
                takeMoney = maxMoney;
            } else {
                takeMoney = cash;
            }
        }
        //if(this.room.getZoneID() != ZoneID.XOC_DIA){
        if (takeMoney > cash || cash == 0) {
            throw new BusinessException("Bạn không đủ tiền để tham gia chơi");
        }

        if (takeMoney < minimumMoney) {
            throw new BusinessException(
                    "Bạn không đủ tiền để tham gia. Số tiền bạn có nhỏ hơn " + minimumMoney + " " + Server.MONEY_TYPE);
        }

        // maxMoney == -1 khong gioi han so tien mang vao
        if (maxMoney > 0 && takeMoney > maxMoney) {
            throw new BusinessException(
                    "Số tiền bạn mang vào bàn không được lớn hơn " + maxMoney + " " + Server.MONEY_TYPE);
        }
        //}
        return takeMoney;

    }

    public void supRemOldVer(long newOwner, int protocolSupport) {
        List<? extends SimplePlayer> playings = getNewPlayings();
        List<? extends SimplePlayer> waitings = getNewWaitings();
        List<SimplePlayer> lstOldVersion = new ArrayList<SimplePlayer>();
        List<SimplePlayer> removePlayers = new ArrayList<SimplePlayer>();

        if (playings != null) {
            int playingSize = playings.size();
            for (int i = 0; i < playingSize; i++) {
                SimplePlayer player = playings.get(i);
//                if(player.currentSession != null && player.currentSession.getByteProtocol()< AIOConstants.PROTOCOL_MODIFY_MID)

                if (player.currentSession != null && player.currentSession.getByteProtocol() < protocolSupport) {
                    //this is an old version we should send cancel message to him
                    lstOldVersion.add(player);
                }

                if (player.isOut || player.notEnoughMoney()) {
                    removePlayers.add(player);
                }

            }
        } else {
            return;
        }

        if (waitings != null) {
            int waitingSize = waitings.size();
            for (int i = 0; i < waitingSize; i++) {
                SimplePlayer player = waitings.get(i);
                if (player.currentSession != null && player.currentSession.getByteProtocol() < AIOConstants.PROTOCOL_BETA
                        && !player.isOut) {
                    //this is an old version we should send cancel message to him
                    lstOldVersion.add(player);
                }
            }
        }

        int oldSize = lstOldVersion.size();
        int removeSize = removePlayers.size();

        if (removeSize == 0 || oldSize == 0) {
            return;
        }

        ISession notNullSession = getNotNullSession();
        if (notNullSession == null) {
            notNullSession = owner.currentSession;
        }

        MessageFactory msgFactory = notNullSession.getMessageFactory();

        for (int i = 0; i < removeSize; i++) {
            SimplePlayer removedPlayer = removePlayers.get(i);
            CancelResponse removeRes = (CancelResponse) msgFactory.getResponseMessage(MessagesID.MATCH_CANCEL);
            removeRes.setSuccess(ResponseCode.SUCCESS, removedPlayer.id);
            removeRes.newOwner = newOwner;
            for (int j = 0; j < oldSize; j++) {
                SimplePlayer oldVerPlayer = lstOldVersion.get(j);
                sendMsg(oldVerPlayer, removeRes);
            }
        }
    }

//    public void validateSizeStartGame() throws BusinessException {
//    	
//    	// Kiem tra tong so nguoi choi
//        int playingSize = this.getNewPlayings().size();        
//        mLog.debug(" Plaing size " + playingSize);
//        
//        if(playingSize == 1) {
//      	    this.isPlaying = false;
//        	throw new BusinessException(Messages.NONE_EXISTS_PLAYER);
//        	
//        } else if (playingSize == 2) {	        	
//        	// check ready
//        	for (int i = 0; i < playingSize; i++) {
//                 SimplePlayer player = this.getNewPlayings().get(i);                
//                 if (player.id != this.owner.id && !player.isReady) {
//               	      this.isPlaying = false;
//                	  throw new BusinessException(Messages.NOT_ALL_PLAYER_READY);
//                 }
//            }        
//        } else if (playingSize > 2) {
//    		// flag all ready
//        	// co it nhat 2 nguoi ready duoi chu ban ra ngoai
//        	// nguoi ko san sang se ngoi xem
//    		int readyCount = this.getReadySize();    	    		
//    		if(readyCount >= 1) {            		        			
//            	for (int i = 0; i < playingSize; i++) {
//            		SimplePlayer player = this.getNewPlayings().get(i);
//                    if (!player.isReady && player.id != owner.id) {
//                    	this.getNewWaitings().add(new SimplePlayer());
//                    }
//                }                               	
//            	for (int i = 0; i < getNewWaitings().size(); i++) {
//                    this.getNewPlayings().remove(getNewWaitings().get(i));
//                }                	
//        	} else {            		
//        		this.isPlaying = false;
//          	  	throw new BusinessException(Messages.NOT_ALL_PLAYER_READY);
//        	}
//        }            	    
//    }
    public int getReadySize() {

        int readyCount = 0;

//    	this.getNewPlayings().addAll(this.getNewWaitings());    	
        for (int i = 0; i < this.getNewPlayings().size(); i++) {
            SimplePlayer player = this.getNewPlayings().get(i);
            if (player.isReady && player.id != owner.id) {
                readyCount++;
            }
        }

        return readyCount;
    }

    public void removeOwner() throws ServerException, JSONException, BusinessException {

        if (this.owner != null) {

            ISession session = this.owner.currentSession;

            if (session == null) {
                return;
            }

            IResponsePackage responsePkg = session.getDirectMessages();
            MessageFactory msgFactory = session.getMessageFactory();
            IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
            CancelRequest request = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
            request.mMatchId = this.matchID;
            business.handleMessage(session, request, responsePkg);

            // remove
            this.getNewPlayings().remove(owner.id);
        }
    }

    // Poker, Xito, Lieng
    public void checkEnoughMoney() throws ServerException, JSONException, BusinessException {

        List<SimplePlayer> removedPlayer = new ArrayList<SimplePlayer>();

        for (int i = 0; i < this.getNewPlayings().size(); i++) {
            SimplePlayer player = this.getNewPlayings().get(i);
//            mLog.debug(" player money " + player.username + "  " + player.cash);
            if (player.notEnoughMoney()) {
                removedPlayer.add(player);
            }
        }

        for (int i = 0; i < this.getNewWaitings().size(); i++) {
            SimplePlayer player = this.getNewWaitings().get(i);
            if (player.notEnoughMoney()) {
                removedPlayer.add(player);
            }
        }

        // kick out playings list
        for (int i = 0; i < removedPlayer.size(); i++) {
            SimplePlayer player = removedPlayer.get(i);

            // forwarding join request
            try {
                IResponsePackage responsePkg = this.getNotNullSession().getDirectMessages();
                MessageFactory msgFactory = this.getNotNullSession().getMessageFactory();
                IBusiness business = msgFactory.getBusiness(MessagesID.MATCH_CANCEL);
                CancelRequest request = (CancelRequest) msgFactory.getRequestMessage(MessagesID.MATCH_CANCEL);
                request.mMatchId = this.matchID;
                business.handleMessage(player.currentSession, request, responsePkg);
            } catch (Exception ex) {
                mLog.equals(" Remove user error matchId " + this.matchID + " player " + player.id + " " + ex.getMessage());
            }
        }

        for (int i = 0; i < removedPlayer.size(); i++) {
            this.getNewPlayings().remove(removedPlayer.get(i));
        }
    }

    public void setReadyStartActive() {

        if (this.isPlaying) {
            return;
        }

        if (this.getReadySize() == (this.getNewPlayings().size() - 1) && this.getNewPlayings().size() > 2) {
            if (this.lastReadyStartActive == 0) {
                this.lastReadyStartActive = System.currentTimeMillis();
            }
        }

        if (this.getReadySize() == 1 && this.lastReadyStartActive > 0) {
            this.lastReadyStartActive = 0;
        }
    }

    public void setMatchPlayingListView() {
        matchPlayingList = "";
        StringBuilder playerData = new StringBuilder();
        String keySpace = "";

        for (int j = 0; j < this.getNewPlayings().size(); j++) {
            SimplePlayer player = this.getNewPlayings().get(j);
            //playerData.append(keySpace + "(" + player.id + ") " + player.username);
            playerData.append(keySpace + " " + player.viewname);
            keySpace = "; ";
        }

        matchPlayingList = playerData.toString();
    }

    public static void checkPlayInMatch(long uid, long newMatchId) throws BusinessException {

        int currentZone = 0;
        long currentMatchId = 0;
        String alertMessage = "";
        int tableIndex = 0;
        String zoneName = "";
        boolean isPlaying = false;

        try {
            // Kiem tra xem co dang ton tai trong ban choi game nao khong
            SimpleTable table = SimpleTable.findUserInTable(uid);

            if (table == null) {
                return;
            }

            currentMatchId = table.getMatchID();
            tableIndex = table.getTableIndex();
            //        	currentZone    = table.getNotNullSession().getCurrentZone();        	
            //        	zoneName	   = ZoneID.getZoneName(currentZone); c        	        		
            isPlaying = table.isPlayerPlaying(uid);
            zoneName = ZoneID.getZoneName(table.room.getZone().getZoneId());

//        	if(!isPlaying) {        		        		
//        		// cancel table
//        		SimplePlayer player = table.findPlayer(uid);
//        		
//        		if(player.currentSession == null) {
//	        		 table.removePlayerNullSession(uid);
//        		}        		
//        		else {
//        			IBusiness business =  player.currentSession.getMessageFactory().getBusiness(MessagesID.MATCH_CANCEL);
//        		 	CancelRequest rqMatchCancel = (CancelRequest)  player.currentSession.getMessageFactory().getRequestMessage(MessagesID.MATCH_CANCEL);
//        	        rqMatchCancel.uid = uid;
//        	        rqMatchCancel.mMatchId = currentMatchId;
//        	        rqMatchCancel.isSendMe = false;          	        
//        	        try {
//        	            business.handleMessage(player.currentSession, rqMatchCancel, player.currentSession.getDirectMessages());
//        	        } catch (ServerException se) {
//        	    		mLog.error("cancelTable " +  se.getCause());
//        	        } catch (Throwable e) {
//        	    		mLog.error("cancelTable " +  e.getCause());
//        	        }	    	                	        
//        		}        		
//        	}     
        } catch (Exception ex) {
            mLog.equals(" Join check Match error " + ex.getMessage());
        }

        mLog.debug(" Current matchId " + currentMatchId + " is playing " + isPlaying);

        if (currentMatchId > 0 && (newMatchId == 0 || currentMatchId != newMatchId) && tableIndex > 0) {
            alertMessage = "Bạn đang chơi dở game " + zoneName + " bàn số " + tableIndex + ", vui lòng vào lại bàn chơi tiếp, tránh bị trừ tiền nếu thua!";
            throw new BusinessException(alertMessage);
        }

    }

    public double caculatorPercent(int cuoc) {
        double percent = 0.1;
//        switch(cuoc){
//            case 100:
//                percent = 0.1;
//                break;
//            case 200:
//                percent = 0.2;
//                break; 
//            case 500:
//                percent = 1;
//                break;
//            case 1000:
//                percent = 2;
//                break;
//            case 2000:
//                percent = 3;
//                break;
//            case 5000:
//                percent = 5;
//                break;
//            case 10000:
//                percent = 10;
//                break; 
//            case 20000:
//                percent = 20;
//                break;    
//            case 30000:
//                percent = 30;
//                break; 
//            case 50000:
//                percent = 50;
//                break;     
//        }
//rubvip
        switch (cuoc) {
            case 500:
                percent = 0.1;
                break;
            case 1000:
                percent = 0.2;
                break;
            case 2000:
                percent = 0.4;
                break;
            case 5000:
                percent = 1;
                break;
            case 10000:
                percent = 2;
                break;
            case 20000:
                percent = 4;
                break;
            case 50000:
                percent = 10;
                break;
            case 100000:
                percent = 20;
                break;
            case 200000:
                percent = 30;
                break;
            case 300000:
                percent = 40;
                break;
            case 500000:
                percent = 50;
                break;
            case 1000000:
                percent = 80;
                break;
        }
        return percent;

    }
}
