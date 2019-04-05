package cn.neusoft.loveread.manager.mapper;

import cn.neusoft.loveread.pojo.TbUserFavorite;

public interface TbUserFavoriteMapper {
    void insert(TbUserFavorite tbItemParamItem);

    TbUserFavorite selectItemParamByItemId(Long itemId);

    String getFavoriteById(Long id);

    void update(TbUserFavorite tbUserFavorite);
}
