package com.MDGround.HaiLanPrint.enumobject;

/**
 * Created by yoghourt on 5/16/16.
 */
public enum OrderStatus {

    All(-1),            // 全部
    UnPaid(0),          // 未支付
    Paid(1),            // 已付款
    Delivered(2),       // 已发货
    Finished(4),        // 已完成
    Refunding(8),       // 退款中
    Refunded(16),       // 已退款
    RefundFail(32),     // 退款失败
    AllRefund(56);      // 所有退款订单(退款中 + 已退款 + 退款失败 = (8 + 16 + 32 = 56))

    private int value;

    private OrderStatus(int product) {
        this.value = product;
    }

    public int value() {
        return value;
    }

    public static OrderStatus fromValue(int value) {
        for (OrderStatus type : OrderStatus.values()) {
            if (type.value() == value) {
                return type;
            }
        }
        return null;
    }
}
