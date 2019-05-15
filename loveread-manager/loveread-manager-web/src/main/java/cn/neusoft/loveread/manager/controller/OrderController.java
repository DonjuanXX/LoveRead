package cn.neusoft.loveread.manager.controller;

import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.order.pojo.OrderInfo;
import cn.neusoft.loveread.order.service.OrderService;
import cn.neusoft.loveread.pojo.TbOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @GetMapping("/{orderId}")
    @ResponseBody
    public OrderInfo getOrderById(@PathVariable Long itemId) {
        return orderService.getOrderById(itemId);
    }

    @GetMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult getOrderList(Integer page, Integer rows) {
        return orderService.getOrderList(page, rows);
    }


//    @RequestMapping("/save")
//    @ResponseBody
//    public LoveReadResult saveItem(TbItem item, String desc) {
//        LoveReadResult result = tbItemService.addItem(item, desc);
//        return result;
//    }
//
//    @RequestMapping("/delete")
//    @ResponseBody
//    public LoveReadResult delete(@RequestBody String id) {
//        String[] ids = id.split("=", 2);
////        if (ids[1].indexOf("%2C") != 0)  indexof没有返回-1
//        String[] target = ids[1].split("%2C");
//        int frequency = target.length;
//        while (frequency-- != 0) {
//            long Id = Long.parseLong(target[frequency]);
//            TbItem tbItem = tbItemService.getItemById(Id);
//            TbItemDesc tbItemDesc = tbItemService.getItemDescById(tbItem.getId());
//            tbItemService.deleteItem(tbItem, tbItemDesc);
//        }
//        return LoveReadResult.ok();
//    }
//
//    @GetMapping("/desc/{itemId}")
//    @ResponseBody
//    public TbItemDesc getItemDescById(@PathVariable Long itemId) {
//        return tbItemService.getItemDescById(itemId);
//    }
//
//    @RequestMapping("/update")
//    @ResponseBody
//    public LoveReadResult update(TbItem item,String desc) {
//        TbItemDesc ItemDesc = tbItemService.getItemDescById(item.getId());
//        ItemDesc.setItemDesc(desc);
//        return tbItemService.updateItem(item,ItemDesc);
//    }
}
