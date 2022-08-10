/* Java API for Libxmp
 * Copyright (C) 2014 Claudio Matsuoka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.helllabs.libxmp;

import java.io.IOException;

public final class Xmp {
	
	public static final int KEY_OFF = 0x81;				// Note number for key off event
	public static final int KEY_CUT = 0x82;				// Note number for key cut event
	public static final int KEY_FADE = 0x83;			// Note number for fade event

	// mixer parameter macros

	// sample format flags
	public static final int FORMAT_8BIT = 1 << 0;		// Mix to 8-bit instead of 16
	public static final int FORMAT_UNSIGNED = 1 << 1;	// Mix to unsigned samples
	public static final int FORMAT_MONO = 1 << 2;		// Mix to mono instead of stereo

	// mixer paramters for xmp_set_player()
	static final int PLAYER_AMP = 0;					// Amplification factor
	static final int PLAYER_MIX = 1;					// Stereo mixing
	static final int PLAYER_INTERP = 2;					// Interpolation type
	static final int PLAYER_DSP = 3;					// DSP effect flags
	static final int PLAYER_FLAGS = 4;					// Player flags
	static final int PLAYER_CFLAGS = 5;					// Player flags for current module
	static final int PLAYER_SMPCTL = 6;					// Sample control flags
	static final int PLAYER_VOLUME = 7;					// Player module volume
	static final int PLAYER_STATE = 8;					// Internal player state
	static final int PLAYER_SMIX_VOLUME = 9;			// SMIX volume
	static final int PLAYER_DEFPAN = 10;				// Default pan setting
	static final int PLAYER_MODE = 11;					// Player personality
	static final int PLAYER_MIXER_TYPE = 12;			// Current mixer
	static final int PLAYER_VOICES = 13;				// Maximum number of mixer voices

	// interpolation types
	public static final int INTERP_NEAREST = 0;			// Nearest neighbor
	public static final int INTERP_LINEAR = 1;			// Linear (default)
	public static final int INTERP_SPLINE = 2;			// Cubic spline

	// dsp effect types
	public static final int DSP_LOWPASS = 1 << 0;		// Lowpass filter effect
	public static final int DSP_ALL = DSP_LOWPASS;

	// player state
	public static final int STATE_UNLOADED = 0;			// Context created
	public static final int STATE_LOADED = 1;			// Module loaded
	public static final int STATE_PLAYING = 2;			// Module playing
	
	// player flags
	public static final int FLAGS_VBLANK = 1 << 0;		// Use vblank timing
	public static final int FLAGS_FX9BUG = 1 << 1;		// Emulate FX9 bug
	public static final int FLAGS_FIXLOOP = 1 << 2;		// Emulate sample loop bug
	public static final int FLAGS_A500 = 1 << 3;		// Use Paula mixer in Amiga modules

	// sample flags
	public static final int SMPCTL_SKIP = 1 << 0;		// Don't load samples

	// limits
	public static final int MAX_KEYS = 121;				// Number of valid keys
	public static final int MAX_ENV_POINTS = 32;		// Max number of envelope points
	public static final int MAX_MOD_LENGTH = 256;		// Max number of patterns in module
	public static final int MAX_CHANNELS = 64;			// Max number of channels in module
	public static final int MAX_SRATE = 49170;			// max sampling rate (Hz)
	public static final int MIN_SRATE = 4000;			// min sampling rate (Hz)
	public static final int MIN_BPM = 20;				// min BPM
	// frame rate = (50 * bpm / 125) Hz
	// frame size = (sampling rate * channels * size) / frame rate
	public static final int MAX_FRAMESIZE = 5 * MAX_SRATE * 2 / MIN_BPM;

	// error codes
	public static final int ERROR_INTERNAL = 2;
	public static final int ERROR_FORMAT = 3;
	public static final int ERROR_LOAD = 4;
	public static final int ERROR_SYSTEM = 6;
	public static final int ERROR_INVALID = 7;
	public static final int ERROR_STATE = 8;

	public static final int PERIOD_BASE = 6847;			// C4 period

	public static final int SAMPLE_16BIT = 1 << 0;		// 16bit sample
	public static final int SAMPLE_LOOP = 1 << 1;		// Sample is looped
	public static final int SAMPLE_LOOP_BIDIR = 1 << 2;	// Bidirectional sample loop
	public static final int SAMPLE_LOOP_FULL = 1 << 4;	// Play full sample before looping


	static final String[] ERROR_STRING = {
		"No error",
		"End of module",
		"Internal error",
		"Unknown module format",
		"Can't load module",
		"Can't decompress module",
		"System error",
		"Invalid parameter"
	};

	private Xmp() {

	}

	// native API methods
	native static long createContext();
	native static void freeContext(long ctx);
	native static int loadModule(long ctx, String path);
	native static int loadModuleFromMemory(long ctx, byte[] mem);
	native static int testModule(String path, Module.TestInfo info);
	native static void releaseModule(long ctx);
	native static int startPlayer(long ctx, int freq, int mode);
	native static int playFrame(long ctx);
	native static void getFrameInfo(long ctx, FrameInfo info);
	native static void endPlayer(long ctx);
	native static int setPlayer(long ctx, int param, int value);
	native static int getPlayer(long ctx, int param);
	native static int injectEvent(long ctx, int chn, Module.Event event);
	native static String[] getFormatList();
	native static int nextPosition(long ctx);
	native static int prevPosition(long ctx);
	native static int setPosition(long ctx, int num);
	native static void scanModule(long ctx);
	native static void stopModule(long ctx);
	native static void restartModule(long ctx);
	native static int seekTime(long ctx, int time);
	native static int channelMute(long ctx, int chn, int val);
	native static int channelVol(long ctx, int chn, int val);
	
	// sample mixer API
	native static int startSmix(long ctx, int chn, int smp);
	native static void endSmix(long ctx);
	native static int smixPlayInstrument(long ctx, int ins, int note, int vol, int chn);
	native static int smixPlaySample(long ctx, int ins, int note, int vol, int chn);
	native static int smixChannelPan(long ctx, int chn, int pan);
	native static int smixLoadSample(long ctx, int num, String path);
	native static int smixReleaseSample(long ctx, int num);

	// native helpers
	native static int getErrno();
	native static String getStrError(int err);
	native static void getModData(long ctx, Module mod);
	native static void getEventData(long ctx, int pat, int row, int chn, Module.Event pattern);
	native static void getPatternData(long ctx, int num, Module.Pattern pattern);
	native static void getInstrumentData(long ctx, int num, Module.Instrument instrument);
	native static void getSampleData(long ctx, int num, Module.Sample sample);

	static {
		try {
			System.loadLibrary("xmp-jni");
		} catch (UnsatisfiedLinkError e) {
			try {
					NativeUtils.loadLibraryFromJar("/natives/" + System.getProperty("os.name").toLowerCase() + "-" + System.getProperty("os.arch") + "/" + System.mapLibraryName("xmp-jni"));
			} catch (IOException e1) {
					throw new RuntimeException(e1);
			}
	}
	}
}
