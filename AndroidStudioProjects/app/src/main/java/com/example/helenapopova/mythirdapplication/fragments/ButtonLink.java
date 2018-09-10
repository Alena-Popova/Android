package com.example.helenapopova.mythirdapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lombok.Getter;

public class ButtonLink extends Fragment implements View.OnClickListener {

    private FTDIconnector connectorFTDI;
    private final String LOG_TAG = "service ButtonLink";

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

    @Getter
    private byte tempFTDI[] = new byte[2];
    @Getter
    private byte bufferFTDI[] = new byte[254];

    private SharedPreferences sp;
    private Unbinder unbinder;


    private Timer myTimer = new Timer(); // Создаем таймер
    private final Handler uiHandler = new Handler();
    private TimerTask tt;


    public static final String APP_PREFERENCES = "mysettings";

    @Override
    public void onAttach(Context context) {
        sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        super.onAttach(context);

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
        initTimerTask();
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
                runSomeTask();
                break;
            default:
                outputTost("Error #2");
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        for (int i = 0; i < 7; i++) {
            char id = Info.getOperationsMode()[i];
            if (sp.contains(Info.getTitlesMode()[i])) {
                id = (char) (sp.getInt(Info.getTitlesMode()[i], i + 40) + 24);
            }
            int code = Integer.valueOf(id) - 24;
            menu.add(0, id, i + 1, Info.getTitlesMode()[i] + "0x" + code);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textView = inflatedView.findViewById(R.id.mode_data);
        if (onClickSend(Character.toString((char) item.getItemId()))) {
            int code = item.getItemId() - 24;
            textView.setText(Info.getTitlesMode()[item.getOrder()] + "0x" + code);
        } else {
            outputTost("no transaction");
            outputTost("check Devices");
        }
        return true;
    }

    /**
     * Инициализируем драйвер, включаем работу сервиса
     */
    public void onClickStart() {
        connectorFTDI = new FTDIconnector(getActivity());
        try {
            connectorFTDI.init();
        } catch (IOException io) {
            outputLogs("onClickStart crash");
        }
        if (connectorFTDI != null) {
            buttonSelect.setEnabled(true);
        }
    }

    @Override
    public void onPause() {
        onClickStop();
        super.onPause();
    }

    public boolean onClickSend(String operation) {
        onDestroyTimer();
        boolean result = false;
        if (connectorFTDI != null && operation != null && operation.length() != 0) {
            try {
                byte[] mask = new byte[1];
                mask[0] = (byte) operation.charAt(0);
                result = connectorFTDI.writeFTDI(mask, 1000);
            } catch (IOException io) {
                outputTost("io init");
            }
        } else {
            outputTost("Open connection, please!");
        }
        return result;
    }

    /**
     * отключаем работу сервиса и драйвера
     */
    public void onClickStop() {
        onDestroyTimer();
        if (connectorFTDI != null) {
            try {
                connectorFTDI.close();
                connectorFTDI = null;
                buttonSelect.setEnabled(false);
            } catch (IOException io) {
                outputTost("already exit");
            }
        } else {
            outputLogs("onClickStop : connectorFTDI == null");
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
            if (connectorFTDI != null) {
                answer = connectorFTDI.readFTDI(bufferFTDI, 30);
                if (answer == 0) {
                    outputTost("no transaction");
                    outputTost("check Devices");
                } else {
                    if (bufferFTDI.length > 0) {
                        for (int i = 0; i < answer; i++) {
                            if (i == 2 || i == 3) {
                                this.tempFTDI[i - 2] = bufferFTDI[i];
                            }
                            result.append(String.format("%02X", bufferFTDI[i])).append(" ");

                        }
                    }
                }
            }
        } catch (IOException io) {
            outputTost("io init");
        }
        bufferFTDI = new byte[254];
        return result.toString();
    }

    /**
     * Снимает значения температуры ,
     * отправляет на экран
     */
    private void runSomeTask() {
        myTimer.schedule(tt, 0L, 1000);

    }

    /**
     * гасит таймер
     */
    public void onDestroyTimer() {
        tt.cancel();
        myTimer.purge();
        outputLogs("onDestroy timer");
    }

    public void outputLogs(String message) {
        Log.i(LOG_TAG, message);
    }


    /**
     * то,что будет делать такска
     */
    private void initTimerTask() {
        tt = new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendMessageAndGetAnswer();
                        StringBuilder result = new StringBuilder();
                        for (byte i : tempFTDI) {
                            result.append(String.format("%02X", tempFTDI[i]));
                        }
                        /*if (result != null)
                            temperature.setText(String.format("%d C", Integer.parseInt(result.toString(), 16)));*/
                    }
                });
            }
        };
    }

}
