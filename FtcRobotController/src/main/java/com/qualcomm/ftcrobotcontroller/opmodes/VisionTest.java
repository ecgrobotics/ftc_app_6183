package com.qualcomm.ftcrobotcontroller.opmodes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.robocol.Telemetry;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;


/**
 * Created by Justin on 2/26/2016.
 */
public class VisionTest extends OpMode implements CameraBridgeViewBase.CvCameraViewListener2 {
    public static Mat mat;
    public static Mat mat2;
    public static Mat circles;
    public static Mat gray;
    public static ArrayList<Mat> dsts;
    public static Size matsizesmall;
    public static Size matsizelarge;
    public static Size previewsize;
    public static Scalar red;
    public static Scalar green;
    public Point center=new Point();
    public Point imagecenter=new Point();
    public int offset;
    public int radius;
    public Mat areaOfInterest;
    public int framecount=0;
    public Point lastcenter=new Point();
    public Point newcenter=new Point();
    public int down;
    public int up;
    public int left;
    public int right;
    double referencex;
    double referencey;
    public Point topleft=new Point();
    public Point bottomright=new Point();
    public Point velocity=new Point();
    public int submat;
    double[] zero={0,0};
    double radiusscalar=3;
    public static DcMotor rightMotor;
    public static DcMotor leftMotor;
    public static GyroSensor gyro;
    int found=1;
    Scalar blue;
    Point tl=new Point(0,305);
    Point br=new Point(360,335);
    public Ball ball=new Ball();
    private CameraBridgeViewBase mCameraView;
    private int initialHeading;
    private int targetHeading;

