package cn.neusoft.loveread.search.controller;

import cn.neusoft.loveread.common.pojo.SearchResult;
import cn.neusoft.loveread.search.service.SearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Reference
    private SearchService searchService;
    @Value("${PAGE_ROWS}")
    private Integer PAGE_ROWS;

    @RequestMapping("/search.html")
    public String search(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
        //调用Service查询商品信息
        SearchResult result = searchService.search(keyword, page, PAGE_ROWS);
        //把结果传递给jsp页面
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("recourdCount", result.getRecourdCount());
        model.addAttribute("page", page);
        model.addAttribute("itemList", result.getItemList());
        //返回逻辑视图
        return "search";
    }

    @RequestMapping("/list.html")
    public String list(Long cid, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception{
        SearchResult result = searchService.list(cid, page, PAGE_ROWS);
        String category = searchService.getNameByCid(cid);
        model.addAttribute("query", cid);
        model.addAttribute("name", category);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("recourdCount", result.getRecourdCount());
        model.addAttribute("page", page);
        model.addAttribute("itemList", result.getItemList());
        return "list";
    }
}
