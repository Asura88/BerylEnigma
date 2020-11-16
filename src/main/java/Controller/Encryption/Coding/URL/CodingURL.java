package Controller.Encryption.Coding.URL;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CodingURL {
    public static String deCode(String in,String charset) throws UnsupportedEncodingException {
        return URLDecoder.decode(in,charset);
    }

    public static String enCode(String in,String charset) throws UnsupportedEncodingException {
        return URLEncoder.encode(in,charset).replaceAll("\\+","%20");
    }
}
