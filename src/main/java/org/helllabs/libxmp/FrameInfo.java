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

import java.nio.ByteBuffer;

public class FrameInfo {
	private int position;		// Current position
	private int pattern;		// Current pattern
	private int row;			// Current row in pattern
	private int numRows;		// Number of rows in current pattern
	private int frame;			// Current frame
	private int speed;			// Current replay speed
	private int bpm;			// Current bpm
	private int time;			// Current module time in ms
	private int totalTime;		// Estimated replay time in ms
	private int frameTime;		// Frame replay time in us
	private ByteBuffer buffer;	// Sound buffer
	private int bufferSize;		// Used buffer size
	private int totalSize;		// Total buffer size
	private int volume;			// Current master volume
	private int loopCount;		// Loop counter
	private int virtChannels;	// Number of virtual channels
	private int virtUsed;		// Used virtual channels
	private int sequence;		// Current sequence

	private final ChannelInfo[] channelInfo = new ChannelInfo[Xmp.MAX_CHANNELS];
	
	private final byte[] bufferArray = new byte[Xmp.MAX_FRAMESIZE];
	
	public static class ChannelInfo {
		private int period;			// Sample period
		private int position;		// Sample position
		private short pitchbend;	// Linear bend from base note
		private byte note;			// Current base note number
		private byte instrument;	// Current instrument number
		private byte sample;		// Current sample number
		private byte volume;		// Current volume
		private byte pan;			// Current stereo pan
		private Module.Event event = new Module.Event();	// Current track event
		
		public int getPeriod() {
			return period;
		}
		
		public int getPosition() {
			return position;
		}
		
		public int getPitchbend() {
			return pitchbend;
		}
		
		public int getNote() {
			return (int)note & 0xff;
		}
		
		public int getInstrument() {
			return (int)instrument & 0xff;
		}
		
		public int getSample() {
			return (int)sample & 0xff;
		}
		
		public int getVolume() {
			return (int)volume & 0xff;
		}
		
		public int getPan() {
			return (int)pan & 0xff;
		}
		
		public Module.Event getEvent() {
			return event;
		}
	}
	
	public FrameInfo() {
		for (int i = 0; i < channelInfo.length; i++) {
			channelInfo[i] = new ChannelInfo();
		}
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getPattern() {
		return pattern;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getFrame() {
		return frame;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getBpm() {
		return bpm;
	}
	
	public int getTime() {
		return time;
	}
	
	public int getTotalTime() {
		return totalTime;
	}
	
	public int getFrameTime() {
		return frameTime;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public byte[] getBufferArray() {
		buffer.clear();
		buffer.get(bufferArray, 0, bufferSize);
		return bufferArray;
	}
	
	public int getBufferSize() {
		return bufferSize;
	}

	public int getTotalSize() {
		return totalSize;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public int getLoopCount() {
		return loopCount;
	}
	
	public int getVirtChannels() {
		return virtChannels;
	}
	
	public int getVirtUsed() {
		return virtUsed;
	}
	
	public int getSequence() {
		return sequence;
	}
	
	public ChannelInfo[] getChannelInfo() {
		return channelInfo;
	}
}

