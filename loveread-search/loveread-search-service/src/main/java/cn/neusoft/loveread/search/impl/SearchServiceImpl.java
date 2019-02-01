package cn.neusoft.loveread.search.impl;

import cn.neusoft.loveread.common.pojo.SearchResult;
import cn.neusoft.loveread.search.dao.SearchDao;
import cn.neusoft.loveread.search.service.SearchService;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Value("${DEFAULT_FIELD}")
    private String DEFAULT_FIELD;

    @Override
    public SearchResult search(String keyword, int page, int rows) throws Exception {
        //创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery(keyword);
        //设置分页条件
        query.setStart((page - 1) * rows);
        //设置rows
        query.setRows(rows);
        //设置默认搜索域
        query.set("df",DEFAULT_FIELD);
        //设置高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        //查询
        SearchResult searchResult = searchDao.search(query);
        //计算总页数
        int recourdCount = searchResult.getRecourdCount();
        int pages=recourdCount/rows;
        if(recourdCount%rows>10){
            pages++;
        }
        //设置返回结果
        searchResult.setTotalPages(pages);
        return searchResult;
    }
}
