package com.MDGround.HaiLanPrint.enumobject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yoghourt on 5/16/16.
 */
public enum ProductMaterial {

    PrintPhoto_Glossy("光面"),
    PrintPhoto_Matte("绒面");

    private String text;

    private ProductMaterial(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        return text;
    }

    // Implementing a fromString method on an enum type
    private static final Map<String, ProductMaterial> stringToEnum = new HashMap<String, ProductMaterial>();

    static {
        // Initialize map from constant name to enum constant
        for (ProductMaterial productMaterial : values()) {
            stringToEnum.put(productMaterial.toString(), productMaterial);
        }
    }

    // Returns Blah for string, or null if string is invalid
    public static ProductMaterial fromString(String symbol) {
        return stringToEnum.get(symbol);
    }
}
