package cn.neusoft.loveread.manager.service.impl;


import cn.neusoft.loveread.manager.mapper.TbItemMapper;
import cn.neusoft.loveread.manager.service.TbItemService;
import cn.neusoft.loveread.pojo.TbItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class TbItemServiceImpl implements TbItemService {
    @Autowired
    private TbItemMapper tbItemMapper;

    @Override
    public TbItem getItemById(Long itemId){
        return tbItemMapper.selectByPrimaryKey(itemId);
    }

}
