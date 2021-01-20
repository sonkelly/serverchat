package allinone.protocol.messages;

import java.util.ArrayList;
import java.util.List;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;
import allinone.data.GameDataEntity;
import allinone.data.UserInfoEntity;


public class GetUserDataResponse extends AbstractResponseMessage {

    public String mErrorMsg;
    public List<GameDataEntity> data = new ArrayList<GameDataEntity>();
    public boolean isFriend = false;
    public long money;
    public int expr;
    public long userId;
    public String nickName;
    public int requestSize;
    
    public String name;
    public String cmt;
    public String address;
    public String phoneNumber;
    public String avatar;
    public String mail;
    public String countryId;
    public String cityId;
    public boolean isMale;
    public long realmoney;
    public void setSuccess(int aCode, List<GameDataEntity> dataList, boolean friend, long money, int expr, long userId, String nickName, int requestSize,
    		String name, String cmt, String phoneNumber, String avatar, String _mail,boolean _isMale, UserInfoEntity usrInfoEntity, long realmoney) {
    	this.mCode = aCode;
    	this.data = dataList;
    	this.isFriend = friend;
        this.money = money;
        this.expr = expr;
        this.userId = userId;
        this.nickName = nickName;
        this.requestSize = requestSize;
        this.name = name;
        this.cmt = cmt;
       
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.mail = _mail;
        this.isMale = _isMale;
        if(usrInfoEntity != null){
            this.countryId = usrInfoEntity.countryId; 
            this.cityId = usrInfoEntity.cityId; 
            this.address = usrInfoEntity.address;
        }
        this.realmoney = realmoney;
       
    }

    public void setFailure(int aCode, String aErrorMsg) {
        mCode = aCode;
        mErrorMsg = aErrorMsg;
    }

    public IResponseMessage createNew() {
        return new GetUserDataResponse();
    }
}
