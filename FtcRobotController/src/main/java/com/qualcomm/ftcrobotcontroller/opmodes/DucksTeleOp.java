package com.qualcomm.ftcrobotcontroller.opmodes;

import android.os.Looper;

import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.ftccommon.FtcRobotControllerService;
import com.qualcomm.ftccommon.Restarter;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.FileInputStream;
import java.util.logging.Handler;

/**
 * Created by Dan on 11/9/2015.
 */
public class DucksTeleOp extends TeleOpCommands {
    @Override
    public void init() {
        winchwheelMC=hardwareMap.dcMotorController.get("winchwheelMC");
        leftsweepMC=hardwareMap.dcMotorController.get("leftsweepMC");
        rightpivotMC=hardwareMap.dcMotorController.get("rightpivotMC");
        climbersLeft=hardwareMap.servo.get("climbersLeft");
        climbersRight=hardwareMap.servo.get("climbersRight");
        tray=hardwareMap.servo.get("tray");
        plow=hardwareMap.servo.get("plow");

        currentPosition=.5;
        tray.setPosition(.5);
        climbersLeft.setPosition(.5);
        climbersRight.setPosition(.5);
    }

    @Override
    public void loop() {


//        setDriveType();
        setWinchPower();
        setTankDrive();
        setPivotPower();
        setClimbersPosition();
        setSweeperPower();
        setTrayPosition();
        setPlowPosition();

        telemetry.addData("UPDATES", UPDATES);
        telemetry.addData("tray",tray.getPosition());
        telemetry.addData("climbersLeft",climbersLeft.getPosition());
        telemetry.addData("climbersRight",climbersRight.getPosition());
    }
    @Override
    public void stop(){

    }
}
