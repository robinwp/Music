package com.music.download;

import com.music.pojo.Music;
import com.music.view.MainView;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
/**
 * author 墨迹 on 2017/5/31.
 */
public class MusicDown {
	
	public static Player play;
	
	public static void stop(){
		if(play!=null){
			play.close();
		}
	}
	
	public static void play(Music music) throws IOException, JavaLayerException{
		URL url = new URL(music.getUrl());
		URLConnection uRLConnection = url.openConnection();
		play = new Player(uRLConnection.getInputStream());
		play.play();
	}

	/**
	 * 下载歌曲
	 * @param music
	 * @param filepath
	 * @param mainView
	 * @throws IOException
	 */
	public static void down(Music music, String filepath,MainView mainView) throws IOException {
		String name = music.getFilename();
		int filesize = Integer.parseInt(music.getFilesize());
		URL url = new URL(music.getUrl());
		URLConnection uRLConnection = url.openConnection();
		InputStream in = uRLConnection.getInputStream();
		FileOutputStream fos = new FileOutputStream(new File(filepath+"."+music.getExtName()));
		byte[] bytes = new byte[1024];
		int len;
		int size = 0;
		while ((len = in.read(bytes)) != -1) {
			fos.write(bytes, 0, len);
			size += len;
			double f = (new Double(size) / new Double(filesize))*100;
			mainView.setInfo(name ,f,false);
		}
		mainView.setInfo(name,0,true);
		fos.close();
		in.close();
	}
}
