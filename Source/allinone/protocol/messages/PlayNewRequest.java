package allinone.protocol.messages;

import vn.game.protocol.AbstractRequestMessage;
import vn.game.protocol.IRequestMessage;

/**
 *
 * @author binh_lethanh
 */
public class PlayNewRequest extends AbstractRequestMessage {

    public long mid;
    public long moneyBet;
    public int roomType;
    public long uid;
    public String password;
    public int size;
    public String roomName;
    public int phongID;
    public int tableIndex;
    public boolean truong;
    //Caro
    public int mRow;
    public int mCol;
    //Phom
    public boolean isKhan = true;
    public boolean isAn = true;
    public boolean isTai = true;
    public int testCode = 0;
    //Cờ tướng
    public int available = 0;//chấp quân gì

    //New_Pika
    public boolean advevntureMode;
    public int matrixSize;
    public int pikaLevel;
    @Override
    public IRequestMessage createNew() {
        return new PlayNewRequest();
    }
}
