package cn.neusoft.loveread.cart.interceptor;

import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.common.utils.CookieUtils;
import cn.neusoft.loveread.pojo.TbUser;
import cn.neusoft.loveread.sso.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Reference
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        //从Cookie取出token
        String loveread_token = CookieUtils.getCookieValue(request,"LOVEREAD_TOKEN");
        if(StringUtils.isBlank(loveread_token)){
            return true;
        }
        LoveReadResult loveReadResult = userService.getUserByToken(loveread_token);
        if (loveReadResult.getStatus()!=200){
            return true;
        }
        request.setAttribute("user",(TbUser) loveReadResult.getData());
        return true;

    }
}
