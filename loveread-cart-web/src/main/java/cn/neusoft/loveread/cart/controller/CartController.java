package cn.neusoft.loveread.cart.controller;

import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.common.utils.CookieUtils;
import cn.neusoft.loveread.common.utils.JsonUtils;
import cn.neusoft.loveread.cart.service.CartService;
import cn.neusoft.loveread.manager.service.TbItemService;
import cn.neusoft.loveread.pojo.TbItem;
import cn.neusoft.loveread.pojo.TbUser;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Value("${Loveread_CART}")
    private String Loveread_CART;
    @Value("${CART_EXPIRE}")
    private Integer CART_EXPIRE;

    @Reference
    private TbItemService itemService;
    @Reference
    private CartService cartService;

    @RequestMapping("/add/{itemId}.html")
    public String addCartItem(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                              HttpServletRequest request, HttpServletResponse response) {
        // 登陆添加至redis
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.addCart(user.getId(), itemId, num);
            return "cartSuccess";
        }
        //未登录、从cookie中查询商品列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //判断商品在商品列表中是否存在
        boolean hasItem = false;
        for (TbItem tbItem : cartList) {
            //对象比较的是地址，应该是值的比较
            if (tbItem.getId() == itemId.longValue()) {
                // 商品数量相加
                tbItem.setNum(tbItem.getNum() + num);
                hasItem = true;
                break;
            }
        }
        if (!hasItem) {
            // 新商品 获取信息
            TbItem tbItem = itemService.getItemById(itemId);
            String image = tbItem.getImage();
            if (StringUtils.isNoneBlank(image)) {
                String[] images = image.split(",");
                tbItem.setImage(images[0]);
            }
            tbItem.setNum(num);
            cartList.add(tbItem);
        }
        //写入cookie
        CookieUtils.setCookie(request, response, Loveread_CART, JsonUtils.objectToJson(cartList), CART_EXPIRE, true);
        return "cartSuccess";
    }

    /**
     * 从cookie中取购物车列表
     */
    private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
        //取购物车列表
        String json = CookieUtils.getCookieValue(request, Loveread_CART, true);
        //判断是否为null
        if (StringUtils.isNoneBlank(json)) {
            //把json转换成商品列表返回
            List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
            return list;
        }
        return new ArrayList<>();
    }

    @RequestMapping("/cart.html")
    public String showCartList(HttpServletRequest request, HttpServletResponse response) {
        //取cookie购物车商品列表
        List<TbItem> cartList = getCartListFromCookie(request);
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            //合并购物车
            cartService.mergeCart(user.getId(), cartList);
            //删除cookie残余信息
            CookieUtils.deleteCookie(request, response, Loveread_CART);
            cartList = cartService.getCartList(user.getId());
        }
        //传递给页面
        request.setAttribute("cartList", cartList);
        return "cart";
    }

    /**
     * 更新购物车列表
     */
    @RequestMapping("/update/num/{itemId}/{num}.action")
    @ResponseBody
    public LoveReadResult updateNum(@PathVariable Long itemId, @PathVariable Integer num,
                                    HttpServletRequest request, HttpServletResponse response) {
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            //登陸要更新
            return cartService.updateCartNum(user.getId(), itemId, num);
        }
        //从cookie中取商品列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //找对应商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                //更新商品数量
                tbItem.setNum(num);
            }
        }
        //写入cookie
        CookieUtils.setCookie(request, response, Loveread_CART, JsonUtils.objectToJson(cartList), CART_EXPIRE, true);
        //响应
        return LoveReadResult.ok();
    }

    @RequestMapping("/delete/{itemId}.html")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
                                 HttpServletResponse response) {
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.deleteCartItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
        //从url取出id，从cookie中去购物车商品列表
        List<TbItem> cartList = getCartListFromCookie(request);
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                cartList.remove(tbItem);
                break;
            }
        }
        CookieUtils.setCookie(request, response, Loveread_CART, JsonUtils.objectToJson(cartList), CART_EXPIRE, true);
        return "redirect:/cart/cart.html";
    }

    @RequestMapping("/delete/all.html")
    public String deleteCart(HttpServletRequest request, HttpServletResponse response) {
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.clearCartList(user.getId());
            return "redirect:/cart/cart.html";
        }
        //从url取出id，从cookie中去购物车商品列表
        List<TbItem> cartList = getCartListFromCookie(request);
        cartList.clear();
        CookieUtils.setCookie(request, response, Loveread_CART, JsonUtils.objectToJson(cartList), CART_EXPIRE, true);
        return "redirect:/cart/cart.html";
    }

}
