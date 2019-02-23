package cn.neusoft.loveread.item.message;

import cn.neusoft.loveread.item.pojo.Item;
import cn.neusoft.loveread.manager.service.TbItemService;
import cn.neusoft.loveread.pojo.TbItem;
import cn.neusoft.loveread.pojo.TbItemDesc;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 接收后台添加商品消息，生成商品详情页面
 */
@Component
public class GeneratePageMessageReceiver {
    @Reference
    private TbItemService itemService;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;
    @Value("${TEMPLATE_NAME}")
    private String TEMPLATE_NAME;
    @Value("${TEMPLATE_FILEPATH}")
    private String TEMPLATE_FILEPATH;

    @JmsListener(destination = "itemAddTopic", containerFactory = "jmsTopicListenerContainerFactory")
    public void itemAddReceiver(Long itemId) {
        try {
            //等待一秒让manager-service提交完事物
            Thread.sleep(1000);
            TbItem tbItem = itemService.getItemById(itemId);
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            Item item = new Item(tbItem);
            //构造上下文Model
            Context context = new Context();
            context.setVariable("item", item);
            context.setVariable("itemDesc", itemDesc);
            //生成页面
            FileWriter writer = new FileWriter(TEMPLATE_FILEPATH + itemId + ".html");
            springTemplateEngine.process(TEMPLATE_NAME, context, writer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
