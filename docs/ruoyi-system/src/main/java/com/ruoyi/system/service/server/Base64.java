package com.ruoyi.system.service.server;

import sun.misc.BASE64Decoder;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * A very fast and memory efficient class to encode and decode to and from
 * BASE64 in full accordance with RFC 2045.<br>
 * <br>
 * On Windows XP sp1 with 1.4.2_04 and later ;), this encoder and decoder is
 * about 10 times faster on small arrays (10 - 1000 bytes) and 2-3 times as fast
 * on larger arrays (10000 - 1000000 bytes) compared to
 * <code>sun.misc.Encoder()/Decoder()</code>.<br>
 * <br>
 * On byte arrays the encoder is about 20% faster than Jakarta Commons Base64
 * Codec for encode and about 50% faster for decoding large arrays. This
 * implementation is about twice as fast on very small arrays (&lt 30 bytes). If
 * source/destination is a <code>String</code> this version is about three
 * times as fast due to the fact that the Commons Codec result has to be recoded
 * to a <code>String</code> from <code>byte[]</code>, which is very
 * expensive.<br>
 * <br>
 * This encode/decode algorithm doesn't create any temporary arrays as many
 * other codecs do, it only allocates the resulting array. This produces less
 * garbage and it is possible to handle arrays twice as large as algorithms that
 * create a temporary array. (E.g. Jakarta Commons Codec). It is unknown whether
 * Sun's <code>sun.misc.Encoder()/Decoder()</code> produce temporary arrays
 * but since performance is quite low it probably does.<br>
 * <br>
 * The encoder produces the same output as the Sun one except that the Sun's
 * encoder appends a trailing line separator if the last character isn't a pad.
 * Unclear why but it only adds to the length and is probably a side effect.
 * Both are in conformance with RFC 2045 though.<br>
 * Commons codec seem to always att a trailing line separator.<br>
 * <br>
 * <b>Note!</b> The encode/decode method pairs (types) come in three versions
 * with the <b>exact</b> same algorithm and thus a lot of code redundancy. This
 * is to not create any temporary arrays for transcoding to/from different
 * format types. The methods not used can simply be commented out. If you find
 * the code useful or you find a bug, please send me a note at base64 @ miginfocom .
 * com. Licence (BSD): ============== Copyright (c) 2004, Mikael Grev, MiG
 * InfoCom AB. (base64 @ miginfocom . com) All rights reserved. Redistribution
 * and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: Redistributions of
 * source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer. Redistributions in binary form must reproduce
 * the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the MiG InfoCom AB nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * @version 2.0
 * @author Mikael Grev Date: 2004-aug-02 Time: 11:31:11
 */

public class Base64
{

    public static String[] decodeString ( String[] param )
    {
        if (null == param)
        {
            return null;
        }
        if (param.length == 0)
        {
            return param;
        } else
        {
            return decodeStringarr(param);
        }
    }

    private static String[] decodeStringarr ( String[] param )
    {
        String[] reStringArray = new String[param.length];
        for (int i = 0; i < param.length; i++)
        {
            if (null == param[i])
            {
                reStringArray[i] = null;
            } else if (param[i].startsWith("Shenandoah:"))
            {
                reStringArray[i] = Base64.decodeString(param[i]);
            } else
            {
                reStringArray[i] = param[i];
            }
        }
        return reStringArray;
    }

    public static void main ( String[] args )
    {
        String[] test = { null, "", null, "casdc", Base64.encodeString("��������"),
                Base64.encodeString("xixixix227*&*&&*^DT^WD^^&^DWTDW*D") };
        for (String ddd : decodeString(test))
        {
            System.out.println(ddd);
        }

    }

    private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                                           .toCharArray();
    private static final int[]  IA = new int[256];
    static
    {
        Arrays.fill(IA, -1);
        for (int i = 0, iS = CA.length; i < iS; i++)
            IA[CA[i]] = i;
        IA['='] = 0;
    }

    /**
     * �����ַ���
     * @param jmq
     * @return
     * @throws Exception
     */
    public final static String encodeString(String jmq){
        String encode = "";
        if(jmq != null && jmq != ""){
            try{
                byte[] bys1 = Base64.encodeToByte(jmq.getBytes("UTF-8"));
                if (bys1 != null)
                    encode =  new String(bys1,"UTF-8");
                    encode = "Shenandoah:" + encode;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return jmq;
            }
        }
        return encode;
    }
    

