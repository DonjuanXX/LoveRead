package cn.neusoft.loveread.manager.controller;


import cn.neusoft.loveread.common.pojo.EasyUITreeNode;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.content.service.ContentCategoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Reference
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(
            @RequestParam(value = "id", defaultValue = "0") Long parentId) {
        return contentCategoryService.getContentCategoryList(parentId);
    }

    @RequestMapping("/create")
    @ResponseBody
    public LoveReadResult createCategory(Long parentId, String name) {
        return contentCategoryService.addContentCategory(parentId, name);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public LoveReadResult deleteCategory(Long id) {
        return contentCategoryService.deleteContentCategory(id);
    }

    @RequestMapping("/update")
    @ResponseBody
    public LoveReadResult updateCategory(Long id, String name) {
        return contentCategoryService.updateContentCategoryReal(id, name);
    }
}
