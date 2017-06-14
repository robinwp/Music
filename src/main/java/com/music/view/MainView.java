package com.music.view;

import java.awt.Toolkit;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.music.download.MusicDown;
import com.music.pojo.Music;
import com.music.sprid.MusicSprid;
import com.music.util.OpenUrlOnDefBs;
import com.music.util.SWTResourceManager;

import javazoom.jl.decoder.JavaLayerException;
import org.eclipse.swt.widgets.Link;

public class MainView {
	protected Shell shell;
	private Text text_keyWord;
	private MusicSprid musicSprid = new MusicSprid();
	private MessageBox mes;
	private org.eclipse.swt.widgets.List list_music;
	private Display display;
	private Button btn_prve;
	private Button btn_next;
	private Button btn_down;
	private CLabel label_info;
	private String keyword;
	private int pageTotal;
	private int pageNow = 1;
	private FileDialog fileSave;
	private List<Music> musics = new ArrayList<Music>();
	private boolean isSelected = false;
	private boolean isDown = false;
	private Button btn_play;
	private boolean isPlay = false;
	private CLabel play_info;
	private Link link;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainView window = new MainView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.MIN | SWT.CLOSE);
		shell.setImage(SWTResourceManager.getImage("img/music.ico"));
		shell.setSize(450, 340);
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;
		if (shellH > screenH)
			shellH = screenH;
		if (shellW > screenW)
			shellW = screenW;
		shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2) - 50);
		shell.setText("墨迹 mp3 下载器");
		shell.setLayout(null);
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				if (isDown) {
					e.doit = false;
					mes = new MessageBox(shell, SWT.ICON_WARNING);
					mes.setText("提示");
					mes.setMessage("还有下载任务在进行");
					mes.open();
				} else {
					System.exit(0);
				}
			}
		});
		fileSave = new FileDialog(shell, SWT.SAVE);
		fileSave.setText("保存");
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		lblNewLabel.setBounds(10, 10, 150, 25);
		lblNewLabel.setText("请输入搜索的关键字：");

		text_keyWord = new Text(shell, SWT.BORDER);
		text_keyWord.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					pageNow = 1;
					keyword = text_keyWord.getText();
					selectMusic();
				}
			}
		});
		text_keyWord.setBounds(166, 10, 196, 25);

		Button btn_select = new Button(shell, SWT.NONE);
		btn_select.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					pageNow = 1;
					keyword = text_keyWord.getText();
					selectMusic();
				}
			}
		});
		btn_select.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		btn_select.setBounds(368, 10, 66, 25);
		btn_select.setText("搜索");
		list_music = new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list_music.setBounds(10, 49, 424, 157);
		btn_prve = new Button(shell, SWT.NONE);
		btn_prve.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					prve();
				}
			}
		});
		btn_prve.setEnabled(false);
		btn_prve.setBounds(10, 212, 80, 27);
		btn_prve.setText("上一页");

		btn_next = new Button(shell, SWT.NONE);
		btn_next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					next();
				}
			}
		});
		btn_next.setEnabled(false);
		btn_next.setText("下一页");
		btn_next.setBounds(96, 212, 80, 27);

		btn_down = new Button(shell, SWT.NONE);
		btn_down.setEnabled(false);
		btn_down.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					down();
				}
			}
		});
		btn_down.setBounds(354, 212, 80, 27);
		btn_down.setText("下载");

		label_info = new CLabel(shell, SWT.NONE);
		label_info.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		label_info.setBounds(10, 277, 342, 27);
		label_info.setText("");

		btn_play = new Button(shell, SWT.NONE);
		btn_play.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					playMp3();
				}

			}
		});
		btn_play.setText("试听");
		btn_play.setEnabled(false);
		btn_play.setBounds(268, 212, 80, 27);

		play_info = new CLabel(shell, SWT.NONE);
		play_info.setToolTipText("");
		play_info.setText("");
		play_info.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		play_info.setBounds(10, 244, 342, 27);

		link = new Link(shell, SWT.NONE);
		link.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					OpenUrlOnDefBs.opUrl("http://118.89.44.108/webMusic");
				}
			}
		});
		link.setBounds(381, 287, 53, 17);
		link.setText("<a>by 墨迹</a>");
	}

	private void playMp3() {
		if (isPlay) {
			MusicDown.stop();
			btn_play.setText("试听");
			play_info.setText("");
		} else {
			int index = list_music.getSelectionIndex();
			if (index == -1) {
				mes = new MessageBox(shell, SWT.ICON_WARNING);
				mes.setText("提示");
				mes.setMessage("请选择要试听的歌曲");
				mes.open();
			} else {
				btn_play.setText("停止");
				Music music = musics.get(index);
				play_info.setText("正在播放：" + music.getFilename());
				play_info.setToolTipText("正在播放：" + music.getFilename());
				new Thread(new Runnable() {
					public void run() {
						try {
							if (music.getUrl() == null) {
								musicSprid.setMusic(music.getHashcode(), music);
								musics.set(index, music);
							}
							isPlay = true;
							MusicDown.play(music);
						} catch (IOException | JavaLayerException e) {
							display.asyncExec(new Runnable() {
								public void run() {
									mes = new MessageBox(shell, SWT.ICON_ERROR);
									mes.setText("错误");
									mes.setMessage("试听失败！");
									mes.open();
								}
							});
						}
						isPlay = false;
						display.asyncExec(new Runnable() {
							public void run() {
								play_info.setText("");
								btn_play.setText("试听");
							}
						});

					}
				}).start();

			}
		}
	}

	private void next() {
		if (!isSelected) {
			if (!isSelected) {
				if (pageNow < pageTotal) {
					pageNow++;
					selectMusic();
				}
			}
		}
	}

	private void down() {
		if (!isDown) {
			int index = list_music.getSelectionIndex();
			if (index == -1) {
				mes = new MessageBox(shell, SWT.ICON_WARNING);
				mes.setText("提示");
				mes.setMessage("请选择要下载的歌曲");
				mes.open();
			} else {
				fileSave.setFileName(musics.get(index).getFilename());
				String filepath = fileSave.open();
				if (filepath != null) {
					btn_down.setEnabled(false);
					isDown = true;
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Music music = musics.get(index);
								if (music.getUrl() == null) {
									musicSprid.setMusic(music.getHashcode(), music);
									musics.set(index, music);
								}
								MusicDown.down(music, filepath, MainView.this);
							} catch (IOException e) {
								display.asyncExec(new Runnable() {
									public void run() {
										mes = new MessageBox(shell, SWT.ICON_ERROR);
										mes.setText("错误");
										mes.setMessage("下载失败！");
										mes.open();
										label_info.setText("下载失败！");
										isDown = false;
										btn_down.setEnabled(true);
									}
								});
							}
						}
					}).start();
				}
			}
		}
	}

	/**
	 * 更新下载进度
	 * 
	 * @param d
	 */
	public void setInfo(String name, double d, boolean isfanish) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!isfanish) {
					DecimalFormat df = new DecimalFormat("#.00");
					label_info.setText("正在下载：" + name + "，进度：" + df.format(d) + "%");
					label_info.setToolTipText("正在下载：" + name + "，进度：" + df.format(d) + "%");
				} else {
					label_info.setText(name + "下载完成！");
					label_info.setToolTipText(name + "下载完成！");
					isDown = false;
					btn_down.setEnabled(true);
				}
			}

		});
	}

	private void prve() {
		if (!isSelected) {
			if (pageNow > 1) {
				pageNow--;
				selectMusic();
			}
		}
	}

	private void selectMusic() {
		if (!isSelected) {
			isSelected = true;
			btn_prve.setEnabled(false);
			btn_next.setEnabled(false);
			btn_down.setEnabled(false);
			btn_play.setEnabled(false);
			label_info.setText("正在搜索中···");
			new Thread(new Runnable() {
				public void run() {
					try {
						musics = musicSprid.getMusic(keyword, pageNow);
						pageTotal = musicSprid.getPageTotal();
						display.syncExec(new Runnable() {
							public void run() {
								list_music.removeAll();
								for (Music music : musics) {
									if (music.getAlbumName() != null && !music.getAlbumName().equals("")) {
										list_music.add("歌名：" + music.getFilename() + "，专辑名：《" + music.getAlbumName()
												+ "》，时长：" + music.getTime());
									} else {
										list_music.add("歌名：" + music.getFilename() + "，时长：" + music.getTime());
									}
								}
								label_info.setText("");
								btn_play.setEnabled(true);
								if (!isDown) {
									btn_down.setEnabled(true);
								}
								if (pageNow > 1) {
									btn_prve.setEnabled(true);
								}
								if (pageNow < pageTotal) {
									btn_next.setEnabled(true);
								}
								isSelected = false;
							}

						});

					} catch (IOException e) {
						display.syncExec(new Runnable() {
							public void run() {
								mes = new MessageBox(shell, SWT.ICON_ERROR);
								mes.setText("错误");
								mes.setMessage("网络连接超时！");
								mes.open();
								label_info.setText("");
								isSelected = false;
							}
						});
					}
				}
			}).start();
		}
	}
}
