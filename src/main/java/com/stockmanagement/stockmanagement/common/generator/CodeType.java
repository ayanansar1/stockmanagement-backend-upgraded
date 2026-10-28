package com.stockmanagement.stockmanagement.common.generator;


import lombok.Getter;

@Getter
public enum CodeType {

    KAREEGAR("KC-"),
    BUYER("BY-"),
    PRODUCT("PC-"),
    SALE("SC-"),
    RETURN("RT-"),
    DESIGN("DC-"),
    DESIGN_ADD("DA-"),
    BANA("BC-"),
    MACHINE("MC-"),
    BANA_KAREEGAR("BK-"),
    DESIGN_KAREEGAR("DK-"),
    PRODUCT_KAREEGAR("PK-"),
    PRODUCT_ADD("PA"),
    STOCK_MOVEMENT("SM-"),
    PRODUCT_SELL("PS-"),
    INVOICE("INV-"),
    PRODUCT_RETURN("PR-"),
    PRODUCT_LISTING("PL-"),
    STOCK_BALANCE("SB-");



    private final String prefix;

    CodeType(String prefix) {
        this.prefix = prefix;
    }
}