package cn.neusoft.loveread.search.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:conf/conf.properties")
public class MySettingFileConfig {
    /*
    //solr集群地址 （zk地址）
    @Value("${spring.data.solr.zk-host}")
    public String ZK_HOST;

    //注入cloudSolrClient
    @Bean
    public CloudSolrClient solrClient(){
        CloudSolrClient solrClient = new CloudSolrClient(ZK_HOST);
        solrClient.setDefaultCollection("collection1");
        return solrClient;
        */
}
