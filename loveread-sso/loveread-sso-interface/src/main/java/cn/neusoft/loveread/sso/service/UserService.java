package cn.neusoft.loveread.sso.service;

import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.pojo.TbUser;

public interface UserService {
    LoveReadResult checkData(String param, Integer type);

    LoveReadResult register(TbUser tbUser);

    LoveReadResult login(String username, String password);

    LoveReadResult getUserByToken(String token);

    LoveReadResult setFavorite(String favorite, TbUser tbUser);

    LoveReadResult updateFavorite(String favorite, Long id);

    String getFavoriteById(Long id);
}
