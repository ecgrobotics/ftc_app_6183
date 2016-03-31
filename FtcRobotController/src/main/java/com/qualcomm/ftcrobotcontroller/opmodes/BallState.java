package com.qualcomm.ftcrobotcontroller.opmodes;


import org.opencv.core.Point;

/**
 * Created by Justin on 3/27/2016.
 */
public class BallState {
    private int radius;
    private Point center;
    private double distance;//inches
    private double angle;
    private long millis;
    private double leftEncoder;
    private double rightEncoder;
    private double leftPower;
    private double rightPower;
    private double heading;

    public BallState(double x, double y, int radius){
        this.center=new Point(x,y);
        this.radius=radius;
        this.millis=System.currentTimeMillis();
        this.angle=(y-320)*(30.145/320);
        this.distance=1.4/Math.tan(Math.toRadians(radius*(30.145/320)));
        this.leftEncoder=VisionTest.leftMotor.getCurrentPosition();
        this.rightEncoder=VisionTest.rightMotor.getCurrentPosition();
        this.leftPower=VisionTest.leftMotor.getPower();
        this.rightPower=VisionTest.rightMotor.getPower();
        this.heading=VisionTest.gyro.getHeading();
    }
    public double angle(){
        return this.angle;
    }
    public long millis(){
        return this.millis;
    }
    public double distance(){
        return this.distance;
    }
    public Point center(){
        return this.center;
    }
    public int radius(){
        return this.radius;
    }
    public double leftPower(){
        return this.leftPower;
    }
    public double rightPower(){
        return this.rightPower;
    }
    public double leftEncoder(){
        return this.leftEncoder;
    }
    public double rightEncoder(){
        return this.rightEncoder;
    }
    public double heading() {
        return this.heading;
    }
}
