package vn.game.workflow;

import java.util.Calendar;

import org.slf4j.Logger;

import tools.CacheUserInfo;
import vn.game.bytebuffer.IByteBuffer;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.BusinessProperties;
import vn.game.protocol.IBusiness;
import vn.game.protocol.IBusinessPropertiesFactory;
import vn.game.protocol.IPackageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IRequestPackage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.protocol.PackageHeader;
import vn.game.protocol.SimpleRequestPackage;
import vn.game.protocol.SimpleResponsePackage;
import vn.game.protocol.messages.ExpiredSessionResponse;
import vn.game.room.ZoneManager;
import vn.game.servers.IServer;
import vn.game.servers.socket.WebSocketServer;
import vn.game.session.ISession;
import vn.game.session.ISessionFactory;
import vn.game.session.SessionManager;
import allinone.chat.data.ChatRoomZone;
import allinone.databaseDriven.DBCache;
import allinone.server.Server;
import allinone.tournement.TourManager;

import com.allinone.vivu.CityManager;

public class SimpleWorkflow implements IWorkflow {

    private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(SimpleWorkflow.class);
    private ISessionFactory mSessionFactory;
    private IBusinessPropertiesFactory mBusinessPropertiesFactory;
    private ZoneManager mZoneMgr;
    private SessionManager mSessionMgr;
    private MessageFactory mMsgFactory;
    private WorkflowConfig mWorkflowConfig;
    private TourManager tourMgr;
    private CityManager mCityManager;
    private ChatRoomZone chatZone;

    public TourManager getTourMgr() {
        return tourMgr;
    }

    public SimpleWorkflow() throws ServerException {
        try {
            this.mWorkflowConfig = new WorkflowConfig();
            this.mWorkflowConfig.getClass();
            this.mLog.info("[WF] 1. Load workflow's config from source " + "conf_vip/workflow-config.xml");
            this.mMsgFactory = new MessageFactory();
            this.mLog.info("[WF] 2. Initial game's messages");
            this.mMsgFactory.initModeling();
            String businessPropertiesFactoryName = this.mWorkflowConfig.getBusinessPropertiesFactory();
            this.mBusinessPropertiesFactory = ((IBusinessPropertiesFactory) Class.forName(businessPropertiesFactoryName).newInstance());
            this.mLog.info("[WF] 3. Generate Business's Properties" + this.mBusinessPropertiesFactory.getClass().getName());
            this.mLog.info("[WF] 4. Start database connection");
            startDB();
            this.mLog.info("[WF] 5. Load zones...");
            this.mZoneMgr = new ZoneManager();
            this.mLog.info("[WF] 5.1 Load zones...DONEEEEEEEEEE");
            //bokhi chay server phu
            Server.isTaiXiu = this.mWorkflowConfig.isUseTaiXiu();
            Server.isBaoTri = this.mWorkflowConfig.isBaoTri();
            Server.lengthPhienTX = this.mWorkflowConfig.lengthPhienTX();
            Server.isNodeBB = this.mWorkflowConfig.getIsNodeBB();
            Server.basePath = this.mWorkflowConfig.getBasePath();
            Server.isBida = this.mWorkflowConfig.isBida();
            Server.isLogBidaMongo = this.mWorkflowConfig.isLogBidaMongo();
            Server.ipMongoDb= this.mWorkflowConfig.ipMongoDB();
            
            this.mLog.info("isTaiXiu:"+Server.isTaiXiu);
            
            
            
        } catch (Throwable t) {
            throw new ServerException(t);
        }
    }

    public void startDB() throws Exception {
        try {
            CacheUserInfo.isUseCache = this.mWorkflowConfig.isUseCache();
            this.mLog.info("[WF] ---- 4.1 CacheUserInfo.isUseCache : " + CacheUserInfo.isUseCache);
            if (CacheUserInfo.isUseCache) {
                CacheUserInfo.initCache();
            }
            Server.REAL_GOT_MONEY = mWorkflowConfig.getRealGotMoney();
            this.mLog.info("[WF] ---- 4.2 Server.REAL_GOT_MONEY : " + Server.REAL_GOT_MONEY);
        } catch (Throwable e) {
            this.mLog.info("Exception Config " + e.getMessage());
        }
        DBCache.reload();
    }

