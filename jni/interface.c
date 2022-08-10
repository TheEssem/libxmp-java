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

#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <jni.h>
#include <xmp.h>

#define METHOD(type,name) JNIEXPORT type JNICALL \
Java_org_helllabs_libxmp_Xmp_##name

static struct test_info {
	jclass class;
	jfieldID name;
	jfieldID type;
} test_info;

static struct frame_info {
	jclass class;
	jfieldID position;
	jfieldID pattern;
	jfieldID row;
	jfieldID numRows;
	jfieldID frame;
	jfieldID speed;
	jfieldID bpm;
	jfieldID time;
	jfieldID totalTime;
	jfieldID frameTime;
	jfieldID buffer;
	jfieldID bufferSize;
	jfieldID totalSize;
	jfieldID volume;
	jfieldID loopCount;
	jfieldID virtChannels;
	jfieldID virtUsed;
	jfieldID sequence;
	jfieldID channelInfo;
} frame_info;

static struct channel_info {
	jclass class;
	jfieldID period;
	jfieldID position;
	jfieldID pitchbend;
	jfieldID note;
	jfieldID instrument;
	jfieldID sample;
	jfieldID volume;
	jfieldID pan;
	jfieldID event;
} channel_info;

static struct event_data {
	jclass class;
	jfieldID note;
	jfieldID ins;
	jfieldID vol;
	jfieldID fxt;
	jfieldID fxp;
	jfieldID f2t;
	jfieldID f2p;
} event_data;

static struct mod_data {
	jclass class;
	jfieldID name;
	jfieldID type;
	jfieldID numPatterns;
	jfieldID numChannels;
	jfieldID numInstruments;
	jfieldID numSamples;
	jfieldID initialSpeed;
	jfieldID initialBpm;
	jfieldID length;
} mod_data;

static struct pattern_data {
	jclass class;
	jfieldID ctx;
	jfieldID num;
	jfieldID numRows;
} pattern_data;

static struct instrument_data {
	jclass class;
	jfieldID name;
	jfieldID numSamples;
	jfieldID sampleID;
} instrument_data;

static struct sample_data {
	jclass class;
	jfieldID name;
	jfieldID length;
	jfieldID loopStart;
	jfieldID loopEnd;
	jfieldID flags;
	jfieldID data;
} sample_data;

#define GET_CLASS(a,b)   a.class = (*env)->FindClass(env, b)
#define GET_FIELD(a,b,c) a.b = (*env)->GetFieldID(env, a.class, #b, c)

static void init_test_info_fields(JNIEnv *env)
{
	if (test_info.class != NULL)
		return;

	GET_CLASS(test_info, "org/helllabs/libxmp/Module$TestInfo");

	GET_FIELD(test_info, name, "Ljava/lang/String;");
	GET_FIELD(test_info, type, "Ljava/lang/String;");
}

static void init_frame_info_fields(JNIEnv *env)
{
	if (frame_info.class != NULL)
		return;

	GET_CLASS(frame_info, "org/helllabs/libxmp/FrameInfo");

	GET_FIELD(frame_info, position, "I");
	GET_FIELD(frame_info, pattern, "I");
	GET_FIELD(frame_info, row, "I");
	GET_FIELD(frame_info, numRows, "I");
	GET_FIELD(frame_info, frame, "I");
	GET_FIELD(frame_info, speed, "I");
	GET_FIELD(frame_info, bpm, "I");
	GET_FIELD(frame_info, time, "I");
	GET_FIELD(frame_info, totalTime, "I");
	GET_FIELD(frame_info, frameTime, "I");
	GET_FIELD(frame_info, buffer, "Ljava/nio/ByteBuffer;");
	GET_FIELD(frame_info, bufferSize, "I");
	GET_FIELD(frame_info, totalSize, "I");
	GET_FIELD(frame_info, volume, "I");
	GET_FIELD(frame_info, loopCount, "I");
	GET_FIELD(frame_info, virtChannels, "I");
	GET_FIELD(frame_info, virtUsed, "I");
	GET_FIELD(frame_info, sequence, "I");
	GET_FIELD(frame_info, channelInfo,
			"[Lorg/helllabs/libxmp/FrameInfo$ChannelInfo;");
}

