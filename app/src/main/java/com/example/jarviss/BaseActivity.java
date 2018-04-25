package com.example.jarviss;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by 肖 on 2018/4/18.
 */

public class BaseActivity extends AppCompatActivity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public static Context GetContext(){
        return context;
    }

    public int getResourceByReflect(String imageName){

        int r_id ;
        try {
            Log.d("imageName", imageName);
            Context ctx=getBaseContext();
            r_id = getResources().getIdentifier(imageName, "drawable" , ctx.getPackageName());
        } catch (Exception e) {
            r_id = -1;
            Log.e("ERROR", "PICTURE NOT　FOUND！");
        }
        return r_id;
    }

}
