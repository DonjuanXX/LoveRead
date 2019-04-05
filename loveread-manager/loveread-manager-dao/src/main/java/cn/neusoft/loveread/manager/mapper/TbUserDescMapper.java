package cn.neusoft.loveread.manager.mapper;


import cn.neusoft.loveread.pojo.TbUserFavorite;
import cn.neusoft.loveread.pojo.TbUserFavoriteAndName;

import java.util.List;

public interface TbUserDescMapper {

    List<TbUserFavoriteAndName> getItemParamList();

    TbUserFavorite getItemParamByCid(Long cid);

    Integer insertItemParam(TbUserFavorite tbUserFavorite);
}
