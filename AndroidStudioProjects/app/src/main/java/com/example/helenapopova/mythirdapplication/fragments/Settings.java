package com.example.helenapopova.mythirdapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helenapopova.mythirdapplication.BuildConfig;
import com.example.helenapopova.mythirdapplication.R;
import com.example.helenapopova.mythirdapplication.box.BoxAdapter;
import com.example.helenapopova.mythirdapplication.box.Mode;
import com.example.helenapopova.mythirdapplication.connect.Info;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lombok.Data;

/**
 * фрагмент для скрина с начтройками
 */
public class Settings extends Fragment {
    private Context context;
    private View fragmentSettings;
    ArrayList<Mode> settings = new ArrayList<>();
    BoxAdapter boxAdapter;
    private Unbinder unbinder;

    @BindView(R.id.list_settings)
    ListView listView;
    @BindView(R.id.settind_clean)
    Button reset;
    LayoutInflater inflaterSett;
    public static final String APP_PREFERENCES = "mysettings";
    private final String TAG = "settingsFragment";
    private  SharedPreferences sp;
    @Override
    public void onAttach(Context _context) {
        this.context = _context;
        sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        super.onAttach(_context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * дгружает настройки из файла setArrays с помощью ресурсов getSharedPreferences и инициализирует кнопку
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflaterSett = inflater;
        fragmentSettings = inflater.inflate(R.layout.settings_fragment, container, false);
        unbinder = ButterKnife.bind(this, fragmentSettings);
        setArrays();
        setResetButton();
        return fragmentSettings;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * очищает наш файл с хранимыми настройками
     */
    public void setResetButton() {
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
        listView.setAdapter(boxAdapter);


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * ыстро заполняем внутренние списки значениями
     */
    public void fillData() {
        int i;
        settings.clear();
        for (i = 0; i < Info.getSize(); i++) {
            String operation = String.valueOf(Info.getOperationsMode()[i]);
           settings.add(new Mode(Info.getTitlesMode()[i], operation, Info.getHints()[i]));
        }
    }

    public void outputTost(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void outputLogs(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

}

