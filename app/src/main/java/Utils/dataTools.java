package Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;

import androidx.documentfile.provider.DocumentFile;

import com.example.hadesstartool.GP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *dataTools 提供一个对android11 Android/data目录下非自身应用文件的一个操作方案
 * by 若忧愁
 * qq 2557594045
 *
 */
public class dataTools {
    Activity context ;//内部操作Activity对象
    int requestCode = 11;//请求标识
    /**
     * 构造方法
     * @context # Activity对象
     * @requestCode  #请求码
     */
    public  dataTools(Activity context,int requestCode) {
        this.context=context;
        this.requestCode=requestCode;
    }
    /**
     *申请data访问权限请在onActivityResult事件中调用savePermissions方法保存权限
     */
    public void requestPermission() {
//        Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
//        uri1= DocumentFile.fromTreeUri(this.context,uri1).getUri();
//        Intent intent1 = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
//        intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
//                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
//        intent1.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri1);
//        context.startActivityForResult(intent1, requestCode);

        startFor(context,requestCode);
    }

    public void startFor(Activity context, int REQUEST_CODE_FOR_DIR) {
//        Uri uri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");

        String uri = changeToUri(GP.to_path.getFile().getPath());//调用方法，把path转换成可解析的uri文本，这个方法在下面会公布
        Uri parse = Uri.parse(uri);
//        uri1= DocumentFile.fromTreeUri(this.context,uri1).getUri();
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, parse);
        }
        context.startActivityForResult(intent, REQUEST_CODE_FOR_DIR);//开始授权
    }

    /**
     * 保存权限onActivityResult返回的参数全部传入即可
     * @requestCode #onActivityResult
     * @resultCode  #onActivityResult
     * @data #onActivityResult
     */
    @SuppressLint("WrongConstant")
    public void savePermissions(int requestCode, int resultCode, Intent data) {
//        if (this.requestCode!=requestCode)return;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            try {
//                Uri uri = data.getData();
//                if (uri==null)return;
//                this.context.getContentResolver().takePersistableUriPermission(uri,data.getFlags()&Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        savePermissionsUp(requestCode,resultCode,data);
    }

    @SuppressLint("WrongConstant")
    public void savePermissionsUp(int requestCode, int resultCode, Intent data){
        Uri uri;
        if (data == null) {
            return;
        }

        if (requestCode == this.requestCode && (uri = data.getData()) != null) {
            context.getContentResolver().takePersistableUriPermission(uri, data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));//关键是这里，这个就是保存这个目录的访问权限

        }


    }



    /**
     * 判断是否获取使用data目录权限
     * @return #返回一个boolean true有权限 false 无权限
     */
    public boolean isPermissions(String path) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            return true;
        }
