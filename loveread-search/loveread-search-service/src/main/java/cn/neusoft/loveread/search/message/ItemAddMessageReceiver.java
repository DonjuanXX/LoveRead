package cn.neusoft.loveread.search.message;

import cn.neusoft.loveread.common.pojo.SearchItem;
import cn.neusoft.loveread.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import javax.xml.bind.Unmarshaller;
import java.io.IOException;

@Component
public class ItemAddMessageReceiver {

    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrClient solrClient;

    @JmsListener(destination = "itemAddTopic", containerFactory = "jmsTopicListenerContainerFactory")
    public void itemAddReceiver(Long msg) {
        try {
            // 0、等待1s让manager-service提交完事务，商品添加成功
            Thread.sleep(1000);
            // 1、根据商品id查询商品信息
            SearchItem searchItem = searchItemMapper.getItemById(msg);
            // 2、创建一SolrInputDocument对象。
            SolrInputDocument document = new SolrInputDocument();
            // 3、使用SolrServer对象写入索引库。
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            // 5、向索引库中添加文档。
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "itemDeleteTopic", containerFactory = "jmsTopicListenerContainerFactory")
    public void itemDelReceiver(Long msg) {
        try {
            // 0、等待1s让manager-service提交完事务
            Thread.sleep(1000);
            // 1、根据商品id查询商品信息
            solrClient.deleteById(msg.toString());
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @JmsListener(destination = "itemUpdateTopic", containerFactory = "jmsTopicListenerContainerFactory")
//    public void itemUpdateReceiver(Long msg) {
//        try {
//            // 0、等待1s让manager-service提交完事务
//            Thread.sleep(1000);
//            // 1、根据商品id查询商品信息
//            SearchItem searchItem = searchItemMapper.getItemById(msg);
//            // 2、创建一SolrInputDocument对象。
//            // 3、使用SolrServer对象写入索引库。
//            solrClient.u(msg.toString());
//            solrClient.commit();
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ListenerExecutionFailedException e) {
//            e.printStackTrace();
//        }
//    }
}
