package cn.neusoft.loveread.manager.mapper;

import cn.neusoft.loveread.pojo.TbItemCat;

import java.util.List;

public interface TbItemCatMapper {
    List<TbItemCat> getItemCatByParentId(long parentId);

    String getItemCatNameById(long id);
}
