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

public class Mixer {
	
	private final long ctx;
	
	public Mixer(final Player player, final int chn, final int smp) {
		ctx = player.getContext();
		final int code = Xmp.startSmix(ctx, chn, smp);
		
		switch (code) {
		case -Xmp.ERROR_STATE:
			
		
		case -Xmp.ERROR_INTERNAL:
			throw new RuntimeException(Xmp.ERROR_STRING[-code]);
		
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		Xmp.endSmix(ctx);
		super.finalize();
	}
	
	public int playInstrument(final int ins, final int note, final int vol, final int chn) {
		return Xmp.smixPlayInstrument(ctx, ins, note, vol, chn);
	}
	
	public int playInstrument(final int ins, final int chn) {
		return Xmp.smixPlayInstrument(ctx, ins, 60, 64, chn);
	}
	
	public int playSample(final int ins, final int note, final int vol, final int chn) {
		return Xmp.smixPlaySample(ctx, ins, note, vol, chn);
	}
	
	public int playSample(final int ins, final int chn) {
		return Xmp.smixPlaySample(ctx, ins, 60, 64, chn);
	}
	
	public int channelPan(final int chn, final int pan) {
		return Xmp.smixChannelPan(ctx, chn, pan);
	}
	
	public int loadSample(final int num, final String path) {
		return Xmp.smixLoadSample(ctx, num, path);
	}
	
	public int releaseSample(final int num) {
		return Xmp.smixReleaseSample(ctx, num);
	}

}
