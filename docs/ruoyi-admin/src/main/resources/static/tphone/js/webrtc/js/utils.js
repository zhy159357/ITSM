/**
 *  工具函数合集
 **/
Utils = {

    /**
     * options配置
     */
    debugAll: true,
    cookieSecure: false,
    defaultLogColor: '#B0E0E6',
    defaultLang: 'zh_CN',

  /**
   * IE浏览器检测
   */
  isIE: function () {
    var Sys = {};
    var ua = navigator.userAgent.toLowerCase();
    var re = /(rv|edge|msie|firefox|chrome|opera|version).*?([\d.]+)/;
    var m = ua.match(re);
    Sys.browser = m[1].replace(/version/, "'safari");
    Sys.version = m[2];
    if (Sys.browser == 'rv' || Sys.browser == 'msie') {
      Sys.browser = 'ie';
    }
    var _browser = Sys.browser;
    if (_browser == 'ie' || _browser == 'edge') {
      return true;
    }
    return false;
  },

  /**
     * cookie操作类
     */
    cookie: {
        /**
         * 读取cookie
         *
         * @param name
         * @returns {*}
         */
        readCookie: function (name) {
            var arr, reg = new RegExp('(^| )' + name + '=([^;]*)(;|$)');
            if (arr = document.cookie.match(reg)) {
                return unescape(arr[2]);
            } else {
                return null;
            }

        },

        /**
         * 写cookie
         *
         * @param name
         * @param value
         */
        setCookie: function (name, value) {
            const days = 1;
            const exp = new Date();
            exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
            if(Utils.cookieSecure) {
                document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";secure=true";
            } else {
                document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
            }
        },

        /**
         * 删除cookie, 实际上是cookie超时
         *
         * @param name
         */
        delCookie: function (name) {
            var val = Utils.cookie.readCookie(name);
            if (val) {
                document.cookie = name + "=" + val + ";expires=" + (new Date(0)).toGMTString();
            }
        }
    },

    /**
     * local storage
     */
    localStorage: {
        setItem: function (name, value) {
            localStorage.setItem(name, value);
        },

        getItem: function (name) {
            localStorage.getItem(name)
        }
    },

    /**
     * 获取URL路径参数
     *
     * @param name
     * @returns {*}
     */
    getQueryVariable: function (name) {
        const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        const urlObj = window.location;
        var r = urlObj.href.indexOf('#') > -1 ? urlObj.hash.split("?")[0].match(reg) : urlObj.search.substr(1).match(reg);
        if(r && r[2]) {
            return unescape(decodeURI(r[2]));
        }
        return '';
    },

    /**
     * 获取url中的用户Id
     *
     * @param lang
     * @returns {string}
     */
    getUserId: function (key) {
        if(!key) {
            key = 'userId';
        }

        return Utils.getQueryVariable(key);
    },

    /**
     * 语种定义
     */
    Lang: {
        zh_CN: 'zh_CN',
        zh_TW: 'zh_TW',
        en_US: 'en_US',
    },

    /**
     * 获取url中的语种信息
     *
     * @param lang
     * @returns {string}
     */
    getLang: function (key) {
        if(!key) {
            key = 'lang';
        }

        const lang = Utils.getQueryVariable(key) || Utils.defaultLang;
        if(lang) {
            Utils.localStorage.setItem(key, lang)
        }

        return lang;
    },

    /**
     * 非标准语种转化
     *
     * @param lang
     * @returns {string}
     */
    transLang2Standard: function (lang) {
        if(lang) {
            if('en' === lang) {
                return 'en_US';
            }
            else if('cn' === lang) {
                return 'zh_CN';
            }
            else if('zh-hk' === lang) {
                return 'zh_TW';
            }
        }

        return 'zh_TW';
    },

    /**
     * 日期时间显示格式化
     *
     * @returns {string}
     */
    ts: function () {
        const d = new Date();
        const Hours = d.getHours(); // 获取当前小时数(0-23)
        const Minutes = d.getMinutes(); // 获取当前分钟数(0-59)
        const Seconds = d.getSeconds(); // 获取当前秒数(0-59)
        const Milliseconds = d.getMilliseconds(); //获取当前毫秒
        return (Hours < 10 ? '0' + Hours : Hours) + ':' + (Minutes < 10 ? '0' + Minutes : Minutes) + ':' + (Seconds < 10 ? '0' + Seconds : Seconds) + ':' + Milliseconds + ' ';
    },

    /**
     * 获取对象属性
     *
     * @param obj
     * @param val
     * @returns {string}
     */
    getObjectKey: function (obj, val) {
        for (var key in obj) {
            if (obj[key] == val) {
                return key;
            }
        }
        return '';
    },

    /**
     * 防止xss注入
     *
     * @param text
     * @returns {*}
     */
    htmlEscape: function(text){
        return text.replace(/[<>&]/g, function(match){
            switch(match){
                case '<': return '&lt;';
                case '>': return '&gt;';
                case '&': return '&amp;';
                case '\"': return '&quot;';
            }
        });
    },


    /**
     * 日志格式
     */
    log: {
        info: function (msg, color) {
            if(Utils.debugAll && msg) {
                color = color ? color : Utils.defaultLogColor;
                console.log('%c' + new Date() + ' ' + Utils.ts() + '[info] ' + (msg || ''), 'background-color: ' + color);
            }
        },
        error: function (msg, color) {
            if(Utils.debugAll && msg) {
                color = color ? color : Utils.defaultLogColor;
                console.log('%c' + new Date() + ' ' + Utils.ts() + '[error] ' + (msg || ''), 'background-color: ' + color);
            }
        },
    },

    /**
     * 类型转换
     *
     * @param urlData
     * @returns {Blob}
     */
    base64ToBlob: function (urlData) {
        var bytes = window.atob(urlData);
        // 去掉url的头，并转换为byte
        var ab = new ArrayBuffer(bytes.length);
        var ia = new Uint8Array(ab);
        for(var i = 0; i < bytes.length; i++) {
            ia[i] = bytes.charCodeAt(i);
        }
        return new Blob([ab], {type:'image/png'});
    },

    /**
     * 随机生成生指定长度成随机数
     *
     * @param length
     * @returns {string}
     */
    randomNumber: function (length) {
        var data = ['0','1','2','3','4','5','6','7','8','9','a','s','d','f','g','h','j','k','l','A','S','D','F','G','H','J','K','L'];
        var result = '';
        for (var i = 0;i < length;i++) {
            var number = Math.floor(Math.random() * 28);
            result += data[number];
        }
        return result;
    },

    /**
     * 生成UUID
     */
    uuid: function () {
        var s = [];
        const hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = '';

        var uuid = s.join("");
        return uuid;
    },

    /**
     * 关闭浏览器窗口
     */
    closeWindow: function () {
        if (navigator.userAgent.indexOf("Firefox") != -1 || navigator.userAgent.indexOf("Chrome") != -1) {
            window.location.href = "about:blank";
            window.close();
        } else {
            window.opener = null;
            window.open("", "_self");
            window.close();
        }
    },

    /**
     * 特殊符号转义，避免XSS攻击
     *
     * @param str
     * @returns {*}
     */
    xssTransfer: function (str) {
        if(str) {
            str = str.replace(/</g, '&lt;');
            str = str.replace(/>/g, '&gt;');

            const reg = /<[^>]*>|<\/[^>]*>/gm;
            str = str.replace(reg,'');

            return str;
        }

        return '';
    },

    /**
     * 时间格式化
     *
     * @param date
     * @returns {string}
     */
    getFormatDate: function (date) {
        let dateNow = new Date();
        if (date) {
            dateNow = date;
        }
        const seperator1 = '-', seperator2 = ':';

        let month = dateNow.getMonth() + 1;
        month = Utils.two_chars(month);

        let strDate = dateNow.getDate();
        strDate = Utils.two_chars(strDate);

        let hours = dateNow.getHours();
        hours = Utils.two_chars(hours);

        let minute = dateNow.getMinutes();
        minute = Utils.two_chars(minute);

        let secs = dateNow.getSeconds();
        secs = Utils.two_chars(secs);

        const currentDate = dateNow.getFullYear() + seperator1 + month + seperator1 + strDate + ' ' + hours + seperator2 + minute + seperator2 + secs;
        return currentDate;
    },

    two_chars: function (count) {
        if (count >= 1 && count <= 9) {
            count = '0' + count;
        }
        return count;
    }
};

