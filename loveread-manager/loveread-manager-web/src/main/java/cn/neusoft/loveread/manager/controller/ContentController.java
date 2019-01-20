package cn.neusoft.loveread.manager.controller;


import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.content.service.ContentService;
import cn.neusoft.loveread.pojo.TbContent;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    @ResponseBody
    @RequestMapping("/query/list")
    public EasyUIDataGridResult getContentListByCategoryId(Long categoryId, Integer page,Integer rows){
        return contentService.getContentListByCategoryId(categoryId,page,rows);
    }

    @RequestMapping("/save")
    @ResponseBody
    public LoveReadResult addContent(TbContent content){
        return contentService.addContent(content);
    }
}

