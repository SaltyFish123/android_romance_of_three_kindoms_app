package com.example.jason.lab5;

/**
 * Created by joey on 11/26/17.
 */

public class SimpleCharacter {
    private String name;
    private String image;
    private String birthday;
    private String deathday;
    private String sex;
    private String kingdom;

    public SimpleCharacter(String _name, String _image, String _birthday, String _deathday, String _sex, String _kingdom) {
        name = _name;
        image = _image;
        birthday = _birthday;
        deathday = _deathday;
        sex = _sex;
        kingdom = _kingdom;
    }

    public boolean setName(String _name) {
        name = _name;
        return true;
    }

    public String getName() {
        return  name;
    }

    public boolean setImage(String _image) {
        image = _image;
        return true;
    }

    public String getImage() {
        return image;
    }

    public boolean setBirthday(String _birthday) {
        birthday = _birthday;
        return true;
    }

    public String getBirthday() {
        return birthday;
    }

    public boolean setDeathday(String _deathday) {
        deathday = _deathday;
        return true;
    }

    public String getDeathday() {
        return deathday;
    }

    public boolean setSex(String _sex) {
        sex = _sex;
        return true;
    }

    public String getSex() {
        return sex;
    }

    public boolean getKingdom(String _kingdom) {
        kingdom = _kingdom;
        return true;
    }

    public String getKingdom() {
        return  kingdom;
    }
}
