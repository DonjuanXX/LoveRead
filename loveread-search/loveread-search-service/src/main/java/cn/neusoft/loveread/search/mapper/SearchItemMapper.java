package cn.neusoft.loveread.search.mapper;

import cn.neusoft.loveread.common.pojo.SearchItem;
import cn.neusoft.loveread.pojo.TbItemCat;
import java.util.List;



public interface SearchItemMapper {
    List<SearchItem> getItemList();

    SearchItem getItemById(Long itemId);

    List<SearchItem> getItemListByCategory(Long cid);

    List<TbItemCat> getCategoryName();

    List<Long> getChildNodeByCid(Long cid);

    List<Long> getUnionNode();
}
