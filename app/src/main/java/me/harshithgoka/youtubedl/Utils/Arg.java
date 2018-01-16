package me.harshithgoka.youtubedl.Utils;

/**
 * Created by harshithg on 17/1/18.
 */



public class Arg {
    public enum argtype {
        INT, STRING, VOID
    };

    argtype type;
    String s;
    int i;

    public Arg(String s) {
        this.s = s;
        type = argtype.STRING;
    }
    public Arg (int i) {
        this.i = i;
        type = argtype.INT;
    }
    public Arg () {
        type = argtype.VOID;
    }
}