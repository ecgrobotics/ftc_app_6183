package com.qualcomm.ftcrobotcontroller.opmodes;

import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Justin on 3/18/2016.
 */
public class Ball {
    private LinkedList<BallState> states=new LinkedList();
    private int MODE;
    private int numberOfSearches;
    private int targetHeading;
    private int startHeading;
    public static final int MODE_SEARCHING=0;
    public static final int MODE_TRACKING=1;
    public Ball(){

    }

    public void setState(double x, double y, int radius){
        BallState ballstate = new BallState(x, y, radius);
        double headingOffset=ballstate.heading()-states.getLast().heading();
        if(headingOffset>180){
            headingOffset=headingOffset-360;
        }else if(headingOffset<180){
            headingOffset=headingOffset+360;
        }
        double angleOffset=ballstate.angle()-states.getLast().angle();
        switch(MODE){
            case MODE_SEARCHING:
                if(ballstate.leftPower()==0 &&ballstate.rightPower()==0){



                    this.targetHeading=this.startHeading+60*numberOfSearches;
                    if(this.targetHeading>360){
                        this.targetHeading=this.targetHeading-360;
                    }
                }
                break;
            case MODE_TRACKING:
                if(exists()) {
                    if (states.size() < 6) {
                        double v=velocity(ballstate);
                        if (ballstate.leftPower()==0 &&ballstate.rightPower()==0 && v<10){ //robot is stopped, ball moving very slowly
                            states.add(ballstate);
                        }else if(Math.abs(headingOffset+angleOffset)<5){ //gyro offset and ball offset are within 5 degrees(ball is stopped)
                            states.add(ballstate);
                        }
                    } else {
                        double v =velocity(ballstate);
                        if (ballstate.leftPower()==0 &&ballstate.rightPower()==0 && v<10){ //robot is stopped, ball moving very slowly
                            states.removeFirst();
                            states.add(ballstate);
                        }else if(Math.abs(headingOffset+angleOffset)<5){ //gyro offset and ba   ll offset are within 5 degrees
                            states.add(ballstate);
                        }
                    }
                }else{
                    states.add(ballstate);
                }
                break;
        }
    }
    public double velocity(BallState ballstate){
        double theta = Math.abs(ballstate.angle() - states.getLast().angle());
        double displacement = Math.sqrt(Math.pow(ballstate.distance(), 2) + Math.pow(states.getLast().distance(), 2)
                - 2 * ballstate.distance() * states.getLast().distance() * Math.cos(Math.toRadians(theta)));
        double v = (displacement / (ballstate.millis() - states.getLast().millis())) * 1000;
        return v;
    }
    public double getAngle(){
        return states.getLast().angle();
    }
    public long getMillis(){
        return states.getLast().millis();
    }
    public double getDistance(){
        return states.getLast().distance();
    }
    public int getRadius(){
        return states.getLast().radius();
    }
    public Point getCenter(){
        return states.getLast().center();
    }
    public boolean exists(){
        if(states.size()>0){
            return true;
        }else{
            return false;
        }
    }
    public void setMode(int mode){
        this.MODE=mode;
        if(mode==MODE_SEARCHING){
            this.numberOfSearches=0;
            this.targetHeading=VisionTest.gyro.getHeading();
            this.startHeading=VisionTest.gyro.getHeading();
        }
    }
    public int getMode(){
        return this.MODE;
    }
    public int getTargetHeading(){
        if()//time elapsed is more than 2 seconds turn again(add 1 to numberofsearches)
        return this.targetHeading;
    }
}
