package com.lwc.common.module.bean;

import java.util.Comparator;

public class SortByValid implements Comparator<PackageBean> {

    @Override
    public int compare(PackageBean o1, PackageBean o2) {
        return o2.getIsValid() - o1.getIsValid();
    }
}