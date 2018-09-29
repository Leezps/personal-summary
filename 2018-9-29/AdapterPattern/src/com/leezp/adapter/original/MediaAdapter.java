package com.leezp.adapter.original;

import com.leezp.adapter.access.AdvancedMediaPalyer;
import com.leezp.adapter.access.Mp4Player;
import com.leezp.adapter.access.VlcPlayer;

public class MediaAdapter implements MediaPlayer {
	
	AdvancedMediaPalyer advancedMusicPlayer;
	
	public MediaAdapter(String audioType) {
		if(audioType.equalsIgnoreCase("vlc")) {
			advancedMusicPlayer = new VlcPlayer();
		} else if (audioType.equalsIgnoreCase("mp4")) {
			advancedMusicPlayer = new Mp4Player();
		}
	}

	@Override
	public void play(String audioType, String fileName) {
		if (audioType.equalsIgnoreCase("vlc")) {
			advancedMusicPlayer.playVlc(fileName);
		} else if (audioType.equalsIgnoreCase("mp4")) {
			advancedMusicPlayer.playMp4(fileName);
		}
	}
}
