package edu.uw.tessa_and_sam.melodious;

/**
 * Created by sam on 3/17/17.
 */

public class MusicUtil {
    public static final double A4_FREQUENCY = 440.00;

    public static class NoteData {
        public String name;
        public double centsOff;
        public int octave;
    }

    public static NoteData freq2name(float freq) {
        double centDiff = 1200 * Math.log(freq / A4_FREQUENCY) / Math.log(2.0);
        double noteDiff = Math.floor(centDiff / 100);

        double matlabModulus = centDiff - 100.0 * Math.floor(centDiff / 100.0);
        if (matlabModulus > 50)
        {
            noteDiff = noteDiff + 1;
        }

        String[] noteNames = new String[] { "C", "C#", "D" , "D#" , "E" , "F" , "F#", "G" , "G#" , "A" , "A#" , "B" };

        NoteData noteData = new NoteData();
        noteData.centsOff = centDiff - noteDiff * 100;
        double noteNumber = noteDiff + 9 + 12 * 4;
        noteData.octave = (int)Math.floor((noteNumber)/12);
        int place = (int)(noteNumber % 12.0) + 1;

        noteData.name = noteNames[place - 1];

        return noteData;
    }
}
