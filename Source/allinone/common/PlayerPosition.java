/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.common;

import java.io.Serializable;

/**
 *
 * @author vipgame10
 */
public class PlayerPosition implements Serializable, Cloneable {

    private int id;
    private double x;
    private double y;
    private double z;

    public PlayerPosition() {
    }

    public PlayerPosition(int id, double x, double y, double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PlayerPosition(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public PlayerPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public PlayerPosition clone() throws CloneNotSupportedException {
        return (PlayerPosition) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    public void reset() {
//        setX(0);
//        setY(0);
        setZ(0);
        setX(0);
        setY(0);
    }

    /**
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }
}
