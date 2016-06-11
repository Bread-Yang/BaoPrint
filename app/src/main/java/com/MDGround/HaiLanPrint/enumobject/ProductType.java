package com.MDGround.HaiLanPrint;

import com.MDGround.HaiLanPrint.application.MDGroundApplication;
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

    public static String getProductName(ProductType productType) {
        switch (productType) {
            case PrintPhoto:
                return MDGroundApplication.mInstance.getString(R.string.print_photo);
            case Postcard:
                return MDGroundApplication.mInstance.getString(R.string.postcard);
            case MagazineAlbum:
                return MDGroundApplication.mInstance.getString(R.string.magazine_album);
            case ArtAlbum:
                return MDGroundApplication.mInstance.getString(R.string.art_album);
            case PictureFrame:
                return MDGroundApplication.mInstance.getString(R.string.picture_frame);
            case Calendar:
                return MDGroundApplication.mInstance.getString(R.string.calendar);
            case PhoneShell:
                return MDGroundApplication.mInstance.getString(R.string.phone_shell);
            case Poker:
                return MDGroundApplication.mInstance.getString(R.string.poker);
            case Puzzle:
                return MDGroundApplication.mInstance.getString(R.string.puzzle);
            case MagicCup:
                return MDGroundApplication.mInstance.getString(R.string.magic_cup);
            case LOMOCard:
                return MDGroundApplication.mInstance.getString(R.string.lomo_card);
            case Engraving:
                return MDGroundApplication.mInstance.getString(R.string.engraving);

        }
        return null;
    }
}
