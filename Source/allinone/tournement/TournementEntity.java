package allinone.tournement;

import java.util.Date;

public class TournementEntity {
	public int id;
	public Date startDate;
	public Date endDate;
	public String name;
	public long money_coc;
	public int game;
	public long creator;
	public boolean isBook;
        public int status;
        public int templateId;
        public int totalUserRegister;
	public TournementEntity(int i, Date s, Date e, String n, long m, int g, long c,int _status, int _templateId, int _totalUserRegister) {
		creator = c;
		id = i;
		startDate = s;
		endDate = e;
		name = n;
		money_coc = m;
		game = g;
                status = _status;
                templateId = _templateId;
                totalUserRegister = _totalUserRegister;
	}
}
