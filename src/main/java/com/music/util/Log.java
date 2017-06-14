package com.music.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author 墨迹 on 2017年5月31日
 *
 */
public final class Log {
    private static FileOutputStream fot;
    private static PrintWriter out;

    static {
        File file = new File("logs/music.txt");
        File filedir = file.getParentFile();
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
        try {
            fot = new FileOutputStream(file);
            out = new PrintWriter(fot);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void logger(String meg) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        out.println(sdf.format(date) + " " + meg);
        out.flush();
    }
}
