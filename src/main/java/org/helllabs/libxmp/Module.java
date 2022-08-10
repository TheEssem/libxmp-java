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
import java.nio.ByteBuffer;

public class Module {

	//private final Player player;	// Xmp player
	private final long ctx;			// Xmp context

	private String name;			// Module title
	private String type;			// Module format
	private int numPatterns;		// Number of patterns
	private int numChannels;		// Number of channels
	private int numInstruments;		// Number of instruments
	private int numSamples;			// Number of samples
	private int initialSpeed;		// Initial speed
	private int initialBpm;			// Initial BPM
	private int length;				// Module length in patterns

	public static class TestInfo {
		private String name;
		private String type;
		
		public String getName() {
			return name;
		};
		
		public String getType() {
			return type;
		}
	};

	public static class Sequence {
		private int entryPoint;
		private int duration;
		
		public int getEntryPoint() {
			return entryPoint;
		}
		
		public int getDuration() {
			return duration;
		}
	}

	public static class Event {
		public int note;
		public int ins;
		public int vol;
		public int fxt;
		public int fxp;
		public int f2t;
		public int f2p;
	}
	
	public static class Pattern {
		private long ctx;
		private int num;
		private int numRows;
		
		public Event eventData(final int row, final int chn, final Event event) {
			Xmp.getEventData(ctx, num, row, chn, event);
			return event;
		}
		
		public int getNumRows() {
			return numRows;
		}
	}
	
	public static class Instrument {
		private String name;
		private int numSamples;
		private int[] sampleID;
		
		public String getName() {
			return name;
		}
		
		public int getNumSamples() {
			return numSamples;
		}
		
		public int sampleID(int n) {
			return sampleID[n];
		}
	}

	public static class Sample {
		private String name;
		private int length;
		private int loopStart;
		private int loopEnd;
		private int flags;
		private ByteBuffer data;
		
		public String getName() {
			return name;
		}
		
		public int getLength() {
			return length;
		}
		
		public int getLoopStart() {
			return loopStart;
		}
		
		public int getLoopEnd() {
			return loopEnd;
		}
		
		public int getFlags() {
			return flags;
		}
		
		public byte[] getData() {
			data.clear();
			final int dataSize = data.remaining();
			final byte[] dataArray = new byte[dataSize];
			data.get(dataArray, 0, dataSize);
			return dataArray;
		}
	}
	
	public Module(final Player player) {
		ctx = player.getContext();
		Xmp.getModData(ctx, this);
	}

	/**
	 * Test if a file is a valid module. Testing a file does not affect
	 * player or module states.
	 * 
	 * @param path Pathname of the module to test.
	 * @return The TestInfo object with module information.
	 * @throws IOException
	 */
	
	public static TestInfo test(final String path) throws IOException {
		if (path == null) {
			throw new IOException("File name is null");
		}
		return test(path, null);
	}

	/**
	 * Test if a file is a valid module, without creating a new TestInfo object.
	 * Testing a file does not affect player or module states.
	 *
	 * @param path Pathname of the module to test.
	 * @param info TestInfo object used to retrieve module data if the file is
	 *   a valid module.
	 * @return The TestInfo object with module information.
	 * @throws IOException
	 */
	public static TestInfo test(final String path, TestInfo info) throws IOException {
		if (info == null) {
			info = new Module.TestInfo();
		}
		final int code = Xmp.testModule(path, info);

		if (code == -Xmp.ERROR_SYSTEM) {
			throw new IOException(Xmp.getStrError(Xmp.getErrno()));
		} else if (code < 0) {
			throw new IOException(Xmp.ERROR_STRING[-code]);
		}

		return info;
	}
		
	public Pattern patternData(final int num) {
		return patternData(num, new Pattern());
	}
	
	public Pattern patternData(final int num, final Pattern pattern) {
		Xmp.getPatternData(ctx, num, pattern);
		return pattern;
	}
	
	/**
	 * Retrieve instrument data.
	 * 
	 * @param num The number of the instrument to retrieve, starting at 0. 
	 * @return The instrument object.
	 */
	public Instrument instrumentData(final int num) {
		return instrumentData(num, new Instrument());
	}
	
	/**
	 * Retrieve instrument data, without creating a new Instrument object.
	 * 
	 * @param num The number of the instrument to retrieve, starting at 0.
	 * @param instrument The object to be used to retrieve instrument data.
	 * @return The instrument object.
	 */
	public Instrument instrumentData(final int num, final Instrument instrument) {
		Xmp.getInstrumentData(ctx, num, instrument);
		return instrument;
	}
	
	/**
	 * Retrieve sample data.
	 * 
	 * @param num The number of the sample to retrieve, starting at 0.
	 * @return The sample object.
	 */
	public Sample sampleData(final int num) {
		return sampleData(num, new Sample());
	}
	
	/**
	 * Retrieve sample data, without creating a new Sample object.
	 * 
	 * @param num The number of the sample to retrieve, starting at 0.
	 * @param sample The object to be used to retrieve sample data.
	 * @return The sample object.
	 */
	public Sample sampleData(final int num, final Sample sample) {
		Xmp.getSampleData(ctx, num, sample);
		return sample;
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getNumPatterns() {
		return numPatterns;
	}

	public int getNumChannels() {
		return numChannels;
	}

	public int getNumInstruments() {
		return numInstruments;
	}

	public int getNumSamples() {
		return numSamples;
	}
	
	public int getInitialSpeed() {
		return initialSpeed;
	}

	public int getInitialBpm() {
		return initialBpm;
	}

	public int getLength() {
		return length;
	}
}
