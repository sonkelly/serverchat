package vn.game.room;

import allinone.data.AIOConstants;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import tools.CacheMatch;
import vn.game.common.LoggerContext;
import vn.game.db.DBException;
import allinone.data.Couple;
import allinone.data.SimpleTable;
import allinone.data.ZoneID;
import allinone.databaseDriven.DatabaseDriver;
import allinone.databaseDriven.RoomDB;
import allinone.logger.LogManager;
import allinone.room.NRoomEntity;
import vn.game.session.ISession;

public class Zone {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(Zone.class);

    public static final long UNSPECIFIED_ROOM_ID = -1L;
    public static final int JOIN_UNLIMITED = -1;
    private int mRoomCapacity = -1;
    private int mPlayerSize = -1;
    private final ConcurrentHashMap<Long, Room> mRooms;
    private final Vector<Long> mRoomIds;

    @SuppressWarnings("unused")
    private ZoneManager.IdRoomGenerator mIdGenerator;
    private int mZoneId;
    private String mZoneName;
    private int mJoinLimited;
    private static boolean isIncreasing = false;

    private static LogManager loggers;

    public static LogManager getLoggers() {
        return loggers;
    }

    public static allinone.logger.Logger getLogger(String fileName) {
        return loggers.getLogger(fileName);
    }

    @SuppressWarnings("static-access")
    public Zone() {
        this.mRooms = new ConcurrentHashMap<>();
        this.mRoomIds = new Vector<>();
        this.loggers = new LogManager();
    }

    void setIdRoomGenerator(ZoneManager.IdRoomGenerator aIdGenerator) {
        this.mIdGenerator = aIdGenerator;
    }

    public void setRoomCapacity(int aRoomCapacity) {
        this.mRoomCapacity = aRoomCapacity;
    }

    public void setPlayerSize(int aPlayerSize) {
        this.mPlayerSize = aPlayerSize;
    }

    void setZoneId(int aZoneId) {
        this.mZoneId = aZoneId;
    }

    public int getZoneId() {
        return this.mZoneId;
    }

    void setZoneName(String aZoneName) {
        this.mZoneName = aZoneName;
    }

    public String getZoneName() {
        return this.mZoneName;
    }

    void setJoinLimited(int aJoinLimited) {
        this.mJoinLimited = aJoinLimited;
    }

    public int getJoinLimited() {
        return this.mJoinLimited;
    }

    public Room findRoom(long aRoomId) {
        synchronized (this.mRooms) {
            return ((Room) this.mRooms.get(Long.valueOf(aRoomId)));
        }
    }

    public Room createRoom(String des, long id, int phong, int tableIndex) {
        // synchronized (this.mRooms) {
        Room newRoom = new Room(this.mRoomCapacity, this.mPlayerSize, this);
        try {
            DatabaseDriver db = new DatabaseDriver();
            long roomId = db.logMatch(id, phong, tableIndex, mZoneId);
            newRoom.setRoomId(roomId);
            newRoom.phongID = phong;

            addRoom(roomId, newRoom);
        } catch (Exception e) {
            //e.printStackTrace();
            mLog.debug("createRoom error");
        }
        return newRoom;
        // }
    }

    public Room createRoom(String des, long id, int phong, int tableIndex, String realmoney) {
        // synchronized (this.mRooms) {
        Room newRoom = new Room(this.mRoomCapacity, this.mPlayerSize, this);
        try {
            DatabaseDriver db = new DatabaseDriver();
            long roomId = db.logMatch(id, phong, tableIndex, mZoneId);
            newRoom.setRoomId(roomId);
            newRoom.phongID = phong;
            if (realmoney.equals(AIOConstants.PRIFIX_REALMONEY)) {
                newRoom.setRealMoney(1);
//                newRoom.
            } else {
                newRoom.setRealMoney(0);
            }
            mLog.debug("newRoom.getRealMoney():" + newRoom.getRealMoney());
            addRoom(roomId, newRoom);
        } catch (Exception e) {
            //e.printStackTrace();
            mLog.debug("createRoom error");
        }
        return newRoom;
        // }
    }

