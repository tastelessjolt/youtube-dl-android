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
import me.harshithgoka.youtubedl.Utils.Utils;

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
    JSONObject objects;

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

        objects = new JSONObject();
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
                Arg lvar = (Arg) local_vars.getJSONObject(m.group(1));
                Arg idx = interpretExpression(m.group(2), local_vars, allowRecursion);

                int index = idx.getInt(VAL);
                Arg curr = (Arg) lvar.getJSONArray(VAL).get(index);
                Arg val = opFunc(op, curr, right_val);
                lvar.getJSONArray(VAL).put(index, val);
                return val;
            }
            else {
                Arg curr = (Arg) local_vars.getJSONObject(m.group(1));
                Arg val = opFunc(op, curr, right_val);
                local_vars.put(m.group(1), val);
                return val;
            }
        }

        try {
            int i = Integer.parseInt(expr);
            return new Arg(i);
        }
        catch (Exception e) {

        }

        Pattern control = Pattern.compile(String.format("(?!if|return|true|false)(?<name>%s)$", _NAME_RE));
        Matcher m = control.matcher(expr);
        if (m.find() ) {
            if (m.group(1) != null) {
                return (Arg) local_vars.get(m.group(1));
            }
        }

        try {
            Arg ret = (Arg) new JSONObject(expr);
            return ret;
        }
        catch (Exception e) {

        }

        Pattern var_arr = Pattern.compile(String.format("(?<in>%s)\\[(?<idx>.+)\\]$", _NAME_RE));
        m = var_arr.matcher(expr);
        if (m.find()) {
            if (m.group(1) != null && m.group(2) != null) {
                Arg val = (Arg) local_vars.get(m.group(1));
                Arg idx = interpretExpression(m.group(2), local_vars, allowRecursion - 1);
                int index = idx.getInt(VAL);
                return (Arg) val.getJSONArray(VAL).get(index);
            }
        }

        Pattern var_dec = Pattern.compile(String.format("(?P<var>%s)(?:\\.(?P<member>[^(]+)|\\[(?P<member2>[^]]+)\\])\\s*(?:\\(+(?P<args>[^()]*)\\))?$", _NAME_RE));
        m = var_dec.matcher(expr);

        if (m.find()) {
            if (m.group(1) != null) {
                String variable = m.group(1);
                String member = Utils.removeQuotes((m.group(2) != null) ? m.group(2) : m.group(3));
                String arg_str = m.group(3);

                JSONObject obj;
                if (local_vars.has(variable)) {
                    obj = (Arg) local_vars.get(variable);
                    obj = obj.getJSONObject(VAL);
                }
                else {
                    if (!objects.has(variable)) {
                        objects.put(variable, extractObject(variable));
                    }
                    obj = (JSONObject) objects.get(variable);
                }

            }

        }


        return new Arg(expr);
    }

    Arg opFunc (String op, Arg cur, Arg right_val) {
        Arg ret = new Arg();
        // TODO: do actual operations here

        if (op.equals("|=") | op.equals("|")) {
            try {
                ret = new Arg(cur.getInt(VAL) | right_val.getInt(VAL));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (op.equals("^=") | op.equals("|")) {
            try {
                ret = new Arg(cur.getInt(VAL) | right_val.getInt(VAL));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (op.equals("&=")) {
            try {
                ret = new Arg(cur.getInt(VAL) | right_val.getInt(VAL));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (op.equals(">>=")) {

        }
        else if (op.equals("<<=")) {

        }
        else if (op.equals("-=")) {

        }
        else if (op.equals("+=")) {

        }
        else if (op.equals("%=")) {

        }
        else if (op.equals("/=")) {

        }
        else if (op.equals("*=")) {

        }
        else if (op.equals("=")) {

        }

            return ret;
    }


    /*
    def extract_object(self, objname):
        _FUNC_NAME_RE = r'''(?:[a-zA-Z$0-9]+|"[a-zA-Z$0-9]+"|'[a-zA-Z$0-9]+')'''
        obj = {}
        obj_m = re.search(
            r'''(?x)
                (?<!this\.)%s\s*=\s*{\s*
                    (?P<fields>(%s\s*:\s*function\s*\(.*?\)\s*{.*?}(?:,\s*)?)*)
                }\s*;
            ''' % (re.escape(objname), _FUNC_NAME_RE),
            self.code)
        fields = obj_m.group('fields')
        # Currently, it only supports function definitions
        fields_m = re.finditer(
            r'''(?x)
                (?P<key>%s)\s*:\s*function\s*\((?P<args>[a-z,]+)\){(?P<code>[^}]+)}
            ''' % _FUNC_NAME_RE,
            fields)
        for f in fields_m:
            argnames = f.group('args').split(',')
            obj[remove_quotes(f.group('key'))] = self.build_function(argnames, f.group('code'))

        return obj

     */

    JSONObject extractObject (String objname) {
        JSONObject obj = new JSONObject();
        String _FUNC_NAME_RE = "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')";
        Pattern objpatt = Pattern.compile(String.format("(?x)\n" +
                "                (?<!this\\.)%s\\s*=\\s*{\\s*\n" +
                "                    (?<fields>(%s\\s*:\\s*function\\s*\\(.*?\\)\\s*{.*?}(?:,\\s*)?)*)\n" +
                "                }\\s*;", Pattern.quote(objname), _FUNC_NAME_RE));
        Matcher m = objpatt.matcher(code);
        if (m.find()) {
            String fields = m.group(1);
            Pattern field_pat = Pattern.compile(String.format("(?x)\n" +
                    "                (?<key>%s)\\s*:\\s*function\\s*\\((?<args>[a-z,]+)\\){(?<code>[^}]+)}", _FUNC_NAME_RE));
            m = objpatt.matcher(fields);
            while (m.find()) {
                String[] args = m.group(2).split(",");
                try {
                    obj.put(Utils.removeQuotes(m.group(1)), new Fun(m.group(1), args, m.group(3)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return obj;
    }


}
