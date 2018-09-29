package com.leezp.adapter.access;

public class VlcPlayer implements AdvancedMediaPalyer {

	@Override
	public void playVlc(String fileName) {
		System.out.println("Playing vlc file. Name: "+fileName);
	}

	@Override
	public void playMp4(String fileName) {
		//什么也不做
	}
}
