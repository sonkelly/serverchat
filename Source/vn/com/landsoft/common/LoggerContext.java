package vn.com.landsoft.common;

//import vn.game.common.SimpleLoggerFactory;

public class LoggerContext {
	static ILoggerFactory mLoggerFactory;

	public static ILoggerFactory getLoggerFactory() {
		if (mLoggerFactory == null) {
			new SimpleLoggerFactory();
		}

		return mLoggerFactory;
	}
}