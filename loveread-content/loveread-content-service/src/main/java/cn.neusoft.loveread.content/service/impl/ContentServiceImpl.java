package cn.neusoft.loveread.content.service.impl;

import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.content.service.ContentService;
import cn.neusoft.loveread.manager.mapper.TbContentMapper;
import cn.neusoft.loveread.pojo.TbContent;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;

    @Override
    public EasyUIDataGridResult getContentListByCategoryId(Long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);

        List<TbContent> tbContents ;
        if (categoryId == 0L) {
            tbContents = contentMapper.getAllContentList();
        } else {
            tbContents = contentMapper.getContentListByCategoryId(categoryId);
        }
        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContents);
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
        easyUIDataGridResult.setRows(tbContents);
        easyUIDataGridResult.setTotal(pageInfo.getTotal());
        return easyUIDataGridResult;
    }

    @Override
    public List<TbContent> getContentList(Long cid) {
        //查询缓存
        try {
            List<TbContent> contents = (List<TbContent>) redisTemplate.opsForHash().get(CONTENT_KEY, cid.toString());
            System.out.println("read redis catch data...");
            if (contents != null && !contents.isEmpty()) {
//            if (!contents.isEmpty() && contents != null) {
                System.out.println("get it");
                return contents;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //根据cid查询内容列表
        List<TbContent> list = contentMapper.getContentListByCategoryId(cid);
        //向缓存中添加数据
        try {
            redisTemplate.opsForHash().put(CONTENT_KEY, cid.toString(), list);
            System.out.println("write redis catch data...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public LoveReadResult addContent(TbContent content) {
        //补全属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入数据
        contentMapper.insertContent(content);
        //缓存同步
        redisTemplate.opsForHash().delete(CONTENT_KEY, content.getCategoryId().toString());
        return LoveReadResult.ok();
    }

    @Override
    public TbContent getContentById(Long id) {
        //直接从db查找
        TbContent tbItem = contentMapper.selectByPrimaryKey(id);
        if (tbItem != null) {
            return tbItem;
        }
        return null;
    }

    @Override
    public LoveReadResult deleteContent(TbContent content) {
        //缓存同步
        redisTemplate.opsForHash().delete(CONTENT_KEY, content.getCategoryId().toString());
        //删除数据
        contentMapper.deleteContent(content);
        return LoveReadResult.ok();
    }

    @Override
    public LoveReadResult updateContent(TbContent content) {
        redisTemplate.opsForHash().delete(CONTENT_KEY, content.getCategoryId().toString());
        content.setUpdated(new Date());
        contentMapper.updateContent(content);
        return LoveReadResult.ok();
    }
}
