package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {

    protected static Logger log = Logger.getLogger(ZipUtil.class);

    /**
     * 把多份檔案壓縮成一個Zip檔
     *
     * @param serverTempPath 取得暫存位置所有檔案
     * @throws IOException
     * @throws ParseException
     */
    public static void createZip(HttpServletResponse response, String serverTempPath, List<String> newReportName) throws IOException, ParseException {
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        zos.setEncoding(System.getProperty("sun.jnu.encoding"));
        String zipName = "";
        if ("重大損壞車輛通報單.doc".equals(newReportName.get(0))) {
            zipName = "重大車損_" + getDateTime() + ".zip";
        } else {
            zipName = getDateTime() + ".zip";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(zipName, "UTF-8"));

        File filePath = new File(serverTempPath);
        File[] list = filePath.listFiles();
        FileInputStream fis = null;
        for (File fileName : list) {
            for (String newFileName : newReportName) {
                if (newFileName.equals(fileName.getName())) {
                    zos.putNextEntry(new ZipEntry(newFileName));
                    fis = new FileInputStream(serverTempPath + "/" + newFileName);
                    int readLen = 0;
                    byte[] buf = new byte[4096];
                    while ((readLen = fis.read(buf)) != -1) {
                        zos.write(buf, 0, readLen);
                    }
                    fis.close();
                    fileName.delete();
                }
            }
        }
        zos.finish();
        zos.close();
    }

    public static void createZip(HttpServletResponse response, String serverTempPath) throws IOException, ParseException {
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        zos.setEncoding(System.getProperty("sun.jnu.encoding"));
        String zipName = getDateTime() + ".zip";
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(zipName, "UTF-8"));

        File filePath = new File(serverTempPath);
        File[] list = filePath.listFiles();
        FileInputStream fis = null;
        for (File fileName : list) {
            zos.putNextEntry(new ZipEntry(fileName.getName()));
            fis = new FileInputStream(serverTempPath + fileName.getName());
            int readLen = 0;
            byte[] buf = new byte[4096];
            while ((readLen = fis.read(buf)) != -1) {
                zos.write(buf, 0, readLen);
            }
            fis.close();
            fileName.delete();
        }
        zos.finish();
        zos.close();
    }

    //自建機關上傳匯入失敗時，下載錯誤資料
    public static void createZipFilter(HttpServletResponse response, String serverTempPath, String filterNm) throws IOException, ParseException {
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        zos.setEncoding(System.getProperty("sun.jnu.encoding"));
        String zipName = "Error_" + getDateTime() + ".zip";
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(zipName, "UTF-8"));

        File filePath = new File(serverTempPath);
        File[] list = filePath.listFiles();
        System.out.println("tmpFilePath:" + filePath.getAbsolutePath() + " size:" + list.length);
        FileInputStream fis = null;
        for (File fileName : list) {
            if (fileName.getName().endsWith("_" + filterNm + ".txt")) {
                zos.putNextEntry(new ZipEntry(fileName.getName()));
                fis = new FileInputStream(serverTempPath + fileName.getName());
                int readLen = 0;
                byte[] buf = new byte[4096];
                while ((readLen = fis.read(buf)) != -1) {
                    zos.write(buf, 0, readLen);
                }
                fis.close();
                fileName.delete();
            }
        }
        zos.finish();
        zos.close();
    }

    private static String getDateTime() throws ParseException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
        String newDate = dateFormat.format(date);
        return newDate;
    }

    /**
     * 建立 zip 檔
     *
     * @param srcFile	想要壓縮的資料夾
     * @param targetZip	壓縮zip檔
     * @throws IOException
     * @throws FileNotFoundException
     */
