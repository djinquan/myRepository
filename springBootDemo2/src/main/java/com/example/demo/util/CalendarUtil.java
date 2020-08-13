package com.example.demo.util;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
	
	public static int getNowYear(){
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}
	public static int getNowMonth(){
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH)+1;
	}
	
	/**
	 * 格式化日期字符串
	 * @param date
	 * @param format
	 * @return 把yyyyMMddHHmmSS 转换成日期字符串
	 */
	@SuppressWarnings("deprecation")
	public static String formatDate(Date date,String format){
		if (date == null)
			return "";
		if(format==null || format.equals(""))
			format="yyyy-MM-dd";
		int y = date.getYear()+1900;
		int m = date.getMonth()+1;
		int d = date.getDate();
		int h = date.getHours();
		int n = date.getMinutes();
		int s = date.getSeconds();
		String months=m<10?"0"+m:m+"";
		String days = d<10?"0"+d:d+"";
		String hours = h<10?"0"+h:h+"";
		String mins = n<10?"0"+n:n+"";
		String secs = s<10?"0"+s:s+"";
		format = format.replaceAll("yyyy", y+"");
		format = format.replaceAll("YYYY", y+"");
		format = format.replaceAll("MM", months+"");
		format = format.replaceAll("dd", days+"");
		format = format.replaceAll("DD", days+"");
		format = format.replaceAll("hh", hours+"");
		format = format.replaceAll("HH", hours+"");
		format = format.replaceAll("mm", mins+"");
		format = format.replaceAll("ss", secs+"");
		format = format.replaceAll("SS", secs+"");
		return format;
	}
	
	public static String formatDate(Date date){
		return formatDate(date,null);
	}
	
	public static Date getDate(String str,String split){
		if(str==null || str.trim().equals("")){
			return null;
		}
		try{
			if(split==null || split.equals(""))
				split="-";
			String[] ary = str.split(split);
			int y = Integer.parseInt(ary[0]);
			int m = Integer.parseInt(ary[1]);
			int d = Integer.parseInt(ary[2]);
			Calendar c = Calendar.getInstance();
			c.clear();
			c.set(y, m-1, d,0,0,0);
			return c.getTime();
		}
		catch(Exception e){
			return null;
		}
	}
	
	public static Date getDate(String str){
		return getDate(str,"-");
	}
	
	public static Date getDateTime(String str,String format){
		if(format==null) format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try{
			return sdf.parse(str);
		}
		catch(Exception e){
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void setDateBegin(Date d){
		d.setHours(0);
		d.setMinutes(0);
		d.setSeconds(0);
	}
	
	@SuppressWarnings("deprecation")
	public static void setDateEnd(Date d){
		d.setHours(23);
		d.setMinutes(59);
		d.setSeconds(59);
	}
	
	public static Date delayDay(int day){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, day);//计算n天后的时间
		return c.getTime();
	}
	//某日期的n天之后的时间
	public static String delayDay(String date,int n ) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date d=sdf.parse(date);
		long time=d.getTime()+(long)n* 24 * 60 * 60 * 1000;
		return sdf.format(new Date(time));
	}
	//某日期的n天之后的时间
	public static Date delayDay2(Date date,int n ) throws ParseException{
		Date d=date;
		long time=d.getTime()+(long)n* 24 * 60 * 60 * 1000;
		return new Date(time);
	}
	//计算时差
	public static BigDecimal getHour(Date sdate,Date edate) {
		BigDecimal nh = new BigDecimal(1000 * 60 * 60);
		BigDecimal diff = new BigDecimal(edate.getTime() - sdate.getTime());
		return diff.divide(nh,2,BigDecimal.ROUND_HALF_UP);
	}
}
