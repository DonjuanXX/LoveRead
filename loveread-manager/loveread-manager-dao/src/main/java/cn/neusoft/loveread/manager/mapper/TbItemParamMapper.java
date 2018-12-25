package cn.neusoft.loveread.manager.mapper;


import cn.neusoft.loveread.pojo.TbItemParam;
import cn.neusoft.loveread.pojo.TbItemParamAndName;

import java.util.List;

public interface TbItemParamMapper {

    List<TbItemParamAndName> getItemParamList();

    TbItemParam getItemParamByCid(Long cid);

    Integer insertItemParam(TbItemParam tbItemParam);
}
