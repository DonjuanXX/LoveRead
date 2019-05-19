package cn.neusoft.loveread.manager.controller;

import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.manager.service.TbItemService;
import cn.neusoft.loveread.pojo.TbItem;
import cn.neusoft.loveread.pojo.TbItemDesc;
import cn.neusoft.loveread.pojo.TbUser;
import cn.neusoft.loveread.sso.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @GetMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult getUserList(Integer page, Integer rows) {
        return userService.getUserList(page, rows);
    }


    @RequestMapping("/delete")
    @ResponseBody
    public LoveReadResult delete(@RequestBody String ids) {
        String[] id = ids.split("=", 2);
        String[] target = id[1].split("%2C");
        int frequency = target.length;
        while (frequency-- != 0) {
            long Id = Long.parseLong(target[frequency]);
            userService.delete(Id);
        }
        return LoveReadResult.ok();
    }


    @RequestMapping("/update")
    @ResponseBody
    public LoveReadResult update(TbUser user) {
        //检查用户名和手机号
        return userService.update(user);
    }
}
