package com.allplatform.box86.other;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.allplatform.box86.MainActivity;
import com.allplatform.box86.R;

public class mDialog {
    public mDialog(FragmentActivity fragmentActivity, Context context, int title, int body, String ok, String cancel, int code) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog, null);
        TextView Tntitle = (TextView) view.findViewById(R.id.dialog_title);
        Tntitle.setText(title);
        TextView Tbody = (TextView) view.findViewById(R.id.dialog_body);
        Tbody.setText(body);
        TextView Tok = (TextView) view.findViewById(R.id.dialog_ok);
        Tok.setText(ok);
        TextView Tcancel = (TextView) view.findViewById(R.id.dialog_cancel);
        Tcancel.setText(cancel);
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        SharedPreferences.Editor Editor = MainActivity.data.edit();
        if (code == 1) {
            click(fragmentActivity, Editor, dialog, Tcancel, Tok);
        }
        if (code == 2) {
            click2(Editor, dialog, Tcancel, Tok);
        }
    }
    private void click2(SharedPreferences.Editor Editor, Dialog dialog, TextView Tcancel, TextView Tok) {
        Tok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Tcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Editor.putInt("lib", 1).apply();
            }
        });
    }

    private void click(FragmentActivity fragmentActivity, SharedPreferences.Editor Editor, Dialog dialog, TextView Tcancel, TextView Tok) {
        Tok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (MainActivity.data.getInt("InstallAssets", 0) == 1) {
                    //
                } else {
                    InstallAssets installAssets = new InstallAssets();
                    installAssets.show(fragmentActivity.getSupportFragmentManager(), "install");
                    Editor.putInt("InstallAssets", 1).apply();
                }
            }
        });
        Tcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Editor.putInt("home", 1).apply();
                if (MainActivity.data.getInt("InstallAssets", 0) == 1) {
                    //
                } else {
                    InstallAssets installAssets = new InstallAssets();
                    installAssets.show(fragmentActivity.getSupportFragmentManager(), "install");
                    Editor.putInt("InstallAssets", 1).apply();
                }
            }
        });
    }
}
