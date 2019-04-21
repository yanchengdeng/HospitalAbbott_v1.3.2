package com.comvee.hospitalabbott.tool;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

/**
 * Created by xingkong on 2018/5/24.
 */

public class LocalLogTool {
    private static final String LOG_NAME = "ComveeRefreshLog.txt";

    public static void saveLog(String log) {
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File saveFile = new File(sdCardDir, LOG_NAME);
        if (saveFile.exists() || createFile()) {
            try {
                FileWriter writer = new FileWriter(saveFile,true);
                writer.write(log);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean createFile() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + LOG_NAME;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
