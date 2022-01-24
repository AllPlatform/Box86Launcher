package com.allplatform.box86.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.allplatform.box86.R;
import com.allplatform.box86.other.Run;

public class wine extends Fragment {
    public static SharedPreferences winescr;
    LinearLayout linearLayout;
    LinearLayout linearLayout2;
    static TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wine, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout3);
        winescr = getContext().getSharedPreferences("WineScreen", Context.MODE_PRIVATE);
        textView = (TextView) view.findViewById(R.id.textView4);
        if (winescr.getString("scr", "") != "") {
            textView.setText(winescr.getString("scr", "").replaceAll("x", "X"));
        }else {
            textView.setText("Screen Not Set");
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenSize screenSize = new ScreenSize();
                screenSize.show(getActivity().getSupportFragmentManager(), "Screen");
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (winescr.getString("scr","") != ""){
                    RunExplorer();
                }else {
                    Toast.makeText(getContext(), "Screen Not Set", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void RunExplorer() {
        //String WINEFREFIX = null;
        //bolean MODE = false; cmd shell Mode
        boolean MODE = true; // graphics Mode
        String WINESCR = winescr.getString("scr","");
        Run run = new Run();
        run.RunWine(WINESCR, null, MODE, getContext());
    }

    public static class ScreenSize extends DialogFragment {
        SharedPreferences.Editor editor = wine.winescr.edit();
        LinearLayout linearLayout640x480, linearLayout800x600, linearLayout1024x768,
                linearLayout1280x720, linearLayout1280x1024, linearLayout1920x1080;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.screensize, container, false);
            linearLayout640x480 = (LinearLayout) view.findViewById(R.id.linearLayout600x800);
            linearLayout800x600 = (LinearLayout) view.findViewById(R.id.linearLayout800x600);
            linearLayout1024x768 = (LinearLayout) view.findViewById(R.id.linearLayout1024x768);
            linearLayout1280x720 = (LinearLayout) view.findViewById(R.id.linearLayout1280x720);
            linearLayout1280x1024 = (LinearLayout) view.findViewById(R.id.linearLayout1280x1024);
            linearLayout1920x1080 = (LinearLayout) view.findViewById(R.id.linearLayout1920x1080);
            ScreenDialog();
            return view;
        }

        private void ScreenDialog() {
            linearLayout640x480.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                    textView.setText("640X480");
                    editor.clear().apply();
                    editor.putString("scr","640x480").apply();
                }
            });
            linearLayout800x600.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                    textView.setText("800X600");
                    editor.clear().apply();
                    editor.putString("scr","800x600").apply();
                }
            });
            linearLayout1024x768.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                    textView.setText("1024X768");
                    editor.clear().apply();
                    editor.putString("scr","1024x768").apply();
                }
            });
            linearLayout1280x720.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                    textView.setText("1280X720");
                    editor.clear().apply();
                    editor.putString("scr","1280x720").apply();
                }
            });
            linearLayout1280x1024.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                    textView.setText("1280X1024");
                    editor.clear().apply();
                    editor.putString("scr","1280x1024").apply();
                }
            });
            linearLayout1920x1080.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                    textView.setText("1920X1080");
                    editor.clear().apply();
                    editor.putString("scr","1920x1080").apply();
                }
            });
        }
    }
}
