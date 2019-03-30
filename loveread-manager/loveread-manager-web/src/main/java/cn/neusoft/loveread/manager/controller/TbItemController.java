package cn.neusoft.loveread.manager.controller;

import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.manager.service.TbItemService;
import cn.neusoft.loveread.pojo.TbItem;

import cn.neusoft.loveread.pojo.TbItemDesc;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/item")
public class TbItemController {

    @Reference
    private TbItemService tbItemService;

    @GetMapping("/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        return tbItemService.getItemById(itemId);
    }

    @GetMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        return tbItemService.getItemList(page, rows);
    }


    @RequestMapping("/save")
    @ResponseBody
    public LoveReadResult saveItem(TbItem item, String desc) {
        LoveReadResult result = tbItemService.addItem(item, desc);
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public LoveReadResult delete(@RequestBody String id) {
        String[] ids = id.split("=", 2);
//        if (ids[1].indexOf("%2C") != 0)  indexof没有返回-1
        String[] target = ids[1].split("%2C");
        int frequency = target.length;
        while (frequency-- != 0) {
            long Id = Long.parseLong(target[frequency]);
            TbItem tbItem = tbItemService.getItemById(Id);
            TbItemDesc tbItemDesc = tbItemService.getItemDescById(Id);
            tbItemService.deleteItem(tbItem, tbItemDesc);
        }
        return LoveReadResult.ok();
    }

    @GetMapping("/desc/{itemId}")
    @ResponseBody
    public TbItemDesc getItemDescById(@PathVariable Long itemId) {
        return tbItemService.getItemDescById(itemId);
    }

    @RequestMapping("/update")
    @ResponseBody
    public LoveReadResult update(TbItem item) {
        System.out.println(item.toString());
        return LoveReadResult.ok();
    }
}
