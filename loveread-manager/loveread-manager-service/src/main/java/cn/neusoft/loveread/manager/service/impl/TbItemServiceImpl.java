package cn.neusoft.loveread.manager.service.impl;


import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.common.utils.IDUtils;
import cn.neusoft.loveread.manager.mapper.TbItemDescMapper;
import cn.neusoft.loveread.manager.mapper.TbItemMapper;
import cn.neusoft.loveread.manager.service.TbItemService;
import cn.neusoft.loveread.pojo.TbItem;
import cn.neusoft.loveread.pojo.TbItemDesc;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class TbItemServiceImpl implements TbItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${ITEM_INFO_KEY}")
    private String ITEM_INFO_KEY;
    @Value("${ITEM_INFO_BASE_KEY}")
    private String ITEM_INFO_BASE_KEY;
    @Value("${ITEM_INFO_DESC_KEY}")
    private String ITEM_INFO_DESC_KEY;
    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;

    @Override
    public TbItem getItemById(Long itemId) {
        //查询缓存
        try {
            TbItem tbItem = (TbItem) redisTemplate.opsForValue().get(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_BASE_KEY);
            if (tbItem != null) {
                System.out.println("read redis item base information....");
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 查询数据库
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        if (tbItem != null) {
            try {
                // 把数据保存到缓存
                redisTemplate.opsForValue().set(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_BASE_KEY, tbItem);
                // 设置缓存有效期
                redisTemplate.expire(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_BASE_KEY, ITEM_INFO_EXPIRE, TimeUnit.HOURS);
                System.out.println("write redis item base information...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tbItem;
        }
        return null;
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        //查询缓存
        try {
            TbItemDesc itemDesc = (TbItemDesc) redisTemplate.opsForValue().get(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_DESC_KEY);
            if (itemDesc != null) {
                System.out.println("read redis item desc information...");
                return itemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //查询数据库
        TbItemDesc itemDesc = tbItemDescMapper.selectItemDescByPrimaryKey(itemId);
        if (itemDesc != null) {
            // 把数据保存到缓存
            try {
                redisTemplate.opsForValue().set(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_DESC_KEY, itemDesc);
                redisTemplate.expire(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_DESC_KEY, ITEM_INFO_EXPIRE, TimeUnit.HOURS);
                System.out.println("write redis item desc information");
            }catch (Exception e){
                e.printStackTrace();
            }
            return itemDesc;
        }
        return null;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        List<TbItem> list = tbItemMapper.getItemList();
        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);

        //创建返回结果对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }
    /*
    * 后台管理添加商品至数据库
    * */
    @Override
    public LoveReadResult addItem(TbItem item, String desc) {
        //生成商品ID
        long itemId = IDUtils.genItemId();
        //不全TbItem对象的属性
        item.setId(itemId);
        //商品状态 1正常 2下架 3删除
        item.setStatus((byte) 1);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        //表 插数据
        tbItemMapper.insert(item);
        //创建一个TbItemDesc对象
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setCreated(date);
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(itemId);
        itemDesc.setUpdated(date);
        //向商品描述表插入数据
        tbItemDescMapper.insert(itemDesc);
        //发送消息队列，通知新增商品
        ActiveMQTopic itemAddTopic = new ActiveMQTopic("itemAddTopic");
        jmsMessagingTemplate.convertAndSend(itemAddTopic, item.getId());
        return LoveReadResult.ok();
    }

}
