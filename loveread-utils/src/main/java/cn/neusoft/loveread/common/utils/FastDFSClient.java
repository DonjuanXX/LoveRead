package cn.neusoft.loveread.common.utils;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;


public class FastDFSClient {

    private TrackerClient trackerClient = null;
    private TrackerServer trackerServer = null;
    private StorageServer storageServer = null;
    private StorageClient1 storageClient = null;

    public FastDFSClient(String conf) throws Exception {
        if (conf.contains("classpath:")) {
            conf = conf.replace("classpath:", this.getClass().getResource("/").getPath());
        }
        ClientGlobal.init(conf);
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getConnection();
        storageServer = null;
        storageClient = new StorageClient1(trackerServer, storageServer);
    }

    /**
     * 上传文件方法
     * Title：uploadFile
     * Description:
     *
     * @param filename 文件全路径
     * @param extName  文件扩展名
     * @param metas    文件扩展信息
     * @return result
     * @throws Exception
     */
    public String uploadFile(String filename, String extName, NameValuePair[] metas) throws Exception {
        return storageClient.upload_file1(filename, extName, metas);
    }

    public String uploadFile(String filename) throws Exception {
        return uploadFile(filename, null, null);
    }

    public String uploadFile(String filename, String extName) throws Exception {
        return uploadFile(filename, extName, null);
    }

    /**
     * 上传文件方法
     * Title：uploadFile
     * Description:
     *
     * @param fileContent 文件的内容，字节数组
     * @param extName     文件扩展名
     * @param metas       文件扩展信息
     * @return
     * @throws Exception
     */
    public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {
        return storageClient.upload_file1(fileContent, extName, metas);
    }

    public String uploadFile(byte[] fileContent) throws Exception {
        return uploadFile(fileContent, null, null);
    }

    public String uploadFile(byte[] fileContent, String extName) throws Exception {
        return uploadFile(fileContent, extName, null);
    }
}