    void addRoom(long aRoomId, Room aRoom) {

        Phong ph = this.getPhong(aRoom.getPhongID());
        ph.createTable(aRoom);

        synchronized (this.mRooms) {
            // deleteRoom(aRoom);

            this.mRooms.put(Long.valueOf(aRoomId), aRoom);
            this.mRoomIds.add(Long.valueOf(aRoomId));
        }
    }

    public void deleteRoom(Room aRoom) {
        // BinhLT
        // deleteIndex(aRoom.getZoneID(), aRoom.phongID, aRoom.index);
        /*
		 * createOrDeleteTable(aRoom.phongID, aRoom.getRoomId(),
		 * aRoom.getIndex(), false);
         */
        //
        Phong ph = this.getPhong(aRoom.phongID);
        if (ph != null) {
            ph.deleteRoom(aRoom);
        }
        // mLog.debug("[Zone]: " + aRoom.getZoneID() + " delete Table "
        // + aRoom.index + " in Room " + aRoom.phongID + " has matchID "
        // + aRoom.getRoomId());
        if ((aRoom != null) && (!(aRoom.isPermanent()))) {

            // if (aRoom.getAttactmentData() != null)
            // aRoom.getAttactmentData().destroy();
            long roomId = aRoom.getRoomId();

            synchronized (this.mRooms) {
                this.mRooms.remove(Long.valueOf(roomId));
                this.mRoomIds.remove(new Long(roomId));
            }

            // if (aRoom.getAttactmentData() instanceof ChanTable) {
            // ChanTable chanT = (ChanTable) aRoom.getAttactmentData();
            // chanT.clearTableWhileDestroyed();
            // }
            CacheMatch.delete(roomId);

        }
    }

    public int getNumPlaylingRoom() {
        int count = 0;

        Enumeration<Room> eRooms;
        synchronized (this.mRooms) {
            eRooms = this.mRooms.elements();
        }

        while ((eRooms != null) && (eRooms.hasMoreElements())) {
            Room room = (Room) eRooms.nextElement();
            long roomStatus = room.getStatus();
            if (roomStatus == 1L) {
                ++count;
            }

        }
        return count;
    }

    /**
     *
     * @param zoneID -- id tienlen zone,...
     * @return -- rooms are playing
     */
    public ArrayList<Vector<RoomEntity>> dumpPlayingRooms(int zoneID) {
        ArrayList<Vector<RoomEntity>> arr = new ArrayList<Vector<RoomEntity>>();

        Vector<RoomEntity> roomEntities = new Vector<>();
        Vector<RoomEntity> roomEntitiesNotPlay = new Vector<>();
        // mLog.debug("RoomSize:" + mRooms.size());

        for (int i = 0; i < this.mRooms.size(); i++) {
            try {
                long roomId = ((Long) this.mRoomIds.get(i)).longValue();
                // Room room = findRoom(roomId); NamNT try to reduce conflict
                // synchronic
                Room room = (Room) this.mRooms.get(roomId);

                /*
				 * if ((room.getStatus() == 1L) && (room.getZoneID() == zoneID))
				 * { roomEntities.add(room.dumpRoom()); }
                 */
                if (room != null && room.getZoneID() == zoneID) {
                    SimpleTable table = room.getAttactmentData();
                    if (table != null && table.isPlaying) {
                        // mLog.debug("Table:" + table.matchID);
                        roomEntities.add(room.dumpRoom());
                    } else if (table != null && !table.isPlaying) {
                        roomEntitiesNotPlay.add(room.dumpRoom());
                    }
                }
            } catch (Exception ex) {
                mLog.error("dump playing room error " + ex.getMessage(), ex);
            }
        }
        arr.add(roomEntities);
        arr.add(roomEntitiesNotPlay);
        return arr;
    }

