package allinone.data;

public class CardTxnEntity {

    public long id;
    public int amount;
    public int amountInput;
    public long uid;
    public int statusId;

    public CardTxnEntity() {
        // TODO Auto-generated constructor stub
    }

    public CardTxnEntity(long i, int _amount, int _amountInput, long _uid, int _status) {
        this.id = i;
        this.amount = _amount;
        this.amountInput = _amountInput;
        this.uid = _uid;
        this.statusId = _status;

    }
}
