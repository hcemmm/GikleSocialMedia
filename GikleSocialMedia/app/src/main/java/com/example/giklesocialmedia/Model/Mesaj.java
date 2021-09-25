package com.example.giklesocialmedia.Model;

public class Mesaj {

    private String gonderen;
    private String alici;
    private String mesaj;
    private String tarih;
    private String saat;
    private boolean isgorulme;

    public Mesaj(String gonderen, String alici, String mesaj,String tarih,String saat,boolean isgorulme) {
        this.gonderen = gonderen;
        this.alici = alici;
        this.mesaj = mesaj;
        this.tarih = tarih;
        this.saat = saat;
        this.isgorulme = isgorulme;
    }
    public Mesaj() {
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getAlici() {
        return alici;
    }

    public void setAlici(String alici) {
        this.alici = alici;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public boolean isIsgorulme() {
        return isgorulme;
    }

    public void setIsgorulme(boolean isgorulme) {
        this.isgorulme = isgorulme;
    }
}
