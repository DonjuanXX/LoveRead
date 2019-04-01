package cn.neusoft.loveread.search.service.impl;

import cn.neusoft.loveread.common.pojo.SearchItem;
import cn.neusoft.loveread.common.pojo.SearchResult;
import cn.neusoft.loveread.pojo.TbItemCat;
import cn.neusoft.loveread.search.dao.SearchDao;
import cn.neusoft.loveread.search.mapper.SearchItemMapper;
import cn.neusoft.loveread.search.service.SearchService;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;
    @Autowired
    private SearchItemMapper itemMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Value("${DEFAULT_FIELD}")
    private String DEFAULT_FIELD;
    @Value("${CATEGORY_INFO_KEY}")
    private String CATEGORY_INFO_KEY;
    @Value("${CATEGORY_INFO_EXPIRE}")
    private Integer CATEGORY_INFO_EXPIRE;
    @Value("${CATEGORY_INFO_BASE_KEY}")
    private String CATEGORY_INFO_BASE_KEY;
    @Value("${CATEGORY_NAME_BASE_KEY}")
    private String CATEGORY_NAME_BASE_KEY;
    @Value("${CATEGORY_NUM_BASE_KEY}")
    private String CATEGORY_NUM_BASE_KEY;

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
        query.set("df", DEFAULT_FIELD);
        //设置高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        //查询
        SearchResult searchResult = searchDao.search(query);
        //计算总页数
        int recourdCount = searchResult.getRecourdCount();
        int pages = recourdCount / rows;
        if (recourdCount % rows > 10) {
            pages++;
        }
        //设置返回结果
        searchResult.setTotalPages(pages);
        return searchResult;
    }

    @Override
    public SearchResult list(Long cid, int page, int rows) throws Exception {
        List<SearchItem> list = new ArrayList<>();
        List<Long> childNode = getRealCategory(cid);
        List<Long> realNode = new ArrayList<>(childNode);
        for (Long node : childNode) {
            realNode.addAll(getRealCategory(node));
        }
        List<Long> Union = getUnionNode();
        realNode.removeAll(Union);
        for (Long node : realNode) {
            list.addAll(getRedis(node));
        }
        List<SearchItem> Showlist = new ArrayList<>(rows);
        int recourdCount = list.size();
        int pages = recourdCount / rows;
        int start = (page - 1) * rows;
        int end = (start + 60) > list.size() ? list.size() : start + 60;
        if (recourdCount % rows > 10) {
            pages++;
        }
        for (int i = start; i < end; i++) {
            Showlist.add(list.get(i));
        }
        SearchResult result = new SearchResult();
        result.setTotalPages(pages);
        result.setRecourdCount(recourdCount);
        result.setItemList(Showlist);
        return result;
    }

    @Override
    public String getNameByCid(Long cid) {
        List<TbItemCat> list = getNameByRedis();
        for (TbItemCat category : list) {
            if (category.getId().equals(cid)) {
                return category.getName();
            }
        }
        return null;
    }

    private List<SearchItem> getRedis(Long cid) {
        try {
            List<SearchItem> list = (List<SearchItem>) redisTemplate.opsForValue().get(CATEGORY_INFO_KEY + ":" + cid.toString() + ":" + CATEGORY_INFO_BASE_KEY);
            if (list != null) {
                System.out.println("read redis cat base information....");
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 查询数据库
        List<SearchItem> list = itemMapper.getItemListByCategory(cid);
        if (list != null) {
            try {
                // 把数据保存到缓存
                redisTemplate.opsForValue().set(CATEGORY_INFO_KEY + ":" + cid.toString() + ":" + CATEGORY_INFO_BASE_KEY, list);
                // 设置缓存有效期
                redisTemplate.expire(CATEGORY_INFO_KEY + ":" + list + ":" + CATEGORY_INFO_BASE_KEY, CATEGORY_INFO_EXPIRE, TimeUnit.HOURS);
                System.out.println("write redis cat base information...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
        return null;
    }

    private List<TbItemCat> getNameByRedis() {
        try {
            List<TbItemCat> list = (List<TbItemCat>) redisTemplate.opsForValue().get(CATEGORY_INFO_KEY + ":" + CATEGORY_NAME_BASE_KEY);
            if (list != null) {
                System.out.println("read redis catName base information....");
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 查询数据库
        List<TbItemCat> list = itemMapper.getCategoryName();
        if (list != null) {
            try {
                // 把数据保存到缓存
                redisTemplate.opsForValue().set(CATEGORY_INFO_KEY + ":" + CATEGORY_NAME_BASE_KEY, list);
                // 设置缓存有效期
                redisTemplate.expire(CATEGORY_INFO_KEY + ":" + list + ":" + CATEGORY_NAME_BASE_KEY, CATEGORY_INFO_EXPIRE, TimeUnit.HOURS);
                System.out.println("write redis catName base information...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
        return null;
    }

    private List<Long> getRealCategory(Long cid) {
        try {
            List<Long> list = (List<Long>) redisTemplate.opsForValue().get(CATEGORY_INFO_KEY + ":" + cid.toString() + ":" + CATEGORY_NUM_BASE_KEY);
            if (list != null) {
                System.out.println("read redis catNum base information....");
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 查询数据库
        List<Long> list = itemMapper.getChildNodeByCid(cid);
        if (list != null) {
            try {
                redisTemplate.opsForValue().set(CATEGORY_INFO_KEY + ":" + cid.toString() + ":" + CATEGORY_NUM_BASE_KEY, list);
                redisTemplate.expire(CATEGORY_INFO_KEY + ":" + list + ":" + CATEGORY_NUM_BASE_KEY, CATEGORY_INFO_EXPIRE, TimeUnit.HOURS);
                System.out.println("write redis catNum base information...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
        return null;
    }

    private List<Long> getUnionNode() {
        try {
            List<Long> list = (List<Long>) redisTemplate.opsForValue().get(CATEGORY_INFO_KEY + ":" + CATEGORY_NUM_BASE_KEY);
            if (list != null) {
                System.out.println("read redis Union base information....");
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 查询数据库
        List<Long> list = itemMapper.getUnionNode();
        if (list != null) {
            try {
                redisTemplate.opsForValue().set(CATEGORY_INFO_KEY + ":" + CATEGORY_NUM_BASE_KEY, list);
                redisTemplate.expire(CATEGORY_INFO_KEY + ":" + list + ":" + CATEGORY_NUM_BASE_KEY, CATEGORY_INFO_EXPIRE, TimeUnit.HOURS);
                System.out.println("write redis Union base information...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
        return null;

    }
}
