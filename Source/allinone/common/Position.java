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
public class Position implements Serializable, Cloneable {

    private double x;
    private double y;
    private double z;

    public Position() {
    }

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    @Override
    public String toString() {
        return "Coordinates [x=" + x + ", y=" + y + ", z=" + z + "]";
    }

    @Override
    public Position clone() throws CloneNotSupportedException {
        return (Position) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

}