/**
 * 重写Date Format方法
 *
 * @param formatStr
 * @returns {*}
 * @constructor
 */
Date.prototype.Format = function(formatStr){
    var str = formatStr;
    var Week = ['日','一','二','三','四','五','六'];

    str = str.replace(/yyyy|YYYY/,this.getFullYear());
    str = str.replace(/yy|YY/,(this.getYear() % 100)>9?(this.getYear() % 100).toString():'0' + (this.getYear() % 100));

    str = str.replace(/MM/,(this.getMonth()+1)>9?(this.getMonth()+1).toString():'0' + (this.getMonth()+1));
    str = str.replace(/M/g,this.getMonth());

    str = str.replace(/w|W/g, Week[this.getDay()]);

    str = str.replace(/dd|DD/,this.getDate()>9?this.getDate().toString():'0' + this.getDate());
    str = str.replace(/d|D/g,this.getDate());

    str = str.replace(/hh|HH/,this.getHours()>9?this.getHours().toString():'0' + this.getHours());
    str = str.replace(/h|H/g,this.getHours());
    str = str.replace(/mm/,this.getMinutes()>9?this.getMinutes().toString():'0' + this.getMinutes());
    str = str.replace(/m/g,this.getMinutes());

    str = str.replace(/ss|SS/,this.getSeconds()>9?this.getSeconds().toString():'0' + this.getSeconds());
    str = str.replace(/s|S/g,this.getSeconds());

    return str;
};

