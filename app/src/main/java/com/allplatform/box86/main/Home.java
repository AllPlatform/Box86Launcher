package com.allplatform.box86.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.allplatform.box86.MainActivity;
import com.allplatform.box86.R;
import com.allplatform.box86.other.Run;
import com.allplatform.box86.other.mDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {
    private List<Homes> HomeList = new ArrayList<>();
    private HomeAdapter HomeAdapter;
    ListView listView;
    String PKGNAME = "/data/data/com.allplatform.box86";
    String HOME = PKGNAME + "/home";
    String WINE = HOME + "/Wine/x86/bin/wine";
    String BOX86 = HOME + "/Wine/box86";
    String WINESCR = MainActivity.winescr.getString("scr","");
    String DESKTOPSHELL = "explorer /desktop=shell," + WINESCR;
    String USER;
    String ICONPATH = HOME + "/.local/share/icons/hicolor";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.home, container, false);
         runfilemgr();
         Addlist();
         listView = (ListView) view.findViewById(R.id.homelist);
         HomeAdapter = new HomeAdapter(getContext(),0, HomeList);
         listView.setAdapter(HomeAdapter);
         if (HomeList.isEmpty()) {
            //
         }else {
             TextView textView = (TextView) view.findViewById(R.id.empty);
             textView.setText("");
         }
         ListClick();
         return view;
    }

    private void ListClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == position) {
                    RunFromListClick(HomeList.get(position).path);
                }

            }
        });

    }

    private void RunFromListClick(String Winefrefix) {
        //bolean MODE = false; cmd shell Mode
        boolean MODE = true;
        Run mRun = new Run();
        mRun.RunWine(WINESCR, Winefrefix, MODE, getContext());
    }

    private void Addlist() {
        try {
            Process process = Runtime.getRuntime().exec("whoami");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            USER = br.readLine();
        }catch (IOException e) {
            Toast.makeText(getContext(), "ERROR: Home.java:89", Toast.LENGTH_SHORT).show();
        }
        String Desktop = HOME + "/.wine/drive_c/users/" + USER + "/Desktop";
        File file = new File(Desktop);
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".desktop");
            }
        });
        String path = "";
        String icon = "";
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                try {
                    BufferedReader br = new BufferedReader(
                            new FileReader(files[i].getPath()));
                    for (int n = 0; n < 6; n++) {
                        if (n == 2) {
                            path = br.readLine()
                                    .replaceAll(" wine ", " " + BOX86 + " " + WINE + " " + DESKTOPSHELL + " ")
                                    .replaceAll("Exec=env ", "");
                        } else if (n == 5) {
                            icon = br.readLine()
                                    .replaceAll("Icon=", "") + ".png";
                        }
                        br.readLine();
                    }

                } catch (IOException e){

                }
                HomeList.add(new Homes(files[i].getName()
                        .replaceAll(".desktop", ""), icon, path));

            }
        } else {
            //
        }

    }

    private void runfilemgr() {

        if (MainActivity.data.getInt("home",0) == 1) {
            //
        } else {
            FragmentActivity fragmentActivity = getActivity();
            mDialog mdialog = new mDialog(fragmentActivity, getContext(), R.string.notification, R.string.notification1, "OK", "Don't Show Again", 1);
        }
    }

    public class Homes {
        private String name;
        private String icon;
        private String path;
        public Homes(String name, String icon, String path) {
            this.name = name;
            this.icon = icon;
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }

        public String getPath() {
            return path;
        }

    }
    public class HomeAdapter extends ArrayAdapter<Homes>{

        List<Homes> HomeList;
        public HomeAdapter(@NonNull Context context, int resource, List<Homes> objects) {
            super(context, resource, objects);
            HomeList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            if(listItemView == null){
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.library_layout, parent, false);
            }
            TextView nameTextView = (TextView) listItemView.findViewById(R.id.libname);
            nameTextView.setText(HomeList.get(position).getName());
            ImageView imageView = (ImageView) listItemView.findViewById(R.id.logolib);
            Bitmap myBitmap = BitmapFactory.decodeFile(ICONPATH + "/256x256/apps/" + HomeList.get(position).icon);
            if (myBitmap == null) {
                myBitmap = BitmapFactory.decodeFile(ICONPATH + "/48x48/apps/" + HomeList.get(position).icon);
            }
            imageView.setImageBitmap(myBitmap);

            return listItemView;
        }
    }

}