static void init_channel_info_fields(JNIEnv *env)
{
	if (channel_info.class != NULL)
		return;

	GET_CLASS(channel_info, "org/helllabs/libxmp/FrameInfo$ChannelInfo");

	GET_FIELD(channel_info, period, "I");
	GET_FIELD(channel_info, position, "I");
	GET_FIELD(channel_info, pitchbend, "S");
	GET_FIELD(channel_info, note, "B");
	GET_FIELD(channel_info, instrument, "B");
	GET_FIELD(channel_info, sample, "B");
	GET_FIELD(channel_info, volume, "B");
	GET_FIELD(channel_info, pan, "B");
	GET_FIELD(channel_info, event, "Lorg/helllabs/libxmp/Module$Event;");
}

static void init_event_data_fields(JNIEnv *env)
{
	if (event_data.class != NULL)
		return;

	GET_CLASS(event_data, "org/helllabs/libxmp/Module$Event");

	GET_FIELD(event_data, note, "I");
	GET_FIELD(event_data, ins, "I");
	GET_FIELD(event_data, vol, "I");
	GET_FIELD(event_data, fxt, "I");
	GET_FIELD(event_data, fxp, "I");
	GET_FIELD(event_data, f2t, "I");
	GET_FIELD(event_data, f2p, "I");
}

static void init_mod_data_fields(JNIEnv *env)
{
	if (mod_data.class != NULL)
		return;

	GET_CLASS(mod_data, "org/helllabs/libxmp/Module");

	GET_FIELD(mod_data, name, "Ljava/lang/String;");
	GET_FIELD(mod_data, type, "Ljava/lang/String;");
	GET_FIELD(mod_data, numPatterns, "I");
	GET_FIELD(mod_data, numChannels, "I");
	GET_FIELD(mod_data, numInstruments, "I");
	GET_FIELD(mod_data, numSamples, "I");
	GET_FIELD(mod_data, initialSpeed, "I");
	GET_FIELD(mod_data, initialBpm, "I");
	GET_FIELD(mod_data, length, "I");
}

static void init_pattern_data_fields(JNIEnv *env)
{
	if (pattern_data.class != NULL)
		return;

	GET_CLASS(pattern_data, "org/helllabs/libxmp/Module$Pattern");

	GET_FIELD(pattern_data, ctx, "J");
	GET_FIELD(pattern_data, num, "I");
	GET_FIELD(pattern_data, numRows, "I");
}

static void init_instrument_data_fields(JNIEnv *env)
{
	if (instrument_data.class != NULL)
		return;

	GET_CLASS(instrument_data, "org/helllabs/libxmp/Module$Instrument");

	GET_FIELD(instrument_data, name, "Ljava/lang/String;");
	GET_FIELD(instrument_data, numSamples, "I");
	GET_FIELD(instrument_data, sampleID, "[I");
}

static void init_sample_data_fields(JNIEnv *env)
{
	if (sample_data.class != NULL)
		return;

	GET_CLASS(sample_data, "org/helllabs/libxmp/Module$Sample");

	GET_FIELD(sample_data, name, "Ljava/lang/String;");
	GET_FIELD(sample_data, length, "I");
	GET_FIELD(sample_data, loopStart, "I");
	GET_FIELD(sample_data, loopEnd, "I");
	GET_FIELD(sample_data, flags, "I");
	GET_FIELD(sample_data, data, "Ljava/nio/ByteBuffer;");
}

/*
 * Native API methods
 */

METHOD(jlong, createContext) (JNIEnv *env, jobject obj)
{
	/* Cache class field IDs */
	init_frame_info_fields(env);
	init_channel_info_fields(env);
	init_event_data_fields(env);
	init_mod_data_fields(env);
	init_pattern_data_fields(env);
	init_instrument_data_fields(env);
	init_sample_data_fields(env);

        return (jlong)xmp_create_context();
}

METHOD(void, freeContext) (JNIEnv *env, jobject obj, jlong ctx)
{
        xmp_free_context((xmp_context)ctx);
}

