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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


@Service
public class TbItemServiceImpl implements TbItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public TbItem getItemById(Long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
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

    @Override
    public LoveReadResult addItem(TbItem item, String desc) {
        //生成商品ID
        long itemId = IDUtils.genItemId();
        //不全TbItem对象的属性
        item.setId(itemId);
        //商品状态 1正常 2下架 3删除
        item.setStatus((byte)1);
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
        tbItemDescMapper.insert(itemDesc);
        return LoveReadResult.ok();
    }

}
