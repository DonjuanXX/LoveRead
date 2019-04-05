package cn.neusoft.loveread.sso.service.impl;


import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.manager.mapper.TbUserFavoriteMapper;
import cn.neusoft.loveread.manager.mapper.TbUserMapper;
import cn.neusoft.loveread.pojo.TbUser;
import cn.neusoft.loveread.pojo.TbUserFavorite;
import cn.neusoft.loveread.sso.service.UserService;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private TbUserFavoriteMapper favoriteMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${REDIS_SESSION_KEY}")
    private String REDIS_SESSION_KEY;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;


    @Override
    public LoveReadResult checkData(String param, Integer type) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("param", param);
        paramMap.put("type", type);
        List<TbUser> tbUsers = userMapper.selectByKey(paramMap);
        if (tbUsers == null || tbUsers.isEmpty()) {
            return LoveReadResult.ok(true);
        } else {
            return LoveReadResult.ok(false);
        }
    }

    @Override
    public LoveReadResult register(TbUser tbUser) {
        return registerMethod(tbUser);
    }

    @Override
    public LoveReadResult login(String username, String password) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", username);
        List<TbUser> users = userMapper.selectUserByNameOrPwd(paramMap);
        if (users == null || users.isEmpty()) {
            return LoveReadResult.build(400, "该用户不存在");
        }
        TbUser tbUser = users.get(0);
        //校验密码
        if (!tbUser.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return LoveReadResult.build(400, "密码错误");
        }
        //登陆成功
        String token = UUID.randomUUID().toString();
        tbUser.setPassword(null);
        redisTemplate.opsForValue().set(REDIS_SESSION_KEY + ":" + token, tbUser);
        redisTemplate.expire(REDIS_SESSION_KEY + ":" + token, SESSION_EXPIRE, TimeUnit.MINUTES);
        return LoveReadResult.ok(token);
    }

    @Override
    public LoveReadResult getUserByToken(String token) {
        TbUser tbUser = (TbUser) redisTemplate.opsForValue().get(REDIS_SESSION_KEY + ":" + token);
        if (tbUser == null) {
            return LoveReadResult.build(201, "用户登录信息已经过期");
        }
        redisTemplate.expire(REDIS_SESSION_KEY + ":" + token, SESSION_EXPIRE, TimeUnit.MINUTES);
        return LoveReadResult.ok(tbUser);
    }

    @Override
    public LoveReadResult setFavorite(String favorite, TbUser tbUser) {
        LoveReadResult result = registerMethod(tbUser);
        if(!result.isOK())
            return result;
        TbUserFavorite tbUserFavorite = new TbUserFavorite();
        tbUserFavorite.setParamData(favorite);
        tbUserFavorite.setCreated(new Date());
        tbUserFavorite.setUpdated(new Date());
        tbUserFavorite.setUserId(tbUser.getId());
        favoriteMapper.insert(tbUserFavorite);
        return LoveReadResult.ok();
    }

    @Override
    public LoveReadResult updateFavorite(String favorite,Long id) {
        TbUserFavorite tbUserFavorite = new TbUserFavorite();
        tbUserFavorite.setUserId(id);
        tbUserFavorite.setParamData(favorite);
        tbUserFavorite.setUpdated(new Date());
        tbUserFavorite.setCreated(new Date());
        favoriteMapper.update(tbUserFavorite);
        return LoveReadResult.ok();
    }

    @Override
    public String getFavoriteById(Long id) {
        return favoriteMapper.getFavoriteById(id);
    }

    private LoveReadResult registerMethod(TbUser tbUser) {
        //校验数据
        if (StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPassword())) {
            return LoveReadResult.build(400, "用户名或密码不能为空");
        }
        LoveReadResult readResult = checkData(tbUser.getUsername(), 1);
        if (!(boolean) readResult.getData()) {
            return LoveReadResult.build(400, "用户名重复");
        }
        if (tbUser.getPhone() != null) {
            readResult = checkData(tbUser.getPhone(), 2);
            if (!(boolean) readResult.getData()) {
                return LoveReadResult.build(400, "手机号重复");
            }
        }
        if (tbUser.getEmail() != null) {
            readResult = checkData(tbUser.getEmail(), 3);
            if (!(boolean) readResult.getData()) {
                return LoveReadResult.build(400, "邮箱重复");
            }
        }
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
        userMapper.insert(tbUser);
        return LoveReadResult.ok();
    }
}
