package cn.neusoft.loveread.cart.service.impl;

import cn.neusoft.loveread.cart.service.CartService;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.manager.mapper.TbItemMapper;
import cn.neusoft.loveread.pojo.TbItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TbItemMapper tbItemMapper;
    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    @Override
    public LoveReadResult addCart(Long userId, Long itemId, int num) {
        Boolean hasItem = redisTemplate.opsForHash().hasKey(REDIS_CART_PRE + ":" + userId, itemId + "");
        if (hasItem) {
            //商品存在，数量相加
            TbItem tbItem = (TbItem) redisTemplate.opsForHash().get(REDIS_CART_PRE + ":" + userId, itemId + "");
            tbItem.setNum(tbItem.getNum() + num);
            redisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId + "", tbItem);
            return LoveReadResult.ok();
        } else {
            //查询并添加
            TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
            item.setNum(num);
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)) {
                item.setImage(image.split(",")[0]);
            }
            redisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId + "", item);
            return LoveReadResult.ok();
        }
    }

    @Override
    public LoveReadResult mergeCart(Long userId, List<TbItem> cookieItemList) {
        for (TbItem tbItem : cookieItemList) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        return LoveReadResult.ok();
    }

    @Override
    public List<TbItem> getCartList(Long userId) {
        List<Object> results = redisTemplate.opsForHash().values(REDIS_CART_PRE + ":" + userId);
        List<TbItem> tbItems = new ArrayList<>();
        for (Object result : results) {
            tbItems.add((TbItem) result);
        }
        return tbItems;
    }

    @Override
    public LoveReadResult updateCartNum(Long userId, Long itemId, int num) {
        TbItem tbItem = (TbItem) redisTemplate.opsForHash().get(REDIS_CART_PRE + ":" + userId, itemId + "");
        tbItem.setNum(tbItem.getNum() + num);
        redisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId + "", tbItem);
        return LoveReadResult.ok();
    }

    @Override
    public LoveReadResult deleteCartItem(Long userId, Long itemId) {
        redisTemplate.opsForHash().delete(REDIS_CART_PRE + ":" + userId, itemId + "");
        return LoveReadResult.ok();
    }

    @Override
    public LoveReadResult clearCartList(Long userId) {
        redisTemplate.delete(REDIS_CART_PRE + ":" + userId);
        return LoveReadResult.ok();
    }


}