    /**
     * �����ַ���
     * @param jmq
     * @return
     * @throws Exception
     */
    public final static String decodeString(String jmq){
        String decode = "";
        if(jmq != null && jmq != "" && jmq.startsWith("Shenandoah:")){
            try{
                jmq = jmq.substring("Shenandoah:".length());
                byte[] bys1 = Base64.decode(jmq.getBytes("UTF-8"));
                if (bys1 != null)
                    decode =  new String(bys1,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return jmq;
            }
        }
        return decode;
    }

    // ****************************************************************************************
    // * char[] version
    // ****************************************************************************************
    public final static char[] encodeToChar ( byte[] sArr )
    {
        return encodeToChar(sArr, false);
    }

    /**
     * Encodes a raw byte array into a BASE64 <code>char[]</code>
     * representation i accordance with RFC 2045.
     * 
     * @param sArr
     *            The bytes to convert. If <code>null</code> or length 0 an
     *            empty array will be returned.
     * @param lineSep
     *            Optional "\r\n" after 76 characters, unless end of file.<br>
     *            No line separator will be in breach of RFC 2045 which
     *            specifies max 76 per line but will be a little faster.
     * @return A BASE64 encoded array. Never <code>null</code>.
     */
    public final static char[] encodeToChar ( byte[] sArr, boolean lineSep )
    {
        // Check special case
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0)
            return new char[0];

        int eLen = (sLen / 3) * 3; // Length of even 24-bits.
        int cCnt = ((sLen - 1) / 3 + 1) << 2; // Returned character count
        int dLen = cCnt + (lineSep ? (cCnt - 1) / 76 << 1 : 0); // Length of
                                                                // returned
                                                                // array
        char[] dArr = new char[dLen];

        // Encode even 24-bits
        for (int s = 0, d = 0, cc = 0; s < eLen;)
        {
            // Copy next three bytes into lower 24 bits of int, paying attension
            // to sign.
            int i = toI(sArr[s++]) << 16 | toI(sArr[s++]) << 8 | toI(sArr[s++]);

            // Encode the int into four chars
            dArr[d++] = CA[(i >>> 18) & 0x3f];
            dArr[d++] = CA[(i >>> 12) & 0x3f];
            dArr[d++] = CA[(i >>> 6) & 0x3f];
            dArr[d++] = CA[i & 0x3f];

            // Add optional line separator
            if (lineSep && ++cc == 19 && d < dLen - 2)
            {
                dArr[d++] = '\r';
                dArr[d++] = '\n';
                cc = 0;
            }
        }

        // Pad and encode last bits if source isn't even 24 bits.
        int left = sLen - eLen; // 0 - 2.
        if (left > 0)
        {
            // Prepare the int
            int i = (toI(sArr[eLen]) << 10)
                    | (left == 2 ? (toI(sArr[sLen - 1]) << 2) : 0);

            // Set last four chars
            dArr[dLen - 4] = CA[i >> 12];
            dArr[dLen - 3] = CA[(i >>> 6) & 0x3f];
            dArr[dLen - 2] = left == 2 ? (char) CA[i & 0x3f] : (char) '=';
            dArr[dLen - 1] = '=';
        }
        return dArr;
    }

    /**
     * Decodes a BASE64 encoded char array. All illegal characters will be
     * ignored and can handle both arrays with and without line separators.
     * 
     * @param sArr
     *            The source array. <code>null</code> or length 0 will return
     *            an empty array.
     * @return The decoded array of bytes. May be of length 0. Will be
     *         <code>null</code> if the legal characters (including '=') isn't
     *         divideable by 4. (I.e. corrupted).
     */
    public final static byte[] decode ( char[] sArr )
    {
        // Check special case
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0)
            return new byte[0];

        // Count illegal characters (including '\r', '\n') to know what size the
        // returned array will be,
        // so we don't have to reallocate & copy it later.
        int sepCnt = 0; // Number of separator characters. (Actually illegal
                        // characters, but that's a bonus...)
        for (int i = 0; i < sLen; i++)
            // If input is "pure" (I.e. no line separators or illegal chars)
            // base64 this loop can be commented out.
            if (IA[sArr[i]] < 0)
                sepCnt++;

        // Check so that legal chars (including '=') are evenly divideable by 4
        // as specified in RFC 2045.
        if ((sLen - sepCnt) % 4 != 0)
            return null;

        int pad = sArr[sLen - 1] == '=' ? (sArr[sLen - 2] == '=' ? 2 : 1) : 0; // Count
                                                                                // '='
                                                                                // at
                                                                                // end.
        int len = ((sLen - sepCnt) * 6 >> 3) - pad;

        byte[] dArr = new byte[len]; // Preallocate byte[] of exact length

