package edu.uw.tessa_and_sam.melodious;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        setSpinners();
        watchSeekBar();
    }

    public void setSpinners() {
        Spinner fromNoteSpinner = (Spinner) findViewById(R.id.fromNote);
        Spinner toNoteSpinner = (Spinner) findViewById(R.id.toNote);
        Spinner instrumentRangeSpinner = (Spinner) findViewById(R.id.instrumentRange);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> noteAdapter = ArrayAdapter.createFromResource(this,
                R.array.notes_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> instrumentAdapter = ArrayAdapter.createFromResource(this,
                R.array.intrument_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        noteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instrumentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        fromNoteSpinner.setAdapter(noteAdapter);
        toNoteSpinner.setAdapter(noteAdapter);
        instrumentRangeSpinner.setAdapter(instrumentAdapter);
    }

    public void watchSeekBar() {
        SeekBar noteSeekBar = (SeekBar) findViewById(R.id.seekBar);

        noteSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            final TextView currentNoteNum = (TextView) findViewById(R.id.currentNoteNum);
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                currentNoteNum.setText(String.valueOf(progress + 1));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