    @Override
    public void start() throws ServerException {
        this.mLog.info("[WF] 6. STARTTT IN SIMPLEWORKFLOW");
        String serverName;
        try {
//            serverName = this.mWorkflowConfig.getServerName();
//            this.mLog.info("[WF] 6.1  SERVER NAME : " + serverName);
//            IServer server = (IServer) Class.forName(serverName).newInstance();
//            server.setWorkflow(this);
//            this.mSessionFactory = server.getSessionFactory();
//            this.mLog.info("[WF] 6.2  SERVER NAME : " + serverName);
//            int serverPort = this.mWorkflowConfig.getServerPort();
//            server.setServerPort(serverPort);
//            this.mLog.info("[WF] 6.3  SERVER NAME : " + serverName);
//            int connectTimeout = this.mWorkflowConfig.getServerConnectTimeout();
//            server.setConnectTimeout(connectTimeout);
//            this.mLog.info("[WF] 6.4  SERVER NAME : " + serverName);
//            int receiveBufferSize = this.mWorkflowConfig.getServerReceiveBufferSize();
//            server.setReceiveBufferSize(receiveBufferSize);
//            this.mLog.info("[WF] 6.5  SERVER NAME : " + serverName);
//            boolean reuseAddress = this.mWorkflowConfig.getReuseAddress();
//            server.setReuseAddress(reuseAddress);
//            this.mLog.info("[WF] 6.6  SERVER NAME : " + serverName);
//            boolean tcpNoDelay = this.mWorkflowConfig.getTcpNoDelay();
//            server.setTcpNoDelay(tcpNoDelay);
//            this.mLog.info("[WF] 6.7  SERVER NAME : " + serverName);
//            //server.start();
//            int sessionTimeout = this.mWorkflowConfig.getSessionTimeout();
//            this.mSessionMgr = new SessionManager(sessionTimeout);
//            this.mSessionMgr.addSessionListener(this.mZoneMgr);
//            this.mLog.info("[WF] 6.8 Create session manager with sessiontimeout = " + sessionTimeout);
//            this.mLog.info("[WF] 6.9 end. Server started with name = " + serverName);
//            this.mLog.info("Port = " + serverPort);
//            this.mLog.info("ConnectTimeout (ms) = " + connectTimeout);
//            this.mLog.info("ReceiveBufferSize (bytes) = " + receiveBufferSize);
//            this.mLog.info("ReuseAddress = " + reuseAddress);
//            this.mLog.info("TcpNoDelay = " + tcpNoDelay);
            // Start websocket server

            IServer server = new WebSocketServer();
            int serverPort = this.mWorkflowConfig.getServerPortWebsocket();

            serverName = this.mWorkflowConfig.getServerName();
            this.mLog.info("[WF] 6.1  SERVER NAME : " + serverName);
            server.setWorkflow(this);
            this.mSessionFactory = server.getSessionFactory();
            this.mLog.info("[WF] 6.2  SERVER NAME : " + serverName);
            server.setServerPort(serverPort);
            this.mLog.info("[WF] 6.3  SERVER NAME : " + serverName);
            int connectTimeout = this.mWorkflowConfig.getServerConnectTimeout();
            server.setConnectTimeout(connectTimeout);
            this.mLog.info("[WF] 6.4  SERVER NAME : " + serverName);
            int receiveBufferSize = this.mWorkflowConfig.getServerReceiveBufferSize();
            server.setReceiveBufferSize(receiveBufferSize);
            this.mLog.info("[WF] 6.5  SERVER NAME : " + serverName);
            boolean reuseAddress = this.mWorkflowConfig.getReuseAddress();
            server.setReuseAddress(reuseAddress);
            this.mLog.info("[WF] 6.6  SERVER NAME : " + serverName);
            boolean tcpNoDelay = this.mWorkflowConfig.getTcpNoDelay();
            server.setTcpNoDelay(tcpNoDelay);
            this.mLog.info("[WF] 6.7  SERVER NAME : " + serverName);
            server.start();
            int sessionTimeout = this.mWorkflowConfig.getSessionTimeout();
            this.mSessionMgr = new SessionManager(sessionTimeout);
            this.mSessionMgr.addSessionListener(this.mZoneMgr);
            this.mLog.info("[WF] 6.8 Create session manager with sessiontimeout = " + sessionTimeout);
            this.mLog.info("[WF] 6.9 end. Server started with name = " + serverName);
            this.mLog.info("Port = " + serverPort);
            this.mLog.info("ConnectTimeout (ms) = " + connectTimeout);
            this.mLog.info("ReceiveBufferSize (bytes) = " + receiveBufferSize);
            this.mLog.info("ReuseAddress = " + reuseAddress);
            this.mLog.info("TcpNoDelay = " + tcpNoDelay);

        } catch (Throwable t) {
            System.out.println("Fail to start Server!");
            t.printStackTrace();
            throw new ServerException(t);
        }
    }

