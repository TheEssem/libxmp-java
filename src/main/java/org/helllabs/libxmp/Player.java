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


public class Player {

	private final int samplingRate;
	private final int mode;
	private final long ctx;
	private Module module;

	public static class Callback {
		public boolean callback(final FrameInfo info, final Object args) {
			return true;
		}
	}

	public Player() {
		this(44100);
	}

	public Player(final int samplingRate) {
		this(samplingRate, 0);
	}	

	public Player(final int samplingRate, final int mode) {
		this.samplingRate = samplingRate;
		this.mode = mode;
		ctx = Xmp.createContext();
	}		

	@Override
	protected void finalize() throws Throwable {
		if (module != null) {
			releaseModule();
		}
		Xmp.freeContext(ctx);
		super.finalize();
	}

	long getContext() {
		return ctx;
	}
	
	/**
	 * Load a module into this player context
	 * 
	 * @param path Pathname to the module file to load,
	 * @return The module just loaded.
	 * @throws IOException
	 */
	public Module loadModule(final String path) throws IOException {
		final int code = Xmp.loadModule(ctx, path);
		if (code < 0) {
			throw new IOException(Xmp.ERROR_STRING[-code]);
		}
		
		module = new Module(this);

		return module;
	}
	
	/**
	 * Release memory allocated by a module in this player context.
	 * 
	 * @return The player object.
	 */
	public Player releaseModule() {
		Xmp.releaseModule(ctx);
		module = null;
		return this;
	}

	/**
	 * Start playing the currently loaded module.
	 * 
	 * @return The player object.
	 */
	public Player start() {
		final int code = Xmp.startPlayer(ctx, samplingRate, mode);
		switch (code) {
		case 0:
			// We're good
			break;
		case -Xmp.ERROR_SYSTEM:
		case -Xmp.ERROR_INTERNAL:
			throw new RuntimeException(Xmp.ERROR_STRING[-code]);
		case -Xmp.ERROR_INVALID:
			throw new IllegalArgumentException("Invalid sampling rate " + samplingRate + "Hz");
		case -Xmp.ERROR_STATE:
			throw new IllegalStateException("Invalid player state");
		default:
			throw new RuntimeException("Unknown load error code " + code);
		}

		return this;
	}

	/**
	 * End module replay and releases player memory.
	 * 
	 * @return The player object.
	 */
	public Player end() {
		Xmp.endPlayer(ctx);
		return this;
	}

	/* Player parameters */
	
