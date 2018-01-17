package me.harshithgoka.youtubedl;

import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.harshithgoka.youtubedl.Utils.Arg;
import me.harshithgoka.youtubedl.Utils.Fun;

import static me.harshithgoka.youtubedl.Utils.Arg.VAL;

/**
 * Created by harshithg on 16/1/18.
 */

public class JSInterpreter {
    String code;
    HashMap<String, Fun> functions;

    String _NAME_RE = "[a-zA-Z_$][a-zA-Z_$0-9]*";

    String _OPS = "{ \n" +
            "\t\"|\": \"op\",\n" +
            "\t\"^\": \"op\",\n" +
            "\t\"&\": \"op\",\n" +
            "\t\">>\": \"op\",\n" +
            "\t\"<<\": \"op\",\n" +
            "\t\"-\": \"op\",\n" +
            "\t\"+\": \"op\",\n" +
            "\t\"%\": \"op\",\n" +
            "\t\"/\": \"op\",\n" +
            "\t\"*\": \"op\"\n" +
            "}";
    String _ASSIGNMENT_OPS = "{ \n" +
            "\t\"|=\": \"op\",\n" +
            "\t\"^=\": \"op\",\n" +
            "\t\"&=\": \"op\",\n" +
            "\t\">>=\": \"op\",\n" +
            "\t\"<<=\": \"op\",\n" +
            "\t\"-=\": \"op\",\n" +
            "\t\"+=\": \"op\",\n" +
            "\t\"%=\": \"op\",\n" +
            "\t\"/=\": \"op\",\n" +
            "\t\"*=\": \"op\",\n" +
            "\t\"=\": \"op\"\n" +
            "}";

    JSONObject OPS;
    JSONObject ASSIGNMENT_OPS;

    JSInterpreter(String code) {
        this.code = code;
        try {
            OPS = new JSONObject(_OPS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ASSIGNMENT_OPS = new JSONObject(_ASSIGNMENT_OPS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Fun extractFunction(String funcname) {

//        String funcname = "NJ";
        String s = String.format("(?x)" +
                "(?:function\\s+%s|[{;,]\\s*%s\\s*=\\s*function|var\\s+%s\\s*=\\s*function)\\s* " +
                "\\((?<args>[^)]*)\\)\\s*" +
                "\\{(?<code>[^}]+)\\}", Pattern.quote(funcname), Pattern.quote(funcname), Pattern.quote(funcname));
        Pattern funcPattern = Pattern.compile(s);
        Matcher m = funcPattern.matcher(code);
        m.find();
        String[] _args = m.group(1).split(",");

        String _code = m.group(2);

        return new Fun(funcname, _args, _code);
    }
    
    

    public Arg callFunction(Fun func, Arg[] args) throws Exception {
        if (args.length != func.argnames.length) {
            throw new Exception("Incorrect number of arguments passed");
        }

        JSONObject local_vars = new JSONObject();
        for (int i = 0; i != args.length; i++) {
            local_vars.put(func.argnames[i], args[i]);
        }

        Arg res = new Arg();
        String[] split = func.code.split(";");
        for (String stmt : split) {
            Pair<Arg, Boolean> ret = interpretStatement(stmt, local_vars, 100);
            res = ret.first;
            Log.d(stmt, res.getString(VAL));
            if (ret.second) {
                break;
            }
        }

        return res;
    }

    public Pair interpretStatement(String stmt, JSONObject local_vars, int allowRecursion) throws Exception {
        Log.d("JSParser", stmt);


        if (allowRecursion < 0) {
            throw new Exception("Recursion limit reached");
        }

        boolean should_abort = false;

        stmt = stmt.trim();
        Pattern assgn = Pattern.compile("var\\s");
        Matcher m = assgn.matcher(stmt);
        String expr = "";
        if (m.find()) {
            expr = stmt.substring(m.group(0).length());
        }
        else {
            m = Pattern.compile("return(?:\\s+|$)").matcher(stmt);
            if (m.find()) {
                expr = stmt.substring(m.group(0).length());
                should_abort = true;
            }
            else {
                expr = stmt;
            }
        }

        Arg arg = interpretExpression(expr, local_vars, allowRecursion);

        return new Pair<Arg, Boolean>(arg, should_abort);
    }

    private Arg interpretExpression(String expr, JSONObject local_vars, int allowRecursion) throws Exception {
        expr = expr.trim();

        if (expr.equals("")) {
            return new Arg();
        }

        if (expr.startsWith("(")) {
            int parens_count = 0;
            Pattern p_pr = Pattern.compile("[()]");
            Matcher m = p_pr.matcher(expr);
            while (m.find()) {
                if (m.group(0).equals("(")) {
                    parens_count += 1;
                }
                else {
                    parens_count -= 1;
                    if (parens_count == 0) {
                        String sub_expr = expr.substring(1, m.start());
                        Arg sub_result = interpretExpression(sub_expr, local_vars, allowRecursion);
                        String remaining_expr = expr.substring(m.end()).trim();
                        if (remaining_expr.equals("")) {
                            return sub_result;
                        }
                        else {
                            expr = sub_result.toString() + remaining_expr;
                        }
                    }
                }
            }
            if (parens_count > 0) {
                throw new Exception("Premature end of parens in " + expr);
            }
        }

        for (Iterator<String> it = ASSIGNMENT_OPS.keys(); it.hasNext(); ) {
            String op = it.next();
            String assgn = String.format("(?x)\n" +
                    "                (?<out>%s)(?:\\[(?<index>[^\\]]+?)\\])?\n" +
                    "                \\s*%s\n" +
                    "                (?<expr>.*)$", _NAME_RE, Pattern.quote(op));
            Pattern p = Pattern.compile(assgn);
            Matcher m = p.matcher(expr);
            Arg right_val;
            if (m.find()) {
                right_val = interpretExpression(m.group(3), local_vars, allowRecursion - 1);
            } else {
                continue;
            }

            if (m.group(2) != null) {
                JSONArray lvar = local_vars.getJSONArray(m.group(1));
                Arg idx = interpretExpression(m.group(2), local_vars, allowRecursion);

                int index = idx.getInt(VAL);
                JSONObject curr = lvar.getJSONObject(index);
                // TODO: Do actual operations on (curr, right_val) and put them back in lvar[index]
            }
            else {
                Arg cur = (Arg) local_vars.getJSONObject(m.group(1));
                // TODO: Do actual operations on (curr, right_val) and put them back in local_vars

            }

            try {
                int i = Integer.parseInt(expr);
                return new Arg(i);
            }
            catch (Exception e) {

            }

        }

        return new Arg(expr);
    }


}
