/*
 * Example mod player using libxmp.jar.
 *
 * This code is in the public domain.
 */

package org.helllabs.examples.callback;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.*;

import org.helllabs.libxmp.Xmp;

public class AudioPlay {

    SourceDataLine line;
    byte[] myBuffer;

    public AudioPlay(int freq) throws LineUnavailableException {
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

        myBuffer = new byte[Xmp.MAX_FRAMESIZE];
    }

    @Override
    protected void finalize() {
        close();
    }

    public void play(ByteBuffer buffer, int bufferSize) {
        buffer.clear();
        buffer.get(myBuffer, 0, bufferSize);
        line.write(myBuffer, 0, bufferSize);
    }

    public void close() {
        line.drain();
        line.close();
    }
}
