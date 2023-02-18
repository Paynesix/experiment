//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xy.experiment.utils.shunde;

public class HexCode {
    private static final char[] UPPER_HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] LOWER_HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public HexCode() {
    }

    public static char[] encode2char(byte[] bytes, boolean lowercase) {
        char[] chars = new char[bytes.length * 2];

        for(int i = 0; i < chars.length; i += 2) {
            byte b = bytes[i / 2];
            char[] HEX_CHARS = LOWER_HEX_CHARS;
            if (!lowercase) {
                HEX_CHARS = UPPER_HEX_CHARS;
            }

            chars[i] = HEX_CHARS[b >>> 4 & 15];
            chars[i + 1] = HEX_CHARS[b & 15];
        }

        return chars;
    }

    public static String encode(byte[] bytes, boolean lowercase) {
        char[] endata = encode2char(bytes, lowercase);
        return new String(endata);
    }

    public static byte[] decode(String data) throws Exception {
        int len = (data.length() + 1) / 2;
        byte[] bytes = new byte[len];
        int index = 0;

        for(int i = 1; i < data.length(); i += 2) {
            char h = data.charAt(i - 1);
            char l = data.charAt(i);
            bytes[index] = decodeByte(h, l);
            ++index;
        }

        return bytes;
    }

    public static byte decodeByte(char hight, char low) throws Exception {
        int value;
        byte data;
        if (hight >= 'A' && hight <= 'F') {
            value = hight - 65 + 10;
            data = (byte)(value << 4);
        } else if (hight >= 'a' && hight <= 'f') {
            value = hight - 97 + 10;
            data = (byte)(value << 4);
        } else {
            if (hight < '0' || hight > '9') {
                throw new Exception("解码错误");
            }

            value = hight - 48;
            data = (byte)(value << 4);
        }

        if (low >= 'A' && low <= 'F') {
            value = low - 65 + 10;
            data |= (byte)value;
        } else if (low >= 'a' && low <= 'f') {
            value = low - 97 + 10;
            data |= (byte)value;
        } else {
            if (low < '0' || low > '9') {
                throw new Exception("解码错误");
            }

            value = low - 48;
            data |= (byte)value;
        }

        return data;
    }
}
