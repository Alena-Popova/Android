package com.example.helenapopova.mythirdapplication.box;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helenapopova.mythirdapplication.BuildConfig;
import com.example.helenapopova.mythirdapplication.R;
import com.example.helenapopova.mythirdapplication.connect.Info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoxAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Mode> settings;
    private LayoutInflater layoutInflater;
    private static final String APP_PREFERENCES = "mysettings";
    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;
    private int contextText;
    Map<String, String> tagAndValue = new HashMap<>();
    private final String TAG = "settingsBoring";

    public BoxAdapter(Context _contex, ArrayList<Mode> _modes) {
        this.context = _contex;
        this.settings = _modes;
        layoutInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public int getCount() {
        return this.settings.size();
    }

    @Override
    public Object getItem(int position) {
        return this.settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_mode, parent, false);
        }
        final Mode p = this.settings.get(position);
        outputLogs(String.valueOf(position));
        CheckBox checkBox = view.findViewById(R.id.checkSave);

        ((TextView) view.findViewById(R.id.mode_title)).setText(p.getTitle());
        final EditText editText = view.findViewById(R.id.custum_mode_data);
        editText.setHint(p.getHint());
        if (sp.contains(p.getTitle())) {
            contextText = sp.getInt(p.getTitle(), 1);
            editText.setText(String.valueOf(contextText));
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSave = editText.getText().toString();
                if(!TextUtils.isEmpty(textSave) && !textSave.equals(contextText)) {
                    editor.putInt(p.getTitle(), Integer.parseInt(textSave)).apply();
                } else if (textSave.length() == 0  && sp.contains(p.getTitle())) {
                    editor.remove(p.getTitle()).apply();
                }
            }
        });
        return view;
    }

    public void outputTost(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void outputLogs(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getViewTypeCount() {
        return Info.getSize();
    }

    @Override
    public int getItemViewType(int position) {
        /* calculate the view type for this row */
        return position;
    }
}
