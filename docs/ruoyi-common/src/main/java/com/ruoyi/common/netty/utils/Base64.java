package com.ruoyi.common.netty.utils;

import com.gosft.tools.core.codec.Base64Decoder;
import com.gosft.tools.core.codec.Base64Encoder;
import com.gosft.tools.core.io.FileUtil;
import com.gosft.tools.core.io.IoUtil;
import com.gosft.tools.core.util.CharsetUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class Base64 {
    public static byte[] encode(byte[] arr, boolean lineSep) {
        return com.gosft.tools.core.codec.Base64Encoder.encode(arr, lineSep);
    }

    public static byte[] encodeUrlSafe(byte[] arr, boolean lineSep) {
        return com.gosft.tools.core.codec.Base64Encoder.encodeUrlSafe(arr, lineSep);
    }

    public static String encode(CharSequence source) {
        return com.gosft.tools.core.codec.Base64Encoder.encode(source);
    }

    public static String encodeUrlSafe(CharSequence source) {
        return com.gosft.tools.core.codec.Base64Encoder.encodeUrlSafe(source);
    }

    public static String encode(CharSequence source, String charset) {
        return com.gosft.tools.core.codec.Base64Encoder.encode(source, CharsetUtil.charset(charset));
    }

    public static String encodeUrlSafe(CharSequence source, String charset) {
        return com.gosft.tools.core.codec.Base64Encoder.encodeUrlSafe(source, CharsetUtil.charset(charset));
    }

    public static String encode(CharSequence source, Charset charset) {
        return com.gosft.tools.core.codec.Base64Encoder.encode(source, charset);
    }

    public static String encodeUrlSafe(CharSequence source, Charset charset) {
        return com.gosft.tools.core.codec.Base64Encoder.encodeUrlSafe(source, charset);
    }

    public static String encode(byte[] source) {
        return com.gosft.tools.core.codec.Base64Encoder.encode(source);
    }

    public static String encodeUrlSafe(byte[] source) {
        return com.gosft.tools.core.codec.Base64Encoder.encodeUrlSafe(source);
    }

    public static String encode(InputStream in) {
        return com.gosft.tools.core.codec.Base64Encoder.encode(IoUtil.readBytes(in));
    }

    public static String encodeUrlSafe(InputStream in) {
        return com.gosft.tools.core.codec.Base64Encoder.encodeUrlSafe(IoUtil.readBytes(in));
    }

    public static String encode(File file) {
        return com.gosft.tools.core.codec.Base64Encoder.encode(FileUtil.readBytes(file));
    }

    public static String encodeUrlSafe(File file) {
        return com.gosft.tools.core.codec.Base64Encoder.encodeUrlSafe(FileUtil.readBytes(file));
    }

    @Deprecated
    public static String encode(byte[] source, String charset) {
        return com.gosft.tools.core.codec.Base64Encoder.encode(source);
    }

    @Deprecated
    public static String encodeUrlSafe(byte[] source, String charset) {
        return com.gosft.tools.core.codec.Base64Encoder.encodeUrlSafe(source);
    }

    @Deprecated
    public static String encode(byte[] source, Charset charset) {
        return com.gosft.tools.core.codec.Base64Encoder.encode(source);
    }

    @Deprecated
    public static String encodeUrlSafe(byte[] source, Charset charset) {
        return com.gosft.tools.core.codec.Base64Encoder.encodeUrlSafe(source);
    }

    public static byte[] encode(byte[] arr, boolean isMultiLine, boolean isUrlSafe) {
        return Base64Encoder.encode(arr, isMultiLine, isUrlSafe);
    }

    public static String decodeStrGbk(CharSequence source) {
        return Base64Decoder.decodeStr(source, CharsetUtil.CHARSET_GBK);
    }

    public static String decodeStr(CharSequence source) {
        return Base64Decoder.decodeStr(source);
    }

    public static String decodeStr(CharSequence source, String charset) {
        return Base64Decoder.decodeStr(source, CharsetUtil.charset(charset));
    }

    public static String decodeStr(CharSequence source, Charset charset) {
        return Base64Decoder.decodeStr(source, charset);
    }

    public static File decodeToFile(CharSequence base64, File destFile) {
        return FileUtil.writeBytes(Base64Decoder.decode(base64), destFile);
    }

    public static void decodeToStream(CharSequence base64, OutputStream out, boolean isCloseOut) {
        IoUtil.write(out, isCloseOut, Base64Decoder.decode(base64));
    }

    public static byte[] decode(CharSequence base64) {
        return Base64Decoder.decode(base64);
    }

    @Deprecated
    public static byte[] decode(CharSequence source, String charset) {
        return Base64Decoder.decode(source);
    }

    @Deprecated
    public static byte[] decode(CharSequence source, Charset charset) {
        return Base64Decoder.decode(source);
    }

    public static byte[] decode(byte[] in) {
        return Base64Decoder.decode(in);
    }
}