/***********************************************************************************************
 *********************************************** 数组 ***************************************
 ***********************************************************************************************/
// 数组功能扩展
Array.prototype.each = function (fn) {
    fn = fn || Function.K;
    var a = [];
    var args = Array.prototype.slice.call(arguments, 1);
    for (var i = 0; i < this.length; i++) {
        var res = fn.apply(this, [this[i], i].concat(args));
        if (res != null) a.push(res);
    }
    return a;
};

// 数组是否包含指定元素
Array.prototype.contains = function (suArr) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == suArr) {
            return true;
        }
    }
    return false;
};

//不重复元素构成的数组
Array.prototype.uniquelize = function () {
    var ra = new Array();
    for (var i = 0; i < this.length; i++) {
        if (!ra.contains(this[i])) {
            ra.push(this[i]);
        }
    }
    return ra;
};

//两个数组的补集
Array.complement = function (a, b) {
    return Array.minus(Array.union(a, b), Array.intersect(a, b));
};

//两个数组的交集
Array.intersect = function (a, b) {
    return a.uniquelize().each(function (o) { return b.contains(o) ? o : null });
};

//两个数组的差集
Array.minus = function (a, b) {
    return a.uniquelize().each(function (o) { return b.contains(o) ? null : o });
};

//两个数组并集
Array.union = function (a, b) {
    return a.concat(b).uniquelize();
};

/***********************************************************************************************
 *********************************************** Base64  ***************************************
 ***********************************************************************************************/
