/*
 * Example mod player using Libxmp.jar.
 *
 * This code is in the public domain.
 */

package org.helllabs.examples.player;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;

import org.helllabs.libxmp.FrameInfo;
import org.helllabs.libxmp.Module;
import org.helllabs.libxmp.Player;

public class ModPlayer {

    private final static int SAMPLE_RATE = 44100;

    private int oldPos = -1;

    private void showInfo(final FrameInfo info) {
        final int pos = info.getPosition();
        final int pat = info.getPattern();

        if (pos != oldPos) {
            System.out.printf("Pos: %d, Pattern: %d\r", pos, pat);
            oldPos = pos;
        }
    }

    private void showHeader(final Module mod) {
        System.out.println("Module name  : " + mod.getName());
        System.out.println("Module type  : " + mod.getType());
        System.out.println("Module length: " + mod.getLength() + " patterns");
        System.out.println("Channels     : " + mod.getNumChannels());
        System.out.println("Instruments  : " + mod.getNumInstruments());
    }

    private void showInstruments(final Module mod) {
        final int num = mod.getNumInstruments();
        Module.Instrument instrument = new Module.Instrument();

        for (int i = 0; i < num; i++) {
            mod.instrumentData(i, instrument);
            System.out.printf("%2d %s\n", i + 1, instrument.getName());
        }
    }

    private void playModule(final Player player, final AudioPlay audio, final String path) throws IOException {
        final Module mod = player.loadModule(path);
        showHeader(mod);
        showInstruments(mod);

        final Player.Callback callback = new Player.Callback() {
            @Override
            public boolean callback(final FrameInfo info, final Object args) {
                audio.play(info.getBufferArray(), info.getBufferSize());
                showInfo(info);
                return true;
            }
        };

        player.play(callback);
    }

    private void run(final String[] args) throws LineUnavailableException {
        final Player player = new Player(SAMPLE_RATE);
        final AudioPlay audio = new AudioPlay(SAMPLE_RATE);

        for (final String arg : args) {
            try {
                Module.test(arg);
                System.out.println("\nPlaying " + arg + "...");
                playModule(player, audio, arg);
            } catch (IOException e) {
                System.out.print("\nCan't play " + arg + ": " + e.getMessage());
            }
            System.out.print("\n");
        }
    }

    public static void main(final String[] args) {
        System.out.println("Libxmp player test");

        if (args.length == 0) {
            System.out.println("No modules to play.");
            System.exit(1);
        }

        if (args[0].equals("-L")) {
            final String[] list = Player.formatList();
            for (int i = 0; i < list.length; i++) {
                System.out.printf("%d:%s\n", i + 1, list[i]);
            }
            System.exit(0);
        }

        final ModPlayer modPlayer = new ModPlayer();
        try {
            modPlayer.run(args);
        } catch (LineUnavailableException e) {
            System.out.println("Can't initialize audio");
        }
    }
}
