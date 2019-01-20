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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public EasyUIDataGridResult getContentListByCategoryId(Long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);

        List<TbContent> tbContents = new ArrayList<>();
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
    public LoveReadResult addContent(TbContent content) {
        //补全属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入数据
        contentMapper.insertContent(content);
        return LoveReadResult.ok();
    }
}