METHOD(jint, loadModule) (JNIEnv *env, jobject obj, jlong ctx, jstring path)
{
	const char *filename;
	int res;

	if (path == NULL) {
		jclass ex = (*env)->FindClass(env,
				"java/lang/NullPointerException");
		(*env)->ThrowNew(env, ex, "File name is null");
		return -XMP_ERROR_LOAD;
	}

	if (path == NULL) {
		jclass ex = (*env)->FindClass(env,
				"java/lang/NullPointerException");
		(*env)->ThrowNew(env, ex, "File name is null");
		return -XMP_ERROR_LOAD;
	}

	filename = (*env)->GetStringUTFChars(env, path, NULL);
	res = xmp_load_module((xmp_context)ctx, (char *)filename);
	(*env)->ReleaseStringUTFChars(env, path, filename);

	return res;
}

METHOD(jint, testModule) (JNIEnv *env, jobject obj, jstring path, jobject info)
{
	const char *filename;
	int res;
	struct xmp_test_info ti;

	/* Cache class field IDs */
	init_test_info_fields(env);

	filename = (*env)->GetStringUTFChars(env, path, NULL);
	res = xmp_test_module((char *)filename, &ti);
	(*env)->ReleaseStringUTFChars(env, path, filename);

	if (res == 0) {
		(*env)->SetObjectField(env, info, test_info.name,
					(*env)->NewStringUTF(env, ti.name));

		(*env)->SetObjectField(env, info, test_info.type,
					(*env)->NewStringUTF(env, ti.type));
	}

	return res;
}

METHOD(void, releaseModule) (JNIEnv *env, jobject obj, jlong ctx)
{
	xmp_release_module((xmp_context)ctx);
}

METHOD(jint, startPlayer) (JNIEnv *env, jobject obj, jlong ctx, jint rate, jint flags)
{
	return xmp_start_player((xmp_context)ctx, rate, flags);
}

METHOD(jobject, getFrameInfo) (JNIEnv *env, jobject obj, jlong ctx, jobject info)
{
	struct xmp_frame_info fi;
	jobjectArray array;
	jobject event;
	int i, len;

	xmp_get_frame_info((xmp_context)ctx, &fi);

	(*env)->SetIntField(env, info, frame_info.position, fi.pos);
	(*env)->SetIntField(env, info, frame_info.pattern, fi.pattern);
	(*env)->SetIntField(env, info, frame_info.row, fi.row);
	(*env)->SetIntField(env, info, frame_info.numRows, fi.num_rows);
	(*env)->SetIntField(env, info, frame_info.frame, fi.frame);
	(*env)->SetIntField(env, info, frame_info.speed, fi.speed);
	(*env)->SetIntField(env, info, frame_info.bpm, fi.bpm);
	(*env)->SetIntField(env, info, frame_info.time, fi.time);
	(*env)->SetIntField(env, info, frame_info.totalTime, fi.total_time);
	(*env)->SetIntField(env, info, frame_info.frameTime, fi.frame_time);

	if ((*env)->GetObjectField(env, info, frame_info.buffer) == NULL) {
		jobject buf = (*env)->NewDirectByteBuffer(env, fi.buffer,
						XMP_MAX_FRAMESIZE);
		(*env)->SetObjectField(env, info, frame_info.buffer, buf);
	}

	(*env)->SetIntField(env, info, frame_info.bufferSize, fi.buffer_size);
	(*env)->SetIntField(env, info, frame_info.totalSize, fi.total_size);
	(*env)->SetIntField(env, info, frame_info.volume, fi.volume);
	(*env)->SetIntField(env, info, frame_info.loopCount, fi.loop_count);
	(*env)->SetIntField(env, info, frame_info.virtChannels, fi.virt_channels);
	(*env)->SetIntField(env, info, frame_info.virtUsed, fi.virt_used);
	(*env)->SetIntField(env, info, frame_info.sequence, fi.sequence);

	/* Channel info */

	array = (*env)->GetObjectField(env, info, frame_info.channelInfo);
	len = (*env)->GetArrayLength(env, array);

	for (i = 0; i < len; i++) {
		struct xmp_channel_info *ci = &fi.channel_info[i];
		jobject obj = (*env)->GetObjectArrayElement(env, array, i);

		(*env)->SetIntField(env, obj, channel_info.period, ci->period);
		(*env)->SetIntField(env, obj, channel_info.position, ci->position);
		(*env)->SetShortField(env, obj, channel_info.pitchbend, ci->pitchbend);
		(*env)->SetByteField(env, obj, channel_info.note, ci->note);
		(*env)->SetByteField(env, obj, channel_info.instrument, ci->instrument);
		(*env)->SetByteField(env, obj, channel_info.sample, ci->sample);
		(*env)->SetByteField(env, obj, channel_info.volume, ci->volume);
		(*env)->SetByteField(env, obj, channel_info.pan, ci->pan);

		/* Event */

		event = (*env)->GetObjectField(env, obj, channel_info.event);

		(*env)->SetIntField(env, event, event_data.note, ci->event.note);
		(*env)->SetIntField(env, event, event_data.ins, ci->event.ins);
		(*env)->SetIntField(env, event, event_data.vol, ci->event.vol);
		(*env)->SetIntField(env, event, event_data.fxt, ci->event.fxt);
		(*env)->SetIntField(env, event, event_data.fxp, ci->event.fxp);
		(*env)->SetIntField(env, event, event_data.f2t, ci->event.f2t);
		(*env)->SetIntField(env, event, event_data.f2p, ci->event.f2p);

		(*env)->DeleteLocalRef(env, obj);
	}

	return info;
}

