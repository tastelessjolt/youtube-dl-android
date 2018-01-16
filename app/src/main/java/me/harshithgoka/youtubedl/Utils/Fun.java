package me.harshithgoka.youtubedl.Utils;

/**
 * Created by harshithg on 17/1/18.
 */

public class Fun {
    public String name;
    public String[] argnames;
    public String code;
    public Fun(String name, String[] argnames, String code) {
        this.name = name;
        this.argnames = argnames.clone();
        this.code = code;
    }
}