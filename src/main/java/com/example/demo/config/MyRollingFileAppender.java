package com.example.demo.config;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zeze.li
 * @version 1.0.0
 * @ClassName MyRollingFileAppender.java
 * @Description TODO
 * @createTime 2020年07月27日 11:28:00
 */
public class MyRollingFileAppender extends RollingFileAppender {
    private long nextRollover = 0;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dfLog = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public void rollOver() {
        File target;
        File file;

        if (qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            nextRollover = size + maxFileSize;
        }
        LogLog.debug("maxBackupIndex=" + maxBackupIndex);

        boolean renameSucceeded = true;
        if (maxBackupIndex > 0) {
            // 删除序号最大（最早的文件）的文件
         /*file = new File(genFileName(fileName, maxBackupIndex));
         if (file.exists())
            renameSucceeded = file.delete();*/

            // 所有文件名序号加1
            for (int i = maxBackupIndex - 1; i >= 1 && renameSucceeded; i--) {
                file = new File(genFileName(fileName, i));
                if (file.exists()) {
                    target = new File(genFileName(fileName, i + 1));
                    renameSucceeded = file.renameTo(target);
                }
            }

            if (renameSucceeded) {
                target = new File(genFileName(fileName, 1));

                this.closeFile();

                file = new File(fileName);
                renameSucceeded = file.renameTo(target);

                if (!renameSucceeded) {
                    try {
                        this.setFile(fileName, true, bufferedIO, bufferSize);
                    } catch (IOException e) {
                        if (e instanceof InterruptedIOException) {
                            Thread.currentThread().interrupt();
                        }
                        LogLog.error("setFile(" + fileName
                                + ", true) call failed.", e);
                    }
                }
            }
        }
        if (renameSucceeded) {
            try {
                this.setFile(fileName, false, bufferedIO, bufferSize);
                nextRollover = 0;
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("setFile(" + fileName + ", false) call failed.", e);
            }
        }
    }

    private String genFileName(String name, int index) {
        String val = "_" + df.format(new Date()) + "_";
        String fileName = "";
        if (index > 0) {
            String num = index < 10 ? "0" + index : String.valueOf(index);
            fileName = name.replace(".log", "") + val + num + ".log";
        } else {
            fileName = name;
        }
        return fileName;
    }

    protected void subAppend(LoggingEvent event) {
        super.subAppend(event);
        boolean flag = false;
        String[] _files = fileName.split("/");
        int len = _files.length;
        String file_name = _files[len - 2];
        String new_file_name = df.format(new Date());
        if (!file_name.equals(new_file_name)) {
            flag = true;
            fileName = fileName.replace(file_name, new_file_name);
        }
        if (fileName != null && qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            if (flag) {
                size = 0L;
            }
            if ((size >= maxFileSize && size >= nextRollover) || flag) {
                rollOver();
            }
        }
    }

    public void setFile(String file) {
        // Trim spaces from both ends. The users probably does not want
        // trailing spaces in file names.
        String val = file.trim();
        fileName = val.substring(0, val.lastIndexOf("/")) + "/" + df.format(new Date()) +
                val.substring(val.lastIndexOf("/"), val.length());
        fileName = val.replace("{date}", df.format(new Date()));
        fileName = fileName.replace(".log", dfLog.format(new Date()) + ".log");
    }


}