METHOD(jint, playFrame) (JNIEnv *env, jobject obj, jlong ctx)
{
	return xmp_play_frame((xmp_context)ctx);
}

METHOD(void, endPlayer) (JNIEnv *env, jobject obj, jlong ctx)
{
	xmp_end_player((xmp_context)ctx);
}

METHOD(int, setPlayer) (JNIEnv *env, jobject obj, jlong ctx, jint param, jlong value)
{
	return xmp_set_player((xmp_context)ctx, param, value);
}

METHOD(int, getPlayer) (JNIEnv *env, jobject obj, jlong ctx, jint param)
{
	return xmp_get_player((xmp_context)ctx, param);
}

METHOD(jobjectArray, getFormatList) (JNIEnv *env, jobject obj)
{
	jobjectArray ret;
	char **list;
	int i;

	list = xmp_get_format_list();
	for (i = 0; list[i]; i++);
	
	ret = (jobjectArray)(*env)->NewObjectArray(env, i,  
				(*env)->FindClass(env, "java/lang/String"),  
				(*env)->NewStringUTF(env, ""));  

	while (i--) {  
		(*env)->SetObjectArrayElement(env, ret, i,
				(*env)->NewStringUTF(env, list[i]));  
	} 

	return ret;
}

METHOD(int, nextPosition) (JNIEnv *env, jobject obj, jlong ctx)
{
	return xmp_next_position((xmp_context)ctx);
}

METHOD(int, prevPosition) (JNIEnv *env, jobject obj, jlong ctx)
{
	return xmp_prev_position((xmp_context)ctx);
}

METHOD(int, setPosition) (JNIEnv *env, jobject obj, jlong ctx, jint num)
{
	return xmp_set_position((xmp_context)ctx, num);
}

METHOD(void, scanModule) (JNIEnv *env, jobject obj, jlong ctx)
{
	xmp_scan_module((xmp_context)ctx);
}

METHOD(void, stopModule) (JNIEnv *env, jobject obj, jlong ctx)
{
	return xmp_stop_module((xmp_context)ctx);
}

METHOD(void, restartModule) (JNIEnv *env, jobject obj, jlong ctx)
{
	return xmp_restart_module((xmp_context)ctx);
}

METHOD(int, seekTime) (JNIEnv *env, jobject obj, jlong ctx, jint time)
{
	return xmp_seek_time((xmp_context)ctx, time);
}

METHOD(int, channelMute) (JNIEnv *env, jobject obj, jlong ctx, jint chn, jint val)
{
	return xmp_channel_mute((xmp_context)ctx, chn, val);
}

METHOD(int, channelVol) (JNIEnv *env, jobject obj, jlong ctx, jint chn, jint val)
{
	return xmp_channel_vol((xmp_context)ctx, chn, val);
}


