package cn.neusoft.loveread.search.config;


import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:conf/conf.properties")
public class MySettingFileConfig {

    //solr集群地址 （zk地址）
    /*
    @Value("${spring.data.solr.zk-host}")
    public String ZK_HOST;

    //注入cloudSolrClient
    @Bean
    public CloudSolrClient solrClient() {
        CloudSolrClient solrClient = new CloudSolrClient(ZK_HOST);
        solrClient.setDefaultCollection("collection2");
        return solrClient;
    }
    */
}
