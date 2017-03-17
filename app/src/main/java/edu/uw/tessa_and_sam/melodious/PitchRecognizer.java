package edu.uw.tessa_and_sam.melodious;

import com.kekstudio.musictheory.Note;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

/**
 * Created by sam on 3/17/17.
 */

public class PitchRecognizer {
    public static final int SAMPLE_RATE = 4410;
    private Note currentNote;
    private float currentFreq;
    private PitchRecognizerDelegate delegate;
    public interface PitchRecognizerDelegate {
        public void pitchUpdated();
    }

    public void start() {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(SAMPLE_RATE,1024,0);

        dispatcher.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.FFT_YIN, SAMPLE_RATE, 1024,
                new PitchDetectionHandler()
                {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                    AudioEvent audioEvent) {
                PitchRecognizer.this.currentFreq = pitchDetectionResult.getPitch();
                PitchRecognizer.this.delegate.pitchUpdated();
            }
        }));
        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    public Note getCurrentNote() {
        return this.currentNote;
    }

    public  float getCurrentFreq() {
        return this.currentFreq;
    }

    public void setDelegate(PitchRecognizerDelegate delegate) {
        this.delegate = delegate;
    }
}