Base64 = {
    // 转码表
    table: [
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/'
    ],

    UTF16ToUTF8: function (str) {
        var res = [], len = str.length;
        for (var i = 0; i < len; i++) {
            var code = str.charCodeAt(i);
            if (code > 0x0000 && code <= 0x007F) {
                // 单字节，这里并不考虑0x0000，因为它是空字节
                // U+00000000 – U+0000007F  0xxxxxxx
                res.push(str.charAt(i));
            } else if (code >= 0x0080 && code <= 0x07FF) {
                // 双字节
                // U+00000080 – U+000007FF  110xxxxx 10xxxxxx
                // 110xxxxx
                var byte1 = 0xC0 | ((code >> 6) & 0x1F);
                // 10xxxxxx
                var byte2 = 0x80 | (code & 0x3F);
                res.push(
                    String.fromCharCode(byte1),
                    String.fromCharCode(byte2)
                );
            } else if (code >= 0x0800 && code <= 0xFFFF) {
                // 三字节
                // U+00000800 – U+0000FFFF  1110xxxx 10xxxxxx 10xxxxxx
                // 1110xxxx
                var byte1 = 0xE0 | ((code >> 12) & 0x0F);
                // 10xxxxxx
                var byte2 = 0x80 | ((code >> 6) & 0x3F);
                // 10xxxxxx
                var byte3 = 0x80 | (code & 0x3F);
                res.push(
                    String.fromCharCode(byte1),
                    String.fromCharCode(byte2),
                    String.fromCharCode(byte3)
                );
            } else if (code >= 0x00010000 && code <= 0x001FFFFF) {
                // 四字节
                // U+00010000 – U+001FFFFF  11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
            } else if (code >= 0x00200000 && code <= 0x03FFFFFF) {
                // 五字节
                // U+00200000 – U+03FFFFFF  111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
            } else /** if (code >= 0x04000000 && code <= 0x7FFFFFFF)*/ {
                // 六字节
                // U+04000000 – U+7FFFFFFF  1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
            }
        }

        return res.join('');
    },

    UTF8ToUTF16: function (str) {
        var res = [], len = str.length;
        var i = 0;
        for (var i = 0; i < len; i++) {
            var code = str.charCodeAt(i);
            // 对第一个字节进行判断
            if (((code >> 7) & 0xFF) == 0x0) {
                // 单字节
                // 0xxxxxxx
                res.push(str.charAt(i));
            } else if (((code >> 5) & 0xFF) == 0x6) {
                // 双字节
                // 110xxxxx 10xxxxxx
                var code2 = str.charCodeAt(++i);
                var byte1 = (code & 0x1F) << 6;
                var byte2 = code2 & 0x3F;
                var utf16 = byte1 | byte2;
                res.push(String.fromCharCode(utf16));
            } else if (((code >> 4) & 0xFF) == 0xE) {
                // 三字节
                // 1110xxxx 10xxxxxx 10xxxxxx
                var code2 = str.charCodeAt(++i);
                var code3 = str.charCodeAt(++i);
                var byte1 = (code << 4) | ((code2 >> 2) & 0x0F);
                var byte2 = ((code2 & 0x03) << 6) | (code3 & 0x3F);
                utf16 = ((byte1 & 0x00FF) << 8) | byte2
                res.push(String.fromCharCode(utf16));
            } else if (((code >> 3) & 0xFF) == 0x1E) {
                // 四字节
                // 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
            } else if (((code >> 2) & 0xFF) == 0x3E) {
                // 五字节
                // 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
            } else /** if (((code >> 1) & 0xFF) == 0x7E)*/ {
                // 六字节
                // 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
            }
        }

        return res.join('');
    },

    encode: function (str) {
        if (!str) {
            return '';
        }
        var utf8 = this.UTF16ToUTF8(str); // 转成UTF8
        var i = 0; // 遍历索引
        var len = utf8.length;
        var res = [];
        while (i < len) {
            var c1 = utf8.charCodeAt(i++) & 0xFF;
            res.push(this.table[c1 >> 2]);
            // 需要补2个=
            if (i == len) {
                res.push(this.table[(c1 & 0x3) << 4]);
                res.push('==');
                break;
            }
            var c2 = utf8.charCodeAt(i++);
            // 需要补1个=
            if (i == len) {
                res.push(this.table[((c1 & 0x3) << 4) | ((c2 >> 4) & 0x0F)]);
                res.push(this.table[(c2 & 0x0F) << 2]);
                res.push('=');
                break;
            }
            var c3 = utf8.charCodeAt(i++);
            res.push(this.table[((c1 & 0x3) << 4) | ((c2 >> 4) & 0x0F)]);
            res.push(this.table[((c2 & 0x0F) << 2) | ((c3 & 0xC0) >> 6)]);
            res.push(this.table[c3 & 0x3F]);
        }

        return res.join('');
    },

    decode: function (str) {
        if (!str) {
            return '';
        }

        var len = str.length;
        var i = 0;
        var res = [];

        while (i < len) {
            code1 = this.table.indexOf(str.charAt(i++));
            code2 = this.table.indexOf(str.charAt(i++));
            code3 = this.table.indexOf(str.charAt(i++));
            code4 = this.table.indexOf(str.charAt(i++));

            c1 = (code1 << 2) | (code2 >> 4);
            c2 = ((code2 & 0xF) << 4) | (code3 >> 2);
            c3 = ((code3 & 0x3) << 6) | code4;

            res.push(String.fromCharCode(c1));

            if (code3 != 64) {
                res.push(String.fromCharCode(c2));
            }
            if (code4 != 64) {
                res.push(String.fromCharCode(c3));
            }

        }

        return this.UTF8ToUTF16(res.join(''));
    },

    // 转为unicode 编码
    encodeUnicode: function(str) {
        var res = [];
        for ( var i=0; i<str.length; i++ ) {
            res[i] = ( "00" + str.charCodeAt(i).toString(16) ).slice(-4);
        }
        return "\\u" + res.join("\\u");
    },

    // 解码
    decodeUnicode: function(str) {
        str = str.replace(/\\/g, "%");
        //转换中文
        str = unescape(str);
        //将其他受影响的转换回原来
        str = str.replace(/%/g, "\\");
        //对网址的链接进行处理
        str = str.replace(/\\/g, "");
        return str;
    }
};

