package com.example.anlikmesajlasmauyg;

public class MesajModel {

    //Mesajları firebase üzerine ekleyebiliyoruz, ancak get edilebilmesi ve ekranda mesajların gösterilebilmesi için böyle bir class'a ihtiyaç var.

    private String from;
    private String text;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MesajModel(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public MesajModel(){

    }

    @Override
    public String toString() {
        return "MesajModel{" +
                "from='" + from + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
