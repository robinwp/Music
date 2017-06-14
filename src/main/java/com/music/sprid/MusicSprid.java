package com.music.sprid;

import com.music.pojo.Music;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MusicSprid {
	private int pageTotal;

	public int getPageTotal() {
		return pageTotal;
	}

	public List<Music> getMusic(String keyword, int page) throws IOException {
		URL url = new URL(
				"http://songsearch.kugou.com/song_search_v2?&keyword="
						+ URLEncoder.encode(keyword, "utf-8") + "&page=" + page + "&pagesize=30&userid=-1&clientver=&platform=WebFilter&filter=2&iscorrection=1&privilege_filter=0&_=1497195202323");
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
		urlConnection.setConnectTimeout(5_000);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream in = urlConnection.getInputStream();
		byte[] buff = new byte[1024];
		int len;
		while ((len = in.read(buff, 0, 1024)) != -1) {
			baos.write(buff, 0, len);
		}
		String info = new String(baos.toByteArray(), "utf-8");
		in.close();
		baos.close();
		JSONObject jsonObject = JSONObject.fromObject(info);
		JSONArray jsonObject1 = jsonObject.getJSONObject("data").getJSONArray("lists");
		pageTotal = (jsonObject.getJSONObject("data").getInt("total") - 1) / 30 + 1;
		List<Music> musics = new ArrayList<>();
		for (int i = 0, size = jsonObject1.size(); i < size; i++) {
			String fileName = jsonObject1.getJSONObject(i).getString("FileName");
			//fileName = fileName.replaceAll("<em>", "").replaceAll("</em>", "");
			String songname = jsonObject1.getJSONObject(i).getString("SongName");
			//songname = songname.replaceAll("<em>", "").replaceAll("</em>", "");
			String author = jsonObject1.getJSONObject(i).getString("SingerName");
			//author = author.replaceAll("<em>", "").replaceAll("</em>", "");
			String hashcode = jsonObject1.getJSONObject(i).getString("FileHash");
			String albumName = jsonObject1.getJSONObject(i).getString("AlbumName");
			String extName = jsonObject1.getJSONObject(i).getString("ExtName");
			int filesize = jsonObject1.getJSONObject(i).getInt("FileSize");
			int bitrate = jsonObject1.getJSONObject(i).getInt("Bitrate");
			int time = filesize * 8 / bitrate/1000;
			int m = time / 60;
			int ss = time % 60;
			Music music = new Music();
			music.setAlbumName(albumName);
			music.setHashcode(hashcode);
			music.setFilesize(String.valueOf(filesize));
			music.setFilename(fileName);
			if (ss < 10) {
				music.setTime(m + ":0" + ss);
			} else {
				music.setTime(m + ":" + ss);
			}
			music.setAuthor(author);
			music.setSongname(songname);
			music.setExtName(extName);
			musics.add(music);
		}
		return musics;
	}

	public void setMusic(String hashcode, Music music) throws IOException {
		URL url = new URL("http://www.kugou.com/yy/index.php?r=play/getdata&hash=" + hashcode+ "&album_id=0");
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
		urlConnection.setConnectTimeout(5_000);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream in = urlConnection.getInputStream();
		byte[] buff = new byte[1024];
		int len;
		while ((len = in.read(buff, 0, 1024)) != -1) {
			baos.write(buff, 0, len);
		}
		String info = new String(baos.toByteArray(), "utf-8");
		in.close();
		baos.close();
		JSONObject jsonObject = JSONObject.fromObject(info).getJSONObject("data");
		String uri = jsonObject.getString("play_url");
		if(uri.equals("")){
			
		}
		music.setUrl(uri);
	}
}
