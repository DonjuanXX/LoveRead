package cn.neusoft.loveread.sso.service;

import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.pojo.TbUser;

public interface UserService {
    LoveReadResult checkData(String param, Integer type);
    LoveReadResult register(TbUser tbUser);
    LoveReadResult login(String username, String password);
    LoveReadResult getUserByToken(String token);
    LoveReadResult setfavorite(String favorite);

}
