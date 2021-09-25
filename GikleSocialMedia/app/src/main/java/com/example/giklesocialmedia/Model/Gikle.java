package com.example.giklesocialmedia.Model;

public class Gikle {

    private String gikid;
    private String gikmetin;
    private String gikpaylasan;
    private String gikzaman;

    public Gikle(String gikid, String gikmetin, String gikpaylasan, String gikzaman) {
        this.gikid = gikid;
        this.gikmetin = gikmetin;
        this.gikpaylasan = gikpaylasan;
        this.gikzaman = gikzaman;
            }

    public Gikle() {
    }


    public String getGikid() {
        return gikid;
    }

    public void setGikid(String gikid) {
        this.gikid = gikid;
    }

    public String getGikmetin() {
        return gikmetin;
    }

    public void setGikmetin(String gikmetin) {
        this.gikmetin = gikmetin;
    }

    public String getGikpaylasan() {
        return gikpaylasan;
    }

    public void setGikpaylasan(String gikpaylasan) {
        this.gikpaylasan = gikpaylasan;
    }

    public String getGikzaman() {
        return gikzaman;
    }

    public void setGikzaman(String gikzaman) {
        this.gikzaman = gikzaman;
    }
}
