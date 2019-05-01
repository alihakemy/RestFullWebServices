package com.hakemy.linkedin_webservices.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.hakemy.linkedin_webservices.MyJsonClass.JsonFromPojo;
import com.hakemy.linkedin_webservices.R;
import com.hakemy.linkedin_webservices.Services.MyIntentServices;
import com.hakemy.linkedin_webservices.utils.DownloadFile;
import com.hakemy.linkedin_webservices.utils.ImageCacheManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashMap;

public class RecyclerView_to_list_data extends AppCompatActivity implements LoaderManager.LoaderCallbacks<HashMap<String,Bitmap>>  {

    private  HashMap<String,Bitmap> mBitmapFromWeb =new HashMap<>()  ;
    private  JsonFromPojo[] jsonFromPojos;
    private  MyRecViewClass adapter ;
    private  RecyclerView mNumberslist;

    private BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.hasExtra(MyIntentServices.my_service_payload)) {

                //String msg =intent.getStringExtra(MyIntentServices.my_service_payload);
//===================================================================================================================
                // make error because it is back arry of parceble object not arry of (JsonFromPOJO) to splve that make cast
               jsonFromPojos = (JsonFromPojo[]) intent.getParcelableArrayExtra(MyIntentServices.my_service_payload);


                   for (JsonFromPojo jsonFromPojo : jsonFromPojos) {
                       //     data.append(jsonFromPojo.getItemName());
                       //    data.append(jsonFromPojo.getImage());

                   }

                   getSupportLoaderManager().restartLoader(0, null, RecyclerView_to_list_data.this).forceLoad();





            }

            else if(intent.hasExtra(MyIntentServices.MyException))
            {
                Toast.makeText(getApplicationContext(), intent.getAction(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), intent.getStringExtra(MyIntentServices.MyException), Toast.LENGTH_LONG).show();
            }



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_to_list_data);
        mNumberslist=(RecyclerView) findViewById(R.id.RecView);





        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver,new IntentFilter(MyIntentServices.my_service_msg));





    }



// lazyload ==========================================================================
    private class BackgroundTask extends AsyncTask<JsonFromPojo,Void,Bitmap> {

        private  String stringurl="" ;
        private JsonFromPojo [] jsonFromPojo1 ;

        @Override
        protected Bitmap doInBackground(JsonFromPojo... jsonFromPojos) {
            InputStream inputStream = null;
            for(int i=0;i<jsonFromPojo1.length;i++)
            {
                try {
                    inputStream =(InputStream) new URL(stringurl+jsonFromPojo1[i].getImage()).getContent();
                    Bitmap bitmapFactory = BitmapFactory.decodeStream(inputStream);
                    return bitmapFactory; // this return to onpostexcute

                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        inputStream.close();
                    }catch (IOException  e)
                    {

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getApplicationContext());
            mNumberslist.setLayoutManager(linearLayoutManager);
            mNumberslist.setHasFixedSize(true);
            adapter=new MyRecViewClass(jsonFromPojos,getApplicationContext(),mBitmapFromWeb);
            mNumberslist.setAdapter(adapter);
        }
    }
//=================================================================================



















    static class MyBitmapDownloader extends AsyncTaskLoader<HashMap<String,Bitmap>> {


        private  String stringurl ;
        private HashMap<String,Bitmap> BitmapHashMap =new HashMap<>();
        private JsonFromPojo [] jsonFromPojo1 ;
        private HashMap<String,Bitmap> bitmap =new HashMap<>();

        public MyBitmapDownloader(@NonNull Context context, String stringurl, JsonFromPojo jsonFromPojo[]) {
            super(context);
            this.stringurl=stringurl;
            this.jsonFromPojo1=jsonFromPojo;

        }

        @Nullable
        @Override
        public HashMap<String, Bitmap> loadInBackground() {
            InputStream inputStream = null;
            for(int i=0;i<jsonFromPojo1.length;i++)
            {
             bitmap.put(jsonFromPojo1[i].getItemName(),ImageCacheManager.getBitmap(getContext(),jsonFromPojo1[i]))  ;

                if (bitmap.get(jsonFromPojo1[i].getItemName()) == null) //ifn
                {
                    try {
                        inputStream =(InputStream) new URL(stringurl+jsonFromPojo1[i].getImage()).getContent();
                        Bitmap bitmapFactory = BitmapFactory.decodeStream(inputStream);
                      //  BitmapHashMap.put(jsonFromPojo1[i].getItemName(),bitmapFactory);
                        ImageCacheManager.putBitmap(getContext(),jsonFromPojo1[i],bitmapFactory);
                        bitmap.put(jsonFromPojo1[i].getItemName(),bitmapFactory);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        try {
                            inputStream.close();
                        }catch (IOException  e)
                        {

                        }
                    }
                }

            }
            return bitmap;
        }



    }


    @NonNull
    @Override
    public Loader<HashMap<String, Bitmap>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return  new MyBitmapDownloader(this,"http://560057.youcanlearnit.net/services/images/",jsonFromPojos);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<HashMap<String, Bitmap>> loader, HashMap<String, Bitmap> stringBitmapMap) {
        mBitmapFromWeb=stringBitmapMap;
        Toast.makeText(getApplicationContext(),String.valueOf(stringBitmapMap.size()),Toast.LENGTH_LONG).show();

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getApplicationContext());
        mNumberslist.setLayoutManager(linearLayoutManager);
        mNumberslist.setHasFixedSize(true);

        adapter=new MyRecViewClass(jsonFromPojos,getApplicationContext(),mBitmapFromWeb);
        mNumberslist.setAdapter(adapter);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<HashMap<String, Bitmap>> loader) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