    public Vector<RoomEntity> dumpRooms(int zoneID) {
        Vector<RoomEntity> roomEntities = new Vector<>();
        for (int i = 0; i < this.mRooms.size(); i++) {
            try {
                long roomId = ((Long) this.mRoomIds.get(i)).longValue();
                Room room = (Room) this.mRooms.get(roomId);
                if (room != null && room.getZoneID() == zoneID) {
                    roomEntities.add(room.dumpRoom());
                }
            } catch (Exception ex) {
                mLog.error("dump playing room error " + ex.getMessage(), ex);
            }
        }
        return roomEntities;
    }

    public Vector<RoomEntity> dumpNewPlayingRooms(int zoneID) {
        Vector<RoomEntity> roomEntities = new Vector<>();
        // mLog.debug("RoomSize:" + mRooms.size());
        Enumeration<Room> eRooms = mRooms.elements();
        while (eRooms.hasMoreElements()) {
            Room room = eRooms.nextElement();
            if (room != null && room.getZoneID() == zoneID) {
                // SimpleTable table = room.getAttactmentData();
                // if (table.isPlaying) {
                // mLog.debug("Table:" + table.matchID);
                roomEntities.add(room.dumpRoom());
                // }
            }
        }
        return roomEntities;
    }

    public Vector<RoomEntity> dumpPlayingRooms(int aOffset, int aLength, int zoneID) {
        Vector<RoomEntity> roomEntities = new Vector<>();

        // synchronized (this.mRooms) {
        int loopIdx = (aOffset >= 0) ? aOffset : 0;

        int numRooms = this.mRooms.size();

        int results = 0;
        while (true) {
            if ((loopIdx >= numRooms) || (results >= aLength)) {
                break;
            }

            long roomId = ((Long) this.mRoomIds.get(loopIdx)).longValue();

            Room room = findRoom(roomId);

            if ((room.getStatus() == 1L) && (room.getZoneID() == zoneID)) {
                roomEntities.add(room.dumpRoom());

                ++results;
            }

            ++loopIdx;
        }
        // }

        return roomEntities;
    }

    public int getNumWaitingRoom() {
        int count = 0;

        Enumeration<Room> eRooms;
        // synchronized (this.mRooms) {
        eRooms = this.mRooms.elements();
        // }

        while ((eRooms != null) && (eRooms.hasMoreElements())) {
            Room room = (Room) eRooms.nextElement();
            long roomStatus = room.getStatus();
            if (roomStatus == 2L) {
                ++count;
            }

        }

        return count;
    }

    public Vector<RoomEntity> dumpWaitingRooms(int aOffset, int aLength) {
        Vector<RoomEntity> roomEntities = new Vector<>();

        // synchronized (this.mRooms) {
        int loopIdx = (aOffset >= 0) ? aOffset : 0;

        int numRooms = this.mRooms.size();

        int results = 0;
        while (true) {
            if ((loopIdx >= numRooms) || (results >= aLength)) {
                break;
            }

            long roomId = ((Long) this.mRoomIds.get(loopIdx)).longValue();

            Room room = findRoom(roomId);

            if (room.getStatus() == 2L) {
                roomEntities.add(room.dumpRoom());

                ++results;
            }

            ++loopIdx;
        }
        // }

        return roomEntities;
    }

