package com.allinone.vivu;

import java.util.ArrayList;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.session.ISession;
import allinone.business.BusinessException;
import allinone.data.Couple;

public class Area {

    private final Group[] mGroups;
    private String mName;
    private int mIndex;
    private int MAX_GROUP = 10;
    private City mCity;
    private int sceneSize = 10;//

    public int getSceneSize() {
        return sceneSize;
    }
    private int xSize = 100;

    public int getxSize() {
        return xSize;
    }
    private int ySize = 100;

    public int getySize() {
        return ySize;
    }
    private static final Logger mLog =
            LoggerContext.getLoggerFactory().getLogger(Area.class);

    public void broadcast(Object obj, long uid) throws ServerException {
        for (int i = 0; i < MAX_GROUP; i++) {
            Group g = mGroups[i];
            g.broadcast(obj, false, uid);
        }
    }

    public Scene getScene(Couple<Integer, Integer> pos) {
        //@ TODO:
        return null;
    }

    public int getMAX_GROUP() {
        return MAX_GROUP;
    }

    public City getmCity() {
        return mCity;
    }

    public int getIndex() {
        return mIndex;
    }

    public Group getGroup(int id) throws BusinessException {
        //synchronized (mGroups) {
            Group g = mGroups[id];
            if (g != null) {
                return g;
            } else {
                throw new BusinessException("Can not find Group:" + id);
            }
        //}

    }

    public String getmName() {
        return mName;
    }

	public ArrayList<Triple<Integer, Integer, Integer>> getGroupList() {
        ArrayList<Triple<Integer, Integer, Integer>> res =
                new ArrayList<Triple<Integer, Integer, Integer>>();
        for (int i = 0; i < MAX_GROUP; i++) {
            Group g = mGroups[i];
            res.add(new Triple<Integer, Integer, Integer>(g.getmGroupIndex(),
                    g.getEnteringSession(), g.getMAX_SESSION()));
        }
        return res;
    }

    public void join(ISession session, int groupID) throws BusinessException {
        Group g = mGroups[groupID];
        if (g.isFull()) {
            throw new BusinessException("Group is full");
        } else {
            g.join(session);
            session.setmGroup(g);
            mLog.debug(session.getUserName()
                    + " has joined into Group:" + g.getmGroupIndex());
        }
    }

    public void join(ISession session) throws BusinessException {
        for (int i = 0; i < MAX_GROUP; i++) {
            Group g = mGroups[i];
            if (!g.isFull()) {
                //g.join(session);
                session.setmGroup(g);
                mLog.debug(session.getUserName()
                        + " has joined into Group:" + g.getmGroupIndex());
                return;
            } else {
                continue;
            }
        }
        throw new BusinessException("All Group has full!");
    }

    public void initGroup() {
        mLog.debug(" Create new Groups!");
        for (int i = 0; i < MAX_GROUP; i++) {
            Group g = new Group(i, this);
            mGroups[i] = g;
        }
    }

    public Area(String name, int index, City city, int sceneS, int xS, int yS) {
        mGroups = new Group[MAX_GROUP];
        mName = name;
        mIndex = index;
        mCity = city;
        sceneSize = sceneS;
        xSize = xS;
        ySize = yS;
    }
}
