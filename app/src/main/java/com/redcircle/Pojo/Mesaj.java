package com.redcircle.Pojo;

public class Mesaj {
    private String gonderen, mesaj, userID, zaman, name;

    public Mesaj() {
    }

    public Mesaj(String gonderen, String mesaj, String userID, String zaman, String name) {
        this.gonderen = gonderen;
        this.mesaj = mesaj;
        this.userID = userID;
        this.zaman = zaman;
        this.name = name;
    }

    public String getGonderen() {
        return gonderen;
    }

    public String getMesaj() {
        return mesaj;
    }

    public String getZaman() {
        return zaman;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

}
