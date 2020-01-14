package tr.edu.mu.ceng.gui.restaurantapp;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import tr.edu.mu.ceng.gui.restaurantapp.interfaces.RefreshDeskStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;



public class StaffService extends Service{

    private static Timer timer = new Timer();
    private Context ctx;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        ctx = this;
        startService();
    }

    private void startService() {
        timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
    }

    private class mainTask extends TimerTask {
        public void run() {
            toastHandler.sendEmptyMessage(0);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {

        }
    };
}
