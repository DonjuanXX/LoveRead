package cn.neusoft.loveread.manager.mapper;

import cn.neusoft.loveread.pojo.TbContent;

import java.util.List;

public interface TbContentMapper {
    List<TbContent> getContentListByCategoryId(Long categoryId);
    List<TbContent> getAllContentList();
    void insertContent(TbContent tbContent);
    TbContent selectByPrimaryKey(Long id);
    void deleteContent(TbContent tbContent);
    void updateContent(TbContent tbContent);
}
