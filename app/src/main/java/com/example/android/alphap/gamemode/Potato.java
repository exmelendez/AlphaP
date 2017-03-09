package com.example.android.alphap.gamemode;

/**
 * Created by asiagibson on 3/4/17.
 */

public class Potato {

    public static final int POTATO_DUDE = 0 ;
    public static final int POTATO_HARY = 1 ;
    public static final int POTATO_THELMA = 2 ;
    public static final int POTATO_TONEE = 3 ;
    public static final int POTATO_TITO = 4 ;


    private int x ;
    private int y ;
    private int type;
    private double y_maximum;
    private double age ;
    private double a ;

    public Potato(int x,int y,int type)
    {
        this.x = x ;
        this.y = y ;
        this.type = type;
        y_maximum = 0;
        a = -9.8;
    }

    public Potato()
    {
        this(0,0,POTATO_DUDE);
    }

    public int get_x()
    {
        return x;
    }

    public int get_y()
    {
        return y;
    }

    public int get_type()
    {
        return type;
    }

    public void set_x(int x)
    {
        this.x = x ;
    }

    public void set_y(int y)
    {
        this.y = y ;
    }

    public void set_type(int type)
    {
        this.type = type;
    }

    public void set_y_final(double y_final)
    {
        this.y_maximum = y_final;
    }

    public double get_y_final()
    {
        return y_maximum;
    }

    public double get_age()
    {
        return age;
    }

    public void set_age(double age)
    {
        this.age = age;
    }

    public void move(int screen_height)
    {
        age+=.1;
        double vo = Math.sqrt(-a*2*y_maximum);
        double distance = vo*age + .5*a*age*age;
        y= (int) (screen_height - distance);
    }



}
