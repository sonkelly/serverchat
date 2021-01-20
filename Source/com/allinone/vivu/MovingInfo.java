/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinone.vivu;

/**
 *
 * @author Vostro 3450
 */
public class MovingInfo {
    public int x;
    public int y;
    public long uid;
    public String name;
    public String attr;
    public boolean isMove;

    public MovingInfo(int x, int y, long uid, String name, String attr, 
            boolean isMove) {
        this.x = x;
        this.y = y;
        this.uid = uid;
        this.name = name;
        this.attr = attr;
        this.isMove = isMove;
    }
    
}
