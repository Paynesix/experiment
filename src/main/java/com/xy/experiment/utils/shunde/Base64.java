//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xy.experiment.utils.shunde;

import java.io.UnsupportedEncodingException;
import org.apache.commons.lang3.StringUtils;

public class Base64 {
    public Base64() {
    }

    public static String encode(String data) {
        return data != null && data.length() != 0 ? encode(data.getBytes()) : null;
    }

    public static String encode(String data, String charset) throws UnsupportedEncodingException {
        return data != null && data.length() != 0 ? encode(data.getBytes(charset)) : null;
    }

    public static String encode(String data, boolean urlSafe) {
        return encode(data.getBytes(), urlSafe);
    }

    public static String encode(String data, String charset, boolean urlSafe) throws UnsupportedEncodingException {
        return encode(data.getBytes(charset), urlSafe);
    }

    public static String encode(byte[] bytes) {
        return bytes != null && bytes.length != 0 ? org.apache.commons.codec.binary.Base64.encodeBase64String(bytes) : null;
    }

    public static String encode(byte[] bytes, boolean urlSafe) {
        if (bytes != null && bytes.length != 0) {
            return urlSafe ? org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(bytes) : org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
        } else {
            return null;
        }
    }

    public static byte[] decode(String data) {
        return data != null && data.length() != 0 ? org.apache.commons.codec.binary.Base64.decodeBase64(data) : null;
    }

    public static String decodeStr(String data) {
        byte[] deData = decode(data);
        return deData == null ? null : new String(deData);
    }

    public static String decodeStr(String data, String decodeCharset) throws UnsupportedEncodingException {
        byte[] deData = decode(data);
        return deData == null ? null : new String(deData, decodeCharset);
    }

    public static String formatBase64Str(String srcText) {
        if (StringUtils.isBlank(srcText)) {
            return null;
        } else {
            int strLen = srcText.length();
            int maxLen = strLen + strLen / 76 * 2 + 10;
            StringBuilder sb = new StringBuilder(maxLen);
            int count = 0;

            for(int i = 0; i < strLen; ++i) {
                char ch = srcText.charAt(i);
                if (ch != '\r' && ch != '\n') {
                    ++count;
                    if ((count - 1) % 76 == 0 && count > 1) {
                        sb.append("\r\n");
                    }

                    sb.append(ch);
                }
            }

            return sb.toString();
        }
    }

    public static void main(String[] args) {
        try {
            String b64_lt_76 = "MIICdwIB";
            System.out.println(formatBase64Str(b64_lt_76));
            String b64_eq_76 = "rph/IUimDCuE8IoLGwGSx2XSLo6DBYC8+qEk59ZMsBFNeNNrSzPY+7+8e4aftyZI+VuSsc4sGKqd";
            System.out.println(formatBase64Str(b64_eq_76));
            String b64_gt_76 = "rnF44XL4/MY7VyrtKL8kgh2/zTo2m2MD3YmTdHVOtMWFNzkA6IVDzGYQgSUPkr9r30p1AgMBAAEC1234";
            System.out.println(formatBase64Str(b64_gt_76));
            String b64_gt_762 = "gYEAopb3KV0nFzUHcUl/BEEuSsCrwqra8vt2kP1RupXHBySqXFl4phI7wf1mzw8oRQHEOhSddAuky+4D+2hy/6v9Q5Kr+Tk8HWi8kNjVlbiGhgvgh7SRzjjfRxSGZcaKHKa36Db0ZpFLuTSBx50vF7I5";
            System.out.println(formatBase64Str(b64_gt_762));
            System.out.println("===end===");
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }
}
