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
    // glide图片缓存文件夹
    public static String GLIDE_DISK_CACHE_FILE_NAME = "Glide_cache_image";

    // 数据库名字
    public static final String DATABASE_NAME = "mdground";
    //照片储存文件夹
    public static final String PHOTO_FILE = "/baoprint";
    //临时相机拍完后照片的名字
    public static final String PHOTO_NAME = "textbaoprint.jpg";
    // 短信接口参数
    public static final String SMS_APP_KEY = "fdd3cf873a60";
    public static final String SMS_APP_SECRECT = "bb31da16613ce49ba8cb815ccf5afe7d";

    public static final String KEY_IS_FIRST_LAUNCH = "key_is_first_launch";

    // application
    public static final String KEY_ALREADY_LOGIN_USER = "key_already_login_user";
    public static final String KEY_ALREADY_CHOOSED_PRODUCT_TYPE = "key_already_choosed_product_type";
    public static final String KEY_ALREADY_CHOOSED_MEASUREMENT = "key_already_choosed_measurement";
    public static final String KEY_ALREADY_CHOOSED_TEMPLATE = "key_already_choosed_template";
    public static final String KEY_PHOTO_TYPE_EXPLAIN_ARRAYLIST = "key_photo_type_explain_arraylist";

    public static final String KEY_NEW_USER = "key_new_user";
    public static final String KEY_PHONE = "key_phone";
    public static final String KEY_CLOUD_IMAGE = "key_cloud_image";
    public static final String KEY_SELECT_IMAGE = "key_select_image";
    public static final String KEY_ALBUM = "key_album";
    public static final String KEY_DELIVERY_ADDRESS = "key_delivery_address";
    public static final String KEY_MAX_IMAGE_NUM = "key_max_image_num";
    public static final String KEY_ORDER_WORK = "key_order_work";
    public static final String KEY_ORDER_WORK_LIST = "key_order_work_list";
    public static final String KEY_ORDER_INFO = "key_order_info";
    public static final String KEY_SELECTED_COUPON = "key_selected_coupon";
    public static final String KEY_ORDER_AMOUNT_FEE = "key_order_amount_fee";
    public static final String KEY_COUPON_LIST = "key_coupon_list";
    public static final String KEY_FROM_PAYMENT_SUCCESS = "key_from_payment_success";
    public static final String KEY_WORKS_DETAILS = "key_from_works_details";
    public static int MAX_DELIVERY_ADDRESS = 10; // 最多十个收货地址

    public static int ITEM_LEFT_TO_LOAD_MORE = 3;

    public static int PRINT_PHOTO_MAX_SELECT_IMAGE_NUM = 100; // 冲印相片最多选择10张
    public static int PICTURE_FRAME_MAX_SELECT_IMAGE_NUM = 1; // 相框
    public static int MAGIC_CUP_MAX_SELECT_IMAGE_NUM = 1; // 魔术杯最多选择一张
    public static int PUZZLEL_MAX_SELECT_IMAGE_NUM = 1; // 拼图最多选择一张
    public static int PHONE_SHELL_MAX_SELECT_IMAGE_NUM = 1; // 手机壳
    public static int ENGRAVING_MAX_SELECT_IMAGE_NUM = 100; // 版画

}
