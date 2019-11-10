package club.spc.filters.core;

/**
 * @ClassName:FilterTypesEnum
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 02:12
 * @Version 1.0.0
 **/
public enum FilterTypesEnum {

    PRE(1, "pre_excutor"),
    SIDE_EFFECT(2, "side_effect_excutor"),
    POST(3, "post_excutor"),
    ERROR(4, "error_excutor");

    private int typeCode;
    private String typeName;

    FilterTypesEnum(int typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }
}
