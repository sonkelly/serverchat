/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.entity;

import java.io.Serializable;

/**
 *
 * @author mac
 */
public class ChatZoneUtity implements Serializable {

    public static final int STATUS_GETLIST = 0;
    public static final int STATUS_CHAT = 1;
    public String viewname;
    public String msg;
  

    public ChatZoneUtity(String _viewname, String _msg) {
        this.viewname = _viewname;
        this.msg = _msg;
       
    }
}