    @Override
    public WorkflowConfig getWorkflowConfig() {
        return this.mWorkflowConfig;
    }

//    @Override
//    public IByteBuffer process(ISession aSession, String request) throws ServerException {
//        boolean canDecode = false;
//        IRequestPackage requestPkg = null;
//        try {
//            aSession.setIsHandling(Boolean.TRUE);
//            aSession.setLastAccessTime(Calendar.getInstance().getTime());
//            aSession.setPackageFormat("json");
//            requestPkg = decode(aSession, request.substring(request.indexOf("{"), request.length()));
//            canDecode = true;
//        } catch (Exception e) {
//        }
//        if (canDecode) {
//            IResponsePackage responsePkg = new SimpleResponsePackage();
//            handleRequest(aSession, requestPkg, responsePkg);
//            responsePkg.prepareEncode(aSession);
//            IByteBuffer result = encode(aSession, responsePkg);
//            return result;
//        }
//        return null;
//    }
    @Override
    public String process(ISession aSession, String request) throws ServerException {
        boolean canDecode = true;
        IRequestPackage requestPkg = null;
        try {
            aSession.setIsHandling(Boolean.TRUE);
            //aSession.setLastAccessTime(Calendar.getInstance().getTime());
            aSession.setPackageFormat("json");
            requestPkg = decode(aSession, request.substring(request.indexOf("{"), request.length()));
            canDecode = true;
        } catch (Exception e) {
        }
        if (canDecode) {
            IResponsePackage responsePkg = new SimpleResponsePackage();
            handleRequest(aSession, requestPkg, responsePkg);
            responsePkg.prepareEncode(aSession);
            String result = encodeWeb(aSession, responsePkg);
            return result;
        }
        return null;
    }

    protected IRequestPackage decode(ISession aSession, String aRequest) throws ServerException {
        String pkgFormat = aSession.getPackageFormat();
        IPackageProtocol pkgProtocol = this.mMsgFactory.getPackageProtocol(pkgFormat);
        IRequestPackage requestPkg = pkgProtocol.decode(aSession, aRequest);
        PackageHeader pkgHeader = requestPkg.getRequestHeader();
        String sessionId = pkgHeader.getSessionID();
        if ((sessionId != null) && (!(sessionId.trim().equals("")))) {
            this.mSessionMgr.addSession(pkgHeader.getSessionID(), aSession);
        }
        return requestPkg;
    }
//	protected IRequestPackage decode(ISession aSession, IByteBuffer aRequest) throws ServerException {
//		String pkgFormat = aSession.getPackageFormat();
//
//		IPackageProtocol pkgProtocol = this.mMsgFactory.getPackageProtocol(pkgFormat);
//		IRequestPackage requestPkg = pkgProtocol.decode(aSession, aRequest);
//
//		PackageHeader pkgHeader = requestPkg.getRequestHeader();
//
//		String sessionId = pkgHeader.getSessionID();
//		if ((sessionId != null) && (!(sessionId.trim().equals("")))) {
//			this.mSessionMgr.addSession(pkgHeader.getSessionID(), aSession);
//		}
//
//		return requestPkg;
//	}

    @Override
    public IByteBuffer processNew(ISession aSession, IByteBuffer aRequest) throws ServerException {

        boolean canDecode = true;

        IRequestPackage requestPkg = null;

        // synchronized (aSession) {
        if (SimpleRequestPackage.canDecode(aSession, aRequest)) {

            aSession.setIsHandling(Boolean.TRUE);

            //aSession.setLastAccessTime(Calendar.getInstance().getTime());
            String pkgFormat = aRequest.getString().toLowerCase();
            // mLog.debug("MessageReceived: Package format " + pkgFormat);

            aSession.setPackageFormat(pkgFormat);

            requestPkg = decodeNew(aSession, aRequest);

            canDecode = true;

        }
        // }

        if (canDecode) {

            filterIn(aSession, requestPkg);

            IResponsePackage responsePkg = new SimpleResponsePackage();

            handleRequest(aSession, requestPkg, responsePkg);

            filterOut(aSession, responsePkg);

            responsePkg.prepareEncode(aSession);

            IByteBuffer result = encode(aSession, responsePkg);

            return result;
        }

        return null;

    }

    protected IRequestPackage decodeNew(ISession aSession, IByteBuffer aRequest) throws ServerException {

        String pkgFormat = aSession.getPackageFormat();

        IPackageProtocol pkgProtocol = this.mMsgFactory.getPackageProtocol(pkgFormat);

        IRequestPackage requestPkg = pkgProtocol.decodeNew(aSession, aRequest);

        PackageHeader pkgHeader = requestPkg.getRequestHeader();

        String sessionId = pkgHeader.getSessionID();

        if ((sessionId != null) && (!(sessionId.trim().equals("")))) {
            this.mSessionMgr.addSession(pkgHeader.getSessionID(), aSession);
        }

        return requestPkg;
    }

    protected void filterIn(ISession aSession, IRequestPackage aRequestPkg) {
        if (aSession.isClosed()) {
            // return;
        }
    }