	public int getAmplificationFactor() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_AMP);
	}
	
	public int getMixing() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_MIX);
	}
	
	public int getInterpolation() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_INTERP);
	}
	
	public int getDSPEffectFlags() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_DSP);
	}
	
	public int getFlags() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_FLAGS);
	}
	
	public int getCurrentModuleFlags() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_CFLAGS);
	}
	
	public int getSampleControlFlags() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_SMPCTL);
	}
	
	public int getVolume() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_VOLUME);
	}
	
	public int getState() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_STATE);
	}
	
	public int getSampleMixerVolume() {
		return Xmp.getPlayer(ctx, Xmp.PLAYER_SMIX_VOLUME);
	}

	static private void checkParameterSet(final int code) {
		switch (code) {
		case -Xmp.ERROR_INVALID:
			throw new IllegalArgumentException();
		case -Xmp.ERROR_STATE:
			throw new IllegalStateException();
		default:
			break;
		}
	}
	
	public void setAmplificationFactor(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_AMP, val));
	}
	
	public void setMixing(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_MIX, val));
	}
	
	public void setvoiderpolation(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_INTERP, val));
	}
	
	public void setDSPEffectFlags(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_DSP, val));
	}
	
	public void setFlags(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_FLAGS, val));
	}
	
	public void setCurrentModuleFlags(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_CFLAGS, val));
	}
	
	public void setSampleControlFlags(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_SMPCTL, val));
	}
	
	public void setVolume(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_VOLUME, val));
	}
	
	public void setState(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_STATE, val));
	}
	
	public void setSampleMixerVolume(final int val) {
		checkParameterSet(Xmp.setPlayer(ctx, Xmp.PLAYER_SMIX_VOLUME, val));
	}

	/**
	 * Dynamically insert a new event into a playing module.
	 * 
	 * @param chn The channel to insert the new event.
	 * @param event The event to insert.
	 */
	public void injectEvent(final int chn, final Module.Event event) {
		Xmp.injectEvent(ctx, chn, event);
	}

	/**
	 * Get the list of supported module formats.
	 * 
	 * @return An array containing the names of all supported module formats.
	 */
	public static String[] formatList() {
		return Xmp.getFormatList();
	}

	/**
	 * Skip replay to the start of the next position.
	 * 
	 * @return The new position index.
	 */
	public int nextPosition() {
		return Xmp.nextPosition(ctx);
	}

	/**
	 * Skip replay to the start of the previous position.
	 * 
	 * @return The new position index.
	 */
	public int prevPosition() {
		return Xmp.prevPosition(ctx);
	}

	/**
	 * Skip replay to the start of the given position.
	 * 
	 * @param num The position index to set.
	 * @return The new position index.
	 */
	public int setPosition(final int num) {
		final int code = Xmp.setPosition(ctx, num);
		if (code == Xmp.ERROR_INVALID) {
			throw new IllegalArgumentException("Invalid position " + num);
		}
		return code;
	}

	/**
	 * Scan the loaded module for sequences and timing. Scanning is automatically
	 * performed when a module is loaded.
	 * 
	 * @return The player object.
	 */
	public Player scan() {
		Xmp.scanModule(ctx);
		return this;
	}

	public Player play() {
		return play(null, false, null);
	}

	public Player play(final Callback callback) {
		return play(callback, false, null);
	}

	public Player play(final Callback callback, final boolean loop) {
		return play(callback, loop, null);
	}

	public Player play(final Callback callback, final boolean loop, final Object args) {
		final FrameInfo info = new FrameInfo();
		start();
		while (playFrame(info)) {

			if (!loop && info.getLoopCount() > 0) {
				break;
			}

			if (callback != null && !callback.callback(info, args)) {
				break;
			}	
		}
		end();

		return this;
	}

	/**
	 * Play one frame of the module. Modules usually play at 50 frames per second.
	 * Use getFrameInfo() to retrieve the buffer containing audio data.
	 * 
	 * @return False if module was stopped, or true otherwise.
	 */
	public boolean playFrame() {
		return Xmp.playFrame(ctx) == 0;
	}

	/**
	 * Play one frame of the module. Modules usually play at 50 frames per second.
	 * 
	 * @param info Frame information will be written in this object.
	 * @return False if module was stopped, or true otherwise.
	 */
	public boolean playFrame(final FrameInfo info) {		
		final int ret = Xmp.playFrame(ctx);
		getFrameInfo(info);
		return ret == 0;
	}

	public Player resetBuffer() {
		return this;
	}

	public boolean playBuffer(final ByteBuffer buffer, final int bufferSize, final boolean loop) {

		return true;
	}

	/**
	 * Stop the currently playing module.
	 * 
	 * @return The player object.
	 */
	public Player stop() {
		Xmp.stopModule(ctx);
		return this;
	}

	/**
	 * Retrieve current frame information.
	 * 
	 * @param info Frame information will be written in this object.
	 * @return Current frame information.
	 */
	public FrameInfo getFrameInfo(final FrameInfo info) {
		final FrameInfo frameInfo = info == null ? new FrameInfo() : info;
		Xmp.getFrameInfo(ctx, frameInfo);
		return frameInfo;
	}

	/**
	 * Restart the currently playing module.
	 * 
	 * @return The player object.
	 */
	public Player restart() {
		Xmp.restartModule(ctx);
		return this;
	}

	/**
	 * Skip replay to the specified time.
	 * 
	 * @param time Time to seek in milliseconds.
	 * @return The new position index.
	 */
	public int seekTime(final int time) {
		return Xmp.seekTime(ctx, time);
	}

	/**
	 * Mute or unmute the specified channel.
	 * 
	 * @param chn The channel to mute or unmute.
	 * @param status 0 to mute channel, 1 to unmute or -1 to query the
	 *   current channel status.
	 * @return The previous channel status
	 */
	public int channelMute(final int chn, final int status) {
		return Xmp.channelMute(ctx, chn, status);
	}

	/**
	 * Set or retrieve the volume of the specified channel.
	 *  
	 * @param chn The channel to set or get volume.
	 * @param vol A value from 0-100 to set the channel volume, or -1 to
	 *   retrieve the current volume.
	 * @return The previous channel volume.
	 */
	public int channelVol(final int chn, final int vol) {
		return Xmp.channelVol(ctx, chn, vol);
	}
	
	/**
	 * Get the player sampling rate.
	 * 
	 * @return The sampling rate.
	 */
	public int getSamplingRate() {
		return samplingRate;
	}

	/**
	 * Get the currently loaded module.
	 *
	 * @return The loaded module, or null if no module is currently loaded.
	 */
	public Module getModule() {
		return module;
	}

	public int getMode() {
		return mode;
	}
}
