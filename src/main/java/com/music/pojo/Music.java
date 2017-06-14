package com.music.pojo;

/**
 * author 墨迹 on 2017/5/31.
 */
public class Music implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
    private String filename;
    private String author;
    private String url;
    private String songname;
    private String hashcode;
    private String time;
    private String filesize;
    private String albumName;
    private String img;
    private String extName;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    private String lyrics;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	@Override
	public String toString() {
		return "Music [id=" + id + ", filename=" + filename + ", author=" + author + ", url=" + url + ", songname="
				+ songname + ", hashcode=" + hashcode + ", time=" + time + ", filesize=" + filesize + ", albumName="
				+ albumName + ", img=" + img + ", extName=" + extName + ", lyrics=" + lyrics + "]";
	}

  
}
