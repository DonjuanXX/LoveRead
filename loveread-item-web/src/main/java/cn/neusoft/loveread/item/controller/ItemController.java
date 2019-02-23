package cn.neusoft.loveread.item.controller;

import cn.neusoft.loveread.item.pojo.Item;
import cn.neusoft.loveread.manager.service.TbItemService;
import cn.neusoft.loveread.pojo.TbItem;
import cn.neusoft.loveread.pojo.TbItemDesc;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {
    @Reference
    private TbItemService itemService;

    @RequestMapping("/item/{itemId}.html")
    public String showItemInfo(@PathVariable Long itemId, Model model){
        //根据商品id查询商品信息
        TbItem tbItem = itemService.getItemById(itemId);
        //把TbItem转换成Item对象
        Item item = new Item(tbItem);
        //根据商品id查询商品描述
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
        //把数据传递给页面
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",tbItemDesc);
        return "item";
    }
}
