package cn.neusoft.loveread.content.service;

import cn.neusoft.loveread.common.pojo.EasyUITreeNode;
import cn.neusoft.loveread.common.pojo.LoveReadResult;

import java.util.List;

public interface ContentCategoryService {
    List<EasyUITreeNode> getContentCategoryList(Long parentId);

    LoveReadResult addContentCategory(long parentId, String name);
    LoveReadResult deleteContentCategory(long Id);
    LoveReadResult updateContentCategoryReal(long parentId, String name);
}
