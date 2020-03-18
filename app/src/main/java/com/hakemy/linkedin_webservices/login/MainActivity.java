package com.hakemy.linkedin_webservices.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hakemy.linkedin_webservices.MyJsonClass.JsonFromPojo;
import com.hakemy.linkedin_webservices.R;
import com.hakemy.linkedin_webservices.RecyclerView.RecyclerView_to_list_data;
import com.hakemy.linkedin_webservices.Services.MyIntentServices;
import com.hakemy.linkedin_webservices.utils.DownloadFile;
import com.hakemy.linkedin_webservices.utils.NetworkHelper;

import java.util.HashMap;
import java.util.List;

import javax.xml.transform.Source;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {



    private static EditText password,user;
    private TextView textView ,data;
    private Button button;
    private String String_url_of_json="http://560057.youcanlearnit.net/secured/json/itemsfeed.php";
   // private String String_url_of_Xml="http://560057.youcanlearnit.net/secured/xml/itemsfeed.php";
    private  boolean NetworkOk;
    private BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

          if(intent.hasExtra(MyIntentServices.my_service_payload))
            {

                //String msg =intent.getStringExtra(MyIntentServices.my_service_payload);
//===================================================================================================================
                // make error because it is back arry of parceble object not arry of (JsonFromPOJO) to splve that make cast
//                JsonFromPojo jsonFromPojos[] = (JsonFromPojo[]) intent.getParcelableArrayExtra(MyIntentServices.my_service_payload);
//                for (JsonFromPojo jsonFromPojo : jsonFromPojos) {
//               //     data.append(jsonFromPojo.getItemName());
//                //    data.append(jsonFromPojo.getImage());
//
//                }


            }
             else if(intent.hasExtra(MyIntentServices.MyException))
            {

                Toast.makeText(getApplicationContext(), intent.getStringExtra(MyIntentServices.MyException), Toast.LENGTH_LONG).show();
            }



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.text);
        password =(EditText) findViewById(R.id.password);
        user=(EditText) findViewById(R.id.name);
        button =(Button) findViewById(R.id.btn);


        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver,new IntentFilter(MyIntentServices.my_service_msg));

            NetworkOk =NetworkHelper.hasNetworkAcces(this);






    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    public void click(View v)
    {
        //new BackgroundTask().execute("st1","st2","st3")
//=======================================================================================
        /*
           1-id
           2-argument Bundles as key and value
           3-loader callback object that react when the loader start and when return data
           4- initloader for use one time only
           5- to use each time click button must use restartloader

           ??how it is run??
           1-init in oncreateloader
           2-return onloaded
         */
      //  getSupportLoaderManager().restartLoader(0,null,this).forceLoad();


  //=======================================================================================




       // startService(intent);
        //startService(intent);



        new BackgroundTask().execute("");





    }




    /* if any configurattion change run task running the task will be killed like change screen orientation which mean
    asynctask tight to activity life cycle
    */
    // best use to run very quick background task
    //1- input parameter to do in background
    //2-type of data that send to OnProgrssuUpdate
    //3-result

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {

        return new MyasyncTaskLoader(this);
    }
    // endregion background

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        textView.append(data+"\n");

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    /*AsyncTaskloader conatin to
       -vision 1- in main sdk    2-android repository
          - uses for long background tasks and dont worry about change activity life cycle or configuration
          -but  make sure use version in android repository
          -android.support.v4.content
          - loadInBackground run in background thread andr doent have access to (ui)
          -you can pass data to loader during init
          - (note) must be static class

     */
    // generic return value is string
    private  static class  MyasyncTaskLoader extends AsyncTaskLoader<String>
    {

        public MyasyncTaskLoader(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public String loadInBackground() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  "fromloader";
        }

        // this give to chance to change data before send to main thread
        @Override
        public void deliverResult(@Nullable String data) {
            super.deliverResult(data);
        }

    }

    //region Background Class
    private class BackgroundTask extends AsyncTask<String,String,Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DownloadFile.responseCode=0;
        }

        @Override
        protected Integer doInBackground(String... strings)
        {
            Intent intent = new Intent(getApplicationContext(), MyIntentServices.class);
            intent.setData(Uri.parse(String_url_of_json));
            intent.putExtra("name",user.getText().toString());
            intent.putExtra("pass",password.getText().toString());

            startService(intent);
            return DownloadFile.responseCode;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textView.append(values[0]+"\n");

        }

        // if call data from web data will retrieve here
        @Override
        protected void onPostExecute(Integer num) {
            super.onPostExecute(num);
            Intent intent2 = new Intent(getApplicationContext(), RecyclerView_to_list_data.class);
            startActivity(intent2);




        }
    }




    /*

        IntentServices class completely decoupled from user interface it is used for download large file and datasets
          -it is running on own thread and does not have access to main thread
          -data sent in broadcast msg that mean you can send data to any component of app
          1-create class extent IntentServices


     */















}