MD5 = function (string) {

    function RotateLeft(lValue, iShiftBits) {
        return (lValue<<iShiftBits) | (lValue>>>(32-iShiftBits));
    }

    function AddUnsigned(lX,lY) {
        var lX4,lY4,lX8,lY8,lResult;
        lX8 = (lX & 0x80000000);
        lY8 = (lY & 0x80000000);
        lX4 = (lX & 0x40000000);
        lY4 = (lY & 0x40000000);
        lResult = (lX & 0x3FFFFFFF)+(lY & 0x3FFFFFFF);
        if (lX4 & lY4) {
            return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
        }
        if (lX4 | lY4) {
            if (lResult & 0x40000000) {
                return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
            } else {
                return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
            }
        } else {
            return (lResult ^ lX8 ^ lY8);
        }
    }

    function F(x,y,z) { return (x & y) | ((~x) & z); }
    function G(x,y,z) { return (x & z) | (y & (~z)); }
    function H(x,y,z) { return (x ^ y ^ z); }
    function I(x,y,z) { return (y ^ (x | (~z))); }

    function FF(a,b,c,d,x,s,ac) {
        a = AddUnsigned(a, AddUnsigned(AddUnsigned(F(b, c, d), x), ac));
        return AddUnsigned(RotateLeft(a, s), b);
    };

    function GG(a,b,c,d,x,s,ac) {
        a = AddUnsigned(a, AddUnsigned(AddUnsigned(G(b, c, d), x), ac));
        return AddUnsigned(RotateLeft(a, s), b);
    };

    function HH(a,b,c,d,x,s,ac) {
        a = AddUnsigned(a, AddUnsigned(AddUnsigned(H(b, c, d), x), ac));
        return AddUnsigned(RotateLeft(a, s), b);
    };

    function II(a,b,c,d,x,s,ac) {
        a = AddUnsigned(a, AddUnsigned(AddUnsigned(I(b, c, d), x), ac));
        return AddUnsigned(RotateLeft(a, s), b);
    };

    function ConvertToWordArray(string) {
        var lWordCount;
        var lMessageLength = string.length;
        var lNumberOfWords_temp1=lMessageLength + 8;
        var lNumberOfWords_temp2=(lNumberOfWords_temp1-(lNumberOfWords_temp1 % 64))/64;
        var lNumberOfWords = (lNumberOfWords_temp2+1)*16;
        var lWordArray=Array(lNumberOfWords-1);
        var lBytePosition = 0;
        var lByteCount = 0;
        while ( lByteCount < lMessageLength ) {
            lWordCount = (lByteCount-(lByteCount % 4))/4;
            lBytePosition = (lByteCount % 4)*8;
            lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount)<<lBytePosition));
            lByteCount++;
        }
        lWordCount = (lByteCount-(lByteCount % 4))/4;
        lBytePosition = (lByteCount % 4)*8;
        lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80<<lBytePosition);
        lWordArray[lNumberOfWords-2] = lMessageLength<<3;
        lWordArray[lNumberOfWords-1] = lMessageLength>>>29;
        return lWordArray;
    };

    function WordToHex(lValue) {
        var WordToHexValue="",WordToHexValue_temp="",lByte,lCount;
        for (lCount = 0;lCount<=3;lCount++) {
            lByte = (lValue>>>(lCount*8)) & 255;
            WordToHexValue_temp = "0" + lByte.toString(16);
            WordToHexValue = WordToHexValue + WordToHexValue_temp.substr(WordToHexValue_temp.length-2,2);
        }
        return WordToHexValue;
    };

    function Utf8Encode(string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";

        for (var n = 0; n < string.length; n++) {

            var c = string.charCodeAt(n);

            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }

        return utftext;
    };

    var x=Array();
    var k,AA,BB,CC,DD,a,b,c,d;
    const S11=7, S12=12, S13=17, S14=22;
    const S21=5, S22=9 , S23=14, S24=20;
    const S31=4, S32=11, S33=16, S34=23;
    const S41=6, S42=10, S43=15, S44=21;

    string = Utf8Encode(string);

    x = ConvertToWordArray(string);

    a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;

    for (k=0;k<x.length;k+=16) {
        AA=a; BB=b; CC=c; DD=d;
        a=FF(a,b,c,d,x[k+0], S11,0xD76AA478);
        d=FF(d,a,b,c,x[k+1], S12,0xE8C7B756);
        c=FF(c,d,a,b,x[k+2], S13,0x242070DB);
        b=FF(b,c,d,a,x[k+3], S14,0xC1BDCEEE);
        a=FF(a,b,c,d,x[k+4], S11,0xF57C0FAF);
        d=FF(d,a,b,c,x[k+5], S12,0x4787C62A);
        c=FF(c,d,a,b,x[k+6], S13,0xA8304613);
        b=FF(b,c,d,a,x[k+7], S14,0xFD469501);
        a=FF(a,b,c,d,x[k+8], S11,0x698098D8);
        d=FF(d,a,b,c,x[k+9], S12,0x8B44F7AF);
        c=FF(c,d,a,b,x[k+10],S13,0xFFFF5BB1);
        b=FF(b,c,d,a,x[k+11],S14,0x895CD7BE);
        a=FF(a,b,c,d,x[k+12],S11,0x6B901122);
        d=FF(d,a,b,c,x[k+13],S12,0xFD987193);
        c=FF(c,d,a,b,x[k+14],S13,0xA679438E);
        b=FF(b,c,d,a,x[k+15],S14,0x49B40821);
        a=GG(a,b,c,d,x[k+1], S21,0xF61E2562);
        d=GG(d,a,b,c,x[k+6], S22,0xC040B340);
        c=GG(c,d,a,b,x[k+11],S23,0x265E5A51);
        b=GG(b,c,d,a,x[k+0], S24,0xE9B6C7AA);
        a=GG(a,b,c,d,x[k+5], S21,0xD62F105D);
        d=GG(d,a,b,c,x[k+10],S22,0x2441453);
        c=GG(c,d,a,b,x[k+15],S23,0xD8A1E681);
        b=GG(b,c,d,a,x[k+4], S24,0xE7D3FBC8);
        a=GG(a,b,c,d,x[k+9], S21,0x21E1CDE6);
        d=GG(d,a,b,c,x[k+14],S22,0xC33707D6);
        c=GG(c,d,a,b,x[k+3], S23,0xF4D50D87);
        b=GG(b,c,d,a,x[k+8], S24,0x455A14ED);
        a=GG(a,b,c,d,x[k+13],S21,0xA9E3E905);
        d=GG(d,a,b,c,x[k+2], S22,0xFCEFA3F8);
        c=GG(c,d,a,b,x[k+7], S23,0x676F02D9);
        b=GG(b,c,d,a,x[k+12],S24,0x8D2A4C8A);
        a=HH(a,b,c,d,x[k+5], S31,0xFFFA3942);
        d=HH(d,a,b,c,x[k+8], S32,0x8771F681);
        c=HH(c,d,a,b,x[k+11],S33,0x6D9D6122);
        b=HH(b,c,d,a,x[k+14],S34,0xFDE5380C);
        a=HH(a,b,c,d,x[k+1], S31,0xA4BEEA44);
        d=HH(d,a,b,c,x[k+4], S32,0x4BDECFA9);
        c=HH(c,d,a,b,x[k+7], S33,0xF6BB4B60);
        b=HH(b,c,d,a,x[k+10],S34,0xBEBFBC70);
        a=HH(a,b,c,d,x[k+13],S31,0x289B7EC6);
        d=HH(d,a,b,c,x[k+0], S32,0xEAA127FA);
        c=HH(c,d,a,b,x[k+3], S33,0xD4EF3085);
        b=HH(b,c,d,a,x[k+6], S34,0x4881D05);
        a=HH(a,b,c,d,x[k+9], S31,0xD9D4D039);
        d=HH(d,a,b,c,x[k+12],S32,0xE6DB99E5);
        c=HH(c,d,a,b,x[k+15],S33,0x1FA27CF8);
        b=HH(b,c,d,a,x[k+2], S34,0xC4AC5665);
        a=II(a,b,c,d,x[k+0], S41,0xF4292244);
        d=II(d,a,b,c,x[k+7], S42,0x432AFF97);
        c=II(c,d,a,b,x[k+14],S43,0xAB9423A7);
        b=II(b,c,d,a,x[k+5], S44,0xFC93A039);
        a=II(a,b,c,d,x[k+12],S41,0x655B59C3);
        d=II(d,a,b,c,x[k+3], S42,0x8F0CCC92);
        c=II(c,d,a,b,x[k+10],S43,0xFFEFF47D);
        b=II(b,c,d,a,x[k+1], S44,0x85845DD1);
        a=II(a,b,c,d,x[k+8], S41,0x6FA87E4F);
        d=II(d,a,b,c,x[k+15],S42,0xFE2CE6E0);
        c=II(c,d,a,b,x[k+6], S43,0xA3014314);
        b=II(b,c,d,a,x[k+13],S44,0x4E0811A1);
        a=II(a,b,c,d,x[k+4], S41,0xF7537E82);
        d=II(d,a,b,c,x[k+11],S42,0xBD3AF235);
        c=II(c,d,a,b,x[k+2], S43,0x2AD7D2BB);
        b=II(b,c,d,a,x[k+9], S44,0xEB86D391);
        a=AddUnsigned(a,AA);
        b=AddUnsigned(b,BB);
        c=AddUnsigned(c,CC);
        d=AddUnsigned(d,DD);
    }

    const temp = WordToHex(a)+WordToHex(b)+WordToHex(c)+WordToHex(d);

    return temp.toLowerCase();
};
