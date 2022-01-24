package com.allplatform.box86.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.allplatform.box86.MainActivity;
import com.allplatform.box86.R;
import com.allplatform.box86.other.Filemgr;
import com.allplatform.box86.other.mDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Library extends Fragment {
    public static final String LIBDIR = "/data/data/com.allplatform.box86/home/Wine/lib";
    private List<Librarys> LibraryList;
    private LibraryAdapter LibraryAdapter;
    ListView listView;
    Filemgr filemgr = new Filemgr();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library, container, false);
        infoaddlib(view);
        LibraryList = new ArrayList<>();
        LibraryList.add(new Librarys("Add new Library"));
        String path = LIBDIR;
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null){
            for (int i = 0; i < files.length; i++){
                LibraryList.add(new Librarys(files[i].getName()));
            }
        }
        listView = (ListView) view.findViewById(R.id.liblistview);
        LibraryAdapter = new LibraryAdapter(getContext(), 0, LibraryList);
        listView.setAdapter(LibraryAdapter);
        ItemLibrary();
        return view;
    }

    private void infoaddlib(View view) {
        if (MainActivity.data.getInt("lib",0) == 1) {
            //
        } else {
            mDialog mdialog = new mDialog(null ,getContext(), R.string.addlib, R.string.contentaddlib, "OK","Don't Show Again", 2);
        }
    }

    private void ItemLibrary() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    AddLibrary();
                }else {
                    LibraryPick(position);
                }
            }

            private void AddLibrary() {
                filemgr.show(getActivity().getSupportFragmentManager(),"File");
            }

            private void LibraryPick(int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Delete File");
                alertDialog.setMessage((CharSequence) LibraryList.get(position).getName());
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(LIBDIR + "/" + LibraryList.get(position).getName());
                        file.delete();
                        LibraryList.remove(position);
                        LibraryAdapter.notifyDataSetChanged();
                        listView.invalidateViews();
                        listView.refreshDrawableState();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }

    public class Librarys {
        private String name;

        public Librarys(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public static class LibraryAdapter extends ArrayAdapter<Librarys> {
        private static final String TAG = LibraryAdapter.class.getSimpleName();

        List<Librarys> LibraryList;

        public LibraryAdapter(Context context, int resource, List<Librarys> objects) {
            super(context, resource, objects);

            LibraryList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;

            if(listItemView == null){
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.library_layout, parent, false);
            }

            TextView nameTextView = (TextView) listItemView.findViewById(R.id.libname);
            nameTextView.setText(LibraryList.get(position).getName());
            ImageView imageView = (ImageView) listItemView.findViewById(R.id.logolib);
            if (position == 0){
                imageView.setImageResource(R.drawable.addfile);
            }else {
                imageView.setImageResource(R.drawable.lib72x72);
            }
            return listItemView;
        }
    }

}
