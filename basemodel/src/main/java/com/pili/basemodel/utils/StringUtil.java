/*
 * Copyright (C) 2014 Togic Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pili.basemodel.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static Pattern IMG_URL = Pattern
            .compile(".*?(gif|jpeg|png|jpg|bmp)");

    private final static Pattern URL = Pattern
            .compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");

    private final static ThreadLocal<SimpleDateFormat> YYYYMMDDHHMMSS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
    };

    private final static ThreadLocal<SimpleDateFormat> YYYYMMDD = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
    };

    private final static ThreadLocal<SimpleDateFormat> YYYYMMDDHHMM = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        }
    };

    private static final Pattern UniversalDatePattern = Pattern.compile(
            "([0-9]{4})-([0-9]{2})-([0-9]{2})[\\s]+([0-9]{2}):([0-9]{2}):([0-9]{2})"
    );

    private static final String SPLIT = ",";
    private static final char DBC_CHAR_START = 33;
    private static final char DBC_CHAR_END = 126;
    private static final char SBC_SPACE = 12288;
    private static final char DBC_SPACE = ' ';
    private static final int CONVERT_STEP = 65248;



    public static final int string2Int(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static final long string2Long(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static boolean isStringArrayValid(String[] array) {
        return array != null && array.length > 0;
    }

    /**
     * after trim
     */
    public static final boolean isEmptyString(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * none trim
     */
    public static final boolean isEqualsString(String s1, String s2) {
        if (s1 == s2) {
            return true;
        } else if (s1 == null || s2 == null) {
            return false;
        } else {
            return s1.equals(s2);
        }
    }

    public static final boolean isUrlString(String str) {
        return str != null && str.startsWith("http");
    }

    public static final boolean isColorString(String str) {
        return str != null && (str.startsWith("#") || str.startsWith("color"));
    }

    public static final boolean isLocalPath(String str) {
        return str != null && str.startsWith("file");
    }

    public static final boolean isAssetFile(String str) {
        return str != null && str.startsWith("asset");
    }

    public static int getStringIndex(List<String> source, String target) {
        if (source == null || target == null) {
            return -1;
        }

        int index = 0;
        int size = source.size();
        for (; index < size; index++) {
            if (target.equals(source.get(index))) {
                return index;
            }
        }
        return -1;
    }

    public static boolean isEqualStringList(List<String> l1, List<String> l2) {
        boolean same = false;
        if (l1 != null && l2 != null) {
            int i;
            int size = l1.size();
            if (size == l2.size()) {
                for (i = 0; i < size; i++) {
                    if (!isEqualsString(l1.get(i), l2.get(i))) {
                        break;
                    }
                }
                if (i == size) {
                    same = true;
                }
            }
        }
        return same;
    }

    public static boolean findSameString(List<String> source, String target) {
        return getStringIndex(source, target) >= 0;
    }

    public static String convert2String(List<String> s) {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        for (int i = 0; i < s.size(); i++) {
            if (i > 0) {
                sb.append(SPLIT);
            }
            sb.append(s.get(i));
        }
        sb.append("}");
        return sb.toString();
    }

    public static List<String> convert2List(String s) {
        if (isEmptyString(s)) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        s = s.substring(1, s.length() -1);// rm {}
        String[] str = s.split(SPLIT);
        for (int i = 0; i < str.length; i++) {
            list.add(str[i]);
        }
        return list;
    }

    public static final String getStringFromFile(Context ctx, String fileName) {
        try {
            File f = ctx.getFileStreamPath(fileName);
            if (f != null && f.exists()) {
                return getStringFromInputStream(ctx.openFileInput(fileName));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringFromFile(String path) throws FileNotFoundException, IOException {
        if (isEmptyString(path)) {
            return "";
        }
        return getStringFromFile(new File(path));
    }

    public static String getStringFromFile(File file) {
        if (file == null || !file.exists() || file.length() <= 0) {
            return "";
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
        }
        return "";
    }
    
    public static final String getStringFromStream(InputStream in) {
        try {
            final StringBuilder sb = new StringBuilder(in.available());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            char[] buffer = new char[1024];
            int len = 0;
            while ((len = br.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeStream(in);
        }
        return "";
    }
    
    public static final String getStringFromAsset(Context ctx, String fileName) {
        try {
            InputStream stream = ctx.getAssets().open(fileName,
                    AssetManager.ACCESS_STREAMING);
            return getStringFromInputStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public static final String getStringFromInputStream(InputStream in) {
        if (in == null) {
            return "";
        }
        
        BufferedReader br = null;
        try {
            final StringBuilder sb = new StringBuilder(in.available());
            br = new BufferedReader(new InputStreamReader(in));

            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeStream(in);
            FileUtil.closeStream(br);
        }
        return "";
    }

    public static String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset;
            }

            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.close();
            bis = new BufferedInputStream(new FileInputStream(file));
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
                System.out.println(loc + " " + Integer.toHexString(read));
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    public static int getNumber(String str) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher ma = pattern.matcher(str);
        int number = 0;
        while (ma.find()) {
            number = Integer.valueOf(ma.group());
        }
        return number;
    }

    public static String toSBC(String str) {
        if (isEmptyString(str)) {
            return str;
        }
        StringBuilder buf = new StringBuilder(str.length());
        char[] ca = str.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            if (ca[i] == DBC_SPACE) {
                buf.append(SBC_SPACE);
            } else if ((ca[i] >= DBC_CHAR_START) && (ca[i] <= DBC_CHAR_END)) {
                buf.append((char) (ca[i] + CONVERT_STEP));
            } else {
                buf.append(ca[i]);
            }
        }
        String newStr = buf.toString();
        newStr = newStr.replaceAll("“", " “ ");
        newStr = newStr.replaceAll("”", " ” ");
        return newStr;
    }

    public static String endWithslash(String str) {
        if (!isEmptyString(str) && !str.endsWith("/")) {
            return str + "/";
        }
        return str;
    }
    
    
    /**
    * is null or its length is 0 or it is made by space
    * 
    * <pre>
    * isBlank(null) = true;
    * isBlank(&quot;&quot;) = true;
    * isBlank(&quot;  &quot;) = true;
    * </pre>
    * 
    * @param str
    * @return if string is null or its size is 0 or it is made by space, return true, else return false.
    */
   public static boolean isBlank(String str) {
       return (str == null || str.trim().length() == 0);
   }

   /**
    * is null or its length is 0
    * 
    * <pre>
    * isEmpty(null) = true;
    * isEmpty(&quot;&quot;) = true;
    * isEmpty(&quot;  &quot;) = false;
    * </pre>
    * 
    * @param str
    * @return if string is null or its size is 0, return true, else return false.
    */
   public static boolean isEmpty(String str) {
       return (str == null || str.length() == 0);
   }

   /**
    * compare two string
    * 
    * @param actual
    * @param expected
    * @return
    */
   public static boolean isEquals(String actual, String expected) {
       return isEqualsString(actual, expected);
   }

   /**
    * null string to empty string
    * 
    * <pre>
    * nullStrToEmpty(null) = &quot;&quot;;
    * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
    * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
    * </pre>
    * 
    * @param str
    * @return
    */
   public static String nullStrToEmpty(String str) {
       return (str == null ? "" : str);
   }

   /**
    * capitalize first letter
    * 
    * <pre>
    * capitalizeFirstLetter(null)     =   null;
    * capitalizeFirstLetter("")       =   "";
    * capitalizeFirstLetter("2ab")    =   "2ab"
    * capitalizeFirstLetter("a")      =   "A"
    * capitalizeFirstLetter("ab")     =   "Ab"
    * capitalizeFirstLetter("Abc")    =   "Abc"
    * </pre>
    * 
    * @param str
    * @return
    */
   public static String capitalizeFirstLetter(String str) {
       if (isEmpty(str)) {
           return str;
       }

       char c = str.charAt(0);
       return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
           : new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1)).toString();
   }

   /**
    * encoded in utf-8
    * 
    * <pre>
    * utf8Encode(null)        =   null
    * utf8Encode("")          =   "";
    * utf8Encode("aa")        =   "aa";
    * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
    * </pre>
    * 
    * @param str
    * @return
    * @throws UnsupportedEncodingException if an error occurs
    */
   public static String utf8Encode(String str) {
       if (!isEmpty(str) && str.getBytes().length != str.length()) {
           try {
               return URLEncoder.encode(str, "UTF-8");
           } catch (UnsupportedEncodingException e) {
               throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
           }
       }
       return str;
   }

   /**
    * encoded in utf-8, if exception, return defultReturn
    * 
    * @param str
    * @param defultReturn
    * @return
    */
   public static String utf8Encode(String str, String defultReturn) {
       if (!isEmpty(str) && str.getBytes().length != str.length()) {
           try {
               return URLEncoder.encode(str, "UTF-8");
           } catch (UnsupportedEncodingException e) {
               return defultReturn;
           }
       }
       return str;
   }

    /***
     * 截取字符串
     *
     * @param start 从那里开始，0算起
     * @param num   截取多少个
     * @param str   截取的字符串
     * @return
     */
    public static String getSubString(int start, int num, String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > length) {
            start = length;
        }
        if (num < 0) {
            num = 1;
        }
        int end = start + num;
        if (end > length) {
            end = length;
        }
        return str.substring(start, end);
    }
    /**
     * 将一个InputStream流转换成字符串
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuilder res = new StringBuilder();
        BufferedReader read = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = read.readLine()) != null) {
                res.append(line).append("<br>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                read.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res.toString();
    }


    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断一个url是否为图片url
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return IMG_URL.matcher(url).matches();
    }

    /**
     * 判断是否为一个合法的url地址
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        return URL.matcher(str).matches();
    }





   /**
    全角转半角
    * @param s
    * @return
    */
   public static String fullWidthToHalfWidth(String s) {
       if (isEmpty(s)) {
           return s;
       }

       char[] source = s.toCharArray();
       for (int i = 0; i < source.length; i++) {
           if (source[i] == 12288) {
               source[i] = ' ';
               // } else if (source[i] == 12290) {
               // source[i] = '.';
           } else if (source[i] >= 65281 && source[i] <= 65374) {
               source[i] = (char)(source[i] - 65248);
           } else {
               source[i] = source[i];
           }
       }
       return new String(source);
   }

   /**
   半角转全角
    * @param s
    * @return
    */
   public static String halfWidthToFullWidth(String s) {
       if (isEmpty(s)) {
           return s;
       }

       char[] source = s.toCharArray();
       for (int i = 0; i < source.length; i++) {
           if (source[i] == ' ') {
               source[i] = (char)12288;
               // } else if (source[i] == '.') {
               // source[i] = (char)12290;
           } else if (source[i] >= 33 && source[i] <= 126) {
               source[i] = (char)(source[i] + 65248);
           } else {
               source[i] = source[i];
           }
       }
       return new String(source);
   }
}
