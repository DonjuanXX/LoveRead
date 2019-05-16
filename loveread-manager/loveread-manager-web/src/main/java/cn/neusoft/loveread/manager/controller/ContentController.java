package cn.neusoft.loveread.manager.controller;


import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.content.service.ContentService;
import cn.neusoft.loveread.pojo.TbContent;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    @ResponseBody
    @RequestMapping("/delete")
    public LoveReadResult deleteContent(@RequestBody String id) {
        String[] ids = id.split("=", 2);
        String[] target = ids[1].split("%2C");
        int frequency = target.length;
        while (frequency-- != 0) {
            long Id = Long.parseLong(target[frequency]);
            TbContent content = contentService.getContentById(Id);
            contentService.deleteContent(content);
        }
        return LoveReadResult.ok();
    }

    @RequestMapping("/edit")
    @ResponseBody
    public LoveReadResult updateContent(TbContent content){//只传递一个id
        TbContent target = contentService.getContentById(content.getId());
        content.setCategoryId(target.getCategoryId());
        content.setCreated(target.getCreated());
        return contentService.updateContent(content);
    }

}

