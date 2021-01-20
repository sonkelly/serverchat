package allinone.data;

public class AuditRegisterEntity {
    private int count;
    private long lastDateTime;
    
    public AuditRegisterEntity(int count, long lastDate)
    {
        this.count = count;
        this.lastDateTime = lastDate;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the lastDateTime
     */
    public long getLastDateTime() {
        return lastDateTime;
    }

    /**
     * @param lastDateTime the lastDateTime to set
     */
    public void setLastDateTime(long lastDateTime) {
        this.lastDateTime = lastDateTime;
    }
}