    @SuppressWarnings("unused")
    protected void handleRequest(ISession aSession, IRequestPackage aRequestPkg, IResponsePackage aResponsePkg) {
        if (aSession.isClosed()) {
            return;
        }
        try {
            while (true) {
                if (!(aRequestPkg.hasNext())) {
                    break;
                }

                IRequestMessage reqMsg = aRequestPkg.next();

                if ((reqMsg != null) && (((!(reqMsg.isNeedLoggedIn())) || (aSession.isLoggedIn())))) {
                    long timeStart;
                    long timeEnd;
                    timeStart = System.currentTimeMillis();

                    int msgId = reqMsg.getID();

                    IBusiness business = this.mMsgFactory.getBusiness(msgId);
                    try {
                        //System.out.println("vaoday");
                        //aSession.setLastAccessTime(Calendar.getInstance().getTime());
                        int result = business.handleMessage(aSession, reqMsg, aResponsePkg);
                        timeEnd = System.currentTimeMillis();

                        // if (timeEnd - timeStart > 200L) {
                        // this.mLog.warn("LONG TIME REQUEST " + msgId + ": " +
                        // (timeEnd - timeStart));
                        // }
                        if (timeEnd - timeStart > 200L) {
                            this.mLog.warn("------------MESSID TIME BUSSINESS " + msgId + ": " + (timeEnd - timeStart));
                        }

                    } catch (ServerException se) {
                        this.mLog.error("[WF] process message " + msgId + " error.", se);
                    } finally {
                    }
                } else if (reqMsg != null) {
                    String sessionId = aSession.getID();
                    // this.mSessionMgr.removeSession(sessionId);
                    this.mLog.debug("Fake message " + reqMsg.getID() + ", sessionid = " + sessionId);
                    ExpiredSessionResponse expiredSession = (ExpiredSessionResponse) this.mMsgFactory.getResponseMessage(9999);
                    expiredSession.mErrorMsg = "Vui lòng đăng nhập lại.";
                    aResponsePkg.addMessage(expiredSession);
                    break;
                }
            }
        } catch (Throwable t) {
            label448:
            this.mLog.error("Unexpected error on handlePackage() method!", t);
        } finally {
            // aSession.setCurrentDBConnection(null);
        }
    }

    protected void filterOut(ISession aSession, IResponsePackage aResponsePkg) {
        if (aSession.isClosed()) {
            // return;
        }
    }

    protected IByteBuffer encode(ISession aSession, IResponsePackage aResponsePkg) throws ServerException {
        if (aSession.isClosed()) {
            return null;
        }

        String pkgFormat = aSession.getPackageFormat();

        IPackageProtocol pkgProtocol = this.mMsgFactory.getPackageProtocol(pkgFormat);

        IByteBuffer result = pkgProtocol.encode(aSession, aResponsePkg);
        return result;
    }

    protected String encodeWeb(ISession aSession, IResponsePackage aResponsePkg) throws ServerException {
        if (aSession.isClosed()) {
            return null;
        }

        String pkgFormat = aSession.getPackageFormat();

        IPackageProtocol pkgProtocol = this.mMsgFactory.getPackageProtocol(pkgFormat);

        String result = pkgProtocol.encodeWeb(aSession, aResponsePkg);
        return result;
    }

    @Override
    public ISession sessionCreated(Object aAttachmentObj, boolean ws) throws ServerException {
        ISession session = this.mSessionFactory.createSession(ws);

        session.setCreatedTime(Calendar.getInstance().getTime());

        this.mSessionMgr.sessionCreated(session);

        session.sessionCreated(aAttachmentObj);

        BusinessProperties businessProps = createBusinessProperties();
        session.setBusinessProperties(businessProps);
        session.setZoneManager(this.mZoneMgr);
        session.setMessageFactory(this.mMsgFactory);

        session.setTourMgr(tourMgr);
        session.setmCityManager(mCityManager);

        return session;
    }

    @Override
    public BusinessProperties createBusinessProperties() {
        return this.mBusinessPropertiesFactory.createBusinessProperties();
    }

    @Override
    public void serverStarted() {
        this.mLog.debug("[WF] Server Started");
    }

    @Override
    public void serverStoppted() {
        /*
		 * if (this.mScheduler != null) { this.mScheduler.stop(); }
		 * 
		 * if (this.mWorkflowConfig.enableDB()) { String dbModelName =
		 * this.mWorkflowConfig.getDBModelName();
		 * DatabaseManager.destroy(dbModelName); }
         */
        this.mLog.debug("[WF] Server Stoppted");
    }

    public ZoneManager getZoneManager() {
        return this.mZoneMgr;
    }

    public SessionManager getmSessionMgr() {
        return mSessionMgr;
    }

    /**
     * @param mSessionMgr the mSessionMgr to set
     */
    public void setmSessionMgr(SessionManager mSessionMgr) {
        this.mSessionMgr = mSessionMgr;
    }

    /**
     * @return the chatZone
     */
    public ChatRoomZone getChatZone() {
        return chatZone;
    }


 
}
