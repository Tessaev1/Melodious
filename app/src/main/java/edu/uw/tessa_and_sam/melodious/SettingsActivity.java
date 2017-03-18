package edu.uw.tessa_and_sam.melodious;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private String fromNote;
    private String toNote;
    private String instrument;
    private Spinner fromNoteSpinner;
    private Spinner toNoteSpinner;
    private Spinner instrumentSpinner;
    private SeekBar noteSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.prefs = this.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        this.fromNote = this.prefs.getString(getString(R.string.fromNote_key),
                getString(R.string.default_fromNote));
        this.toNote = this.prefs.getString(getString(R.string.toNote_key),
                getString(R.string.default_toNote));
        this.instrument = this.prefs.getString(getString(R.string.instrument_key),
                getString(R.string.default_instrument));
        this.fromNoteSpinner = (Spinner) findViewById(R.id.fromNote);
        this.toNoteSpinner = (Spinner) findViewById(R.id.toNote);
        this.instrumentSpinner = (Spinner) findViewById(R.id.instrumentRange);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3D4249")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        setSpinners();
        watchSeekBar();
    }

    public void setSpinners() {
        ArrayAdapter<CharSequence> noteAdapter = ArrayAdapter.createFromResource(this,
                R.array.notes_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> instrumentAdapter = ArrayAdapter.createFromResource(this,
                R.array.intrument_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        noteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instrumentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        this.fromNoteSpinner.setAdapter(noteAdapter);
        int fromPosition = noteAdapter.getPosition(this.fromNote);
        this.fromNoteSpinner.setSelection(fromPosition);

        this.toNoteSpinner.setAdapter(noteAdapter);
        int toPosition = noteAdapter.getPosition(this.toNote);
        this.toNoteSpinner.setSelection(toPosition);

        this.instrumentSpinner.setAdapter(instrumentAdapter);
        int instrumentPosition = instrumentAdapter.getPosition(this.instrument);
        this.instrumentSpinner.setSelection(instrumentPosition);
    }

    public void watchSeekBar() {
        String noteSequence = this.prefs.getString(getString(R.string.note_sequence_key),
                getString(R.string.default_note_sequence));

        this.noteSeekBar = (SeekBar) findViewById(R.id.seekBar);
        this.noteSeekBar.setProgress(Integer.parseInt(noteSequence));

        final TextView currentNoteNum = (TextView) findViewById(R.id.currentNoteNum);
        currentNoteNum.setText((Integer.parseInt(noteSequence) + 1) + "");

        this.noteSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
    public void onBackPressed() {
        super.onBackPressed();

        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString(getString(R.string.fromNote_key),
                this.fromNoteSpinner.getSelectedItem().toString());
        editor.putString(getString(R.string.toNote_key),
                this.toNoteSpinner.getSelectedItem().toString());
        editor.putString(getString(R.string.instrument_key),
                this.instrumentSpinner.getSelectedItem().toString());
        editor.putString(getString(R.string.note_sequence_key),
                String.valueOf(this.noteSeekBar.getProgress()));
        editor.commit();
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
                this.onBackPressed();
                return true;

            default:
                // user's action was not recognized.
                return super.onOptionsItemSelected(item);

        }
    }
}
