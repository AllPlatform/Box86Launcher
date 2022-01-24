package com.allplatform.box86.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.allplatform.box86.R;
import com.allplatform.box86.other.Run;

public class About extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container, false);
        Button button = (Button) view.findViewById(R.id.button9);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), x.org.server.MainActivity.class));
            }
        });
        Button button1 = (Button) view.findViewById(R.id.button10);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), com.termux.app.TermuxActivity.class));
            }
        });

        Button button2 = (Button) view.findViewById(R.id.WineCMD);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean MODE = false;
                Run run = new Run();
                run.WineCmd(getContext(), MODE);
            }
        });

        return view;
    }
}
