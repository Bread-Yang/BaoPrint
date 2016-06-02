package com.MDGround.HaiLanPrint.constants;

/**
 * Created by yoghourt on 5/5/16.
 */
public final class Constants {

    public static final String HOST = "http://psuat.yideguan.com/";
//    public static final String HOST = "http://192.168.0.152:9999/";

    public static final String FILE_HOST = "http://psuat.yideguan.com/";
//    public static final String FILE_HOST = "http://192.168.0.152:9999/";

    // APP在sdcar的目录
    public static final String APP_PATH = "/mdground_hailan_print";
    // Preference文件名
    public static String PREFERENCE = "hailan_print";

    // 数据库名字
    public static final String DATABASE_NAME = "mdground";

    // 短信接口参数
    public static final String SMS_APP_KEY = "fdd3cf873a60";
    public static final String SMS_APP_SECRECT = "bb31da16613ce49ba8cb815ccf5afe7d";

    public static String KEY_ALREADY_LOGIN_USER = "key_already_login_user";
    public static final String KEY_NEW_USER = "key_new_user";
    public static final String KEY_PHONE = "key_phone";
    public static final String KEY_CLOUD_IMAGE = "key_cloud_image";
    public static final String KEY_ALBUM = "key_album";
    public static final String KEY_DELIVERY_ADDRESS = "key_delivery_address";
    public static final String KEY_MAX_IMAGE_NUM = "key_max_image_num";

    public static int MAX_DELIVERY_ADDRESS = 10; // 最多十个收货地址

    public static int ITEM_LEFT_TO_LOAD_MORE = 3;

    public static int PRINT_PHOTO_MAX_SELECT_IMAGE_NUM = 100; // 冲印相片最多选择100张
    public static int POSTCARD_MAX_SELECT_IMAGE_NUM = 8; // 明信片
    public static int PICTURE_FRAME_MAX_SELECT_IMAGE_NUM = 1; // 相框
    public static int MAGIC_CUP_MAX_SELECT_IMAGE_NUM = 1; // 魔术杯最多选择一张
    public static int PUZZLEL_MAX_SELECT_IMAGE_NUM = 1; // 拼图最多选择一张
    public static int LOMO_CARD_MAX_SELECT_IMAGE_NUM = 16; // lomo卡
    public static int PHONE_SHELL_MAX_SELECT_IMAGE_NUM = 1; // 手机壳
    public static int ENGRAVING_MAX_SELECT_IMAGE_NUM = 100; // 版画
}
