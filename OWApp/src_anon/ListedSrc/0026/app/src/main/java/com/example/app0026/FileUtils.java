package com.example.app0026;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    private FileUtils() {
    }

    public static void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteRecursive(child);
            }
        }
        file.delete();
    }

    public static File copyToExternalStorage(Context context, Uri uri) {
        File out = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            // Create a new file in the external storage directory
            File externalStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (externalStorageDir != null && !externalStorageDir.exists()) {
                externalStorageDir.mkdirs(); // Create the directory if it does not exist
            }

            out = new File(externalStorageDir, "" + System.currentTimeMillis());

            // Open input stream from the content URI
            inputStream = context.getContentResolver().openInputStream(uri);

            if (inputStream != null) {
                // Create an output stream to the destination file
                outputStream = new FileOutputStream(out);

                // Copy data from input stream to output stream
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Close streams in the finally block to avoid resource leaks
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return out;
    }
}