package com.mero.wyt_register.xposed;

import android.telephony.TelephonyManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.view.View.X;

/**
 * Created by chenlei on 2016/10/15.
 */

public class XposedHookModule implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XSharedPreferences pre = new XSharedPreferences(this.getClass().getPackage().getName(),"prefs");
        HookMethod(TelephonyManager.class, "getDeviceId",
                pre.getString("imei", null));
    }

    private void HookMethod(final Class cl, final String method,
                            final String result)
    {
        try
        {
            XposedHelpers.findAndHookMethod(cl, method,
                    new Object[] { new XC_MethodHook()
                    {
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable
                        {
                            param.setResult(result);
                        }

                    } });
        } catch (Throwable e)
        {
          Logger.getAnonymousLogger().log(Level.ALL,"所有日志信息");
        }
    }
}