//    File srcFile = new File("C:\\servlet\\");
//    File targetZip = new File("C:\\123.zip");
//    new ZipUtil().makeZip(srcFile, targetZip);
//    
    public void makeZip(File srcFile, File targetZip)
            throws IOException, FileNotFoundException {

        //若資料夾未建立就建立
        if (!targetZip.getParentFile().exists()) {
            targetZip.getParentFile().mkdirs();
        }
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetZip));
        String dir = "";
        recurseFiles(srcFile, zos, dir);
        zos.close();
    }

    /**
     * 壓縮 主程式
     *
     * @param file
     * @param zos
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void recurseFiles(File file, ZipOutputStream zos, String dir)
            throws IOException, FileNotFoundException {
        //目錄
        dir = new String(dir.getBytes("UTF-8"), "UTF-8");
        if (file.isDirectory()) {
            //System.out.println("找到資料夾:" + file.getName());
            dir += file.getName() + File.separator;
            String[] fileNames = file.list();
            if (fileNames != null) {
                for (int i = 0; i < fileNames.length; i++) {
                    recurseFiles(new File(file, fileNames[i]), zos, dir);
                }
            }
        } //Otherwise, a file so add it as an entry to the Zip file.
        else {
            //System.out.println("壓縮檔案:" + file.getName());

            //byte[] buf = new byte[1024];
            byte[] buf = new byte[4096];
            int len;

            //Create a new Zip entry with the file's name.
            dir = dir.substring(dir.indexOf(File.separator) + 1);
            ZipEntry zipEntry = new ZipEntry(dir + file.getName());
            //Create a buffered input stream out of the file
            //we're trying to add into the Zip archive.
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fin);
            zos.putNextEntry(zipEntry);
            //Read bytes from the file and write into the Zip archive.

            while ((len = in.read(buf)) >= 0) {
                zos.write(buf, 0, len);
            }

            //Close the input stream.
            in.close();

            //Close this entry in the Zip stream.
            zos.closeEntry();
        }
    }

    //zip multiple files with password
    public void pack(List<String> files, String password, String zip_file_name) throws ZipException, IOException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
//        zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        zipParameters.setPassword(password);
        String destinationZipFilePath = "";
        if(zip_file_name.contains(".zip")){
            destinationZipFilePath = zip_file_name;
        }else{
            destinationZipFilePath = zip_file_name + ".zip";
        }
        for (String filePath : files) {
            ZipFile zipFile = new ZipFile(destinationZipFilePath);
            File file = new File(filePath);
            if (file.isFile()) {
                zipFile.addFile(file, zipParameters);
                //delete file
                file.delete();
            } else if (file.isDirectory()) {
                zipFile.addFolder(file, zipParameters);
                FileUtils.deleteDirectory(file);
            }

        }
    }

    public static void unzip(String inFile, String dest, String passwd) {

        try {
            ZipFile encZipFile = new ZipFile(inFile);
            encZipFile.setFileNameCharset("GBK");
            if (!encZipFile.isValidZipFile()) {
                throw new net.lingala.zip4j.exception.ZipException("壓縮檔可能損壞");
            }
            File destDir = new File(dest);
            if (destDir.isDirectory() && !destDir.exists()) {
                destDir.mkdir();
            }
            if (encZipFile.isEncrypted()) {
                encZipFile.setPassword(passwd.toCharArray());
            }
            encZipFile.extractAll(dest);
        } catch (net.lingala.zip4j.exception.ZipException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] a) throws ZipException, IOException {
        File srcFile = new File("C:\\servlet\\");
        File targetZip = new File("C:\\123.zip");
//        File extractDir = new File("C:\\servlet2\\");
//
//        List files = new ArrayList();
//        files.add("C:\\NM106-505\\ExportFile\\DrugCase\\exchange_1070621.xml");
//        new ZipUtil().pack(files,"34drugExchange","C:\\NM106-505\\ExportFile\\DrugCase\\exchange_1070621.zip");
//        new ZipUtil().unzip("C:\\NM106-505\\ExportFile\\DrugCase\\exchange_1070621.zip", "C:\\NM106-505\\ExportFile\\DrugCase", "34drugExchange");
//            //壓縮
//            new ZipUtil().makeZip(srcFile, targetZip);
//            //解壓縮
//            new Zip().unzipFile(targetZip, extractDir);
    }
}
