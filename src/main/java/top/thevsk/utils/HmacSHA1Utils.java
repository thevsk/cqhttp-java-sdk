package top.thevsk.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA1Utils {

    public static String hmacSha1(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String str;
        for (int n = 0; b != null && n < b.length; n++) {
            str = Integer.toHexString(b[n] & 0XFF);
            if (str.length() == 1)
                hs.append('0');
            hs.append(str);
        }
        return hs.toString().toUpperCase();
    }
}
