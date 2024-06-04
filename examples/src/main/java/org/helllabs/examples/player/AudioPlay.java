/*
 * Example mod player using Coremod.jar.
 *
 * This code is in the public domain.
 */

package org.helllabs.examples.player;

import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioPlay {

    private final SourceDataLine line;

    public AudioPlay(final int freq) throws LineUnavailableException {
        final boolean isBigEndian = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
        final AudioFormat format = new AudioFormat(freq, 16, 2, true, isBigEndian);
        final DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line matching " + info + " is not supported.");
            throw new LineUnavailableException();
        }

        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public void play(final byte[] buffer, final int bufferSize) {
        line.write(buffer, 0, bufferSize);
    }

    public void close() {
        line.drain();
        line.close();
    }
}
