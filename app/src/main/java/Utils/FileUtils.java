package Utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.documentfile.provider.DocumentFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 文件操作工具类
 *
 * @author 靈凛
 * @version jdk:18.0.2
 *          <p>
 *          版本:1.1
 *
 */
public class FileUtils {





    /**
     * 传入txt路径读取txt文件
     *
     * @param txtPath
     * @return 返回读取到的内容
     */
    public static List<String> readTxt(String txtPath) {
        File file = new File(txtPath);
        if (file.isFile() && file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                List<String> strings = new ArrayList<>();
                String text = null;
                while ((text = bufferedReader.readLine()) != null) {
                    strings.add(text);
                }
                return strings;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 使用FileOutputStream来写入txt文件
     *
     * @param txtPath txt文件路径
     * @param content 需要写入的文本
     */
    public static boolean writeTxt(String txtPath, String content,boolean Append) {
        FileOutputStream fileOutputStream = null;
        File file = new File(txtPath);
        try {
            if (file.exists()) {
                // 判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file,Append);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int newFilecount;

    /**
     * 新建文件/文件夹
     *
     * @param newfile     文件路径
     * @param isDirectory 是否为文件夹
     * @return
     */
    public static boolean newFile(File newfile, boolean isDirectory) {
        try {
            newfile = newfile.getCanonicalFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        newFilecount = 0;
        return _newFile(newfile, isDirectory);
    }

    private static boolean _newFile(File newfile, boolean isDirectory) {
        if (newfile.exists()) {
            if (newFilecount == 0) {
                if (isDirectory) {
                    newfile = new File(newfile.getParent(), StringUtils.getFileName(newfile) + "-" + newFilecount);
                } else {
                    newfile = new File(newfile.getParent(), StringUtils.getFileName(newfile) + "-" + newFilecount
                            + "." + StringUtils.getFileExtension(newfile));
                }
            } else {
                if (isDirectory) {
                    newfile = new File(newfile.getParent(),
                            StringUtils.getFileName(newfile).substring(0,
                                    StringUtils.getFileName(newfile).length() - MathUtils.getdigits(newFilecount))
                                    + newFilecount);
                } else {
                    newfile = new File(newfile.getParent(),
                            StringUtils.getFileName(newfile).substring(0,
                                    StringUtils.getFileName(newfile).length() - MathUtils.getdigits(newFilecount))
                                    + newFilecount + "." + StringUtils.getFileExtension(newfile));
                }
            }
            newFilecount++;
            return _newFile(newfile, isDirectory);
        }
        if (isDirectory) {
            return newfile.mkdirs();
        }
        try {
            return newfile.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("FileUtils:" + "_newFile:" + e.getMessage());
            e.printStackTrace();
            return false;

        }
    }

    /**
     * 判断两个File所指的路径是否相同
     *
     * @return ture相同
     *         <p>
     *         false不同
     */
    public static boolean equalsFile(File file1, File file2) {
        try {
            return file1.getCanonicalPath().equals(file2.getCanonicalPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除
     *
     * @param deleteFile 需要删除的路径
     * @return true 该路径已不存在
     *         <p>
     *         false 删除失败
     */
    public static boolean delete(File deleteFile) {
        if (deleteFile != null && deleteFile.exists()) {
            if (deleteFile.isFile()) {
                return deleteFile.delete();
            } else {
                for (File files : deleteFile.listFiles()) {
                    delete(files);
                }
                return deleteFile.delete();
            }
        }
        return true;
    }



    /** 检查inFile1与inFile2内容是否完全相同 **/
    public static boolean checkContent(File inFile1, File inFile2){
        if(!inFile1.exists()||!inFile2.exists()) return false;
        if(inFile1.length()!=inFile2.length()) return false;
        try {
            byte[] read1 = new byte[10];
            byte[] read2 = new byte[10];
            FileInputStream inS1 = new FileInputStream(inFile1);
            FileInputStream inS2 = new FileInputStream(inFile2);
            int len1=0;
            int len2=0;
            while (len1!=-1||len2!=-1){
                len1 = inS1.read(read1);
                len2 = inS2.read(read2);
                if(!Arrays.equals(read1, read2)){
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /** 检查inFile1与inFile2内容是否完全相同 **/
    public static boolean checkContent(Context context,DocumentFile inFile1, DocumentFile inFile2){
        if(!inFile1.exists()||!inFile2.exists()) return false;
//        if(inFile1.length()!=inFile2.length()) return false;
        try {
            byte[] read1 = new byte[10];
            byte[] read2 = new byte[10];
            InputStream inS1 = context.getContentResolver().openInputStream(inFile1.getUri());
            InputStream inS2 = context.getContentResolver().openInputStream(inFile2.getUri());
            int len1=0;
            int len2=0;
            while (len1!=-1||len2!=-1){
                len1 = inS1.read(read1);
                len2 = inS2.read(read2);
                if(!Arrays.equals(read1, read2)){
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /** 检查inFile2是否为inFile1的子目录 */
    public static boolean checkSubdirectory(File inFile1, File inFile2) {
        try {
            inFile1 = inFile1.getCanonicalFile();
            inFile2 = inFile2.getCanonicalFile();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        if (inFile1 == null || inFile2 == null) {
            return false;
        }
        if (inFile1.toString().equals(inFile2.toString())) {
            return true;
        } else {
            return checkSubdirectory(inFile1, inFile2.getParentFile());
        }
        // return false;
    }

    /** 排序类 */
    public static class Sort {
        /**
         * 按A-Z排序
         * <p>
         * (不支持中文)
         */
        public static File[] sortFilesA_Z(File[] files) {
            List<File> list;
            if (files == null) {
                list = new ArrayList<>();
            } else {
                list = Arrays.asList(files);
            }
            Collections.sort(list, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    String name1 = o1.getName().toUpperCase();
                    char c1 = name1.toCharArray()[0];
                    String name11 = "";
                    // for (char c11 : name1.toCharArray()) {
                    // name11 += Pinyin.toPinyin(c11);
                    // }

                    if (c1 >= 0x4E00 && c1 <= 0x9FA5) {
                        name1 = "." + name11;
                    } else if (c1 < 48) {
                        name1 = "{" + name11;
                    } else if (c1 > 57 && c1 < 65) {
                        name1 = "{" + name11;
                    }

                    String name2 = o2.getName().toUpperCase();

                    char c2 = name2.toCharArray()[0];

                    String name22 = "";
                    // for (char c22 : name2.toCharArray()) {
                    // name22 += Pinyin.toPinyin(c22);
                    // }

                    if (c2 >= 0x4E00 && c2 <= 0x9FA5) {
                        name2 = "." + name22;
                    } else if (c2 < 48) {
                        name2 = "{" + name22;
                    } else if (c2 > 57 && c2 < 65) {
                        name2 = "{" + name22;
                    }
                    return name1.compareTo(name2);
                }
            });
            File[] files2 = new File[list.size()];
            for (int i = 0; i < list.size(); i++) {
                files2[i] = list.get(i);
            }
            return files2;
        }
    }

    /** 复制类 */
    public static class Copy {
        public static boolean filetoport(File inFile, File outPort,boolean overlay) {
            return fileToPortRename(inFile, outPort, inFile.getName(),overlay);
        }

        public static boolean fileToPortRename(File inFile, File outPort, String reName,boolean overlay) {

            return fliePortName(inFile.getParentFile(), outPort, inFile.getName(), reName,overlay);
        }

        public static boolean fliePortName(File inPort, File outPort, String inName, String outName,boolean overlay) {
            File in = new File(inPort, inName);
            File out = new File(outPort, outName);
            if (!in.exists()) {
                return false;
            }
            outPort.mkdirs();
            // if (out.isDirectory()) {
            // return false;
            // }
            return FtoF(in, out,overlay);

        }

        private static int copycount = 0;

        /** 将 inFile 复制到 outFile */
        public static boolean FtoF(File inFile, File outFile,boolean overlay) {
            copycount = 0;
            return _FtoF(inFile, outFile,overlay);
        }

        /**
         * 将 inFile 复制到 outFile
         * <p>
         * 计数封装
         */
        private static boolean _FtoF(File inFile, File outFile,boolean overlay) {
            if (inFile != null && inFile.exists() && outFile != null) {
                boolean isSubdirectory = FileUtils.checkSubdirectory(inFile, outFile);
                if (isSubdirectory && !inFile.toString().equals(outFile.toString())) {
                    return false;
                }
                if (!overlay && outFile.exists()) {
                    if (copycount == 0) {
                        outFile = new File(outFile.getParent(),
                                StringUtils.getFileName(outFile) + "-copy" + copycount + "."
                                        + StringUtils.getFileExtension(outFile));
                    } else {
                        outFile = new File(outFile.getParent(),
                                StringUtils.getFileName(outFile).substring(0,
                                        StringUtils.getFileName(outFile).length() - MathUtils.getdigits(copycount))
                                        + copycount + "." + StringUtils.getFileExtension(outFile));
                    }
                    copycount++;
                    return _FtoF(inFile, outFile,overlay);
                }

                if (inFile.isFile()) {
                    try (FileInputStream FIPS = new FileInputStream(inFile);
                         FileOutputStream FOPS = new FileOutputStream(outFile);) {
                        boolean b = IOS(FIPS, FOPS);
                        FIPS.close();
                        FOPS.close();
                        return b;
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return false;
                } else {
                    outFile.mkdirs();
                    boolean b = true;
                    for (File files : inFile.listFiles()) {
                        if (!FtoF(files, new File(outFile, files.getName()),overlay) && b) {
                            b = false;
                        }
                    }
                    return b;
                }
            }
            return false;
        }

        public static boolean IOS(InputStream ips, OutputStream ops) {
            try (BufferedInputStream bis = new BufferedInputStream(ips);
                 BufferedOutputStream bos = new BufferedOutputStream(ops);) {
                byte[] bys = new byte[1024];
                int len;
                while ((len = bis.read(bys)) != -1) {
                    bos.write(bys, 0, len);
                }
                bis.close();
                bos.close();
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        }
    }

    public static class FileChooseUtil {

        private Context context;
        private static FileChooseUtil util = null;

        private FileChooseUtil(Context context) {
            this.context = context;
        }

        public static FileChooseUtil getInstance(Context context) {
            if (util == null) {
                util = new FileChooseUtil(context);
            }
            return util;
        }

        /**
         * 对外接口  获取uri对应的路径
         *
         * @param uri
         * @return
         */
        public String getChooseFileResultPath(Uri uri) {
            String chooseFilePath = null;
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                chooseFilePath = uri.getPath();
//                ToastUtils.toast(context, chooseFilePath);
                return chooseFilePath;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                chooseFilePath = getPath(context, uri);
            } else {//4.4以下下系统调用方法
                chooseFilePath = getRealPathFromURI(uri);
            }
            return chooseFilePath;
        }

        private String getRealPathFromURI(Uri contentUri) {
            String res = null;
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
                cursor.close();
            }
            return res;
        }

        /**
         * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
         */
        @SuppressLint("NewApi")
        private String getPath(final Context context, final Uri uri) {

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
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
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
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection, selectionArgs);

                }

            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);

            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                uri.getPath();

            }
            return null;
        }

        private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {column};
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
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
        private boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        private boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }
    }

}
