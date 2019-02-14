package cn.neusoft.loveread.manager.controller;


import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.search.service.SearchItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {

    @Reference(timeout = 300000)
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public LoveReadResult importItemIndex(){
        LoveReadResult readResult = searchItemService.importItems();
        return readResult;
    }
}
