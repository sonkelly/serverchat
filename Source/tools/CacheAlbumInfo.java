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
import allinone.data.AlbumEntity;
import allinone.data.FileEntity;
import allinone.databaseDriven.AlbumDB;

/**
 * 
 * @author mcb
 */

public class CacheAlbumInfo extends CacheUserInfo {
	private static final String ALBUM_NAME_SPACE = "album";
	private static final String TOP_ALBUM_NAME_SPACE = "tAlbum";
	private static final String NEWEST_ALBUM_NAME_SPACE = "nAlbum";
	private static final String ALBUM_DETAIL_NAME_SPACE = "aDetail";
	private static final int ALBUM_TIME_CACHE = 300;
	private static final int TOP_ALBUM_TIME_CACHE = 1200;
	private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(CacheAlbumInfo.class);

	private List<AlbumEntity> loadAlbumsFromDB(long userId) throws SQLException {
		AlbumDB db = new AlbumDB();
		return db.getAlbums(userId);
	}

	private List<AlbumEntity> getTopAlbumFromDB() throws SQLException {
		AlbumDB db = new AlbumDB();
		return db.getTopAlbums();
	}

	private List<AlbumEntity> getNewestAlbumFromDB() throws SQLException {
		AlbumDB db = new AlbumDB();
		return db.getNewestAlbums();
	}

	@SuppressWarnings("unchecked")
	public List<AlbumEntity> getAlbums(long uid) {
		try {
			if (isUseCache) {
				IMemcacheClient client = cachedPool.borrowClient();
				List<AlbumEntity> lstAlbums = null;
				try {
					String key = ALBUM_NAME_SPACE + Long.toString(uid);
					lstAlbums = (List<AlbumEntity>) client.get(key);
					if (lstAlbums == null) {
						// loadFromDatabase++;
						lstAlbums = loadAlbumsFromDB(uid);
						client.set(key, ALBUM_TIME_CACHE, lstAlbums);
					}

				} catch (SQLException ex) {
					mLog.error(ex.getMessage(), ex);
				} catch (Exception ex) {
					mLog.error(ex.getMessage(), ex);
				}
				cachedPool.returnClient(client);
				return lstAlbums;

			}
		}

		catch (Exception ex) {
			mLog.error(ex.getMessage(), ex);

		}

		try {
			return loadAlbumsFromDB(uid);
		} catch (SQLException ex) {
			mLog.error(ex.getMessage(), ex);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<AlbumEntity> getTopAlbums() {
		try {
			if (isUseCache) {
				IMemcacheClient client = cachedPool.borrowClient();
				List<AlbumEntity> lstAlbums = null;
				try {
					String key = TOP_ALBUM_NAME_SPACE;
					lstAlbums = (List<AlbumEntity>) client.get(key);
					if (lstAlbums == null) {
						// loadFromDatabase++;
						lstAlbums = getTopAlbumFromDB();
						client.set(key, TOP_ALBUM_TIME_CACHE, lstAlbums);
					}

				} catch (SQLException ex) {
					mLog.error(ex.getMessage(), ex);

				} catch (Exception ex) {
					mLog.error(ex.getMessage(), ex);
					return null;
				}

				cachedPool.returnClient(client);
				return lstAlbums;

			}
		}

		catch (Exception ex) {
			mLog.error(ex.getMessage(), ex);

		}

		try {
			return getTopAlbumFromDB();
		} catch (SQLException ex) {
			mLog.error(ex.getMessage(), ex);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<AlbumEntity> getNewestAlbums() {
		try {
			if (isUseCache) {
				IMemcacheClient client = cachedPool.borrowClient();
				List<AlbumEntity> lstAlbums = null;
				try {
					String key = NEWEST_ALBUM_NAME_SPACE;
					lstAlbums = (List<AlbumEntity>) client.get(key);
					if (lstAlbums == null) {
						// loadFromDatabase++;
						lstAlbums = getNewestAlbumFromDB();
						client.set(key, TOP_ALBUM_TIME_CACHE, lstAlbums);
					}

				} catch (SQLException ex) {
					mLog.error(ex.getMessage(), ex);

				} catch (Exception ex) {
					mLog.error(ex.getMessage(), ex);
					return null;
				}

				cachedPool.returnClient(client);
				return lstAlbums;

			}
		}

		catch (Exception ex) {
			mLog.error(ex.getMessage(), ex);

		}

		try {
			return getNewestAlbumFromDB();
		} catch (SQLException ex) {
			mLog.error(ex.getMessage(), ex);
		}

		return null;
	}

	private List<FileEntity> loadFilesFromDB(long albumId) throws SQLException {
		AlbumDB db = new AlbumDB();
		return db.getAlbumDetail(albumId);
	}

	public void deleteCacheAlbums(long uid) {
		try {
			if (isUseCache) {
				IMemcacheClient client = cachedPool.borrowClient();
				try {
					String key = ALBUM_NAME_SPACE + Long.toString(uid);

					client.delete(key);

				} finally {

					cachedPool.returnClient(client);
				}

			}
		}

		catch (Exception ex) {
			mLog.error(ex.getMessage(), ex);

		}

	}

	public void deleteCacheAlbum(long albumId) {
		try {
			if (isUseCache) {
				IMemcacheClient client = cachedPool.borrowClient();
				try {
					String keyAlbumDetail = ALBUM_DETAIL_NAME_SPACE + Long.toString(albumId);
					String keyAlbum = ALBUM_NAME_SPACE + Long.toString(albumId);

					client.delete(keyAlbumDetail);
					client.delete(keyAlbum);

				} finally {

					cachedPool.returnClient(client);
				}

			}
		}

		catch (Exception ex) {
			mLog.error(ex.getMessage(), ex);

		}

	}

	@SuppressWarnings("unchecked")
	public List<FileEntity> getAlbumDetail(long albumId) {
		try {
			if (isUseCache) {
				IMemcacheClient client = cachedPool.borrowClient();

				String key = ALBUM_DETAIL_NAME_SPACE + Long.toString(albumId);
				List<FileEntity> lstFiles = (List<FileEntity>) client.get(key);
				if (lstFiles == null) {
					// loadFromDatabase++;
					lstFiles = loadFilesFromDB(albumId);
					client.set(key, ALBUM_TIME_CACHE, lstFiles);
				} else {
					// loadFromCache++;
					mLog.debug("use cache ");
				}

				cachedPool.returnClient(client);
				return lstFiles;

			}
		} catch (SQLException ex) {
			mLog.error(ex.getMessage(), ex);
			return null;
		} catch (Exception ex) {
			mLog.error(ex.getMessage(), ex);

		}

		try {
			return loadFilesFromDB(albumId);
		} catch (SQLException ex) {
			mLog.error(ex.getMessage(), ex);
		}

		return null;
	}
}
