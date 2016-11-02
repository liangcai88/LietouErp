package com.ccai.utils;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Tools {

	public final static boolean isMobileNum(String mobileNum) {
		if (mobileNum == null)
			return false;
		int len = mobileNum.length();
		if (len < 11 || len > 14)
			return false;
		if ("13888888888".equals(mobileNum) || "13811111111".equals(mobileNum) || "13800138000".equals(mobileNum))
			return false;
		if (mobileNum.charAt(len - 11) == '1') {
			char c = 0;
			for (int i = len - 10; i < len; i++) {
				c = mobileNum.charAt(i);
				if (c < '0' || c > '9')
					return false;
			}
			return true;
		}
		return false;
	}

	public final static boolean stringIsNotNull(String str) {
		return str != null && !"".equals(str.toString().trim()) && !"null".equalsIgnoreCase(str);
	}
	
	/**
	 * 将对象转换成long值
	 * 
	 * @param str
	 * @return
	 */
	public final static long toLong(Object str)
	{
		return toLong(str, -1);
	}

	/**
	 * 将对象转换成long值
	 * @param str
	 * @param defaultValue	当对象为空时的缺省值
	 * @return
	 */
	public final static long toLong(Object str, long defaultValue)
	{
		if (str != null)
		{
			try
			{
				Class cType = str.getClass();
				if (cType == Double.class || cType == double.class)
					return ((Double) str).longValue();
				else if (cType == Float.class || cType == float.class)
					return ((Float) str).longValue();
				else if (cType == Long.class || cType == long.class)
					return ((Long) str).longValue();
				else if (cType == Integer.class || cType == int.class)
					return ((Integer) str).longValue();
				else if (cType == Character.class || cType == char.class)
					return (long)((Character) str).charValue();
				else if (cType == Byte.class || cType == byte.class)
					return ((Byte) str).longValue();
				else if (str instanceof Date)
					return ((Date) str).getTime();
				return Long.parseLong(str.toString());
			} catch (Exception e)
			{

			}
		}
		return defaultValue;
	}

	/**
	 * 将对象转换成int值
	 * 
	 * @param str
	 * @return
	 */
	public final static int toInt(Object str)
	{
		return toInt(str, -1);
	}

	/**
	 * 
	 * @param str
	 * @param defaultValue	当对象为空时的缺省值
	 * @return
	 */
	public final static int toInt(Object str, int defaultValue)
	{
		if (str != null)
		{
			try
			{
				Class cType = str.getClass();
				if (cType == Double.class || cType == double.class)
					return ((Double) str).intValue();
				else if (cType == Float.class || cType == float.class)
					return ((Float) str).intValue();
				else if (cType == Long.class || cType == long.class)
					return ((Long) str).intValue();
				else if (cType == Integer.class || cType == int.class)
					return ((Integer) str).intValue();
				else if (cType == Character.class || cType == char.class)
					return (int)((Character) str).charValue();
				else if (cType == Byte.class || cType == byte.class)
					return ((Byte) str).intValue();
				return Integer.parseInt(str.toString());
			} catch (Exception e)
			{

			}
		}
		return defaultValue;
	}

	/**
	 * 将对象转换成double值
	 * 
	 * @param str
	 * @return
	 */
	public final static double toDouble(Object str)
	{
		return toDouble(str, -1);
	}

	/**
	 * 将对象转换成double值
	 * @param str
	 * @param defaultValue	当对象为空时的缺省值
	 * @return
	 */
	public final static double toDouble(Object str, double defaultValue)
	{
		if (str != null)
		{
			try
			{
				Class cType = str.getClass();
				if (cType == Double.class || cType == double.class)
					return ((Double) str).doubleValue();
				else if (cType == Float.class || cType == float.class)
					return ((Float) str).doubleValue();
				else if (cType == Long.class || cType == long.class)
					return ((Long) str).doubleValue();
				else if (cType == Integer.class || cType == int.class)
					return ((Integer) str).doubleValue();
				else if (cType == Character.class || cType == char.class)
					return (double)((Character) str).charValue();
				else if (cType == Byte.class || cType == byte.class)
					return ((Byte) str).doubleValue();
				else if (str instanceof Date)
					return (double)((Date) str).getTime();
				return Double.parseDouble(str.toString());
			} catch (Exception e)
			{

			}
		}
		return defaultValue;
	}

	/**
	 * 将对象转换成float值
	 * 
	 * @param str
	 * @return
	 */
	public final static float toFloat(Object str)
	{
		return toFloat(str, -1);
	}

	/**
	 * 将对象转换成float值
	 * @param str
	 * @param defaultValue	当对象为空时的缺省值
	 * @return
	 */
	public final static float toFloat(Object str, float defaultValue)
	{
		if (str != null)
		{
			try
			{
				Class cType = str.getClass();
				if (cType == Double.class || cType == double.class)
					return ((Double) str).floatValue();
				else if (cType == Float.class || cType == float.class)
					return ((Float) str).floatValue();
				else if (cType == Long.class || cType == long.class)
					return ((Long) str).floatValue();
				else if (cType == Integer.class || cType == int.class)
					return ((Integer) str).floatValue();
				else if (cType == Character.class || cType == char.class)
					return (float)((Character) str).charValue();
				else if (cType == Byte.class || cType == byte.class)
					return ((Byte) str).floatValue();
				return Float.parseFloat(str.toString());
			} catch (Exception e)
			{

			}
		}
		return defaultValue;
	}


	public final static boolean stringIsNotNull(String... str) {
		boolean ok = false;
		if (str != null) {
			for (String s : str) {
				if (!stringIsNotNull(s))
					return false;
				else
					ok = true;
			}
		}
		return ok;
	}

	
	/**
	 * 获取资源文件路径
	 * @param fileName
	 * @return
	 */
	public final static String getResourceFileName(String fileName)
	{
		return replaceAll(Tools.class.getResource("/").getPath(), "%20", " ") + fileName;
	}
	
	  /**
     * 将字符串中某些字符串替换成其他字符串
     * @param str
     * @param key
     * @param to
     * @return
     */
    public final static String replaceAll(String str, String key, String to)
    {
        if(str != null && key != null
                && !str.equals("")&& !key.equals(""))
        {
            StringBuffer strbuf = new StringBuffer();
            int begin = 0, slen = str.length();
            for(int npos = 0, klen = key.length();
                begin < slen && (npos = str.indexOf(key, begin)) >= begin;
                begin = npos + klen)
            {
                strbuf.append(str.substring(begin, npos));
                if(to != null)
                    strbuf.append(to);
            }
            if(begin == 0)
                return str;
            if(begin < slen)
                strbuf.append(str.substring(begin));
            return strbuf.toString();
        }
        return str;
    }
    
    
    public static <T> T getEnumValue(Class<T> enumClass,int value){
		if(enumClass.isEnum()){
			T[] ems =enumClass.getEnumConstants();
			for (int i = 0; i < ems.length; i++) {
				if(i==value){ 
					return ems[i];
				}
			}
		}
		return null;
	}
	 
	public static <T extends Enum<?>> T getEnumValue(Class<T> enumClass,String name){
		if(enumClass.isEnum()){
			T[] ems =enumClass.getEnumConstants();
			for (int i = 0; i < ems.length; i++) {
				if(ems[i].name().equalsIgnoreCase(name)){ 
					return ems[i];
				}
			}
		}
		return null;
	}
	
	private static final String checkPattern = "^([a-z0-9A-Z]+[\\_|\\-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 判断所给字符串
	 * @param emailAddress
	 * @return
	 */
	public final static boolean isEmail(String emailAddress)
	{
		return emailAddress != null && emailAddress.matches(checkPattern);
	}
	
	/**
	 * str转date
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Date getDatefromString(String str,String pattern){
		Date date=null;
		SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
		try {
			date=dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
