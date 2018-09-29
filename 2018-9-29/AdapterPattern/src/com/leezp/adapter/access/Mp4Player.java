package com.leezp.adapter.access;

public class Mp4Player implements AdvancedMediaPalyer {

	@Override
	public void playVlc(String fileName) {
		//什么也不做
	}

	@Override
	public void playMp4(String fileName) {
		System.out.println("Playing mp4 file. Name: "+fileName);
	}

}
