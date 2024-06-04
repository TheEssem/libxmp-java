/*
 * Example mod player using Libxmp.jar.
 *
 * This code is in the public domain.
 */

package org.helllabs.examples.showpattern;

import java.io.IOException;

import org.helllabs.libxmp.Module.Event;
import org.helllabs.libxmp.Module;
import org.helllabs.libxmp.Player;
import org.helllabs.libxmp.Module.Pattern;

public class ShowPattern {

    private void showHeader(Module module) {
        System.out.println("Module name  : " + module.getName());
        System.out.println("Module type  : " + module.getType());
        System.out.println("Module length: " + module.getLength() + " patterns");
    }

    private String formatNote(final int num) {
        final String[] note = {
                "C ", "C#", "D ", "D#", "E ", "F ",
                "F#", "G ", "G#", "A ", "A#", "B "
        };

        if (num == 0) {
            return "---";
        } else {
            return String.format("%s%d", note[(num - 1) % 12], (num - 1) / 12);
        }
    }

    private String formatNum(final int num) {
        return num == 0 ? "--" : String.format("%02X", num);
    }

    private void showEvent(final Event e) {
        System.out.printf(" | %s %s %s", formatNote(e.note), formatNum(e.ins), formatNum(e.vol));
    }

    private void run(final String path, final int num) throws IOException {
        final Player player = new Player();
        final Module module = player.loadModule(path);

        showHeader(module);

        final Pattern pattern = module.patternData(num);
        final Event event = new Event();

        System.out.println("\nPATTERN " + num);

        for (int row = 0; row < pattern.getNumRows(); row++) {
            System.out.printf("%02X", row);
            for (int chn = 0; chn < module.getNumChannels(); chn++) {
                pattern.eventData(row, chn, event);
                showEvent(event);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println("Libxmp show pattern test");

        if (args.length == 0) {
            System.out.println("Usage: showpattern <module> <num>");
            System.exit(0);
        }

        final String path = args[0];
        final int num = Integer.parseInt(args[1]);

        try {
            Module.test(path);
            final ShowPattern app = new ShowPattern();
            app.run(path, num);
        } catch (IOException e) {
            System.out.print("\nError: " + path + ": " + e.getMessage());
        }
    }
}
