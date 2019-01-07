package cn.neusoft.loveread.manager.service.impl;

import cn.neusoft.loveread.common.pojo.EasyUITreeNode;
import cn.neusoft.loveread.manager.mapper.TbItemCatMapper;
import cn.neusoft.loveread.manager.service.ItemCatService;
import cn.neusoft.loveread.pojo.TbItemCat;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<EasyUITreeNode> getCatList(Long parentId){
        // 1、根据parentId查询节点列表
        //设置查询条件
        List<TbItemCat> list = itemCatMapper.getItemCatByParentId(parentId);
        // 2、转换成EasyUITreeNode列表。
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbItemCat tbItemCat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getParent() ? "closed" : "open");
            //添加到列表
            resultList.add(node);
        }
        return resultList;
    }
}
