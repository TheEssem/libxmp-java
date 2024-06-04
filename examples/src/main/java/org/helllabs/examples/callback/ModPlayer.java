/*
 * Example mod player using libxmp.jar.
 *
 * This code is in the public domain.
 */

package org.helllabs.examples.callback;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;

import org.helllabs.libxmp.*;
import org.helllabs.libxmp.Module;

public class ModPlayer {

    private final static int SAMPLE_RATE = 44100;

    private int oldPos = -1;

    private void showInfo(FrameInfo fi) {
        if (fi.getPosition() != oldPos) {
            System.out.printf("Pos: %d, Pattern: %d\r", fi.getPosition(), fi.getPattern());
            oldPos = fi.getPosition();
        }
    }

    private void showHeader(Module mod) {
        System.out.println("Module name  : " + mod.getName());
        System.out.println("Module type  : " + mod.getType());
        System.out.println("Module length: " + mod.getLength() + " patterns");
    }

    private void play(String path) throws LineUnavailableException {
        AudioPlay audio = new AudioPlay(SAMPLE_RATE);

        Player xmp = new Player(SAMPLE_RATE);
        Module mod;
        try {
            mod = xmp.loadModule(path);
            showHeader(mod);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FrameInfo fi = new FrameInfo();

        xmp.start();
        while (xmp.playFrame()) {
            xmp.getFrameInfo(fi);
            if (fi.getLoopCount() > 0) {
                break;
            }

            audio.play(fi.getBuffer(), fi.getBufferSize());
            showInfo(fi);
        }
        xmp.end();
        xmp.releaseModule();
    }

    public static void main(String[] args) {
        System.out.println("Libxmp-Java player test");

        ModPlayer player = new ModPlayer();
        Module.TestInfo ti = new Module.TestInfo();

        for (String arg : args) {
            try {
                Module.test(arg, ti);
            } catch (IOException e) {
                // Invalid Module
                System.err.println("Arg: " + arg + ": " + e.getLocalizedMessage());
                continue;
            }

            System.out.println("\nPlaying " + arg + "...");

            try {
                player.play(arg);
            } catch (LineUnavailableException e) {
                e.printStackTrace();

            }
            System.out.print("\n");
        }

        System.out.println("End");
    }
}
