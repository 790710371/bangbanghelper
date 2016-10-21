package com.mero.wyt_register.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chenlei on 2016/10/21.
 */

public class BashUtils {
    private static final String TAG = "BashUtils";
    public static String do_exec(String cmd) {
        String s = "\n";
        try {
            String[] cmdline = { "sh", "-c", cmd};
            Process p = Runtime.getRuntime().exec(cmdline);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(p.getOutputStream())), true);
            //out.println(cmd);
            while ((line = in.readLine()) != null) {
                s += line + "\n";
            }
            in.close();
//          out.close();
            Log.v(TAG, s);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // text.setText(s);
        return s;
    }
}
