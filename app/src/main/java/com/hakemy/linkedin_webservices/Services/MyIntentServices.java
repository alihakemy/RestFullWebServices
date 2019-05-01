package com.hakemy.linkedin_webservices.Services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.EventLogTags;
import android.util.Log;

import com.hakemy.linkedin_webservices.MyJsonClass.JsonFromPojo;
import com.hakemy.linkedin_webservices.RecyclerView.RecyclerView_to_list_data;
import com.hakemy.linkedin_webservices.parser.MyXmlParser;
import com.hakemy.linkedin_webservices.utils.DownloadFile;

import java.io.IOException;


public class MyIntentServices  extends IntentService {

    public static final String  my_service_msg="mymsg";
    public static final String  my_service_payload="mymsg";//key
    public static final String MyException="Myexception";
    private static final String TAG = "MyIntentServices"; //logt


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentServices(String name) {
        super(name);
    }

    public MyIntentServices() {
        super("my");
    }

     /*
      it well called automatic when the services is started and recieve intent object as argument
      declare service in manifests and (android:exported=false) this mean can not call services by another app it to my app only
     */

    @Override
    protected void onHandleIntent( Intent intent) {
        // this method call many times when write startservices manttime
        Uri uri =intent.getData();
        Log.d(TAG, String.valueOf(uri));

        String json=null;

        try {
            // nadias
            // NadiasPassword
            json= DownloadFile.downloadUrl(uri.toString(),intent.getStringExtra("name"),intent.getStringExtra("pass"));



        }
        catch (IOException e) {
            e.printStackTrace();
            Intent intent1 = new Intent(my_service_msg); // constuctor that recieve single string kwon as action this my own action
            intent1.putExtra(MyException, e.getMessage());
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            localBroadcastManager.sendBroadcast(intent1);

        }
        //================================json===========================================
        // Gson gson =new Gson();
        //1- string that contain json file
        //2- tell json while class use to represent data in memory
        //JsonFromPojo jsonFromPojo[] =gson.fromJson(json,JsonFromPojo[].class);
//=================================================================================

//=================================xml=============================================

        if(json!=null)
        {
            JsonFromPojo[] jsonFromPojo = MyXmlParser.parseFeed(json);

            //local Broadcast
            // note you can send JsonFromPOJo  because implement Parcable  that make you set data in extra
            // that mean can package data up in intent and pass back to activity
            Intent intent1 = new Intent(my_service_msg); // constuctor that recieve single string kwon as action
            intent1.putExtra(my_service_payload, jsonFromPojo);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            localBroadcastManager.sendBroadcast(intent1);

        }




    }

    @Override
    public void onCreate() {
        super.onCreate();
        // this call before onHandleIntent
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
