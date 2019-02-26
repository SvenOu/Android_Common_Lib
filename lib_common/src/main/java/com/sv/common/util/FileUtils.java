package com.sv.common.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sven-ou on 2015/10/23.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();
    private static final String DEFUALT_ENCODING_TYPE = "UTF-8";
    private static final String BLANK = "";

    private static ObjectMapper om = new ObjectMapper();
    static {
        om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 把对象以文件形式保存。其保存的格式为json
     * @param filePath
     * @param object
     * @param <T>
     * @return
     */
    public static <T> boolean saveObjectToFile(String filePath, T object) {
        try {
            om.writeValue(new File(filePath), object);
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
            return false;
        };
        return false;
    }

    public static <T> T readObjectFromFile(String filePath, Class<T> clazz) {
        try {
            return (T) om.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
        }
        return null;
    }

    public static boolean createFile(File file) {
        try {
            if (!file.getParentFile().exists()) {
                mkdir(file.getParentFile());
            }
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 比较日期大小
     * @param before
     * @param after
     * @return
     */
    public static int compareToDay(Date before, Date after){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(before);
        c2.setTime(after);
        int beforeYear = c1.get(Calendar.YEAR);
        int afterYear = c2.get(Calendar.YEAR);
        Logger.d(TAG, "beforeYear:" + beforeYear + ", afterYear:" +afterYear);
        int beforeDay = c1.get(Calendar.DAY_OF_YEAR);
        int afterDay = c2.get(Calendar.DAY_OF_YEAR);
        Logger.d(TAG, "beforeDay:" + beforeDay + ", afterDay:" +afterDay);
        if (beforeYear == afterYear) {
            if (beforeDay == afterDay) {
                return 0;
            }
            return beforeDay - afterDay;
        } else {
            return beforeYear - afterYear;
        }
    }

    public static boolean mkdir(File file) {
        while (!file.getParentFile().exists()) {
            mkdir(file.getParentFile());
        }
        return file.mkdir();
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }
    /**
     * 把流转换成字符串
     *
     * @param is
     *            输入流
     * @return 字符串
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
        return sb.toString();
    }

    /**
     * 关闭流
     *
     * @param stream
     *            可关闭的流
     */
    public static void closeStream(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {

        }
    }

    public static byte[] InputStreamToByte(InputStream is) throws IOException {

        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte byteData[] = bytestream.toByteArray();
        bytestream.close();
        return byteData;
    }

    public static String getImageFileMimeType(Context context, Uri fileUri)
    {
        String strMimeType = null;

        Cursor cursor = context.getContentResolver().query(fileUri,
                new String[] { MediaStore.MediaColumns.MIME_TYPE },
                null, null, null);

        if (cursor != null && cursor.moveToNext())
        {
            strMimeType = cursor.getString(0);
        }
        return strMimeType;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getFilePathFromURI(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();

        }else{
            ///for not sure file
            Cursor cursor = null;
            try {
                String[] proj = { MediaStore.Images.Media.DATA };
                cursor = context.getContentResolver().query(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } catch (Exception e) {
                Logger.e(TAG,e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void copyDatabaseToSdCardWithPath(Context context, String dbName, String destinationPath) {
        Logger.i(TAG, "********************************");
        InputStream in = null;
        OutputStream out = null;
        try {
            File f1 = context.getDatabasePath(dbName);
            if (f1.exists()) {
                File f2 = new File(destinationPath);
                FileUtils.createFile(f2);
                in = new FileInputStream(f1);
                out = new FileOutputStream(f2);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                Logger.i(TAG, "copy database: " + dbName + " to " + f2.getAbsolutePath() + " success!");
            }
        } catch (FileNotFoundException ex) {
            Logger.i(TAG, ex.getMessage() + " in the specified directory.");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.i(TAG, e.getMessage());
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {

            }
        }
        Logger.i(TAG, "********************************");
    }

    public static void fullyReadFileToBytes(File f) {
        byte[] buf = new byte[1024];
        byte[] result= null;
        ByteArrayOutputStream bos = null;
        try {
            FileInputStream fis = new FileInputStream(f);
            bos = new ByteArrayOutputStream();
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 小文件才能这样处理
     * @param filePath xxx/xxx/{profileId}.sqlite
     * @param fileData
     */
    public static void readAndWriteBytesToFile(String filePath,  byte[] fileData){
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                createFile(file);
            }
            fos = new FileOutputStream(file);
            fos.write(fileData);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
