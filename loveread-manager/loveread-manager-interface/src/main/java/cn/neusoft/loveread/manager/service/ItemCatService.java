package cn.neusoft.loveread.manager.service;

import cn.neusoft.loveread.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    List<EasyUITreeNode> getCatList(Long parentId);
}
