package com.example.giklesocialmedia.Model;

public class Kullanici {

    private String id;
    private String kullaniciadi;
    private String advesoyad;
    private String profil_pp;
    private String bio;
    private String mail;
    private String sifre;



    private String durum;



    public Kullanici(String id, String kullaniciadi, String advesoyad, String profil_pp, String bio, String mail, String sifre,String durum) {
        this.id = id;
        this.kullaniciadi = kullaniciadi;
        this.advesoyad = advesoyad;
        this.profil_pp = profil_pp;
        this.bio = bio;
        this.mail = mail;
        this.sifre = sifre;
        this.durum = durum;


    }

    public Kullanici() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKullaniciadi() {
        return kullaniciadi;
    }

    public void setKullaniciadi(String kullaniciadi) {
        this.kullaniciadi = kullaniciadi;
    }

    public String getAdvesoyad() {
        return advesoyad;
    }

    public void setAdvesoyad(String advesoyad) {
        this.advesoyad = advesoyad;
    }

    public String getProfil_pp() {
        return profil_pp;
    }

    public void setProfil_pp(String profil_pp) {
        this.profil_pp = profil_pp;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

}
