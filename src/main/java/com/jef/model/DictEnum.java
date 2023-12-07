package com.jef.model;

/**
 * 枚举出处理的相应状态
 *
 * @author Jef
 * @date 2019/9/8
 */
public enum DictEnum {
    /**
     * 第一个状态
     */
    ONE_TYPE("typeOne", "第一个类型"),
    /**
     * 第二个状态
     */
    TWO_TYPE("typeTwo", "第二个类型");
    /**
     * 类型
     */
    private String type;
    /**
     * 类型描述
     */
    private String typeDescription;

    DictEnum(String type, String typeDescription) {
        this.type = type;
        this.typeDescription = typeDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    /**
     * 通过状态值获取对应的状态
     *
     * @param type 这里使用Integer，允许放入空值进来
     * @return
     */
    public static DictEnum getByType(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        for (DictEnum dictEnum : DictEnum.values()) {
            if (dictEnum.getType().equals(type)) {
                return dictEnum;
            }
        }
        return null;
    }

}