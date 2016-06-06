package com.MDGround.HaiLanPrint;

import com.MDGround.HaiLanPrint.enumobject.OrderStatus;

/**
 * Created by yoghourt on 5/16/16.
 */
public enum ProductType {

    PrintPhoto(1),
    Postcard(2),
    MagazineAlbum(3),
    ArtAlbum(4),
    PictureFrame(5),
    Calendar(6),
    PhoneShell(7),
    Poker(8),
    Puzzle(9),
    MagicCup(10),
    LOMOCard(11),
    Engraving(12);

    private int value;

    private ProductType(int product) {
        this.value = product;
    }

    public int value() {
        return value;
    }

    public static ProductType fromValue(int value) {
        for (ProductType type : ProductType.values()) {
            if (type.value() == value) {
                return type;
            }
        }
        return null;
    }
}
