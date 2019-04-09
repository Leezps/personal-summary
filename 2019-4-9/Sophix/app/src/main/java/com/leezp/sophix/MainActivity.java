package com.leezp.sophix;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.leezp.sophix.exception.Caclutor;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_test:
                Caclutor caclutor = new Caclutor();
                int data = caclutor.caculator();
                Toast.makeText(this, "caculator data:"+data, Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_main_repair:
                File file = new File(Environment.getExternalStorageDirectory(), "out.dex");
                PatchManager patchManager = new PatchManager(file, this);
                patchManager.loadPatch();
                break;
        }

    }
}
