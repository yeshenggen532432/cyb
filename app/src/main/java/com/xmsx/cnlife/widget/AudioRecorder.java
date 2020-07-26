package com.xmsx.cnlife.widget;

import android.media.MediaRecorder;

import com.qwb.utils.Constans;

import java.io.File;
import java.io.IOException;

public class AudioRecorder {
	private static int SAMPLE_RATE_IN_HZ = 8000;

	MediaRecorder recorder;

	private String name;

	public AudioRecorder(String name) {
		this.name = name;
	}

	public void start() throws IOException {
		File directory = new File(Constans.DIR_VOICE);
		if (!directory.isDirectory()) {
			directory.mkdirs();
		}
		if(recorder==null){
			recorder = new MediaRecorder();
		}
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		recorder.setOutputFile(Constans.DIR_VOICE + name + ".amr");
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// recorder.setAudioChannels(1);
		// recorder.setAudioEncodingBitRate(64);
		recorder.prepare();
		recorder.start();
	}

	public void stop() throws IOException {
		if(recorder!=null){
			recorder.stop();
			recorder.release();
		}
	}

	public double getAmplitude() {
		if (recorder != null) {
			return (recorder.getMaxAmplitude());
		} else
			return 0;
	}
}