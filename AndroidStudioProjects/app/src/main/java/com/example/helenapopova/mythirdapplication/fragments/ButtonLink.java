package com.example.helenapopova.mythirdapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helenapopova.mythirdapplication.R;
import com.example.helenapopova.mythirdapplication.connect.FTDIconnector;
import com.example.helenapopova.mythirdapplication.connect.Info;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lombok.Getter;

public class ButtonLink extends Fragment implements View.OnClickListener {

    private FTDIconnector connectorFTDI;

    @BindView(R.id.data_operation)
    EditText textViewSelectMode;

    @BindView(R.id.button)
    Button buttonStart;
    @BindView(R.id.button2)
    Button buttonStop;
    @BindView(R.id.button_for_select_mode)
    Button buttonSelect;

    View inflatedView;
    @BindView(R.id.temperature_in_data)
    TextView temperature;
    @BindView(R.id.accepted_data)
    TextView acceptStr;

    SharedPreferences sp;
    Context contextButton;
    private Unbinder unbinder;


    @Getter
    private byte bufferFTDI[] = new byte[254];
    public static final String APP_PREFERENCES = "mysettings";

    @Override
    public void onAttach(Context context) {
        sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        super.onAttach(context);
        contextButton = context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.button_link, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);
        // обработчики нажатия кнопок
        registerButtons(inflatedView);
        // контекстное меню для селекта
        registerForContextMenu(buttonSelect);
        return inflatedView;
    }

    public void registerButtons(View context) {
        buttonSelect.setEnabled(false);
    }



    @Override
    @OnClick({R.id.button, R.id.button2, R.id.button_for_select_mode})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                onClickStart();
                break;
            case R.id.button2:
                onClickStop();
                break;
            case R.id.button_for_select_mode:
                sendMessageAndGetAnswer();
                break;
            default:
                outputTost("Error #2");
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        String change;

        for (int i = 0; i < 7; i++) {
            char id = Info.getOperationsMode()[i];
            if (sp.contains(Info.getTitlesMode()[i])) {
                id = (char) (sp.getInt(Info.getTitlesMode()[i],i + 40) + 24);
            }
            int code = Integer.valueOf(id) - 24;
            menu.add(0, id, i + 1, Info.getTitlesMode()[i] + "0x" + code);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textView = inflatedView.findViewById(R.id.mode_data);
        onClickSend(Character.toString((char) item.getItemId()));
        int code = item.getItemId() - 24;
        textView.setText(Info.getTitlesMode()[item.getOrder()] + "0x" + code);
        return true;
    }

    public void onClickStart() {
        connectorFTDI = new FTDIconnector(getActivity());
        try {
            connectorFTDI.init();
        } catch (IOException io) {
            outputTost("io init");
        }
        buttonSelect.setEnabled(true);
    }

    @Override
    public void onPause() {
        onClickStop();
        super.onPause();
    }

    public boolean onClickSend(String operation) {
        boolean result = false;
        if (connectorFTDI != null && operation != null && operation.length() != 0) {
            try {
                byte[] mask = new byte[1];
                mask[0] = (byte) operation.charAt(0);
                connectorFTDI.writeFTDI(mask, 1000);
                result = true;
            } catch (IOException io) {
                outputTost("io init");
            }
        } else {
            outputTost("Open connection, please!");
        }
        return result;
    }

    public void onClickStop() {
        if (connectorFTDI != null) {
            try {
                connectorFTDI.close();
                buttonSelect.setEnabled(false);
            } catch (IOException io) {
                outputTost("already exit");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void outputTost(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    //---------------- Принятие данных , все методы :

    public boolean justSendMessage() {
        boolean result = false;
       // textViewSelectMode = getActivity().findViewById(R.id.data_operation);
        if (textViewSelectMode != null && textViewSelectMode.length() > 0) {
            result = onClickSend(textViewSelectMode.getText().toString());
        } else {
            outputTost("Enter text, please!");
        }
        return result;
    }

    public void sendMessageAndGetAnswer() {
        try {
            byte[] mask = new byte[1];
            int offer = sp.getInt(Info.getTitleDataQuery(), 1);
            mask[0] = (byte) String.valueOf(offer).charAt(0);
            connectorFTDI.writeFTDI(mask, 1000);
            acceptStr.setText(getBufferToStr());

        } catch (IOException io) {
            outputTost("io init");
        }
    }

    public String getBufferToStr() {
        int answer = 0;
        StringBuilder result = new StringBuilder();
        result.append(" ");
        try {
            answer = connectorFTDI.readFTDI(bufferFTDI, 30);
        } catch (IOException io) {
            outputTost("io init");
        }
        for (int i = 0; i < answer; i++) {
            result.append(String.format("%02X", bufferFTDI[i])).append(" ");
        }
        bufferFTDI = new byte[254];
        return result.toString();
    }

}
