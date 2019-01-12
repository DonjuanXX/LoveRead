package cn.neusoft.loveread.manager.mapper;

import cn.neusoft.loveread.common.utils.FastDFSClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastDFSTest {

    @Test
    public void testFastDfsClient() throws Exception{
        FastDFSClient fastDFSClient = new FastDFSClient("E:\\J2seWorkspace\\LoveRead\\loveread-manager\\loveread-manager-web\\src\\main\\resources\\conf\\fastdfs-client.conf");
        String file = fastDFSClient.uploadFile("C:\\Users\\ALL\\Pictures\\Camera Roll\\timg.jpg");
        System.out.println(file);
    }
}
