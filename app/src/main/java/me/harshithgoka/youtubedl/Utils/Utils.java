package me.harshithgoka.youtubedl.Utils;

/**
 * Created by harshithg on 22/1/18.
 */

public class Utils {

    public static String removeQuotes(String arg) {
        char[] quotes = {'"', '\''};
        for (char quote : quotes) {
            if (arg.charAt(0) == quote && arg.charAt(arg.length() - 1) == quote) {
                return arg.substring(1, arg.length() - 1);
            }
        }
        return arg;
    }
}
