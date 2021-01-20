/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.tournement;

import java.util.Date;

/**
 *
 * @author mac
 */
public class TourTemplateEntity {

    public int id;
    public Date startDate;
    public Date endDate;
    public String name;
    public long money_coc;
    public long money_cuoc;
    public int game;
    public long creator;
    public boolean isBook;
    public int status;
    public int templateId;
    public int moneyType; //0 xu, 1 quan
    public TourTemplateEntity(int i, Date s, Date e, String n, long coc,long cuoc, int g, long c, int _status,int _moneyType) {
        creator = c;
        id = i;
        startDate = s;
        endDate = e;
        name = n;
        money_coc = coc;//cuo so tien trong giai dau
        money_cuoc = cuoc;// tien moi van bi
        game = g;
        status = _status;
        moneyType = _moneyType;

    }
}
