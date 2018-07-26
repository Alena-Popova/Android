package com.example.helenapopova.mythirdapplication.box;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helenapopova.mythirdapplication.R;

import java.util.ArrayList;

import lombok.Getter;

public class BoxAdapter extends BaseAdapter {
    private Context context;
    @Getter
    ArrayList<Mode> settings;
    LayoutInflater layoutInflater;
    public static final String APP_PREFERENCES = "mysettings";
    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;
    String contextText;

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

        ((TextView) view.findViewById(R.id.mode_title)).setText(p.getTitle());
        final EditText editText = view.findViewById(R.id.custum_mode_data);
        editText.setHint(p.getHint());
        if (sp.contains(p.getTitle())) {
            contextText = sp.getString(p.getTitle(), String.valueOf('\u0000'));
            editText.setText(contextText);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().toString().length() != 0 && !editText.getText().toString().equals(String.valueOf('\u0000'))) {
                   editor.putString(p.getTitle(), editText.getText().toString()).apply();
                }
            }
        });
        return view;
    }

    public void outputTost(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
