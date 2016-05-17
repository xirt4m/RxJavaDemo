package hu.bme.aut.amorg.education.rxjava.util;

public class TextWithColor {

    private final String text;
    private final Integer color;

    public TextWithColor(String text, Integer integer) {
        this.text = text;
        this.color = integer;
    }

    public String getText() {
        return text;
    }

    public Integer getColor() {
        return color;
    }

}
