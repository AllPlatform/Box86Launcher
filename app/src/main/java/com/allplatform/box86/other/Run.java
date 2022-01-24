package com.allplatform.box86.other;

import android.content.Context;
import android.content.Intent;

import com.allplatform.box86.MainActivity;
import com.termux.app.RunCommandService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Run {
    final String PKG = "/data/data/com.allplatform.box86";
    final String BOX86 = PKG + "/home/Wine/box86";
    final String WINE = PKG + "/home/Wine/x86/bin/wine";
    final String LIBDIR = PKG+ "/home/Wine/lib";
    final String LD_LIBRARY_PATH="LD_LIBRARY_PATH=" + LIBDIR;
    final String RUN = PKG + "/home/run.sh";
    public void RunWine(String Winescr, String Winefrefix, boolean MODE, Context context) {
        final String SETTING;
        final String WINEFREFIX = Winefrefix;
        String WINESCR = Winescr;
        String TFM = PKG + "/home/TFM.exe";
        String DESKTOPSHELL = "explorer /desktop=shell," + WINESCR + " " + TFM;
        if (MainActivity.setting.getAll().toString().replaceAll("\\{","").replaceAll("\\}","") == ""){
            SETTING = "";
        }else {
            SETTING = MainActivity.setting.getAll().toString().replaceAll("\\{", "").replaceAll("\\}", "").replaceAll(",","") + " ";
        }

        if (WINEFREFIX == null) {
            try {
                FileWriter mFileWriter = new FileWriter(RUN);
                mFileWriter.write(LD_LIBRARY_PATH + " " + SETTING + " " + BOX86 + " " + WINE + " " + DESKTOPSHELL + ">/sdcard/Box86Log.txt");
                mFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter mFileWriter = new FileWriter(RUN);
                mFileWriter.write(LD_LIBRARY_PATH + " " + SETTING + " " + WINEFREFIX);
                mFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Runtime.getRuntime().exec("chmod 777 " + RUN);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //x.org.server.Settings

        File resetsetting = new File(PKG + "/files/libsdl-settings.cfg");
        if (resetsetting.exists()) {
            resetsetting.delete();
        }
        //RUN XServer XSDL
        context.startActivity(new Intent(context, x.org.server.MainActivity.class));
        //Run TERMUX Service
        StartTermux(context, RUN ,MODE);
    }

    private void StartTermux(Context context, String RUN, boolean MODE) {
        context.startService(new Intent(context, RunCommandService.class)
                .putExtra("com.termux.RUN_COMMAND_PATH", "/system/bin/sh")
                .putExtra("com.termux.RUN_COMMAND_ARGUMENTS", new String[]{RUN})
                .putExtra("com.termux.RUN_COMMAND_BACKGROUND", MODE));
    }

    public void WineCmd(Context context, boolean MODE) {
        try {
            FileWriter mFileWriter = new FileWriter(RUN);
            mFileWriter.write(LD_LIBRARY_PATH + " " + BOX86 + " " + WINE + " " + "cmd");
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StartTermux(context, RUN, MODE);
    }
}
