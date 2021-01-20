package allinone.data;

public class AvatarEntity {

    public int id;
    public String avatar;
    public long money;

    public AvatarEntity() {

    }

    public AvatarEntity(int id, String d) {
        this.id = id;
        this.avatar = d;
        //this.money = money;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getId() {
        return id;
    }
}