//        Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
//        String uri = changeToUri(GP.to_path.getFile().getPath());//调用方法，把path转换成可解析的uri文本，这个方法在下面会公布
//        Uri parse = Uri.parse(uri);
//        DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(changeToUri3(GP.to_path.getFile().getPath())));
//        DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, parse);
//        if(documentFile==null)return false;
//        return documentFile.canWrite();
        return isGrant(context,path);
    }

    //判断是否已经获取了Data权限，改改逻辑就能判断其他目录，懂得都懂
    public static boolean isGrant(Context context,String path) {
        for (UriPermission persistedUriPermission : context.getContentResolver().getPersistedUriPermissions()) {
            if (persistedUriPermission.isReadPermission() && persistedUriPermission.getUri().toString().equals("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata")) {
                return true;
            }
            if (persistedUriPermission.getUri().toString().equals(changeToUri3(path))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将sdcard中的文件拷贝至data目录中
     * @sourcePath #sdcard中的完整文件路径
     * @targetDir  #拷贝至的文件目录以data开始 如拷贝至data/test/目录 那就是 /test
     * @targetName #目标文件名
     * @fileType 目录文件类型 如txt文件 application/txt
     * @return #返回一个boolean true成功 false 失败
     */
    public boolean copyToData(String sourcePath, String targetDir ,String targetName , String fileType) {
        targetDir=textual(targetDir,targetName,"");
        if ((new File(sourcePath)).exists()) {
            try {
                InputStream inStream = new FileInputStream(sourcePath);
                // byte[] buffer = new byte[inStream.available()];
                Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata" );
                DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
                String[] list=targetDir.split("/");
                int i=0;
                while (i<list.length) {
                    if (!list[i].equals("")) {
                        DocumentFile a = getDocumentFile1(documentFile,list[i]);
                        if(a==null){
                            documentFile=documentFile.createDirectory(list[i]);
                        }else{
                            documentFile=a;
                        }
                    }
                    i++;
                }
                DocumentFile newFile = null;
                if (exists(documentFile,targetName)) {
                    newFile = documentFile.findFile(targetName);
                } else {
                    newFile = documentFile.createFile(fileType, targetName);
                }
                OutputStream excelOutputStream = this.context.getContentResolver().openOutputStream(newFile.getUri());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buffer)) != -1)
                {
                    excelOutputStream.write(buffer, 0, len);
                }
                inStream.close();
                excelOutputStream.close();
                return true;
            } catch (Exception var8) {
                var8.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * 将sdcard中的文件拷贝至data目录中与copyToData方法不同的是,此方法在遇到已经存在的文件时,将自动删除原文件然后重新创建,可以实现文件覆盖。
     * @sourcePath #sdcard中的完整文件路径
     * @targetDir  #拷贝至的文件目录以data开始 如拷贝至data/test/目录 那就是 /test
     * @targetName #目标文件名
     * @fileType 目录文件类型 如txt文件 application/txt
     * @return #返回一个boolean true成功 false 失败
     */
    public boolean copyToData_cover(String sourcePath, String targetDir ,String targetName , String fileType) {
        targetDir=textual(targetDir,targetName,"");
        if ((new File(sourcePath)).exists()) {
            try {
                InputStream inStream = new FileInputStream(sourcePath);
                // byte[] buffer = new byte[inStream.available()];
                Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata" );
                DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
                String[] list=targetDir.split("/");
                int i=0;
                while (i<list.length) {
                    if (!list[i].equals("")) {
                        DocumentFile a = getDocumentFile1(documentFile,list[i]);
                        if(a==null){
                            documentFile=documentFile.createDirectory(list[i]);
                        }else{
                            documentFile=a;
                        }
                    }
                    i++;
                }
                DocumentFile newFile = null;
                if (exists(documentFile,targetName)) {
                    newFile = documentFile.findFile(targetName);
                } else {
                    newFile = documentFile.createFile(fileType, targetName);
                }
                OutputStream excelOutputStream = this.context.getContentResolver().openOutputStream(newFile.getUri());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buffer)) != -1)
                {
                    excelOutputStream.write(buffer, 0, len);
                }
                inStream.close();
                excelOutputStream.close();
                return true;
            } catch (Exception var8) {
                var8.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * 通过目录路径,获取该目录的DocumentFile对象,如目录不存在将自动创建,获取失败将返回null,这样做可以避免重复获取同一个路径的DocumentFile导致的耗时,推荐配合copyToData_find_cover使用
     * @dir  #获取DocumentFile的目录名
     * @return #返回DocumentFile对象,获取失败将null
     */
    public DocumentFile getDocumentFile(String dir) {
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata" );
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
            String[] list=dir.split("/");
            int i=0;
            while (i<list.length) {
                if (!list[i].equals("")) {
                    DocumentFile a = getDocumentFile1(documentFile,list[i]);
                    if(a==null){
                        documentFile=documentFile.createDirectory(list[i]);
                    }else{
                        documentFile=a;
                    }
                }
                i++;
            }
            return documentFile;
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }
    /**
     * 根据传入的路径,获取文件的输入流
     * @targetDir  #获取输入流文件的目录 如拷贝至data/test/目录 那就是 /test
     * @targetName #目标文件名
     * @return #返回该文件的输入流,如果失败则返回null
     */
    public InputStream getInputStream( String targetDir ,String targetName) {
        targetDir=textual(targetDir,targetName,"");
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata" );
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
            String[] list=targetDir.split("/");
            int i=0;
            while (i<list.length) {
                if (!list[i].equals("")) {
                    DocumentFile a = getDocumentFile1(documentFile,list[i]);
                    if(a==null){
                        documentFile=documentFile.createDirectory(list[i]);
                    }else{
                        documentFile=a;
                    }
                }
                i++;
            }
            DocumentFile newFile = null;
            if (exists(documentFile,targetName)) {
                newFile = documentFile.findFile(targetName);
            } else {
                return null;
            }
            return  this.context.getContentResolver().openInputStream(newFile.getUri());

        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }

    }
    /**
     * 将sdcard中的文件拷贝至data目录中与copyToData_cover方法不同的是,此方法支持,传入一个起始的DocumentFile对象,这样可以更高性能的操作,避免重复获取同一个目录对象的耗时。
     * @sourcePath #sdcard中的完整文件路径
     * @targetDir  #拷贝至的文件目录以data开始 如拷贝至data/test/目录 那就是 /test
     * @targetName #目标文件名
     * @fileType 目录文件类型 如txt文件 application/txt
     * @return #返回一个boolean true成功 false 失败
     */
    public boolean copyToData_find_cover(DocumentFile startDocumentFile,String sourcePath, String targetDir ,String targetName , String fileType) {
        targetDir=textual(targetDir,targetName,"");
        if ((new File(sourcePath)).exists()) {
            try {
                InputStream inStream = new FileInputStream(sourcePath);
                DocumentFile documentFile = startDocumentFile;
                String[] list=targetDir.split("/");
                int i=0;
                while (i<list.length) {
                    if (!list[i].equals("")) {
                        DocumentFile a = getDocumentFile1(documentFile,list[i]);
                        if(a==null){
                            documentFile=documentFile.createDirectory(list[i]);
                        }else{
                            documentFile=a;
                        }
                    }
                    i++;
                }
                DocumentFile newFile = null;
                if (exists(documentFile,targetName)) {
                    newFile = documentFile.findFile(targetName);
                } else {
                    newFile = documentFile.createFile(fileType, targetName);
                }
                OutputStream excelOutputStream = this.context.getContentResolver().openOutputStream(newFile.getUri());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buffer)) != -1)
                {
                    excelOutputStream.write(buffer, 0, len);
                }
                inStream.close();
                excelOutputStream.close();
                return true;
            } catch (Exception var8) {
                var8.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * 将Android/data中的文件拷贝至sdcard
     * @sourceDir #文件原目录以data开始 如拷贝data/test/目录中的文件 那就是 /test
     * @sourceFilename #拷贝的文件名 如拷贝 data/test/1.txt 那就是1.txt
     * @targetPath #目标文件路径需提供完整的路径目录+文件名
     * @return #返回一个boolean true成功 false 失败
     */
    public boolean copyToSdcard(String sourceDir,String sourceFilename, String targetPath) {
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
            String[] list = sourceDir.split("/");
            int i = 0;
            while (i < list.length) {
                if (!list[i].equals("")) {
                    DocumentFile a = getDocumentFile1(documentFile, list[i]);
                    if (a == null) {
                        documentFile = documentFile.createDirectory(list[i]);
                    } else {
                        documentFile = a;
                    }
                }
                i++;
            }
            documentFile=documentFile.findFile(sourceFilename);
            InputStream   inputStream = this.context.getContentResolver().openInputStream(documentFile.getUri());
            FileOutputStream fs = new FileOutputStream(targetPath);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1)
            {
                fs.write(buffer, 0, len);
            }
            inputStream.close();
            fs.close();
            return true;
        } catch (Exception var8) {
            var8.printStackTrace();
            return false;
        }
    }
    /**
     * 删除data目录中的指定路径的文件
     * @dir  #删除文件的目录目录以data开始 如拷贝至data/test/目录 那就是 /test
     * @fileName #目标文件名
     * @return #返回一个boolean true成功 false 失败
     */
    public boolean delete(String dir,String fileName) {
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
            String[] list = dir.split("/");
            int i = 0;
            while (i < list.length) {
                if (!list[i].equals("")) {
                    DocumentFile a = getDocumentFile1(documentFile, list[i]);
                    if (a == null) {
                        documentFile = documentFile.createDirectory(list[i]);
                    } else {
                        documentFile = a;
                    }
                }
                i++;
            }
            documentFile=documentFile.findFile(fileName);
            return documentFile.delete();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 重命名文件
     * @dir  #重命名文件目录 目录以data开始 如拷贝至data/test/目录 那就是 /test
     * @fileName #目标文件名
     * @targetName #重命名后的文件名
     * @return #返回一个boolean true成功 false 失败
     */
    public boolean renameTo(String dir,String fileName,String targetName) {
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
            String[] list = dir.split("/");
            int i = 0;
            while (i < list.length) {
                if (!list[i].equals("")) {
                    DocumentFile a = getDocumentFile1(documentFile, list[i]);
                    if (a == null) {
                        documentFile = documentFile.createDirectory(list[i]);
                    } else {
                        documentFile = a;
                    }
                }
                i++;
            }
            documentFile=documentFile.findFile(fileName);
            return documentFile.renameTo(targetName);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 获取目录下所有文件返回文本型数组
     * @dir  #文件目录 目录以data开始 如拷贝至data/test/目录 那就是 /test
     * @return #返回一个文本数组为该目录下所有的文件名
     */
    public String [] getList(String dir) {
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
            String[] list = dir.split("/");
            int i = 0;
            while (i < list.length) {
                if (!list[i].equals("")) {
                    DocumentFile a = getDocumentFile1(documentFile, list[i]);
                    if (a == null) {
                        documentFile = documentFile.createDirectory(list[i]);
                    } else {
                        documentFile = a;
                    }
                }
                i++;
            }
            DocumentFile[] documentFile1 = documentFile.listFiles();
            String[] res = new String[documentFile1.length];
            int i1 =0;
            while (i1<documentFile1.length){
                res[i1]=documentFile1[i1].getName();
                i1++;
            }
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 判断目录是否存在
     * @dir  #判断文件目录 目录以data开始 如拷贝至data/test/目录 那就是 /test
     * @return #返回一个boolean true存在 false 不存在
     */
    public boolean dirIsExist(String dir) {
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
            String[] list = dir.split("/");
            int i = 0;
            while (i < list.length) {
                if (!list[i].equals("")) {
                    DocumentFile a = getDocumentFile1(documentFile, list[i]);
                    if (a == null) {
                        return false;
                    } else {
                        documentFile = a;
                    }
                }
                i++;
            }
            return documentFile.exists();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 重命名目录
     * @dir  #重命名文件目录 目录以data开始 如拷贝至data/test/目录 那就是 /test
     * @targetName #重命名后的文件夹名
     * @return #返回一个boolean true成功 false 失败
     */
    public boolean reNameDir(String dir,String targetName) {
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
            String[] list = dir.split("/");
            int i = 0;
            while (i < list.length) {
                if (!list[i].equals("")) {
                    DocumentFile a = getDocumentFile1(documentFile, list[i]);
                    if (a == null) {
                        return false;
                    } else {
                        documentFile = a;
                    }
                }
                i++;
            }
            return documentFile.renameTo(targetName);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 将byte[] 写出到data目录的文件中如果没有这个文件会自动创建目录及文件
     * @Dir  #写出的文件目录以data开始 如拷贝至data/test/目录 那就是 /test
     * @fileName #写出的文件名
     * @fileType 目录文件类型 如txt文件 application/txt
     * @return #返回一个boolean true成功 false 失败
     */
    public boolean write(String dir,String fileName, String fileType,byte[] bytes) {
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata" );
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context, uri1);
            String[] list = dir.split("/");
            int i=0;
            while (i<list.length) {
                if (!list[i].equals("")) {
                    DocumentFile a = getDocumentFile1(documentFile,list[i]);
                    if(a==null){
                        documentFile=documentFile.createDirectory(list[i]);
                    }else{
                        documentFile=a;
                    }
                }
                i++;
            }
            DocumentFile newFile = null;
            if (exists(documentFile,fileName)) {
                newFile = documentFile.findFile(fileName);
            } else {
                newFile = documentFile.createFile(fileType, fileName);
            }
            OutputStream excelOutputStream = this.context.getContentResolver().openOutputStream(newFile.getUri());
            return doDataOutput2(bytes, excelOutputStream);
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }
    /**
     * 读取data下指定路径的文件为byte[]
     * @Dir  #读取的文件目录以data开始 如读取data/test/目录 那就是 /test
     * @fileName #写出的文件名
     * @fileType 目录文件类型 如txt文件 application/txt
     * @return #返回一个byte[] 如文件为空或者不存在此返回可能为null请判断后使用
     */
    public byte[] read(String dir ,String fileName) {
        byte[] buffer = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        try {
            Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
            DocumentFile documentFile = DocumentFile.fromTreeUri(this.context,uri1);
            String[] list = dir.split("/");
            int i=0;
            while (i<list.length) {
                if (!list[i].equals("")) {
                    documentFile = getDocumentFile1(documentFile,list[i]);
                }
                i++;
            }
            documentFile=documentFile.findFile(fileName);
            inputStream = this.context.getContentResolver().openInputStream(documentFile.getUri());
            buffer=new byte[inputStream.available()];
            while (true)
            {
                int readLength = inputStream.read(buffer);
                if (readLength == -1) break;
                arrayOutputStream.write(buffer, 0, readLength);
            }
            inputStream.close();
            arrayOutputStream.close();
        } catch (Exception var5) {
            var5.printStackTrace();
            if(inputStream!=null){
                try {
                    inputStream.close();
                    arrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }
    /**
     * 异步读取data下指定路径的文件为byte[]
     * @Dir  #读取的文件目录以data开始 如读取data/test/目录 那就是 /test
     * @fileName #写出的文件名
     * @fileType 目录文件类型 如txt文件 application/txt
     * @return #将在asyncRead接口中的onRead中返回数据和传入时的taskId
     */
    public void asyncRead(String dir ,String fileName,int taskId,AsyncRead asyncRead) {
        new Thread(new Runnable() {//保留java1.7的写法方便工程移值
            @Override
            public void run() {
                byte[] buffer = null;
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                InputStream inputStream = null;
                try {
                    Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
                    DocumentFile documentFile = DocumentFile.fromTreeUri(context,uri1);
                    String[] list = dir.split("/");
                    int i=0;
                    while (i<list.length) {
                        if (!list[i].equals("")) {
                            documentFile = getDocumentFile1(documentFile,list[i]);
                        }
                        i++;
                    }
                    documentFile=documentFile.findFile(fileName);
                    inputStream = context.getContentResolver().openInputStream(documentFile.getUri());
                    buffer=new byte[inputStream.available()];
                    while (true)
                    {
                        int readLength = inputStream.read(buffer);
                        if (readLength == -1) break;
                        arrayOutputStream.write(buffer, 0, readLength);
                    }
                    inputStream.close();
                    arrayOutputStream.close();
                } catch (Exception var5) {
                    var5.printStackTrace();
                    if(inputStream!=null){
                        try {
                            inputStream.close();
                            arrayOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                byte[] finalBuffer = buffer;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        asyncRead.onRead(finalBuffer,taskId);
                    }
                });

            }
        }).start();
    }
    /**
     * 异步读取接口
     * @data #返回的数据可能为空需要判断
     * @taskId #调用时传入的任务id
     */
    public interface AsyncRead{
        void onRead(byte[] data,int taskId);
    }
    /**
     * 判断是否获得全部文件访问权限
     * @return  #获取权限返回true没有获得返回false
     */
    public boolean isAllFilePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }else {
            return false;
        }
    }
    /**
     * 申请全部文件访问权限
     * @requestCode 请求权限请求码
     * @return  #onActivityResult 中回调请判断请求码并使用isAllFilePermission检查权限
     */
    public void requestAllFilePermission(int requestCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, requestCode);
        }
    }
    private boolean doDataOutput2(byte[] bytes ,OutputStream outputStream){
        try {
            outputStream.write( bytes,0,bytes.length);
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                outputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return false;
        }
    }
    private boolean exists(DocumentFile documentFile ,String name){
        try {
            return documentFile.findFile(name).exists();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private DocumentFile getDocumentFile(DocumentFile documentFile,String dir){
        if (documentFile==null)return null;
        DocumentFile [] documentFiles =documentFile.listFiles();
        DocumentFile res = null;
        int i = 0 ;
        while (i<documentFile.length()){
            if(documentFiles[i].getName().equals(dir)&&documentFiles[i].isDirectory()){
                res=documentFiles[i];
                return  res;
            }
            i++;
        }
        return res;
    }

    private DocumentFile getDocumentFile1(DocumentFile documentFile,String dir){
        if (documentFile==null)return null;
        try {
            DocumentFile[] documentFiles = documentFile.listFiles();
            DocumentFile res = null;
            int i = 0;
            while (i < documentFile.length()) {
                if (documentFiles[i].getName().equals(dir) && documentFiles[i].isDirectory()) {
                    res = documentFiles[i];
                    return res;
                }
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static String textual (String str, String find, String replace) {
        if (!"".equals(find) && !"".equals(str)) {
            find = "\\Q" + find + "\\E";
            return str.replaceAll(find, replace);
        } else {
            return "";
        }
    }

    //转换至uriTree的路径
    public static String changeToUri(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        String path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
        return "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A" + path2;
    }

//    //转换至uriTree的路径
//    public static String changeToUri2(String path) {
//        if (path.endsWith("/")) {
//            path = path.substring(0, path.length() - 1);
//        }
//        String path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
//        return "content://com.android.externalstorage.documents/tree/primary%3A" + path2;
//    }

    //转换至uriTree的路径
    public static String changeToUri3(String path) {
        path = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
        return ("content://com.android.externalstorage.documents/tree/primary%3A" + path);

    }
    //转换至uriTree的路径
    public static String changeToUri4(String path) {
        path = StringUtils.cutEnd("Android/data/",path);
        path = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
        return ("content://com.android.externalstorage.documents/tree/primary%3A" + path);

    }

//    //根据路径获得document文件
//    public static DocumentFile getDoucmentFile(Context context, String path) {
//        if (path.endsWith("/")) {
//            path = path.substring(0, path.length() - 1);
//        }
//        String path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
//        return DocumentFile.fromSingleUri(context, Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A" + path2));
//    }

    //根据路径获得document文件
    public static DocumentFile getDocumentFile(Context context, File path) {
        if(path==null)return null;
        DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(dataTools.changeToUri3(path.getParent())));
        DocumentFile on = null;
        if(documentFile == null)return null;

        for (DocumentFile df:documentFile.listFiles()){
            String b = df.getName();
            if(df.getName().equals(path.getName())){
                on = df;
                return on;
            }
        }
        return null;
    }

    /**
     * 拷贝所有的文件
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public boolean copyAllFiles(DocumentFile sourceFile, DocumentFile targetFile) {
        if (sourceFile.exists() && targetFile.canWrite()) {
            if(sourceFile.isFile()){
                return writeFileByStream(sourceFile,targetFile);
            }
            //获取源文件下的所有文件
            DocumentFile[] documentFiles = sourceFile.listFiles();
            for (DocumentFile documentFile1 : documentFiles) {
                if (documentFile1.isFile()) {//是文件而不是文件夹或目录
                    //创建文件，第一个参数是文件类型，第二个是文件名
                    DocumentFile createFile = targetFile.createFile(documentFile1.getType(), documentFile1.getName());
                    return writeFileByStream(documentFile1, createFile);
                } else {
                    //如果是目录则先获取该文件夹名称
                    String directoryName = documentFile1.getName();
                    //创建目录
                    DocumentFile createDirFile = targetFile.createDirectory(directoryName);
                    //递归文件目录
                    return copyAllFiles(documentFile1, createDirFile);
                }
            }
        }
        return false;
    }

    /**
     * 通过流的方式写文件
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public boolean writeFileByStream(DocumentFile sourceFile, DocumentFile targetFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(sourceFile.getUri());
            OutputStream outputStream = context.getContentResolver().openOutputStream(targetFile.getUri());

            byte[] buffer = new byte[1024];
            int byreRead;
            while (-1 != (byreRead = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, byreRead);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
