package edu.uw.tessa_and_sam.melodious;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.*;

public class MainActivity extends AppCompatActivity implements PitchRecognizer.PitchRecognizerDelegate {
    private SharedPreferences prefs;
    static final int MY_RECORD_AUDIO_PERMISSION_CONST = 927;
    private PitchRecognizer pitchRecognizer;
    private PitchGenerator pitchGenerator;
    private GridView gridview;
    private View selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.prefs = this.getSharedPreferences(this.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        this.gridview = (GridView) findViewById(R.id.grid);

        this.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (MainActivity.this.selectedItem == null) {
                    MainActivity.this.selectedItem = v;
                    MainActivity.this.selectedItem.setBackgroundResource(R.drawable.border_clicked);
                }
                if (MainActivity.this.selectedItem != v) {
                    MainActivity.this.selectedItem.setBackgroundResource(R.drawable.border);
                    MainActivity.this.selectedItem = v;
                    v.setBackgroundResource(R.drawable.border_clicked);
                }
            }
        });

        this.pitchRecognizer = new PitchRecognizer();
        this.pitchRecognizer.setDelegate(this);
//        this.checkPermissionsAndStartPitchRecognizer();

        this.pitchGenerator = new PitchGenerator();
//        this.pitchGenerator.play(440, 2000);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3D4249")));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setGridAdapter();
    }

    private void setGridAdapter() {
        String noteSequence = this.prefs.getString(this.getString(R.string.note_sequence_key),
                this.getString(R.string.default_note_sequence));
        String[] notes = new String[Integer.parseInt(noteSequence) + 1];

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        if (notes.length < 4) {
            this.gridview.setColumnWidth((size.x / notes.length) - 100);
        } else {
            this.gridview.setColumnWidth(250);
        }

        this.gridview.setAdapter(new GridAdapter(this, size.x));
    }

    public void pitchUpdated() {
        float freq = pitchRecognizer.getCurrentFreq();
        if (freq < 0) {
            return;
        }

        MusicUtil.NoteData noteData = MusicUtil.freq2name(freq);
        Log.i("MainActivity", "note: " + noteData.name + noteData.octave);
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

