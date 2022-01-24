package com.allplatform.box86.other;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.allplatform.box86.R;
import com.allplatform.box86.main.Library;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Filemgr extends DialogFragment {
    private List<FileName> FileList;
    private FileAdapter FileAdapter;
    private ListView listView;
    static File file;
    File[] files;
    String path = "/sdcard";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.filemgr, container, false);
        listView = (ListView) view.findViewById(R.id.filemgr);
        ListFile();
        FileAdapter = new FileAdapter(getContext(), 0, FileList);
        listView.setAdapter(FileAdapter);
        ItemClick();
        return view;
    }

    private void ItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                path = String.valueOf(file.getParentFile());
                FileList.clear();
                FileAdapter.notifyDataSetChanged();
                listView.invalidateViews();
                listView.refreshDrawableState();
                ListFile();
                }else if (position == position){
                    path = String.valueOf(file) + "/" + FileList.get(position).getName();
                    File check = new File(path);
                    if (check.isFile() && path.endsWith(".so")){
                        try {
                            Runtime.getRuntime().exec("cp " + check + " " + Library.LIBDIR);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "ERROR Copy File", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            Runtime.getRuntime().exec("chmod 777 " + Library.LIBDIR
                                    + "/" + FileList.get(position).getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), FileList.get(position).getName() + " Is Added", Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();

                    }else if (check.isHidden()) {

                    } else if (check.isDirectory()){
                        FileList.clear();
                        FileAdapter.notifyDataSetChanged();
                        listView.invalidateViews();
                        listView.refreshDrawableState();
                        ListFile();
                    } else {
                        dismiss();
                    }
                }
            }
        });
    }

    private void ListFile() {
        FileList = new ArrayList<>();
        file = new File(path);
        if (String.valueOf(file).equals("/sdcard")) {
            //
        }else{
            FileList.add(new FileName(".."));
        }
        files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Long.compare(o2.lastModified(), o1.lastModified());
            }
        });
        for (int i = 0; i < files.length; i++){
            FileList.add(new FileName(files[i].getName()));
        }
        Collections.sort(FileList, new Comparator<FileName>() {
            @Override
            public int compare(FileName o1, FileName o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        listView.invalidateViews();
        listView.refreshDrawableState();
        FileAdapter = new FileAdapter(getContext(), 0, FileList);
        listView.setAdapter(FileAdapter);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    public class FileName {
        private String name;

        public FileName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public static class FileAdapter extends ArrayAdapter<FileName> {
        private static final String TAG = FileAdapter.class.getSimpleName();

        List<FileName> FileList;

        public FileAdapter(Context context, int resource, List<FileName> objects) {
            super(context, resource, objects);

            FileList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;

            if(listItemView == null){
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.library_layout, parent, false);
            }
            File mFile = new File(file + "/" + FileList.get(position).getName());
            TextView nameTextView = (TextView) listItemView.findViewById(R.id.libname);
            nameTextView.setText(FileList.get(position).getName());
            ImageView imageView = (ImageView) listItemView.findViewById(R.id.logolib);
            if (FileList.get(position).getName().equals("..")){
                imageView.setImageResource(R.drawable.back72x72);
            }else if (mFile.isDirectory() || mFile.isHidden()){
                imageView.setImageResource(R.drawable.openfl64x64);
            }else if (mFile.isFile() && FileList.get(position).getName().endsWith(".tar")){
                imageView.setImageResource(R.drawable.tar72x72);
            }else if (mFile.isFile() && FileList.get(position).getName().endsWith(".so")){
                imageView.setImageResource(R.drawable.lib72x72);
            }else if (mFile.isFile()){
                imageView.setImageResource(R.drawable.un72x72);
            }
            return listItemView;
        }
    }
}