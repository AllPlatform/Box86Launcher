package com.allplatform.box86.other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.allplatform.box86.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InstallAssets extends DialogFragment {
    ProgressBar progressBar;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_loading, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        getDialog().setCancelable(false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    unzip(new File("/data/data/com.allplatform.box86/home"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Runtime.getRuntime().exec("chmod -R 777 /data/data/com.allplatform.box86/home");
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error chmod", Toast.LENGTH_SHORT).show();
                }
            }
        });
        thread.start();
        return view;
    }
    public void unzip(File targetDirectory) throws IOException {
        InputStream inputStream = getActivity().getAssets().open("WineBox86.zip");
        ZipInputStream zis = new ZipInputStream(inputStream);
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
            dismiss();
        }
    }
}

