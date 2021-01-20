/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.db;

import allinone.server.Server;
import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

public class MongoBase {

//    private static final String CONNECTION_STRING = "mongodb://192.168.2.247:27017/vipgame?readPreference=primary&appname=MongoDB%20Compass&ssl=false&maxPoolSize=5";
    private static final String USERNAME = "mgdb";
    private static final String PASSWORD = "1234";
    private static final String DATABASE_VIPGAME = "vipgame";
    private static final String IP = Server.ipMongoDb;
    private static final int PORT = 27017;

    private static MongoClient mongoClient = null;
//    // Cách kết nối vào MongoDB không bắt buộc bảo mật.
//    public ManagerServerData managerServerData;

    private static MongoClient getMongoClient_1(String ip, int port) throws UnknownHostException {

        CodecRegistry pojoCodeRegisty = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        if (mongoClient == null) {
            MongoClientOptions builder = new MongoClientOptions.Builder() //
                    .socketKeepAlive(true)
                    .connectionsPerHost(20)
                    .socketTimeout(5 * 1000)
                    .serverSelectionTimeout(5 * 1000)
                    .codecRegistry(pojoCodeRegisty)
                    .build();
            mongoClient = new MongoClient(new ServerAddress(ip, port), builder);
        }

        return mongoClient;
    }

    // Cách kết nối vào DB MongoDB có bảo mật.
//    private static MongoClient getMongoClient_2() throws UnknownHostException {
//        MongoCredential credential = MongoCredential.createMongoCRCredential(
//                USERNAME, DATABASE, PASSWORD.toCharArray());
//
//        MongoClient mongoClient = new MongoClient(
//                new ServerAddress(HOST, PORT), Arrays.asList(credential));
//        return mongoClient;
//    }
    public static MongoClient getMongoClient(String ip, int port) throws UnknownHostException {

        // Kết nối vào MongoDB không bắt buộc bảo mật.
        return getMongoClient_1(ip, port);
        // Bạn có thể thay thế bởi getMongoClient_2()
        // trong trường hợp kết nối vào MongoDB có bảo mật.
    }

