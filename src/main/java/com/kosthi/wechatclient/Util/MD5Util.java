package com.kosthi.wechatclient.Util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String encrypt(String s) {
        return DigestUtils.md5Hex(s);
    }
}
