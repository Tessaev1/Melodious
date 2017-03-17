package edu.uw.tessa_and_sam.melodious;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements PitchRecognizer.PitchRecognizerDelegate {
    static final int MY_RECORD_AUDIO_PERMISSION_CONST = 927;
    private PitchRecognizer pitchRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pitchRecognizer = new PitchRecognizer();
        pitchRecognizer.setDelegate(this);
        this.checkPermissionsAndStartPitchRecognizer();
    }

    public void pitchUpdated() {
        Log.i("MainActivity", "freq: " + pitchRecognizer.getCurrentFreq());
    }

    private void checkPermissionsAndStartPitchRecognizer() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{ Manifest.permission.RECORD_AUDIO },
                    MY_RECORD_AUDIO_PERMISSION_CONST);
        }
        else
        {
            pitchRecognizer.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case MY_RECORD_AUDIO_PERMISSION_CONST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    this.pitchRecognizer.start();
                }
                else
                {
                    Log.i("MainActivity", "Audio Permission Denied");
                }
                return;
            }
        }
    }

}
