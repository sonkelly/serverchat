/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.room;

/**
 *
 * @author NamNT
 */
public class NZoneConfigEntity {

    public int id; // auto increament id

    public String zoneName;
    public String ip;
    public String ipios;
    public int port;
    public int portws;
    public String androidLink;
    public String iosLink;

    public NZoneConfigEntity() {
    }

    public NZoneConfigEntity(int id, String _zoneName, String _ip, String _ipios, int _port, int _portws, String linkAndroid, String linkIos) {

        this.id = id;
        this.zoneName = _zoneName;
        this.ip = _ip;
        this.ipios = _ipios;
        this.port = _port;
        this.portws = _portws;
        this.androidLink = linkAndroid;
        this.iosLink = linkIos;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

}
