package cn.neusoft.loveread.content.service.impl;

import cn.neusoft.loveread.common.pojo.EasyUITreeNode;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.content.service.ContentCategoryService;
import cn.neusoft.loveread.manager.mapper.TbContentCategoryMapper;
import cn.neusoft.loveread.pojo.TbContentCategory;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
        // 1、取查询参数id，parentId
        // 2、根据parentId查询tb_content_category，查询子节点列表
        // 3、得到List<TbContentCategory>
        List<TbContentCategory> list = contentCategoryMapper.selectTbContentCatsByParentId(parentId);
        // 4、把列表转换成List<EasyUITreeNode>ub
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory contentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(contentCategory.getId());
            node.setText(contentCategory.getName());
            node.setState(contentCategory.getParent() ? "closed" : "open");
            // 添加到列表
            resultList.add(node);
        }
        return resultList;
    }

    @Override
    public LoveReadResult addContentCategory(long parentId, String name) {
        // 1、接收两个参数：parentId、name
        // 2、向tb_content_category表中插入数据。
        // a)创建一个TbContentCategory对象
        TbContentCategory tbContentCategory = new TbContentCategory();
        // b)补全TbContentCategory对象的属性
        tbContentCategory.setParent(false);
        tbContentCategory.setName(name);
        tbContentCategory.setParentId(parentId);
        //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        tbContentCategory.setSortOrder(1);
        //状态。可选值:1(正常),2(删除)
        tbContentCategory.setStatus(1);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        // c)向tb_content_category表中插入数据
        contentCategoryMapper.insertCategory(tbContentCategory);
        // 3、判断父节点的isparent是否为true，不是true需要改为true。
        TbContentCategory parentNode = contentCategoryMapper.selectTbContentCatById(parentId);
        if (!parentNode.getParent()) {
            parentNode.setParent(true);
            //更新父节点
            contentCategoryMapper.updateContentCategoryById(parentNode);
        }
        // 4、需要主键返回。
        // 5、返回LRResult，其中包装TbContentCategory对象
        return LoveReadResult.ok(tbContentCategory);
    }

    @Override
    public LoveReadResult deleteContentCategory(long Id) {
        //发送过来的是该类目的id
        TbContentCategory tbContentCategory = contentCategoryMapper.selectTbContentCatById(Id);
        TbContentCategory parentNode = contentCategoryMapper.selectTbContentCatById(tbContentCategory.getParentId());
        if(tbContentCategory.getParent()){
            List<TbContentCategory> list = contentCategoryMapper.selectTbContentCatsByParentId(Id);
            for (TbContentCategory contentCategory : list) {
                contentCategoryMapper.deleteContentCategoryById(contentCategory.getId());
            }
        }
        contentCategoryMapper.deleteContentCategoryById(Id);
        if (parentNode.getParent()) {
            if (contentCategoryMapper.selectTbContentCatsByParentId(parentNode.getId()).size() == 0) {
                parentNode.setParent(false);
                parentNode.setUpdated(new Date());
                contentCategoryMapper.updateContentCategoryById(parentNode);
            }
        }
        return LoveReadResult.ok(parentNode);
    }

    @Override
    public LoveReadResult updateContentCategoryReal(long id, String name) {
        TbContentCategory tbContentCategory = contentCategoryMapper.selectTbContentCatById(id);
        tbContentCategory.setName(name);
        tbContentCategory.setUpdated(new Date());
        contentCategoryMapper.updateContentCategoryById(tbContentCategory);
        return LoveReadResult.ok();
    }
}
