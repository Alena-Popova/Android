package com.example.helenapopova.mythirdapplication;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.helenapopova.mythirdapplication.connect.GmailLissener;
import com.example.helenapopova.mythirdapplication.fragments.MainFragment;
import com.example.helenapopova.mythirdapplication.fragments.Settings;


public class LaunchActivity extends AppCompatActivity {

    private final String TAG = "lifecicle";
    private Settings fragSettings;
    private MainFragment mainFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        fragSettings = new Settings();
        mainFragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.general_window, mainFragment)
                .addToBackStack(mainFragment.getClass().getSimpleName())
                .commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menulaunch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_settings:
                openNeedsFragmentViwer(R.id.set_settings);
                return true;
            case R.id.update:
                outputTost("Go to update!");
                return true;
            case R.id.support_service:
                sendMessageToSuoourtService();
                return true;
            case R.id.show_tempereture:
                openNeedsFragmentViwer(R.id.show_tempereture);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Выводит логи в режиме дебага
     * @param message сообшения логгера
     */
    public void outputLogs(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

//_______________________________________________________________________________
//-------------------------------------------------------------------------------

    public void outputTost(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void sendMessageToSuoourtService() {
        GmailLissener gmailLissener = new GmailLissener(this);
        gmailLissener.sendMessageToEmail();
    }

    public void openNeedsFragmentViwer(int wivwr) {
        fragmentTransaction = fragmentManager.beginTransaction();
        int size = fragmentManager.getFragments().size();
        switch (wivwr) {
            case R.id.set_settings:
                if (!fragmentManager.getFragments().isEmpty() && fragmentManager.getFragments().get(size - 1).getId() != fragSettings.getId()) {
                    fragmentTransaction
                            .replace(R.id.general_window, fragSettings)
                            .addToBackStack(fragSettings.getClass().getSimpleName()).commit();
                }
                break;
            case R.id.show_tempereture:
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStackImmediate();
                        fragmentTransaction.replace(R.id.general_window, mainFragment).commit();
                    }
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
