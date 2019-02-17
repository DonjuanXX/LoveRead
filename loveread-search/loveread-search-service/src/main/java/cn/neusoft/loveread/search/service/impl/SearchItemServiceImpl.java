package cn.neusoft.loveread.search.service.impl;

import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.common.pojo.SearchItem;
import cn.neusoft.loveread.search.mapper.SearchItemMapper;
import cn.neusoft.loveread.search.service.SearchItemService;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper itemMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public LoveReadResult importItems() {
        try {
            //查询商品列表
            List<SearchItem> itemList = itemMapper.getItemList();
            //导入索引库
            for (SearchItem searchItem : itemList){
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档中添加域
                document.addField("id",searchItem.getId());
                document.addField("item_title",searchItem.getTitle());
                document.addField("item_sell_point",searchItem.getSell_point());
                document.addField("item_price",searchItem.getPrice());
                document.addField("item_image",searchItem.getImage());
                document.addField("item_category_name",searchItem.getCategory_name());
                //写入索引库
                solrClient.add(document);
            }
            //提交
            solrClient.commit();
            return LoveReadResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return LoveReadResult.build(500,"导入书籍失败");
        }
    }
}