        for (int s = 0, d = 0; d < len;)
        {
            // Assemble three bytes into an int from four "valid" characters.
            int i = 0;
            for (int j = 0; j < 4; j++)
            { // j only increased if a valid char was found.
                int c = IA[sArr[s++]];
                if (c >= 0)
                    i |= c << (18 - j * 6);
                else
                    j--;
            }
            // Add the bytes
            dArr[d++] = (byte) (i >> 16);
            if (d < len)
            {
                dArr[d++] = (byte) (i >> 8);
                if (d < len)
                    dArr[d++] = (byte) i;
            }
        }
        return dArr;
    }

    // ****************************************************************************************
    // * byte[] version
    // ****************************************************************************************

    public final static byte[] encodeToByte ( byte[] sArr )
    {
        return encodeToByte(sArr, false);
    }

    /**
     * Encodes a raw byte array into a BASE64 <code>byte[]</code>
     * representation i accordance with RFC 2045.
     * 
     * @param sArr
     *            The bytes to convert. If <code>null</code> or length 0 an
     *            empty array will be returned.
     * @param lineSep
     *            Optional "\r\n" after 76 characters, unless end of file.<br>
     *            No line separator will be in breach of RFC 2045 which
     *            specifies max 76 per line but will be a little faster.
     * @return A BASE64 encoded array. Never <code>null</code>.
     */
    public final static byte[] encodeToByte ( byte[] sArr, boolean lineSep )
    {
        // Check special case
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0)
            return new byte[0];

        int eLen = (sLen / 3) * 3; // Length of even 24-bits.
        int cCnt = ((sLen - 1) / 3 + 1) << 2; // Returned character count
        int dLen = cCnt + (lineSep ? (cCnt - 1) / 76 << 1 : 0); // Length of
                                                                // returned
                                                                // array
        byte[] dArr = new byte[dLen];

        // Encode even 24-bits
        for (int s = 0, d = 0, cc = 0; s < eLen;)
        {
            // Copy next three bytes into lower 24 bits of int, paying attension
            // to sign.
            int i = toI(sArr[s++]) << 16 | toI(sArr[s++]) << 8 | toI(sArr[s++]);

            // Encode the int into four chars
            dArr[d++] = (byte) CA[(i >>> 18) & 0x3f];
            dArr[d++] = (byte) CA[(i >>> 12) & 0x3f];
            dArr[d++] = (byte) CA[(i >>> 6) & 0x3f];
            dArr[d++] = (byte) CA[i & 0x3f];

            // Add optional line separator
            if (lineSep && ++cc == 19 && d < dLen - 2)
            {
                dArr[d++] = '\r';
                dArr[d++] = '\n';
                cc = 0;
            }
        }

        // Pad and encode last bits if source isn't an even 24 bits.
        int left = sLen - eLen; // 0 - 2.
        if (left > 0)
        {
            // Prepare the int
            int i = (toI(sArr[eLen]) << 10)
                    | (left == 2 ? (toI(sArr[sLen - 1]) << 2) : 0);

            // Set last four chars
            dArr[dLen - 4] = (byte) CA[i >> 12];
            dArr[dLen - 3] = (byte) CA[(i >>> 6) & 0x3f];
            dArr[dLen - 2] = left == 2 ? (byte) CA[i & 0x3f] : (byte) '=';
            dArr[dLen - 1] = '=';
        }
        return dArr;
    }

    /**
     * Decodes a BASE64 encoded byte array. All illegal characters will be
     * ignored and can handle both arrays with and without line separators.
     * 
     * @param sArr
     *            The source array. <code>null</code> or length 0 will return
     *            an empty array.
     * @return The decoded array of bytes. May be of length 0. Will be
     *         <code>null</code> if the legal characters (including '=') isn't
     *         divideable by 4. (I.e. corrupted).
     */
    public final static byte[] decode ( byte[] sArr )
    {
        // Check special case
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0)
            return new byte[0];

        // Count illegal characters (including '\r', '\n') to know what size the
        // returned array will be,
        // so we don't have to reallocate & copy it later.
        int sepCnt = 0; // Number of separator characters. (Actually illegal
                        // characters, but that's a bonus...)
        for (int i = 0; i < sLen; i++)
        { // If input is "pure" (I.e. no line separators or illegal chars)
            // base64 this loop can be commented out.
            byte b = sArr[i];
            if (b < 0 || IA[b] < 0)
                sepCnt++;
        }

        // Check so that legal chars (including '=') are evenly divideable by 4
        // as specified in RFC 2045.
        if ((sLen - sepCnt) % 4 != 0)
            return null;

        int pad = sArr[sLen - 1] == '=' ? (sArr[sLen - 2] == '=' ? 2 : 1) : 0; // Count
                                                                                // '='
                                                                                // at
                                                                                // end,
        int len = ((sLen - sepCnt) * 6 >> 3) - pad;

        byte[] dArr = new byte[len]; // Preallocate byte[] of exact length

        for (int s = 0, d = 0; d < len;)
        {
            // Assemble three bytes into an int from four "valid" characters.
            int i = 0;
            for (int j = 0; j < 4; j++)
            { // j only increased if a valid char was found.
                int c = IA[sArr[s++]];
                if (c >= 0)
                    i |= c << (18 - j * 6);
                else
                    j--;
            }
            // Add the bytes
            dArr[d++] = (byte) (i >> 16);
            if (d < len)
            {
                dArr[d++] = (byte) (i >> 8);
                if (d < len)
                    dArr[d++] = (byte) i;
            }
        }
        return dArr;
    }

    // ****************************************************************************************
    // * String version
    // ****************************************************************************************
    public final static String encodeToString ( byte[] sArr )
    {
        return encodeToString(sArr, false);
    }

    /**
     * Encodes a raw byte array into a BASE64 <code>String</code>
     * representation i accordance with RFC 2045.
     * 
     * @param sArr
     *            The bytes to convert. If <code>null</code> or length 0 an
     *            empty array will be returned.
     * @param lineSep
     *            Optional "\r\n" after 76 characters, unless end of file.<br>
     *            No line separator will be in breach of RFC 2045 which
     *            specifies max 76 per line but will be a little faster.
     * @return A BASE64 encoded array. Never <code>null</code>.
     */
    public final static String encodeToString ( byte[] sArr, boolean lineSep )
    {
        // Reuse char[] since we can't create a String incrementally anyway and
        // StringBuffer/Builder would be slower.
        return new String(encodeToChar(sArr, lineSep));
    }

    /**
     * Decodes a BASE64 encoded <code>String</code>. All illegal characters
     * will be ignored and can handle both strings with and without line
     * separators.<br>
     * <b>Note!</b> It can be up to about 2x the speed to call
     * <code>decode(str.toCharArray())</code> instead. That will create a
     * temporary array though. This version will use <code>str.charAt(i)</code>
     * to iterate the string.
     * 
     * @param str
     *            The source string. <code>null</code> or length 0 will return
     *            an empty array.
     * @return The decoded array of bytes. May be of length 0. Will be
     *         <code>null</code> if the legal characters (including '=') isn't
     *         divideable by 4. (I.e. corrupted).
     */
    public final static byte[] decode ( String str )
    {
        // Check special case
        int sLen = str != null ? str.length() : 0;
        if (sLen == 0)
            return new byte[0];

        // Count illegal characters (including '\r', '\n') to know what size the
        // returned array will be,
        // so we don't have to reallocate & copy it later.
        int sepCnt = 0; // Number of separator characters. (Actually illegal
                        // characters, but that's a bonus...)
        for (int i = 0; i < sLen; i++)
        {
            if ( str.charAt(i) >= IA.length )
                return null; //Li Ma: invalid string detected, return null 
            // If input is "pure" (I.e. no line separators or illegal chars)
            // base64 this loop can be commented out.
            if (IA[str.charAt(i)] < 0)
                sepCnt++;
        }
        // Check so that legal chars (including '=') are evenly divideable by 4
        // as specified in RFC 2045.
        if ((sLen - sepCnt) % 4 != 0)
            return null;

        // Count '=' at end
        int pad = str.charAt(sLen - 1) == '=' ? (str.charAt(sLen - 2) == '=' ? 2
                : 1)
                : 0; // Count '=' at end.
        int len = ((sLen - sepCnt) * 6 >> 3) - pad;

        byte[] dArr = new byte[len]; // Preallocate byte[] of exact length

        for (int s = 0, d = 0; d < len;)
        {
            // Assemble three bytes into an int from four "valid" characters.
            int i = 0;
            for (int j = 0; j < 4; j++)
            { // j only increased if a valid char was found.
                int c = IA[str.charAt(s++)];
                if (c >= 0)
                    i |= c << (18 - j * 6);
                else
                    j--;
            }
            // Add the bytes
            dArr[d++] = (byte) (i >> 16);
            if (d < len)
            {
                dArr[d++] = (byte) (i >> 8);
                if (d < len)
                    dArr[d++] = (byte) i;
            }
        }
        return dArr;
    }

    /**
     * If the byte is negative add 256.
     * 
     * @param b
     *            The byte
     * @return a value 0..255.
     */
    private static final int toI ( byte b )
    {
        return b < 0 ? b + 256 : b;
    }


    ///----cuicc 2021-09-11 ------- 开始
    /**
     * 将 s 进行 BASE64 编码
     *
     * @return String
     * @author lifq
     * @date 2015-3-4 上午09:24:02
     */
    public static String encode4login(String s) {
        if (s == null)
            return null;
        String res = "";
        try {
            res = new sun.misc.BASE64Encoder().encode(s.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 将 BASE64 编码的字符串 s 进行解码
     *
     * @return String
     * @author lifq
     * @date 2015-3-4 上午09:24:26
     */
    public static String decode4login(String s) {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b,"GBK");
        } catch (Exception e) {
            return null;
        }
    }
    ///----cuicc 2021-09-11 ------- 结束

}
