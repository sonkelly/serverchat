package allinone.protocol.messages;

import java.util.ArrayList;

import vn.game.protocol.AbstractResponseMessage;
import vn.game.protocol.IResponseMessage;

public class GetOtherPokerResponse extends AbstractResponseMessage {

    public long uid;
    public byte[] tienlenCards;
    public boolean isNew;
    public String samCard;

    public void setSamCard(String sC) {
        samCard = sC;
    }

    public void setTienLenCards(byte[] cards) {
        this.tienlenCards = cards;
    }

    public void setSuccess(int aCode, long id, boolean i) {
        mCode = aCode;
        uid = id;
        isNew = i;
    }

    @Override
    public IResponseMessage createNew() {
        return new GetOtherPokerResponse();
    }
}
