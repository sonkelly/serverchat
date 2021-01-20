/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;
import vn.game.memcache.IMemcacheClient;
import vn.game.memcache.MemcacheClientPool;
import allinone.data.Message;
import allinone.data.MessageEntity;
import allinone.data.UserEntity;
import allinone.data.UserInfoEntity;
import allinone.databaseDriven.MessageDB;
import allinone.databaseDriven.UserDB;
import allinone.databaseDriven.UserInfoDB;

/**
 *
 * @author mcb
 */
public class CacheUserInfo {

    //public static boolean isUseCache = true;
    public static boolean isUseCache = false;
    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CacheUserInfo.class);

    private static final String USER_NAMESPACE = "user";
    private static final String USER_ADD_INFO_NAMESPACE = "usrAI";
    //private static final String SERVER_LIST = "125.212.192.97:9501";
    private static final String SERVER_LIST = "localhost:9501";
    private static final String LANDSOFT_NAMESPACE = "lsstart1!";
    private static final int TIME_CACHE = 600; // 5'

    private static final int TOP_BLOGGER_CACHE = 6000;
    private static final String TOP_BLOGGER_NAMESPACE = "topBlogger";
    private static final String GET_MESSAGE_NAMESPACE = "gMessage";

    private static final String TOP_PLAY_NAMESPACE = "topplayer";
    private static final int TOP_PLAY_TIME_CACHE = 60000;

    protected static MemcacheClientPool cachedPool;

    public static void finishCache() {
        isUseCache = false;
        cachedPool.shutdown();
    }

    public static void initCache() {
        cachedPool = new MemcacheClientPool();
        cachedPool.start(SERVER_LIST, LANDSOFT_NAMESPACE);
    }

    private static UserEntity loadUserInfoFromDB(long userId, String isRealmoney) throws SQLException {
        UserDB db = new UserDB();
        return db.getUserInfo(userId, isRealmoney);
    }

    private UserInfoEntity loadUserAddInfoFromDB(long userId) throws SQLException {
        UserInfoDB db = new UserInfoDB();
        return db.getUserInfo(userId);
    }

    private static List<UserEntity> getTopBloggerFromDB() throws SQLException, Exception {
        UserDB db = new UserDB();
        return db.getTopBlogger();
    }

    @SuppressWarnings("static-access")
    private static List<UserEntity> getRankPlayerFromDB(int type, String isRealmoney) throws SQLException, Exception {
        UserDB db = new UserDB();
        return db.getRankPlayer(type, isRealmoney);
    }

    private MessageEntity getMessageFromDB(long userId) throws Exception {
        MessageDB db = new MessageDB();
        List<Message> lstMessage = db.receiveMessage(userId);
        MessageEntity entity = new MessageEntity(lstMessage);

        return entity;
    }

    public UserEntity getUserInfo(long userId, String isRealmoney) {

//		try {
//			if (isUseCache) {
//				IMemcacheClient client = cachedPool.borrowClient();
//				UserEntity entity = null;
//				try {
//
//					String key = USER_NAMESPACE + Long.toString(userId);
//					entity = (UserEntity) client.get(key);
//					if (entity == null) {
//						// loadFromDatabase++;
//						entity = loadUserInfoFromDB(userId);
//						client.set(key, TIME_CACHE, entity);
//						// String keyUsrName = USER_NAMESPACE +
//						// entity.mUsername;
//						// client.set(keyUsrName, USERNAME_TIME_CACHE, entity);
//
//					}
//					// else
//					// {
//					// loadFromCache++;
//					// mLog.debug("use cache " + loadFromCache);
//					// }
//				} catch (SQLException ex) {
//					mLog.error(ex.getMessage(), ex);
//				} catch (Exception ex) {
//					mLog.error(ex.getMessage(), ex);
//				}
//				cachedPool.returnClient(client);
//				return entity;
//
//			}
//		}
//
//		catch (Exception ex) {
//			mLog.error(ex.getMessage(), ex);
//
//		}
//
//		try {
//			return loadUserInfoFromDB(userId);
//		} catch (SQLException ex) {
//			mLog.error(ex.getMessage(), ex);
//		}
//
//		return null;
        UserEntity entity = null;
        try {

            entity = loadUserInfoFromDB(userId, isRealmoney);
            if (entity != null) {
                UserInfoDB userinfoObj = new UserInfoDB();
                entity.usrInfoEntity = userinfoObj.getUserInfo(userId);
            }

        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return entity;

    }

    public UserEntity getFullUserInfo(long userId, String isRealmoney) {
//		try {
//			if (isUseCache) {
//				IMemcacheClient client = cachedPool.borrowClient();
//				UserEntity entity = null;
//				try {
//
//					String key = USER_NAMESPACE + Long.toString(userId);
//					entity = (UserEntity) client.get(key);
//					if (entity == null) {
//						// loadFromDatabase++;
//						entity = loadUserInfoFromDB(userId);
//						client.set(key, TIME_CACHE, entity);
//						// String keyUsrName = USER_NAMESPACE +
//						// entity.mUsername;
//						// client.set(keyUsrName, USERNAME_TIME_CACHE, entity);
//
//					}
//
//					String keyAdd = USER_ADD_INFO_NAMESPACE + Long.toString(userId);
//					UserInfoEntity addInfo = (UserInfoEntity) client.get(keyAdd);
//					if (addInfo == null) {
//						addInfo = loadUserAddInfoFromDB(userId);
//						client.set(keyAdd, TIME_CACHE, addInfo);
//
//					}
//					entity.usrInfoEntity = addInfo;
//
//					// else
//					// {
//					// loadFromCache++;
//					// mLog.debug("use cache " + loadFromCache);
//					// }
//				} catch (SQLException ex) {
//					mLog.error(ex.getMessage(), ex);
//				} catch (Exception ex) {
//					mLog.error(ex.getMessage(), ex);
//				}
//				cachedPool.returnClient(client);
//				return entity;
//
//			}
//		}
//
//		catch (Exception ex) {
//			mLog.error(ex.getMessage(), ex);
//
//		}

        try {
            UserEntity entity = loadUserInfoFromDB(userId, isRealmoney);
            if (entity != null) {
                UserInfoEntity addInfo = loadUserAddInfoFromDB(userId);
                entity.usrInfoEntity = addInfo;
            }
            return entity;
        } catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return null;
    }

    public static void updateUserCashFromDB(long uid, long cash) {
        try {
            if (isUseCache) {

                IMemcacheClient client = cachedPool.borrowClient();
                try {

                    String key = USER_NAMESPACE + Long.toString(uid);
                    UserEntity entity = (UserEntity) client.get(key);
                    if (entity != null) {
                        entity.money = cash;
                        client.set(key, TIME_CACHE, entity);
                    }
                } catch (Exception ex) {
                    // put isUserCa
                    mLog.error(ex.getMessage(), ex);
                }

                cachedPool.returnClient(client);

            }
        } catch (Exception ex) {
            // put isUserCa
            mLog.error(ex.getMessage(), ex);
        }
    }

    public void updateCacheUserInfo(UserEntity entity) {
        try {
            if (isUseCache) {

                IMemcacheClient client = cachedPool.borrowClient();
                try {
                    String key = USER_NAMESPACE + Long.toString(entity.mUid);
                    client.set(key, TIME_CACHE, entity);
                } finally {
                    cachedPool.returnClient(client);
                }

            }
        } catch (Exception ex) {
            // put isUserCa
            mLog.error(ex.getMessage(), ex);
        }
    }

    public static void updateUserActiveCashFromDB(long uid, long cash) {
        try {
            if (isUseCache) {

                IMemcacheClient client = cachedPool.borrowClient();
                try {
                    String key = USER_NAMESPACE + Long.toString(uid);

                    UserEntity entity = (UserEntity) client.get(key);
                    if (entity != null) {
                        entity.money = cash;
                        entity.isActive = true;
                        client.set(key, TIME_CACHE, entity);
                    }
                } finally {
                    cachedPool.returnClient(client);
                }

            }
        } catch (Exception ex) {
            // put isUserCa
            mLog.error(ex.getMessage(), ex);
        }
    }

    public void deleteCacheUser(UserEntity usrEntity) {
        try {
            if (isUseCache) {

                IMemcacheClient client = cachedPool.borrowClient();
                try {
                    String key = USER_NAMESPACE + Long.toString(usrEntity.mUid);
                    client.delete(key);
                    // key = USER_NAMESPACE + usrEntity.mUsername;
                    // client.delete(key);
                } finally {
                    cachedPool.returnClient(client);
                }

            }
        } catch (Exception ex) {
            // put isUserCa
            mLog.error(ex.getMessage(), ex);
        }
    }

    public void deleteFullCacheUser(UserEntity usrEntity) {
        try {
            if (isUseCache) {

                IMemcacheClient client = cachedPool.borrowClient();
                try {
                    String key = USER_NAMESPACE + Long.toString(usrEntity.mUid);
                    String keyADD = USER_ADD_INFO_NAMESPACE + Long.toString(usrEntity.mUid);
                    client.delete(key);
                    client.delete(keyADD);

                } finally {
                    cachedPool.returnClient(client);
                }

            }
        } catch (Exception ex) {
            // put isUserCa
            mLog.error(ex.getMessage(), ex);
        }
    }

    @SuppressWarnings({"unchecked", "static-access"})
    public List<UserEntity> getRankPlayer(int type, String isRealmoney) {
//		try {
//			if (isUseCache) {
//				IMemcacheClient client = cachedPool.borrowClient();
//				List<UserEntity> enity = null;
//				try {
//					String key = this.TOP_PLAY_NAMESPACE + type;
//					enity = (List<UserEntity>) client.get(key);
//					if (enity == null) {
//						enity = this.getRankPlayerFromDB(type);
//						client.set(key, this.TOP_PLAY_TIME_CACHE, enity);
//					}
//				}
//
//				catch (Exception ex) {
//					mLog.error(ex.getMessage(), ex);
//				}
//				cachedPool.returnClient(client);
//				return enity;
//			}
//		} catch (Exception ex) {
//			mLog.error(ex.getMessage(), ex);
//		}
        try {
            return this.getRankPlayerFromDB(type, isRealmoney);
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<UserEntity> getTopBlogger() {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                List<UserEntity> enity = null;
                try {
                    String key = TOP_BLOGGER_NAMESPACE;
                    enity = (List<UserEntity>) client.get(key);
                    if (enity == null) {
                        // loadFromDatabase++;
                        enity = getTopBloggerFromDB();
                        client.set(key, TOP_BLOGGER_CACHE, enity);
                    }
                } catch (Exception ex) {
                    mLog.error(ex.getMessage(), ex);
                }

                cachedPool.returnClient(client);
                return enity;

            }
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        try {
            return getTopBloggerFromDB();
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return null;
    }

    public MessageEntity getMessage(long userId) {
        try {
            if (isUseCache) {
                IMemcacheClient client = cachedPool.borrowClient();
                MessageEntity enity = null;
                try {
                    String key = GET_MESSAGE_NAMESPACE + Long.toString(userId);
                    enity = (MessageEntity) client.get(key);
                    if (enity == null) {
                        // loadFromDatabase++;
                        enity = getMessageFromDB(userId);
                        client.set(key, TIME_CACHE, enity);
                    }
                } catch (Exception ex) {
                    mLog.error(ex.getMessage(), ex);
                }

                cachedPool.returnClient(client);
                return enity;

            }
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        try {
            return getMessageFromDB(userId);
        } catch (Exception ex) {
            mLog.error(ex.getMessage(), ex);
        }

        return null;
    }

    public void deleteCacheMessage(long userId) {
        try {
            if (isUseCache) {

                IMemcacheClient client = cachedPool.borrowClient();
                try {
                    String key = GET_MESSAGE_NAMESPACE + Long.toString(userId);

                    client.delete(key);

                } finally {
                    cachedPool.returnClient(client);
                }

            }
        } catch (Exception ex) {
            // put isUserCa
            mLog.error(ex.getMessage(), ex);
        }
    }

    public static void main(String[] args) {
        // UserEntity entity = getUserInfo(1);
        // System.out.println(entity.mUsername);

        // convert int to byte array
        byte[] mid = intToByteArray(120);
        for (int i = 0; i < mid.length; i++) {
            System.out.println(mid[i]);
        }

    }

    public static byte[] convertMid(int value) {
        return new byte[]{(byte) (value >>> 8), (byte) value};
    }

    public static final byte[] intToByteArray(int value) {
        return new byte[]{(byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};

        // int temp = ((int)tmpByte[0] << 8) + (tmpByte[1] & 0xff);
    }

}