/*
 * Helpers
 */

METHOD(int, getErrno) (JNIEnv *env, jobject obj)
{
	return errno;
}

METHOD(jobject, getStrError) (JNIEnv *env, jobject obj, jint err)
{
	char c[128];
	strerror_r(err, c, 128);
	return (*env)->NewStringUTF(env, c);
}

METHOD(void, getModData) (JNIEnv *env, jobject obj, jlong ctx, jobject mod)
{
	struct xmp_module_info mi;

	xmp_get_module_info((xmp_context)ctx, &mi);

	(*env)->SetObjectField(env, mod, mod_data.name,
				(*env)->NewStringUTF(env, mi.mod->name));
	(*env)->SetObjectField(env, mod, mod_data.type,
				(*env)->NewStringUTF(env, mi.mod->type));
	(*env)->SetIntField(env, mod, mod_data.numPatterns, mi.mod->pat);
	(*env)->SetIntField(env, mod, mod_data.numChannels, mi.mod->chn);
	(*env)->SetIntField(env, mod, mod_data.numInstruments, mi.mod->ins);
	(*env)->SetIntField(env, mod, mod_data.numSamples, mi.mod->smp);
	(*env)->SetIntField(env, mod, mod_data.initialSpeed, mi.mod->spd);
	(*env)->SetIntField(env, mod, mod_data.initialBpm, mi.mod->bpm);
	(*env)->SetIntField(env, mod, mod_data.length, mi.mod->len);
}

METHOD(void, getEventData) (JNIEnv *env, jobject obj, jlong ctx, jint pat, jint row, jint chn, jobject event)
{
	struct xmp_module_info mi;
	struct xmp_pattern *xxp;
	struct xmp_event *e;

	xmp_get_module_info((xmp_context)ctx, &mi);

	if (pat >= mi.mod->pat) {
		jclass ex = (*env)->FindClass(env,
				"java/lang/IndexOutOfBoundsException");
		(*env)->ThrowNew(env, ex, "Invalid pattern number");
		return;
	}

	xxp = mi.mod->xxp[pat];

	if (row >= xxp->rows) {
		jclass ex = (*env)->FindClass(env,
				"java/lang/IndexOutOfBoundsException");
		(*env)->ThrowNew(env, ex, "Invalid Row number");
		return;
	}

	if (chn >= mi.mod->chn) {
		jclass ex = (*env)->FindClass(env,
				"java/lang/IndexOutOfBoundsException");
		(*env)->ThrowNew(env, ex, "Invalid channel number");
		return;
	}

	e = &mi.mod->xxt[mi.mod->xxp[pat]->index[chn]]->event[row];

	(*env)->SetIntField(env, event, event_data.note, e->note);
	(*env)->SetIntField(env, event, event_data.ins, e->ins);
	(*env)->SetIntField(env, event, event_data.vol, e->vol);
	(*env)->SetIntField(env, event, event_data.fxt, e->fxt);
	(*env)->SetIntField(env, event, event_data.fxp, e->fxp);
	(*env)->SetIntField(env, event, event_data.f2t, e->f2t);
	(*env)->SetIntField(env, event, event_data.f2p, e->f2p);
}

METHOD(void, getPatternData) (JNIEnv *env, jobject obj, jlong ctx, jint num, jobject pattern)
{
	struct xmp_module_info mi;
	struct xmp_pattern *xxp;

	xmp_get_module_info((xmp_context)ctx, &mi);

	if (num >= mi.mod->pat) {
		jclass ex = (*env)->FindClass(env,
				"java/lang/IndexOutOfBoundsException");
		(*env)->ThrowNew(env, ex, "Invalid pattern number");
		return;
	}

	xxp = mi.mod->xxp[num];

	(*env)->SetLongField(env, pattern, pattern_data.ctx, ctx);
	(*env)->SetIntField(env, pattern, pattern_data.num, num);
	(*env)->SetIntField(env, pattern, pattern_data.numRows, xxp->rows);
}

