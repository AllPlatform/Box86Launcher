package com.allplatform.box86.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.allplatform.box86.MainActivity;
import com.allplatform.box86.R;

import java.util.ArrayList;
import java.util.List;

public class Setting extends Fragment {
    private List<Settings> ListSetting;
    private SettingAdapter SettingAdapter;
    ListView listView;
    static SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);
        sharedPreferences = getContext().getSharedPreferences("SavedSetting", Context.MODE_PRIVATE);
        MainActivity.setting = getContext().getSharedPreferences("DataSetting", Context.MODE_PRIVATE);
        ListSetting = new ArrayList<>();
        ListSetting.add(new Settings("BOX86_LOG", sharedPreferences.getString("0", ""),"Controls the Verbosity level of the logs\n" +
                " * 0: NONE : No message (except some fatal error). (Default.)\n" +
                " * 1: INFO : Show some minimum log (Example: librairies not found)"));
        ListSetting.add(new Settings("BOX86_DUMP", sharedPreferences.getString("1", ""), "Controls the Dump of elf content\n" +
                " * 0: No dump of elf information (Default)\n" +
                " * 1: Dump elf sections and relocations and other information"));
        ListSetting.add(new Settings("BOX86_DLSYM_ERROR", sharedPreferences.getString("2", ""), "Enables/Disables the logging of `dlsym` errors.\n" +
                " * 0 : Don't log `dlsym` errors. (Default.)\n" +
                " * 1 : Log dlsym errors."));
        ListSetting.add(new Settings("BOX86_NOSIGSEGV", sharedPreferences.getString("3", ""), "Disable handling of SigSEGV. (Very useful for debugging.)\n" +
                " * 0 : Let the x86 program set sighandler for SEGV (Default.)\n" +
                " * 1 : Disable the handling of SigSEGV."));
        ListSetting.add(new Settings("BOX86_NOSIGILL", sharedPreferences.getString("4", ""), "Disable handling of SigILL (to ease debugging mainly).\n" +
                " * 0 : Let x86 program set sighandler for Illegal Instruction\n" +
                " * 1 : Disables the handling of SigILL"));
        ListSetting.add(new Settings("BOX86_X11GLX", sharedPreferences.getString("5", ""), "Force libX11's GLX extension to be present.\n" +
                "* 0 : Do not force libX11's GLX extension to be present. \n" +
                "* 1 : GLX will always be present when using XQueryExtension. (Default.)"));
        ListSetting.add(new Settings("BOX86_DYNAREC_DUMP", sharedPreferences.getString("6", ""), "Enables/disables Box86's Dynarec's dump.\n" +
                " * 0 : Disable Dynarec's blocks dump. (Default.)\n" +
                " * 1 : Enable Dynarec's blocks dump."));
        ListSetting.add(new Settings("BOX86_DYNAREC_LOG", sharedPreferences.getString("7", ""), "Set the level of DynaRec's logs.\n" +
                " * 0 : NONE : No Logs for DynaRec. (Default.)\n" +
                " * 1 : INFO : Minimum Dynarec Logs (only unimplemented OpCode)."));
        ListSetting.add(new Settings("BOX86_DYNAREC", sharedPreferences.getString("8", ""), "Enables/Disables Box86's Dynarec.\n" +
                " * 0 : Disables Dynarec.\n" +
                " * 1 : Enable Dynarec. (Default.)"));
        ListSetting.add(new Settings("BOX86_DYNAREC_TRACE", sharedPreferences.getString("9", ""), "Enables/Disables trace for generated code.\n" +
                " * 0 : Disable trace for generated code. (Default.)\n" +
                " * 1 : Enable trace for generated code (like regular Trace, this will slow down the program a lot and generate huge logs)."));
        ListSetting.add(new Settings("BOX86_ALLOWMISSINGLIBS", sharedPreferences.getString("10", ""),"Allow Box86 to continue even if a library is missing.\n" +
                " * 0 : Box86 will stop if a library cannot be loaded. (Default.)\n" +
                " * 1 : Continue even if a needed library cannot be loaded. Unadvised, this will, in most cases, crash later on."));
        ListSetting.add(new Settings("BOX86_NOPULSE", sharedPreferences.getString("11", ""), "Disables the load of pulseaudio libraries.\n" +
                " * 0 : Load pulseaudio libraries if found. (Default.)\n" +
                " * 1 : Disables the load of pulse audio libraries (libpulse and libpulse-simple), both the native library and the x86 library"));
        ListSetting.add(new Settings("BOX86_NOVULKAN", sharedPreferences.getString("12", ""), "Disables the load of vulkan libraries.\n" +
                " * 0 : Load vulkan libraries if found.\n" +
                " * 1 : Disables the load of vulkan libraries, both the native and the i386 version (can be useful on Pi4, where the vulkan driver is not quite there yet.)"));

        listView = (ListView) view.findViewById(R.id.settinglistview);
        SettingAdapter = new SettingAdapter(getContext(), 0, ListSetting);
        listView.setAdapter(SettingAdapter);
        ItemSetting();
        return view;
    }

    private void ItemSetting() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == position){
                    SetOnOff(position);
                }
            }
        });
    }

    private void SetOnOff(int position) {
        SharedPreferences.Editor editor1 = MainActivity.setting.edit();
        sharedPreferences = getContext().getSharedPreferences("SavedSetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle((CharSequence) ListSetting.get(position).getName());
        alertDialog.setMessage((CharSequence) ListSetting.get(position).getDesciption());
        alertDialog.setPositiveButton("Disabled", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListSetting.set(position, new Settings(ListSetting.get(position).getName(),"",ListSetting.get(position).description));
                SettingAdapter.notifyDataSetChanged();
                listView.invalidateViews();
                listView.refreshDrawableState();
                editor.putString(String.valueOf(position), "").apply();
                editor1.remove(ListSetting.get(position).getName()).apply();
            }
        });
        alertDialog.setNegativeButton("Enabled", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListSetting.set(position, new Settings(ListSetting.get(position).getName(),"1",ListSetting.get(position).description));
                SettingAdapter.notifyDataSetChanged();
                listView.invalidateViews();
                listView.refreshDrawableState();
                editor.putString(String.valueOf(position), "1").apply();
                editor1.putString(ListSetting.get(position).getName(),ListSetting.get(position).getAge()).apply();
            }
        });
        alertDialog.show();

    }

    public class Settings {
        private String name;
        private String age;
        private String description;

        public Settings(String name, String age, String description) {
            this.name = name;
            this.age = age;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getAge() {
            return age;
        }

        public String getDesciption() {
            return description;
        }
    }

    public static class SettingAdapter extends ArrayAdapter<Settings> {
        private static final String TAG = SettingAdapter.class.getSimpleName();

        List<Settings> ListSetting;

        public SettingAdapter(Context context, int resource, List<Settings> objects) {
            super(context, resource, objects);

            ListSetting = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            if(listItemView == null){
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_setting, parent, false);
            }
            TextView nameTextView = (TextView) listItemView.findViewById(R.id.env);
            nameTextView.setText(ListSetting.get(position).getName());
            TextView ageTextView = (TextView) listItemView.findViewById(R.id.onoff);
            if (ListSetting.get(position).getAge().equals("")) {
                ageTextView.setText("Disabled");
                ageTextView.setTextColor(Color.parseColor("#DC143C"));
            }else {
                ageTextView.setText("Enabled");
                ageTextView.setTextColor(Color.parseColor("#2979FF"));
            }

            return listItemView;
        }
    }
}
