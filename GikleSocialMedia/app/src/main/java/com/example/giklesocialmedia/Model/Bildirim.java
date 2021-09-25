package com.example.giklesocialmedia.Model;

public class Bildirim {

    private String bildirimkullaniciid;
    private String bildirimmetin;
    private String bildirimpostid;
    private boolean bildirimpost;
    private boolean bildirimgik;

    public Bildirim(String bildirimkullaniciid, String bildirimmetin, String bildirimpostid, boolean bildirimpost,boolean bildirimgik) {
        this.bildirimkullaniciid = bildirimkullaniciid;
        this.bildirimmetin = bildirimmetin;
        this.bildirimpostid = bildirimpostid;
        this.bildirimpost = bildirimpost;
        this.bildirimgik = bildirimgik;
    }

    public Bildirim() {
    }

    public boolean isBildirimgik() {
        return bildirimgik;
    }

    public void setBildirimgik(boolean bildirimgik) {
        this.bildirimgik = bildirimgik;
    }

    public String getBildirimkullaniciid() {
        return bildirimkullaniciid;
    }

    public void setBildirimkullaniciid(String bildirimkullaniciid) {
        this.bildirimkullaniciid = bildirimkullaniciid;
    }

    public String getBildirimmetin() {
        return bildirimmetin;
    }

    public void setBildirimmetin(String bildirimmetin) {
        this.bildirimmetin = bildirimmetin;
    }

    public String getBildirimpostid() {
        return bildirimpostid;
    }

    public void setBildirimpostid(String bildirimpostid) {
        this.bildirimpostid = bildirimpostid;
    }

    public boolean isBildirimpost() {
        return bildirimpost;
    }

    public void setBildirimpost(boolean bildirimpost) {
        this.bildirimpost = bildirimpost;
    }
}
