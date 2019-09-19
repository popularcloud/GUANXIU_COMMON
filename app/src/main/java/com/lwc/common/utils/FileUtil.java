package com.lwc.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.lwc.common.configs.FileConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * 文件操作工具类
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 */
public class FileUtil {

    /**
     * 构建文件夹路径
     *
     * @param path
     * @version 1.0
     * @createTime 2017-11-3,下午12:08:58
     * @updateTime 2017-11-3,下午12:08:58
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static void mkdirs(String path) {
        new File(path).mkdirs();
        try {
            new File(path + "/.nomedia").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算文件夹大小（非递归遍历文件夹）
     *
     * @param path 文件夹路径
     * @return 文件夹大小
     * @version 1.0
     * @createTime 2017-11-3,下午3:42:55
     * @updateTime 2017-11-3,下午3:42:55
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    @SuppressWarnings("rawtypes")
    public static float getDirectorySize(String path) {
        LinkedList list = new LinkedList(); // 保存待遍历文件夹的列表
        File file = new File(path);
        File tmp = null;
        float size = 0;

        size += getDirRootSize(file, list); // 调用遍历文件夹根目录文件的方法
        while (!list.isEmpty()) {
            tmp = (File) list.removeFirst();
            if (tmp.isDirectory()) {
                size += getDirRootSize(tmp, list);
            } else {
                size += tmp.length();

            }
        }

        return size;
    }

    /**
     * 遍历指定文件夹根目录下的文件
     *
     * @param file 需要遍历的文件夹
     * @param list 存放文件链接的列表
     * @version 1.0
     * @createTime 2017-11-3,下午3:27:12
     * @updateTime 2017-11-3,下午3:27:12
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static float getDirRootSize(File file, LinkedList list) {
        // 每个文件夹遍历都会调用该方法
        float size = 0;
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return size;
        }

        for (File f : files) {
            if (f.isDirectory()) {
                list.add(f);
            } else {
                // 这里列出当前文件夹根目录下的所有文件
                size += f.length();
            }
        }
        return size;
    }

    /**
     * 清理文件夹(递归清理)
     *
     * @param file
     * @version 1.0
     * @createTime 2017-11-3,下午5:10:09
     * @updateTime 2017-11-3,下午5:10:09
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static void clearDirectory(File file) {
        File[] flist = file.listFiles();
        if (flist == null || flist.length == 0) {
            file.delete();
        } else {
            for (File f : flist) {
                if (f.isDirectory()) {
                    clearDirectory(f);
                } else {
                    f.delete();
                }
            }
            file.delete();
        }
    }


    /**
     * 获取Assets下的缓存文件
     *
     * @param name
     * @return
     */
    public static String readAssetsFile(Context context, String name) {

        String result = null;
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer stringBuffer = new StringBuffer();
            String str = null;
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            br.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 普通权限复制文件
     *
     * @param fromfile 源文件
     * @param tofile   目标文件
     * @version 1.0
     * @createTime 2017-11-3,上午11:29:26
     * @updateTime 2017-11-3,上午11:29:26
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static boolean commoncopyfile(File fromfile, File tofile) {

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        byte[] buffer = new byte[4 * 1024];
        int temp;
        try {
            tofile.createNewFile();
            inputStream = new FileInputStream(fromfile);
            outputStream = new FileOutputStream(tofile);
            while ((temp = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            tofile.delete();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            tofile.delete();
            return false;
        } finally {
            try {
                buffer.clone();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    /**
     * 构建本地文件目录
     *
     * @version 1.0
     * @createTime 2017-11-3,下午3:44:53
     * @updateTime 2017-11-3,下午3:44:53
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static void createAllFile(String userName) {

        mkdirs(FileConfig.PATH_BASE);
        mkdirs(FileConfig.PATH_CAMERA);
        mkdirs(FileConfig.PATH_DOWNLOAD);
        mkdirs(FileConfig.PATH_IMAGES);
        mkdirs(FileConfig.PATH_LOG);
        mkdirs(FileConfig.PATH_HTML);

        new File(FileConfig.PATH_PHOTOS).mkdirs();
        // 以登录用户的帐号生成Md5串作为用户私有文件夹
        FileConfig.PATH_USER_FILE = FileConfig.PATH_BASE + EncryptUtil.md5Encrypt(userName) + "/";
        FileConfig.PATH_USER_AUDIO = FileConfig.PATH_USER_FILE + "audio/";
        FileConfig.PATH_USER_IMAGE = FileConfig.PATH_USER_FILE + "image/";
        FileConfig.PATH_USER_THUMBNAIL = FileConfig.PATH_USER_FILE + "thumbnail/";
        FileConfig.PATH_USER_VIDEO = FileConfig.PATH_USER_FILE + "video/";
//        FileConfig.PATH_USER_FAVORITES = FileConfig.PATH_USER_FILE + "favorites/";
        mkdirs(FileConfig.PATH_USER_FILE);
        mkdirs(FileConfig.PATH_USER_IMAGE);
        mkdirs(FileConfig.PATH_USER_THUMBNAIL);
        mkdirs(FileConfig.PATH_USER_VIDEO);
        mkdirs(FileConfig.PATH_USER_AUDIO);
//        mkdirs(FileConfig.PATH_USER_FAVORITES);
    }

    /**
     * 清除本地文件目录
     *
     * @version 1.0
     * @createTime 2017-11-3,下午3:44:53
     * @updateTime 2017-11-3,下午3:44:53
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static void clearAllFile(String userName) {
        FileConfig.PATH_USER_FILE = FileConfig.PATH_BASE + EncryptUtil.md5Encrypt(userName) + "/";
        FileConfig.PATH_USER_AUDIO = FileConfig.PATH_USER_FILE + "audio/";
        FileConfig.PATH_USER_IMAGE = FileConfig.PATH_USER_FILE + "image/";
        FileConfig.PATH_USER_THUMBNAIL = FileConfig.PATH_USER_FILE + "thumbnail/";
        FileConfig.PATH_USER_VIDEO = FileConfig.PATH_USER_FILE + "video/";
//        FileConfig.PATH_USER_FAVORITES = FileConfig.PATH_USER_FILE + "favorites/";
        clearDirectory(new File(FileConfig.PATH_USER_FILE));
        clearDirectory(new File(FileConfig.PATH_USER_IMAGE));
        clearDirectory(new File(FileConfig.PATH_USER_THUMBNAIL));
        clearDirectory(new File(FileConfig.PATH_USER_VIDEO));
        clearDirectory(new File(FileConfig.PATH_USER_AUDIO));
//        clearDirectory(new File(FileConfig.PATH_USER_FAVORITES));
    }

    /**
     * 判断文件是否存在
     *
     * @version 1.0
     * @createTime 2017-11-3,早上10:44:53
     * @updateTime 2017-11-3,早上10:44:53
     * @createAuthor wujianxing
     * @updateAuthor wujianxing
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            return f.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param imageUri
     * @author 何栋
     * @date 2017-11-3
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    /**
     * 查询媒体文件所在路径
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     * @version 1.0
     * @createTime 2013-11-3, 下午2:18:31
     * @updateTime 2013-11-3, 下午2:18:31
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 获取本APP缓存文件夹路径
     *
     * @param context
     * @return
     */
    public static String getAppCachePath(Context context) {
        File file = context.getExternalCacheDir();
        return file.getPath();
    }


    public static String readFile(String file) {

        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result = result + "\n" + s;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 给文件写内容
     *
     * @param file    输入内容的文件
     * @param content 要输入的内容
     */
    public static void writeFile(File file, String content) {

        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fw = null;
                fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建一个有路径的文件， 注意未生成文件
     *
     * @param path
     * @return
     */
    public static File createFile(String path) {

        return new File(path);
    }

    /**
     * 文件存在就删除
     * @param path
     */
    public static void deleteFile(String path) {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        return;
    }

    public static File saveMyBitmap(Bitmap mBitmap) throws IOException {
//        File dir = null;
//        // showToast(activity, "若添加实时拍摄照片导致重启，请尝试在应用外拍照，再选择从相册中获取进行添加！");
//        if (!dir.exists()) {
//            dir.mkdirs();// 创建照片的存储目录
//        }
        File f = new File(FileConfig.PATH_IMAGES + DateUtil.getCurrentTimeToSecond() + ".jpg");// 给新照的照片文件命名
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
        }
        try {
            fOut.close();
        } catch (IOException e) {
        }
        return f;
    }

}
