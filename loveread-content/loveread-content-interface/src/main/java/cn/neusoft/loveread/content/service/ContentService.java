package cn.neusoft.loveread.content.service;

import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.pojo.TbContent;

public interface ContentService {
    LoveReadResult addContent(TbContent content);

    EasyUIDataGridResult getContentListByCategoryId(Long categoryId, int page, int rows);
}
