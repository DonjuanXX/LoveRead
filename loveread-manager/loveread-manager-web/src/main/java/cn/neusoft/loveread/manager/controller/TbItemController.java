package cn.neusoft.loveread.manager.controller;

import cn.neusoft.loveread.manager.service.TbItemService;
import cn.neusoft.loveread.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item")
public class TbItemController {

    @Autowired
    private TbItemService tbItemService;

    @GetMapping("/{itemId}")
    @ResponseBody
    public TbItem hello(@PathVariable Long itemId){
        return tbItemService.getItemById(itemId);
    }
}