    public Vector<RoomEntity> dumpWaitingRooms(int aOffset, int aLength, int aLevel, int minLevel, int zoneID) {
        Vector<RoomEntity> roomEntities = new Vector<>();

        // synchronized (this.mRooms) {
        int loopIdx = (aOffset >= 0) ? aOffset : 0;
        // int numRooms = this.mRooms.size();

        // System.out.println("Dump waiting : " + numRooms);
        int results = 0;
        while (true) {
            if ((loopIdx >= this.mRooms.size()) || (results >= aLength)) {
                break;
            }

            long roomId = ((Long) this.mRoomIds.get(loopIdx)).longValue();

            Room room = findRoom(roomId);

            if (room.getNumOnline() == 0) {
                try {
                    mLog.error("Room : " + room.getName() + " is Empty. ; roomID : " + room.getRoomId() + "; playingSize : " + room.playingSize());
                    room.allLeft();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ((room.getLevel() <= aLevel) && (room.getNumOnline() > 0) && (room.getLevel() >= minLevel) && (room.getZoneID() == zoneID)) {
                roomEntities.add(room.dumpRoom());

                ++results;
            }

            ++loopIdx;
        }
        // }

        return roomEntities;
    }

    public Vector<RoomEntity> dumpWaitingRooms() {
        Vector<RoomEntity> roomEntities = new Vector<>();

        // synchronized (this.mRooms) {
        for (int i = 0; i < this.mRooms.size(); i++) {
            long roomId = ((Long) this.mRoomIds.get(i)).longValue();
            Room room = findRoom(roomId);

            /*
			 * if ((room.getStatus() == 1L) && (room.getZoneID() == zoneID)) {
			 * roomEntities.add(room.dumpRoom()); }
             */
            if (!room.isPlaying() || room.getStatus() == 2L) {
                roomEntities.add(room.dumpRoom());
            }

        }

        // }
        return roomEntities;
    }

    // Binhlt
    public boolean deleteAllRoomByOwner(String owner) {
        synchronized (this.mRooms) {
            Enumeration<Long> keys = this.mRooms.keys();
            while (keys.hasMoreElements()) {
                long k = keys.nextElement();
                Room room = this.mRooms.get(Long.valueOf(k));
                if (owner.toLowerCase().equalsIgnoreCase(room.getOwnerName().toLowerCase())) {
                    room.allLeft();
                }
            }
        }
        return true;
    }

    // public SimpleTable findTalbeByUserId(long userId) {
    //
    // Enumeration<Long> keys;
    // keys = this.mRooms.keys();
    //
    // while (keys.hasMoreElements()) {
    // long k = keys.nextElement();
    // Room room = this.mRooms.get(Long.valueOf(k));
    //
    // if (owner.toLowerCase()
    // .compareTo(room.getOwnerName().toLowerCase()) == 0) {
    // return room;
    // }
    // }
    //
    // return null;
    // }
    public Room findRoomByOwner(String owner) {
        // NamNT
        // fix error because if there are 2 users: nam1 and nam11, when nam11
        // create zoom it will kick out nam1
        Enumeration<Long> keys;

        keys = this.mRooms.keys();

        while (keys.hasMoreElements()) {
            long k = keys.nextElement();
            Room room = this.mRooms.get(Long.valueOf(k));

            if (owner.toLowerCase().compareTo(room.getOwnerName().toLowerCase()) == 0) {
                return room;
            }
        }

        return null;
    }

    public List<SimpleTable> dumpWaitingTables(int phongId) {
        ArrayList<SimpleTable> tables = new ArrayList<>();

        for (long rId : this.mRoomIds) {
            Room room = findRoom(rId);
            if ((room.getPhongID() == phongId) && (room.getNumOnline() > 0)) {
                tables.add(room.getAttactmentData());
            }
        }

        return tables;
    }

    public List<SimpleTable> dumpNewWaitingTables(int phongId) {
        ArrayList<SimpleTable> tables = new ArrayList<>();

        Phong phong = this.getPhong(phongId);
        if (phong != null) {
            List<Room> deleteTables = new ArrayList<>();
            for (Room room : phong.getRooms()) {
                if (room != null) {
                    SimpleTable table = room.getAttactmentData();
                    if (table.getTableSize() == 0) {
                        mLog.error("error table(fix bug table)" + table.matchID);
                        deleteTables.add(room);
                    }

                    tables.add(room.getAttactmentData());
                }
            }

            int deleteSize = deleteTables.size();
            for (int i = 0; i < deleteSize; i++) {
                phong.deleteRoom(deleteTables.get(i));
            }
        }

        return tables;
    }

    public List<SimpleTable> dumpAllTablesOfPhongTest(int phongId) {
        ArrayList<SimpleTable> tables = new ArrayList<>();
        ArrayList<Integer> moneys = new ArrayList<Integer>();

        moneys.add(new Integer(100));
        moneys.add(new Integer(200));
        moneys.add(new Integer(500));
        moneys.add(new Integer(1000));
        moneys.add(new Integer(2000));
        moneys.add(new Integer(5000));
        moneys.add(new Integer(10000));
        moneys.add(new Integer(20000));
        moneys.add(new Integer(50000));

        for (int i = 0; i < 2; i++) {
            SimpleTable table = new SimpleTable();
            int money = new Random().nextInt(9);
            table.firstCashBet = moneys.get(money);
            table.setTableIndex(i + 1);
            table.setMaximumPlayer(4);
            table.isPlaying = true;
            table.matchID = table.tableIndex + 100000;
            table.name = "" + table.getTableIndex();
            tables.add(table);
        }

        return tables;
    }

    public List<SimpleTable> dumpAllTablesOfPhong(int phongId) {
        ArrayList<SimpleTable> tables = new ArrayList<>();
        Phong phong = this.getPhong(phongId);
        if (phong != null) {
            for (Room room : phong.getRooms()) {
                if (room != null) {
                    tables.add(room.getAttactmentData());
                }
            }
        }
        return tables;
    }

    public Couple<Integer, Long> fastPlay(long lastFastMatchId, long maxCashBet, ISession aSession) throws Exception, SQLException, DBException {
        try {
            // ArrayList<Integer> iDs = getRoomSameLevel(room);
            // for (int roomID : iDs) {
            // Hashtable<Integer, Long> temp = tableInRoom.get(roomID);
            // Collection<Phong> phongs = this.mPhongs.values();
            //List<Phong> lstPhongs = new ArrayList<>(this.mPhongs.values());
            //mLog.debug("aSession.getRealMoney():" + aSession.getRealMoney());
            List<Phong> lstPhongs = this.getListPhongByMoneyType(aSession.getRealMoney());

            // Enumeration<Integer> keys = temp.keys();
            int lastIndex = 0;
            long foundMatchId = 0;
            long foundBetMoney = 0;
            int foundIndex = 0;
            int phongSize = lstPhongs.size();
            int phongSize1 = phongSize - 1;
            isIncreasing = !isIncreasing;
            boolean flag = isIncreasing;
            int foundTimes = 0;

            for (int i = phongSize1; i > -1; i--) {
                // for (Phong p : phongs) {
                // int k = keys.nextElement();

                Phong p;

                if (flag) {
                    p = lstPhongs.get(phongSize1 - i);
                } else {
                    p = lstPhongs.get(i);
                }
                // Room[] rooms = (Room[])p.getRooms().toArray();
                List<Room> rooms = new ArrayList<>(p.getRooms());
                int roomSize = rooms.size();
                //mLog.debug("roomSize:" + roomSize);
                for (int j = 0; j < roomSize; j++) {
                    // long matchID = temp.get(k);
                    // Room r = this.findRoom(matchID);
                    Room r = rooms.get(j);
                    if (r == null) {
                        continue;
                    }

                    long matchID = r.getRoomId();
                    int k = r.index;

                    SimpleTable t = (SimpleTable) r.getAttactmentData();
                    switch (mZoneId) {
                       
                        default: {
                            if (!t.isPlaying && !t.isFullTable() && t.firstCashBet <= maxCashBet) {
                                if (matchID == lastFastMatchId) {
                                    lastIndex = k;
                                    continue;
                                } else {
                                    if (foundBetMoney < t.firstCashBet && aSession.getRealMoney().equals(t.getRealMoney())) {
                                        foundBetMoney = t.firstCashBet;
                                        foundMatchId = t.matchID;
                                        foundIndex = k;
                                        foundTimes++;
                                        if (foundTimes > 2) {
                                            return new Couple<>(foundIndex, foundMatchId);
                                        }
                                    }
                                }
                                // return new Couple<Integer, Long>(k, matchID);
                            }

                            break;
                        }

                        // default:
                        // break;
                    }
                }
            }
//            mLog.debug("foundIndex:"+foundIndex);
//            mLog.debug("lastIndex:"+lastIndex);
            if (foundIndex > 0) {
                return new Couple<>(foundIndex, foundMatchId);
            }

            if (lastIndex > 0) {
                // NamNT there 's only one free table

                return new Couple<>(lastIndex, lastFastMatchId);
            }
            // }
            //mLog.debug("vaoday");
            return null;
        } catch (Exception e) {
            //mLog.debug("error"+e.getMessage());
            throw new Exception("Không tìm được bàn chơi!");
            
        }

    }

    @SuppressWarnings("unused")
    public long botfastPlay(long maxCashBet) {

        // ArrayList<Integer> iDs = getRoomSameLevel(room);
        // for (int roomID : iDs) {
        // Hashtable<Integer, Long> temp = tableInRoom.get(roomID);
        // Collection<Phong> phongs = this.mPhongs.values();
        List<Phong> lstPhongs = new ArrayList<>(this.mPhongs.values());
        // Enumeration<Integer> keys = temp.keys();
        int lastIndex = 0;
        long foundMatchId = 0;
        long foundBetMoney = 0;
        int foundIndex = 0;
        int phongSize = lstPhongs.size();
        int phongSize1 = phongSize - 1;
        // isIncreasing = !isIncreasing;
        // boolean flag = isIncreasing;
        int foundTimes = 0;

        for (int i = phongSize1; i > -1; i--) {
            // for (Phong p : phongs) {
            // int k = keys.nextElement();

            Phong p;

            // if(flag)
            // {
            // p =lstPhongs.get(phongSize1 -i);
            // }
            // else
            // {
            p = lstPhongs.get(i);
            // }
            // Room[] rooms = (Room[])p.getRooms().toArray();
            List<Room> rooms = new ArrayList<>(p.getRooms());
            int roomSize = rooms.size();

            for (int j = 0; j < roomSize; j++) {
                // long matchID = temp.get(k);
                // Room r = this.findRoom(matchID);
                Room r = rooms.get(j);
                if (r == null) {
                    continue;
                }

                long matchID = r.getRoomId();
                int k = r.index;

                SimpleTable t = (SimpleTable) r.getAttactmentData();
                if (t.isKickoutBot) {
                    continue;
                }

                switch (mZoneId) {
                    // case ZoneID.XOC_DIA:
                    // case ZoneID.NEW_BA_CAY:
                    // case ZoneID.STAR_WAR:
                    // case ZoneID.BAU_CUA_TOM_CA:
                    // case ZoneID.XITO:
                    // case ZoneID.LINE_ONLINE:
                    // case ZoneID.PIKACHU:
                    // case ZoneID.GEM_ONLINE:
                    // case ZoneID.AILATRIEUPHU:
                    default: {
                        if (!t.isPlaying && !t.isFullTable() && t.firstCashBet <= maxCashBet && (t.getTableSize() == 1) && !t.hasBotUser()) {
                            // NamNT check last fast matchId 2 times dont want to
                            // came the same room
                            // if(matchID == lastFastMatchId)
                            // {
                            // lastIndex = k;
                            // continue;
                            // }
                            // else
                            // {
                            if (foundBetMoney < t.firstCashBet) {
                                foundBetMoney = t.firstCashBet;
                                foundMatchId = t.matchID;
                                foundIndex = k;
                                foundTimes++;
                                if (foundTimes > 2) {
                                    return foundMatchId;
                                }
                            }
                        }
                        // return new Couple<Integer, Long>(k, matchID);
                        break;
                    }

                    // default:
                    // break;
                }
            }
        }

        if (foundIndex > 0) {
            return foundMatchId;
        }

        // if(lastIndex > 0)
        // {
        // //NamNT there 's only one free table
        //
        // return new Couple<Integer, Long>(lastIndex, lastFastMatchId);
        // }
        // }
        return 0;

    }

    // RoomID --> (TableIndex -->MatchID)
    private final ConcurrentHashMap<Integer, Phong> mPhongs = new ConcurrentHashMap<>();

    // public Hashtable<Integer, Hashtable<Integer, Long>> tableInRoom = new
    // Hashtable<Integer, Hashtable<Integer, Long>>();
    public Phong getPhong(int index) {
        try {
            return this.mPhongs.get(index);
        } catch (Exception e) {
            mLog.error("getPhong error" + e.getMessage());
            return null;
        }
    }

    public List<Phong> getListPhongByMoneyType(String isRealMoney) {
        List<Phong> lstPhongs = new ArrayList<>(this.mPhongs.values());
        List<Phong> lstPhongsRealMoney = new ArrayList<>();
        List<Phong> lstPhongsMoney = new ArrayList<>();
        int phongSize = lstPhongs.size();
        for (int i = 0; i < phongSize; i++) {
            Phong phong = lstPhongs.get(i);
            if (phong.getMoneyType() == 0) {
                lstPhongsMoney.add(phong);
            } else {
                lstPhongsRealMoney.add(phong);
            }
        }
        if (isRealMoney.equals(AIOConstants.PRIFIX_MONEY)) {
            lstPhongs = lstPhongsMoney;
        } else {
            lstPhongs = lstPhongsRealMoney;
        }
        return lstPhongs;
    }

    public Phong phongAvailable(String isRealMoney) {
        List<Phong> lstPhongs = new ArrayList<>(this.mPhongs.values());
        List<Phong> lstPhongsRealMoney = new ArrayList<>();
        List<Phong> lstPhongsMoney = new ArrayList<>();
        int phongSize = lstPhongs.size();
        for (int i = 0; i < phongSize; i++) {
            Phong phong = lstPhongs.get(i);
            if (phong.getMoneyType() == 0) {
                lstPhongsMoney.add(phong);
            } else {
                lstPhongsRealMoney.add(phong);
            }
        }
        if (isRealMoney.equals(AIOConstants.PRIFIX_MONEY)) {
            lstPhongs = lstPhongsMoney;
        } else {
            lstPhongs = lstPhongsRealMoney;
        }

        phongSize = lstPhongs.size();
        int availableIndex = 0;

        int playing = -1000;
        int phongId = 0;

        for (int i = 0; i < phongSize; i++) {
            Phong phong = lstPhongs.get(i);
            int tmpPlaying = phong.getPlaying();

            if (phong.level > 1 || tmpPlaying > 1000) {
                continue;
            }

            if (playing < tmpPlaying || (playing == tmpPlaying && phong.id > phongId)) {
                if (phong.getMoneyType() == 0) {
                    availableIndex = i;
                    playing = tmpPlaying;
                    phongId = phong.id;
                }

            }
        }

        return lstPhongs.get(availableIndex);
    }

    public Phong phongAvailableByLevel(int level, String isRealMoney) {
        List<Phong> lstPhongs = new ArrayList<>(this.mPhongs.values());
        List<Phong> lstPhongsRealMoney = new ArrayList<>();
        List<Phong> lstPhongsMoney = new ArrayList<>();
        int phongSize = lstPhongs.size();
        for (int i = 0; i < phongSize; i++) {
            Phong phong = lstPhongs.get(i);
            if (phong.getMoneyType() == 0) {
                lstPhongsMoney.add(phong);
            } else {
                lstPhongsRealMoney.add(phong);
            }
        }
        if (isRealMoney.equals(AIOConstants.PRIFIX_MONEY)) {
            lstPhongs = lstPhongsMoney;
        } else {
            lstPhongs = lstPhongsRealMoney;
        }
        int availableIndex = 0;
        int playing = -1000;
        int phongId = 0;
        for (int i = 0; i < phongSize; i++) {
            Phong phong = lstPhongs.get(i);
            int tmpPlaying = phong.getPlaying();

            if (tmpPlaying > 1000) {
                continue;
            }

            if (phong.level == level) {
                if (playing < tmpPlaying || (playing == tmpPlaying && phong.id > phongId)) {
                    availableIndex = i;
                    playing = tmpPlaying;
                    phongId = phong.id;
                }
            }
        }

        return lstPhongs.get(availableIndex);
    }

    public Phong phongAvailableByMinBet(long minBet, String isRealMoney) {
        List<Phong> lstPhongs = new ArrayList<>(this.mPhongs.values());
        List<Phong> lstPhongsRealMoney = new ArrayList<>();
        List<Phong> lstPhongsMoney = new ArrayList<>();
        int phongSize = lstPhongs.size();
        for (int i = 0; i < phongSize; i++) {
            Phong phong = lstPhongs.get(i);
            // mLog.debug("phongid:"+phong.id +"  phong.getMoneyType():"+phong.getMoneyType() + " isRealMoney:"+isRealMoney);
            if (phong.getMoneyType() == 0) {
                lstPhongsMoney.add(phong);
            } else {

                lstPhongsRealMoney.add(phong);
            }
        }
        if (isRealMoney.equals(AIOConstants.PRIFIX_MONEY)) {
            lstPhongs = lstPhongsMoney;
        } else {
            lstPhongs = lstPhongsRealMoney;
        }
        int availableIndex = 0;
        int playing = -1000;
        int phongId = 0;
        for (int i = 0; i < lstPhongs.size(); i++) {
            Phong phong = lstPhongs.get(i);
            int tmpPlaying = phong.getPlaying();

            if (tmpPlaying > 1000) {
                continue;
            }

            if (phong.money == minBet) {
                ///if (playing < tmpPlaying || (playing == tmpPlaying && phong.id > phongId)) {
                availableIndex = i;
                playing = tmpPlaying;
                phongId = phong.id;
                break;
                //}
            }
        }

        return lstPhongs.get(availableIndex);
    }

    public Collection<Phong> phongValues() {
        return this.mPhongs.values();
    }

    public Phong getFirstPhong() {
        for (Phong p : this.mPhongs.values()) {
            return p;
        }

        return null;
    }

    // private Hashtable<Integer, V>
    /*
	 * private ArrayList<Integer> getRoomSameLevel(int phongID) throws
	 * SQLException, DBException { ArrayList<Integer> res = new
	 * ArrayList<Integer>(); // res.add(phongID); RoomDB db = new RoomDB();
	 * List<NRoomEntity> rs = db.getRooms(mZoneId); int level = 0; for
	 * (NRoomEntity r : rs) { if (r.getId() == phongID) { level = r.getLv();
	 * break; } } for (NRoomEntity r : rs) { if (r.getLv() == level) {
	 * res.add(r.getId()); } } return res; }
     */
    public void initPhong(String isRealmoney) throws SQLException, DBException {
        RoomDB db = new RoomDB();
        List<NRoomEntity> rs = db.getRooms(mZoneId, isRealmoney);
        if (rs != null) {
            for (NRoomEntity r : rs) {
                int id = r.getId();
                Phong p = new Phong(id, this.mZoneId, r.getLv(), this, r.getMoneyType(), r.getMinCash());
                r.setPhong(p);
                //mLog.debug("this.mZoneId:"+this.mZoneId+" +p.id:"+p.id +" r.getMinCash():"+r.getMinCash());
                this.mPhongs.put(p.id, p);
            }
        }
    }

    public int numberOnline() {
        int res = 0;
        Enumeration<Phong> p = this.mPhongs.elements();

        while (p.hasMoreElements()) {
            Phong ph = p.nextElement();
            res += ph.numberOnline();
        }

        return res;
    }
}
