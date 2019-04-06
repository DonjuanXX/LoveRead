package cn.neusoft.loveread.manager.mapper;


import cn.neusoft.loveread.pojo.TbUserDesc;
import cn.neusoft.loveread.pojo.TbUserFavorite;
import cn.neusoft.loveread.pojo.TbUserFavoriteAndName;

import java.util.List;

public interface TbUserDescMapper {

    List<TbUserFavoriteAndName> getItemParamList();

    TbUserDesc getItemParamByCid(Long cid);

    void insertItemParam(TbUserDesc tbUserDesc);

}
