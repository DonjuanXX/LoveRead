package cn.neusoft.loveread.common.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.Serializable;
import java.util.List;

public class LoveReadResult implements Serializable {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    //响应业务状态
    private Integer status;
    //响应消息
    private String msg;
    //响应中的数据
    private Object data;

    public LoveReadResult() {
    }

    public LoveReadResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public LoveReadResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public static LoveReadResult ok(Object data) {
        return new LoveReadResult(data);
    }

    public static LoveReadResult ok() {
        return new LoveReadResult(null);
    }

    public static LoveReadResult build(Integer status, String msg, Object data) {
        return new LoveReadResult(status, msg, data);
    }

    public static LoveReadResult build(Integer status, String msg) {
        return new LoveReadResult(status, msg, null);
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 将json结果集转化为LoveReadResult对象
     *
     * @param jsonData json数据
     * @param clazz    result中的object类型
     */

    public static LoveReadResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, LoveReadResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     *
     * @param json json
     */
    public static LoveReadResult format(String json) {
        try {
            return MAPPER.readValue(json, LoveReadResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合转化
     *
     * @param jsonData json
     * @param clazz    集合中的类型
     */
    public static LoveReadResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object object = null;
            if (data.isArray() && data.size() > 0) {
                object = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), object);
        } catch (Exception e) {
            return null;
        }
    }
}