METHOD(void, getInstrumentData) (JNIEnv *env, jobject obj, jlong ctx, jint num, jobject instrument)
{
	struct xmp_module_info mi;
	struct xmp_instrument *xxi;
	jint *region;
	jobject sid;
	int i;
	
	xmp_get_module_info((xmp_context)ctx, &mi);

	if (num >= mi.mod->ins) {
		jclass ex = (*env)->FindClass(env,
				"java/lang/IndexOutOfBoundsException");
		(*env)->ThrowNew(env, ex, "Invalid instrument number");
		return;
	}

	xxi = &mi.mod->xxi[num];

	(*env)->SetObjectField(env, instrument, instrument_data.name,
				(*env)->NewStringUTF(env, xxi->name));
	(*env)->SetIntField(env, instrument, instrument_data.numSamples,
				xxi->nsm);

	sid = (jintArray)(*env)->NewIntArray(env, xxi->nsm);

	if ((region = malloc(xxi->nsm * sizeof(jint))) == NULL)
		return;

	for (i = 0; i < xxi->nsm; i++) {  
		region[i] = xxi->sub[i].sid;
	} 

	(*env)->SetIntArrayRegion(env, sid, 0, xxi->nsm, region);
	(*env)->SetObjectField(env, instrument, instrument_data.sampleID, sid);

	free(region);
}

METHOD(void, getSampleData) (JNIEnv *env, jobject obj, jlong ctx, jint num, jobject sample)
{
	struct xmp_module_info mi;
	struct xmp_sample *xxs;
	jobject data;
	int size;
	
	xmp_get_module_info((xmp_context)ctx, &mi);

	if (num >= mi.mod->smp) {
		jclass ex = (*env)->FindClass(env,
				"java/lang/IndexOutOfBoundsException");
		(*env)->ThrowNew(env, ex, "Invalid sample number");
		return;
	}

	xxs = &mi.mod->xxs[num];

	(*env)->SetObjectField(env, sample, sample_data.name,
				(*env)->NewStringUTF(env, xxs->name));
	(*env)->SetIntField(env, sample, sample_data.length, xxs->len);
	(*env)->SetIntField(env, sample, sample_data.loopStart, xxs->lps);
	(*env)->SetIntField(env, sample, sample_data.loopEnd, xxs->lpe);
	(*env)->SetIntField(env, sample, sample_data.flags, xxs->flg);

	size = xxs->len;
	if (xxs->flg & XMP_SAMPLE_16BIT)
		size <<= 1;

	data = (*env)->NewDirectByteBuffer(env, xxs->data, size);
	(*env)->SetObjectField(env, sample, sample_data.data, data);
}

/* Mixer API */

METHOD(jint, startSmix) (JNIEnv *env, jobject obj, jlong ctx, jint chn, jint smp)
{
	return xmp_start_smix((xmp_context)ctx, chn, smp);
}

METHOD(void, endSmix) (JNIEnv *env, jobject obj, jlong ctx)
{
	xmp_end_smix((xmp_context)ctx);
}

METHOD(jint, smixPlayInstrument) (JNIEnv *env, jobject obj, jlong ctx, jint ins, jint note, jint vol, jint chn)
{
	return xmp_smix_play_instrument((xmp_context)ctx, ins, note, vol, chn);
}

METHOD(jint, smixPlaySample) (JNIEnv *env, jobject obj, jlong ctx, jint ins, jint note, jint vol, jint chn)
{
	return xmp_smix_play_sample((xmp_context)ctx, ins, note, vol, chn);
}

METHOD(jint, smixChannelPan) (JNIEnv *env, jobject obj, jlong ctx, jint chn, jint pan)
{
	return xmp_smix_channel_pan((xmp_context)ctx, chn, pan);
}

METHOD(jint, smixLoadSample) (JNIEnv *env, jobject obj, jlong ctx, jint num, jstring path)
{
	const char *filename;
	int res;

	filename = (*env)->GetStringUTFChars(env, path, NULL);
	res = xmp_smix_load_sample((xmp_context)ctx, num, (char *)filename);
	(*env)->ReleaseStringUTFChars(env, path, filename);

	return res;
}

METHOD(jint, smixReleaseSample) (JNIEnv *env, jobject obj, jlong ctx, jint num)
{
	return xmp_smix_release_sample((xmp_context)ctx, num);
}