    public static MongoDatabase getDatabaseVipGame() throws UnknownHostException {

        try {
            return getMongoClient_1(IP, PORT).getDatabase(DATABASE_VIPGAME);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoBase.class.getName()).log(Level.SEVERE, null, ex);
            if (mongoClient != null) {
                return mongoClient.getDatabase(DATABASE_VIPGAME);
            } else {
                throw new UnknownHostException();
            }
        }
    }

    public static boolean insert(Object object) {
        try {
            Class<?> myClass = object.getClass();
            String collectionName = myClass.getSimpleName();
            MongoDatabase database = getDatabaseVipGame();
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document document = new Document();

            Field[] allFields = myClass.getDeclaredFields();
            for (Field field : allFields) {
                field.setAccessible(true);
                document.append(field.getName(), field.get(object));
            }
            collection.insertOne(document);
            ObjectId id = (ObjectId) document.get("_id");

            int x = 1;
            return true;

        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | UnknownHostException ex) {
            Logger.getLogger(MongoBase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insert(Object object, String collectionName) {
        try {
            if (collectionName.equals("")) {
                collectionName = "ChatSocialDefaulfObject";
            }
            Class<?> myClass = object.getClass();
            MongoDatabase database = getDatabaseVipGame();
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document document = new Document();

            Field[] allFields = myClass.getDeclaredFields();
            for (Field field : allFields) {
                field.setAccessible(true);
                document.append(field.getName(), field.get(object));
            }
            collection.insertOne(document);
            return true;

        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | UnknownHostException ex) {
            Logger.getLogger(MongoBase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Iterator<Document> findData(String collectionName, String propertiesName, int PropertiesValue) throws UnknownHostException {
        MongoDatabase database = getDatabaseVipGame();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Iterator<Document> iterDoc = collection.find(Filters.eq(propertiesName, PropertiesValue)).iterator();
        return iterDoc;
    }

    public String checkRoom(String collectionName, String[] listUserId) throws UnknownHostException {
        String room1 = listUserId[0] + "" + listUserId[1];
        String room2 = listUserId[1] + "" + listUserId[0];
        String id1 = check(collectionName, room1);
        String id2 = check(collectionName, room2);
        if (id1 != "") {
            return id1;
        } else {
            if (id2 != "") {
                return id2;
            } else {
                return "";
            }
        }
    }

    public String check(String collectionName, String roomId) throws UnknownHostException {
        String ckeckRoom = "";
        MongoDatabase database = getDatabaseVipGame();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Iterator<Document> iterDoc = collection.find(Filters.eq("roomId", roomId)).iterator();
        try {
            Document docs = new Document();
            while (iterDoc.hasNext()) {
                docs = iterDoc.next();
                if (docs != null) {
                    ckeckRoom = (String) docs.get("roomId");
                } else {
                    ckeckRoom = "";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(MongoBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ckeckRoom;
    }

    public Iterator<Document> findUserData(String collectionName, long userId) throws UnknownHostException {
        MongoDatabase database = getDatabaseVipGame();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Iterator<Document> iterDoc = collection.find(Filters.eq("uId", userId)).iterator();
        return iterDoc;
    }

    public Iterator<Document> findMember(String collectionName, String roomId) throws UnknownHostException {
        MongoDatabase database = getDatabaseVipGame();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Iterator<Document> iterDoc = collection.find(Filters.eq("roomId", roomId)).iterator();
        return iterDoc;
    }

    public Iterator<Document> findListRoom(String collectionName, long uId) throws UnknownHostException {
        MongoDatabase database = getDatabaseVipGame();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Iterator<Document> iterDoc = collection.find(Filters.eq("uId", uId)).iterator();
        return iterDoc;
    }

    public Iterator<Document> findListMessage(String collectionName, String roomId) throws UnknownHostException {
        MongoDatabase database = getDatabaseVipGame();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Iterator<Document> iterDoc = collection.find(Filters.eq("roomId", roomId)).iterator();
        return iterDoc;
    }

    public static MongoCollection<Document> getCollection(Object object) {
        try {
            Class<?> myClass = object.getClass();
            String collectionName = myClass.getSimpleName();
            MongoDatabase database = getDatabaseVipGame();
            return database.getCollection(collectionName);

        } catch (IllegalArgumentException | SecurityException | UnknownHostException ex) {
            Logger.getLogger(MongoBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

//    public static void main(String[] args) {
//
//        Iterator<Document> iterDoc;
//        try {
//            iterDoc = findListRoom("mxh_listMessage", Long.parseLong("244177"));
//            Document docs = new Document();
//            while (iterDoc.hasNext()) {
//                docs = iterDoc.next();
//                System.err.println("ROOM: "+docs.get("roomName"));
//            }
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(MongoBase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        CreateRoomSocialResponse res = new CreateRoomSocialResponse();
//        ArrayList<UserModel> listUser = new  ArrayList<>();
//        MessageContentModel user1 = new MessageContentModel();
//        user1.uId = 1542;
//        user1.email = "User3@gmail.com";
//        user1.sex = "Nam";
//        user1.userName = "ngoccong";
//        user1.viewName = "Ngọc Công";
//        
//        MessageContentModel user2 = new MessageContentModel();
//        user2.uId = 1245;
//        user2.email = "User4@gmail.com";
//        user2.sex = "Nữ";
//        user2.userName = "nhatlinh89";
//        user2.viewName = "Nhật Linh";
//        listUser.add(user1);
//        listUser.add(user2);
//        res.createedTime = new Date().toString();
//        res.listUser = listUser;
//        res.ownerId = 1212;
//        res.roomId = 12334343;
//          
//        MongoBase.insert(user2);
//    }
}
