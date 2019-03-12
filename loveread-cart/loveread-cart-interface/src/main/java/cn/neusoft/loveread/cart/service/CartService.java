package cn.neusoft.loveread.cart.service;

import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.pojo.TbItem;

import java.util.List;

public interface CartService {
    LoveReadResult addCart(Long userId,Long itemId,int num);
    LoveReadResult mergeCart(Long userId, List<TbItem> cookieItemList);
    List<TbItem> getCartList(Long userId);
    LoveReadResult updateCartNum(Long userId,Long itemId,int num);
    LoveReadResult deleteCartItem(Long userId,Long itemId);
}
