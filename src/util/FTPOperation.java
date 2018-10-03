/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.syscom.util.Config;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
//import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.log4j.Logger;
import static org.apache.naming.SelectorContext.prefix;
//import org.apache.commons.vfs2.provider.local.LocalFile;

/**
 *
 * @author Administrator
 */
public class FTPOperation {

    static String HOSTNAME;
    static String USERNAME;
    static String PASSWORD;

    Logger log = Logger.getLogger(FTPOperation.class);
//    public FTPOperation() {
//        HOSTNAME = Config.getString("FTP_HOSTNAME");
//        USERNAME = Config.getString("FTP_USERNAME");
//        PASSWORD = Config.getString("FTP_PASSWORD");
//    }
    
    public enum SVR_TYPE{//FTP08 OR 檔案伺服器
        FTP08,
        FILESVR
    }
    /**
     * 
     * @param type SVR_TYPE FTP OR FSERVER   
     */
    public FTPOperation(SVR_TYPE type) {        
        String postfix = "";//NPAUtil.getPropertiesPostfix();//若為公司測試，property名稱後面會加上:_S
        HOSTNAME = Config.getString(type + "_HOSTNAME" + postfix);
        USERNAME = Config.getString(type + "_USERNAME" + postfix);
        PASSWORD = Config.getString(type + "_PASSWORD" + postfix);        
    }
//    private static FTPOperation instance = null;
//
//    public static FTPOperation getInstance() {
//        if (instance == null) {
//            instance = new FTPOperation();
//        }
//        return instance;
//    }

    private static String createConnectionString(String remoteFilePath) {
        return "ftp://" + USERNAME + ":" + PASSWORD + "@" + HOSTNAME + "/"
                + remoteFilePath;
    }

    private static FileSystemOptions createDefaultOptions()
            throws FileSystemException, org.apache.commons.vfs2.FileSystemException {
        // Create SFTP options
        FileSystemOptions opts = new FileSystemOptions();

        // Root directory set to user home
        FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);

        // Timeout is count by Milliseconds
        FtpFileSystemConfigBuilder.getInstance().setConnectTimeout(opts, 10000);
        FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
        return opts;
    }

    public static void upload(String destFolder, String destFileName, File file) throws FileSystemException {
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create local file object
            FileObject localFile = manager.resolveFile(file.getAbsolutePath());

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(destFolder + "\\" + destFileName), createDefaultOptions());

            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);

            System.out.println("File upload success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }

    }

    public static void copy(String srcFolder, String srcFileName, String destFolder, String destFileName) {
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create local file object
            FileObject localFile = manager.resolveFile(
                    createConnectionString(srcFolder + "\\" + srcFileName), createDefaultOptions());

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(destFolder + "\\" + destFileName), createDefaultOptions());

            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);

            System.out.println("File upload success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
    }

    public static void delete(String srcFolder, String srcFileName) {
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create remote object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(srcFolder + "\\" + srcFileName), createDefaultOptions());
            if (remoteFile.exists()) {
                remoteFile.delete();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
    }

    public static void rename(String srcFolder, String srcFileName, String rename) {
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create local file object
            FileObject localFile = manager.resolveFile(
                    createConnectionString(srcFolder + "\\" + srcFileName), createDefaultOptions());

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(srcFolder + "\\" + rename), createDefaultOptions());

            // Copy local file to sftp server
            localFile.moveTo(remoteFile);

            System.out.println("File upload success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
    }

    public static InputStream downloadFromInputStream(String srcFolder, String srcFileName) {

        StandardFileSystemManager manager = new StandardFileSystemManager();
        InputStream is = null;
        try {
            manager.init();

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(srcFolder + "\\" + srcFileName), createDefaultOptions());
            FileContent fc = remoteFile.getContent();
            is = fc.getInputStream();
            // Copy local file to sftp server
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
        return is;
    }
    
    public static InputStream downloadFromInputStream(String srcFolder) {

        StandardFileSystemManager manager = new StandardFileSystemManager();
        InputStream is = null;
        try {
            manager.init();

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(
                    createConnectionString(srcFolder), createDefaultOptions());
            FileContent fc = remoteFile.getContent();
            is = fc.getInputStream();
            // Copy local file to sftp server
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
        return is;
    }

    public void insertFileToFTP(String destFolder, String destFileName, byte[] srcData) throws IOException {
        String suffix = ".cjt";//cj的temp檔
        File tempFile = File.createTempFile(prefix, suffix, null);
        log.debug("ftp tmp file:" + tempFile.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(srcData);
        FTPOperation.upload(destFolder, destFileName, tempFile);

    }
    /**
     * 測試
     * @param p 
     */
    public static void main(String[] p){
        FTPOperation ftpO = new FTPOperation(SVR_TYPE.FTP08);
        try {
            File fi = new File("c:/tempImg/test1.png");
            byte[] img = Files.readAllBytes(fi.toPath());
            System.out.println(img.length + "img size:");
            String unitCd = "AW";
            String type = "NM";
            ftpO.insertFileToFTP("UnitSealPic", unitCd + "-" + type, img);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
}

