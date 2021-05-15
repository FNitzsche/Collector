package me.shaakashee.collector.model;

import java.util.HashMap;

public class Etikett {
    private String fam;
    private String gattung;
    private String art;
    private String autor;
    private String name;
    private String fundort;
    private String standort;
    private String leg;
    private String det;
    private String date;
    private String text;
    private String url;

    private String pageID;
    private HashMap<String, String> possiblePages;

    public static Etikett build(String fam, String gattung, String art, String autor, String name, String fundort, String standort, String leg, String det, String date, String text, String url){
        Etikett etikett = new Etikett();
        etikett.fam = fam;
        etikett.gattung = gattung;
        etikett.art = art;
        etikett.autor = autor;
        etikett.name = name;
        etikett.fundort = fundort;
        etikett.standort = standort;
        etikett.leg = leg;
        etikett.det = det;
        etikett.date = date;
        etikett.text = text;
        etikett.url = url;
        return etikett;
    }

    public String getFam() {
        return fam;
    }

    public void setFam(String fam) {
        this.fam = fam;
    }

    public String getGattung() {
        return gattung;
    }

    public void setGattung(String gattung) {
        this.gattung = gattung;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFundort() {
        return fundort;
    }

    public void setFundort(String fundort) {
        this.fundort = fundort;
    }

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }

    public String getLeg() {
        return leg;
    }

    public void setLeg(String leg) {
        this.leg = leg;
    }

    public String getDet() {
        return det;
    }

    public void setDet(String det) {
        this.det = det;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