    public void onCameraViewStarted(int width, int height) {
    }
    public void onCameraViewStopped(){
    }
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mat=inputFrame.gray();
        Imgproc.resize(mat,mat,matsizesmall);
        Core.transpose(mat, mat2);
        Core.flip(mat2, mat2, 1);
        mat2.copyTo(gray);
//        Imgproc.cvtColor(mat2, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 13);
        if(framecount==0){
            gray.copyTo(areaOfInterest);
            framecount=1;
            submat=0;
        }else{
            double speed=Math.sqrt(Math.pow(velocity.x,2)+Math.pow(velocity.y,2));
            left=(int)(center.x-(radiusscalar+speed/50)*radius+1.2*velocity.x);
            right=(int)(center.x+(radiusscalar+speed/50)*radius+1.2*velocity.x);
            up=(int)(center.y-(radiusscalar+speed/50)*radius+1.2*velocity.y);
            down=(int)(center.y+(radiusscalar+speed/50)*radius+1.2*velocity.y);
            submat=1;
            if(left<0){
                left=1;
            }
            if(right>gray.cols()){
                right=gray.cols()-1;
            }
            if(up<0){
                up=1;
            }
            if(down>gray.rows()){
                down=gray.rows()-1;
            }
            if(left>right){
                left=0;
                right=gray.cols()-1;
                up=0;
                down=gray.rows()-1;
            }else if(up>down){
                left=0;
                right=gray.cols()-1;
                up=0;
                down=gray.rows()-1;
            }
            areaOfInterest = gray.submat(up, down, left, right);
            referencex=center.x-left;
            referencey=center.y-up;
        }
        Imgproc.HoughCircles(areaOfInterest, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 2000, 100, 40, 0, 0);
        double[] imagepoint={mat2.width()/2,mat2.height()/2};
        imagecenter.set(imagepoint);
        for(int i=0;i<circles.cols();i++){
            double c[]=circles.get(0,i);
            double[] point={center.x+c[0]-referencex,center.y+c[1]-referencey};
            if(areaOfInterest.get((int)c[1],(int)c[0])[0]>100){
                center.set(point);
                radius=(int)c[2];
                double[] topleft1={left,up};
                double[] bottomright1={right,down};
                topleft.set(topleft1);
                bottomright.set(bottomright1);
                ball.setState(center.x, center.y, radius);
                if(submat==0){
                    lastcenter.set(point);
                }
                double[] velocitypoint={center.x-lastcenter.x,center.y-lastcenter.y};
                velocity.set(velocitypoint);
                lastcenter.set(point);
                submat=1;
            }else{
                framecount=0;
                center.set(zero);
                referencex=0;
                referencey=0;
                velocity.set(zero);
                submat=0;
            }
        }
        if(ball.exists()) {
            Imgproc.circle(mat2, ball.getCenter(), ball.getRadius(), red, 5);
            Imgproc.circle(mat2, ball.getCenter(), 3, green, 5);
            Imgproc.rectangle(mat2, topleft, bottomright, green, 3);
        }
        if(circles.cols()==0){
            framecount=0;
            center.set(zero);
            referencex=0;
            referencey=0;
            velocity.set(zero);
            submat=0;
        }
        offset=(int)(imagecenter.y-center.y);
//        Imgproc.resize(mat2, mat2, previewsize);
        //previewsize numbers must match those in AllocateCache in CameraBridgeViewBase.java
        return mat2;
    }


    @Override
    public void init() {
        mat=new Mat();
        mat2=new Mat();
        gray=new Mat();
        circles=new Mat();
        matsizesmall= new Size(640,360);
        matsizelarge=new Size(720,405);
        previewsize=new Size(360,640);
        green=new Scalar(0,255,0);
        red=new Scalar(255,0,0);
        blue=new Scalar(0,0,255);
        dsts=new ArrayList<Mat>();
        leftMotor=hardwareMap.dcMotor.get("left");
        rightMotor=hardwareMap.dcMotor.get("right");
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        gyro=hardwareMap.gyroSensor.get("gyro");
        setUpView();
    }
    public void setUpView(){
        mCameraView=FtcRobotControllerActivity.mOpenCvCameraView;
        mCameraView.enableView();
        mCameraView.setVisibility(SurfaceView.VISIBLE);
        mCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void loop() {
        leftMotor.setPower(gamepad1.left_stick_y/3);
        rightMotor.setPower(-gamepad1.right_stick_y/3);
        if(gamepad1.a){
            ball.setMode(Ball.MODE_SEARCHING);
        }
        if(ball.getMode()==Ball.MODE_SEARCHING){
            if(Math.abs(gyro.getHeading()-ball.getTargetHeading())<5){
                leftMotor.setPower(0);
                rightMotor.setPower(0);
            }else{
                leftMotor.setPower(.1);  //turn right
                rightMotor.setPower(.1);
            }
        }

//        if(gamepad1.a){               obsolete
//            found=0;
//        }
//        if(gamepad1.b){
//            found=1;
//        }
//        if(found==0){
//           if(Math.abs(offset)>15){
//                if(offset>0){
//                    leftMotor.setPower(0);
//                    rightMotor.setPower(.05);
//                }else if(offset<0){
//                    leftMotor.setPower(-.05);
//                    rightMotor.setPower(0);
//                }
//            }else if(Math.abs(offset)<15){
//                if(Math.abs(radius-80)>10){//ball larger than 100+-10 pixels
//                    double scale=.200-(.100*(radius/80.000));
//                    telemetry.addData("Scale",scale);
//                    leftMotor.setPower(-scale);
//                    rightMotor.setPower(scale);
//                }
////                else if(radius-80<-10){//ball smaller than 100+- pixels
////                    telemetry.addData("Turn", "");
////                    telemetry.addData("Drive", "forwards");
////                    leftMotor.setPower(-.1);
////                    rightMotor.setPower(.1);
////                }
//                else if(Math.abs(radius-80)<=10){//ball within 10 pixels of 100
//                    rightMotor.setPower(0);
//                    leftMotor.setPower(0);
//                    found=1;
//                }
//            }else{
//                telemetry.addData("Turn","cannot find ball");
//            }
//        }
//        telemetry.addData("Left Power",leftMotor.getPower());
//        telemetry.addData("Right Power",rightMotor.getPower());
    }
    @Override
    public void stop(){
        mCameraView.disableView();
    }
}
