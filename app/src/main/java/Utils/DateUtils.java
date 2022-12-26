package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private int year;
    private int month;
    private int day;
    private int Hour;
    private int minute;
    private int second;

    private Date date;
    public SimpleDateFormat formatter;

    public DateUtils() {
        this(Calendar.getInstance().getTime());
    }

    public DateUtils(String pattern){
        this(new SimpleDateFormat(pattern));
    }

    public DateUtils(SimpleDateFormat formatter) {
        this(Calendar.getInstance().getTime(), formatter);
    }

    public DateUtils(Date date) {
        this(date, new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z"));
    }

    public DateUtils(Date date, SimpleDateFormat formatter) {
        this.date = date;
        this.formatter = formatter;
        this.year = Integer.valueOf(new SimpleDateFormat("yyyy").format(date));
        this.month = Integer.valueOf(new SimpleDateFormat("MM").format(date));
        this.day = Integer.valueOf(new SimpleDateFormat("dd").format(date));
        this.Hour = Integer.valueOf(new SimpleDateFormat("HH").format(date));
        this.minute = Integer.valueOf(new SimpleDateFormat("mm").format(date));
        this.second = Integer.valueOf(new SimpleDateFormat("ss").format(date));
    }

    public DateUtils(int year, int month, int day, int Hour, int minute, int second) throws ParseException {
        this(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(year+"-"+month+"-"+day+"-"+Hour+"-"+minute+"-"+second));
    }

    public DateUtils(String date,SimpleDateFormat formatter) throws ParseException {
        this(formatter.parse(date),formatter);
    }

    public DateUtils(String date,String pattern) throws ParseException {
        this(date,new SimpleDateFormat(pattern));
    }



    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return Hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return formatter.format(date);
    }
}
