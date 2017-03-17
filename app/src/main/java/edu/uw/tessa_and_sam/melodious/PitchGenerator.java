package edu.uw.tessa_and_sam.melodious;

import be.tarsos.dsp.filters.LowPassFS;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.AudioGenerator;
import be.tarsos.dsp.io.android.AndroidAudioPlayer;
import be.tarsos.dsp.synthesis.SineGenerator;

/**
 * Created by sam on 3/17/17.
 */

public class PitchGenerator implements Runnable {
    private AudioGenerator generator;
    private Thread generatorThread;

    public PitchGenerator() {
    }

    public void play(float freq, int stopAfter) {
        generator = new AudioGenerator(1024,0);
        generator.addAudioProcessor(new LowPassFS(1000, 44100));
        generator.addAudioProcessor(new SineGenerator(0.15, freq));
        this.generator.addAudioProcessor(new AndroidAudioPlayer( new TarsosDSPAudioFormat(44100, 16, 1, true, false)));

        generatorThread = new Thread(this);
        generatorThread.start();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        generator.stop();
                    }
                },
                stopAfter
        );
    }

    public void run() {
        this.generator.run();
    }
}
