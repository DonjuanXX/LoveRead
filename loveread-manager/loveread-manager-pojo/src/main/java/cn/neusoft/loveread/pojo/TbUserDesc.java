package cn.neusoft.loveread.pojo;

import java.io.Serializable;
import java.util.Date;

public class TbUserDesc implements Serializable {
    private Long id;
    private Long userId;
    private Date created;
    private Date updated;
    private String nickName;
    private String phoneNum;
    private String descData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getDescData() {
        return descData;
    }

    public void setDescData(String descData) {
        this.descData = descData == null ? null : descData.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }
}
