package vn.game.protocol.json;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.bytebuffer.ByteBufferFactory;
import vn.game.bytebuffer.IByteBuffer;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractPackageProtocol;
import vn.game.protocol.IMessageProtocol;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IRequestPackage;
import vn.game.protocol.IResponseMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.protocol.PackageHeader;
import vn.game.protocol.SimpleRequestPackage;
import vn.game.room.Room;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.SimpleTable;

public class PackageProtocolJSON extends AbstractPackageProtocol {

    private final Logger mLog;

    public PackageProtocolJSON() {
        this.mLog = LoggerContext.getLoggerFactory().getLogger(PackageProtocolJSON.class);
    }

    public void logLocal(ISession aSession, String s) {
        try {
            s = s.replace("allinone.protocol.messages.", "");
            Vector<Room> joinedRoom = aSession.getJoinedRooms();
            if (joinedRoom.size() > 0) {

                Room r = joinedRoom.firstElement();

                SimpleTable p = (SimpleTable) r.getAttactmentData();

                if (s.contains("receive")) {
                    p.getOutCodeSB().append(SimpleTable.NEW_LINE);
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");
                p.getOutCodeSB().append(format.format(new Date())).append(s).append(SimpleTable.NEW_LINE).append(SimpleTable.NEW_LINE);

            }

        } catch (Exception e) {
        }
    }

    @SuppressWarnings("unused")
    @Override
    public IRequestPackage decode(ISession aSession, String aEncodedObj) throws ServerException {
        try {

            JSONObject jsonPkg = new JSONObject(aEncodedObj);
            IRequestPackage pkgRequest = new SimpleRequestPackage();
            aSession.receiveMessage();
            JSONArray requests;
            requests = jsonPkg.getJSONArray("r");
            int size = requests.length();
            for (int i = 0; i < size; ++i) {
                JSONObject jsonMsg = (JSONObject) requests.get(i);
                String value = jsonMsg.getString("v");
                String[] arrV = value.split(AIOConstants.SEPERATOR_NEW_MID);
                int msgId = Integer.parseInt(arrV[0]);
                IMessageProtocol msgProtocol = getMessageProtocol(msgId);
                MessageFactory msgFactor = aSession.getMessageFactory();
                IRequestMessage requestMsg = msgFactor.getRequestMessage(msgId);
                String v = "";
                if (arrV.length > 1) {
                    v = arrV[1];
                }
                jsonMsg.put("v", v);
                boolean decodedResult = msgProtocol.decode(jsonMsg, requestMsg);
                JSONObject objectPut = new JSONObject();
                objectPut.put("v", value);
                requestMsg.setObject(objectPut);
                if (decodedResult) {
                    pkgRequest.addMessage(requestMsg);
                }
            }
            return pkgRequest;
        } catch (Exception e) {
            mLog.error("Decode error : ", e);
            throw new ServerException(e);
        }
    }

    @Override
    public IRequestPackage decodeNew(ISession aSession, IByteBuffer aEncodedObj) throws ServerException {

        // mLog.debug("MessageReceived: Decode Package protocol ");
        String reqData;

        try {

            reqData = aEncodedObj.getString();

            JSONObject jsonPkg = new JSONObject(reqData);

            IRequestPackage pkgRequest = new SimpleRequestPackage();

            aSession.receiveMessage();

            JSONArray requests;

            requests = jsonPkg.getJSONArray("r");

            int size = requests.length();

            // mLog.debug("MessageReceived: Request Arr Message length " +
            // size);
            for (int i = 0; i < size; ++i) {

                JSONObject jsonMsg = (JSONObject) requests.get(i);
                String value = jsonMsg.getString("v");
                String[] arrV = value.split(AIOConstants.SEPERATOR_NEW_MID);
                int msgId = Integer.parseInt(arrV[0]);
                if (AIOConstants.DEBUG_PACKAGE_SEND) {
                    if (msgId != 2 && !aSession.isUploadAvatar()) {
                        this.mLog.debug("[Received]" + aSession.userInfo() + msgId + " : " + jsonMsg);
                        this.mLog.warn("--------------[Received]" + aSession.userInfo() + msgId + " : " + jsonMsg);
                    } else {
                        this.mLog.debug("[Received]" + aSession.userInfo() + msgId + " : upload avatar ");
                    }
                }
                IMessageProtocol msgProtocol = getMessageProtocol(msgId);

                MessageFactory msgFactor = aSession.getMessageFactory();
                IRequestMessage requestMsg = msgFactor.getRequestMessage(msgId);
                // logLocal(aSession, "[Received]" + aSession.userInfo() +
                // "[ msg : " + msgId + " : " + requestMsg + " ] : " + jsonMsg);
                String v = "";
                if (arrV.length > 1) {
                    v = arrV[1];
                }
                jsonMsg.put("v", v);

                boolean decodedResult = msgProtocol.decode(jsonMsg, requestMsg);
                JSONObject objectPut = new JSONObject();
                objectPut.put("v", value);
                requestMsg.setObject(objectPut);
                if (decodedResult) {
                    pkgRequest.addMessage(requestMsg);
                }
            }

            return pkgRequest;
        } catch (Exception e) {
            e.printStackTrace();
            mLog.error("Decode error : ", e);
            throw new ServerException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ArrayList<JSONObject> encodeToJSON(ISession aSession, IResponsePackage aResPkg) throws ServerException {
        try {

            Vector pkgData = aResPkg.optAllMessages();

            ArrayList<JSONObject> responses = new ArrayList<>();

            int size = pkgData.size();

            for (int i = 0; i < size; ++i) {
                IResponseMessage resMsg = (IResponseMessage) pkgData.get(i);

                IMessageProtocol msgProtocol = getMessageProtocol(resMsg.getID());

                JSONObject jsonMsg = (JSONObject) msgProtocol.encode(resMsg);

                String debugInfo;

                if (AIOConstants.DEBUG_PACKAGE_SEND) {
                    debugInfo = "[Send] [id " + aSession.getUserName() + "][" + aSession.getUID() + "]" + jsonMsg;
                    mLog.debug(debugInfo);
                }
                if (jsonMsg != null) {

                    responses.add(jsonMsg);
                }
            }
            return responses;
        } catch (Throwable t) {
            mLog.error("Encode error : " + t);
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public IByteBuffer encode(ISession aSession, IResponsePackage aResPkg) throws ServerException {

        JSONObject jsonPkg;

        try {
            jsonPkg = new JSONObject();

            Vector pkgData = aResPkg.optAllMessages();

            JSONArray responses = new JSONArray();

            int size = pkgData.size();

            for (int i = 0; i < size; ++i) {
                IResponseMessage resMsg = (IResponseMessage) pkgData.get(i);

                IMessageProtocol msgProtocol = getMessageProtocol(resMsg.getID());

                JSONObject jsonMsg = (JSONObject) msgProtocol.encode(resMsg);

                String debugInfo;

                debugInfo = "[Send] [id " + aSession.getUserName() + "][" + aSession.getUID() + "]" + jsonMsg;
                int maxLength = debugInfo.length() - 1;
                // mLog.error(debugInfo);
                debugInfo = debugInfo.substring(0, maxLength > 500 ? 500 : maxLength);
                // logLocal(aSession, debugInfo);
                if (AIOConstants.DEBUG_PACKAGE_SEND) {
                    mLog.debug(debugInfo);
                }
                if (jsonMsg != null) {

                    responses.put(jsonMsg);
                }
            }

            jsonPkg.put("r", responses);

            String resData = jsonPkg.toString();

            String pkgFormat = aSession.getPackageFormat();

            int dataSize = resData.getBytes("utf-8").length + 2 + pkgFormat.getBytes("utf-8").length + 2;

            IByteBuffer encodingBuffer = ByteBufferFactory.allocate(dataSize + 4);

            // IByteBuffer encodingBuffer1 =
            // ByteBufferFactory.allocate(dataSize);
            // encodingBuffer1.putString(pkgFormat);
            //
            // encodingBuffer1.putString(resData);
            // encodingBuffer1.flip();
            encodingBuffer.putInt(dataSize);

            encodingBuffer.putString(pkgFormat);

            encodingBuffer.putString(resData);

            encodingBuffer.flip();

            // System.out.println("Content length:" +
            // encodingBuffer.remaining());
            // IRequestPackage pak;
            // try {
            // if (dataSize > 10000) {
            // pak = decodeNew(aSession, encodingBuffer);
            //
            // }
            // } catch (Throwable e) {
            // //e.printStackTrace();
            // pak = decodeNew(aSession, encodingBuffer1);
            // }
            return encodingBuffer;

        } catch (Throwable t) {
            mLog.error("Encode error : " + t);
            throw new ServerException(t);
        }
    }

    final static Charset ENCODING = StandardCharsets.UTF_8;

    void writeLargerTextFile(String aFileName, String aLine) throws IOException {
        Path path = Paths.get(aFileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)) {
            writer.write(aLine);
        }
    }
    //add by zep

    @SuppressWarnings("rawtypes")
    @Override
    public String encodeWeb(ISession aSession, IResponsePackage aResPkg) throws ServerException {

        JSONObject jsonPkg;

        try {
            jsonPkg = new JSONObject();

            Vector pkgData = aResPkg.optAllMessages();

            JSONArray responses = new JSONArray();

            int size = pkgData.size();

            for (int i = 0; i < size; ++i) {
                IResponseMessage resMsg = (IResponseMessage) pkgData.get(i);

                IMessageProtocol msgProtocol = getMessageProtocol(resMsg.getID());

                JSONObject jsonMsg = (JSONObject) msgProtocol.encode(resMsg);
//                mLog.debug("debugInfo Web");
//                mLog.debug(resMsg.toString());
                String debugInfo;

                debugInfo = "[Send] [id " + aSession.getUserName() + "][" + aSession.getUID() + "]" + jsonMsg;
                int maxLength = debugInfo.length() - 1;
                //mLog.error(debugInfo);
                debugInfo = debugInfo.substring(0, maxLength > 500 ? 500 : maxLength);
                // logLocal(aSession, debugInfo);
                if (AIOConstants.DEBUG_PACKAGE_SEND) {
                    mLog.debug(debugInfo);
                }
                if (jsonMsg != null) {

                    responses.put(jsonMsg);
                }
            }

            jsonPkg.put("r", responses);

            String resData = jsonPkg.toString();
            //String resData = "{\"r\":[{\"cmd\":1000,\"v\":{\"u\":\"test02\",\"p\":\"de88e3e4ab202d87754078cbb2df6063\",\"device\":\"abc\",\"time\":23534646}}]}";
            String pkgFormat = aSession.getPackageFormat();

            int dataSize = resData.getBytes("utf-8").length + 2 + pkgFormat.getBytes("utf-8").length + 2;

            IByteBuffer encodingBuffer = ByteBufferFactory.allocate(dataSize + 4);

            // IByteBuffer encodingBuffer1 =
            // ByteBufferFactory.allocate(dataSize);
            // encodingBuffer1.putString(pkgFormat);
            //
            // encodingBuffer1.putString(resData);
            // encodingBuffer1.flip();
            encodingBuffer.putInt(dataSize);

            encodingBuffer.putString(pkgFormat);

            encodingBuffer.putString(resData);

            encodingBuffer.flip();

            // System.out.println("Content length:" +
            // encodingBuffer.remaining());
            // IRequestPackage pak;
            // try {
            // if (dataSize > 10000) {
            // pak = decodeNew(aSession, encodingBuffer);
            //
            // }
            // } catch (Throwable e) {
            // //e.printStackTrace();
            // pak = decodeNew(aSession, encodingBuffer1);
            // }
            return resData;

        } catch (Throwable t) {
            mLog.error("Encode error : " + t);
            throw new ServerException(t);
        }
    }

}
