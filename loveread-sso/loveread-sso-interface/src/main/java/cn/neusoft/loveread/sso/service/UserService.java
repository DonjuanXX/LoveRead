package cn.neusoft.loveread.sso.service;

import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.pojo.TbItem;
import cn.neusoft.loveread.pojo.TbUser;
import cn.neusoft.loveread.pojo.TbUserDesc;

public interface UserService {
    LoveReadResult checkData(String param, Integer type);

    LoveReadResult register(TbUser tbUser);

    LoveReadResult login(String username, String password);

    LoveReadResult getUserByToken(String token);

    LoveReadResult setFavorite(String favorite, TbUser tbUser);

    LoveReadResult updateFavorite(String favorite, Long id);

    String getFavoriteById(Long id);

    LoveReadResult addHome(TbUserDesc tbUserDesc);

    LoveReadResult getDescById(Long id);

    EasyUIDataGridResult getUserList(Integer page, Integer rows);

    LoveReadResult update(TbUser user);

    void delete(Long id);

}
