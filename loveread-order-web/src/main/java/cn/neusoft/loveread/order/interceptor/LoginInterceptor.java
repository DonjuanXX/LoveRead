package cn.neusoft.loveread.order.interceptor;

import cn.neusoft.loveread.cart.service.CartService;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.common.utils.CookieUtils;
import cn.neusoft.loveread.common.utils.JsonUtils;
import cn.neusoft.loveread.pojo.TbItem;
import cn.neusoft.loveread.pojo.TbUser;
import cn.neusoft.loveread.sso.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义登陆拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Reference
    private UserService userService;
    @Reference
    private CartService cartService;
    @Value("${SSO_SERVICE_URL}")
    private String SSO_SERVICE_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String Loveread_token = CookieUtils.getCookieValue(request, "LOVEREAD_TOKEN");
        if (StringUtils.isBlank(Loveread_token)) {
            response.sendRedirect(SSO_SERVICE_URL + "/page/login?redirect=" + request.getRequestURI());
            return false;
        }
        //通过token调用sso取出用户信息
        LoveReadResult loveReadResult = userService.getUserByToken(Loveread_token);
        if (loveReadResult.getStatus() != 200) {
            response.sendRedirect(SSO_SERVICE_URL + "/page/login?redirect=" + request.getRequestURI());
            return false;
        }
        TbUser user = (TbUser) loveReadResult.getData();
        request.setAttribute("user", user);
        String jsonCartList = CookieUtils.getCookieValue(request, "Loveread_CART", true);
        if (StringUtils.isNotBlank(jsonCartList)) {
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
        }
        return true;
    }
}
