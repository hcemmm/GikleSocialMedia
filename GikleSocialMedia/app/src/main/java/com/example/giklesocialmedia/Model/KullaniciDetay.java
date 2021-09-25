package com.example.giklesocialmedia.Model;

public class KullaniciDetay {


    private String isyeri;
    private String ispozisyon;
    private String facebookadres;
    private String twitteradres;
    private String instagramadress;
    private String telefon;
    private String mailadres;
    private String yas;
    private String universiteadi;
    private String universitebolum;
    private String hakkimda;
    private String websitesi;
    private String kullanicid;

    public KullaniciDetay(String isyeri, String ispozisyon, String facebookadres, String twitteradres, String instagramadress,
                          String telefon, String mailadres, String yas, String universiteadi,
                          String universitebolum, String hakkimda,String websitesi,String kullanicid) {
        this.isyeri = isyeri;
        this.ispozisyon = ispozisyon;
        this.facebookadres = facebookadres;
        this.twitteradres = twitteradres;
        this.instagramadress = instagramadress;
        this.telefon = telefon;
        this.mailadres = mailadres;
        this.yas = yas;
        this.universiteadi = universiteadi;
        this.universitebolum = universitebolum;
        this.hakkimda = hakkimda;
        this.websitesi = websitesi;
        this.kullanicid = kullanicid;
    }

    public KullaniciDetay(){
    }

    public String getWebsitesi() {
        return websitesi;
    }

    public void setWebsitesi(String websitesi) {
        this.websitesi = websitesi;
    }

    public String getIsyeri() {
        return isyeri;
    }

    public String getKullanicid() {
        return kullanicid;
    }

    public void setKullanicid(String kullanicid) {
        this.kullanicid = kullanicid;
    }

    public void setIsyeri(String isyeri) {
        this.isyeri = isyeri;
    }

    public String getIspozisyon() {
        return ispozisyon;
    }

    public void setIspozisyon(String ispozisyon) {
        this.ispozisyon = ispozisyon;
    }

    public String getFacebookadres() {
        return facebookadres;
    }

    public void setFacebookadres(String facebookadres) {
        this.facebookadres = facebookadres;
    }

    public String getTwitteradres() {
        return twitteradres;
    }

    public void setTwitteradres(String twitteradres) {
        this.twitteradres = twitteradres;
    }

    public String getInstagramadress() {
        return instagramadress;
    }

    public void setInstagramadress(String instagramadress) {
        this.instagramadress = instagramadress;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMailadres() {
        return mailadres;
    }

    public void setMailadres(String mailadres) {
        this.mailadres = mailadres;
    }

    public String getYas() {
        return yas;
    }

    public void setYas(String yas) {
        this.yas = yas;
    }

    public String getUniversiteadi() {
        return universiteadi;
    }

    public void setUniversiteadi(String universiteadi) {
        this.universiteadi = universiteadi;
    }

    public String getUniversitebolum() {
        return universitebolum;
    }

    public void setUniversitebolum(String universitebolum) {
        this.universitebolum = universitebolum;
    }

    public String getHakkimda() {
        return hakkimda;
    }

    public void setHakkimda(String hakkimda) {
        this.hakkimda = hakkimda;
    }
}
