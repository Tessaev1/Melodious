package edu.uw.tessa_and_sam.melodious;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements PitchRecognizer.PitchRecognizerDelegate {
    static final int MY_RECORD_AUDIO_PERMISSION_CONST = 927;
    private PitchRecognizer pitchRecognizer;
    private PitchGenerator pitchGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.pitchRecognizer = new PitchRecognizer();
        this.pitchRecognizer.setDelegate(this);
        this.checkPermissionsAndStartPitchRecognizer();

        this.pitchGenerator = new PitchGenerator();
        this.pitchGenerator.play(440, 2000);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void getSettings(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

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

