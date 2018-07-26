package com.example.helenapopova.mythirdapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helenapopova.mythirdapplication.R;
import com.example.helenapopova.mythirdapplication.box.BoxAdapter;
import com.example.helenapopova.mythirdapplication.box.Mode;
import com.example.helenapopova.mythirdapplication.connect.Info;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import lombok.Data;

public class Settings extends Fragment {
    private static String[] operationsMode = new String[Info.getSize()];
    private Context context;
    private View fragmentSettings;
    ArrayList<Mode> settings = new ArrayList<>();
    BoxAdapter boxAdapter;
    ListView listView;
    Button reset;
    LayoutInflater inflaterSett;
    public static final String APP_PREFERENCES = "mysettings";
    private  SharedPreferences sp;
    @Override
    public void onAttach(Context _context) {
        this.context = _context;
        super.onAttach(_context);
        sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflaterSett = inflater;
        fragmentSettings = inflater.inflate(R.layout.settings_fragment, container, false);
        setArrays();
        setResetButton();
        return fragmentSettings;
    }

    public void setResetButton() {
        reset = fragmentSettings.findViewById(R.id.settind_clean);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().clear().commit();
            }
        });
    }

    /**
     * Задание списка настроек
     */
    private void setArrays() {
        fillData();
        boxAdapter = new BoxAdapter(context, this.settings);
        listView = fragmentSettings.findViewById(R.id.list_settings);
        listView.setAdapter(boxAdapter);


    }

    @Override
    public void onPause() {
        outputTost(String.valueOf(boxAdapter.getCount()));
        super.onPause();
    }


    public void fillData() {
        int i;
        settings.clear();
        for (i = 0; i < Info.getSize(); i++) {
            operationsMode[i] = String.valueOf(Info.getOperationsMode()[i]);
           settings.add(new Mode(Info.getTitlesMode()[i], operationsMode[i], Info.getHints()[i]));
        }
    }

    public void outputTost(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}

