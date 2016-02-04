package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;

import java.util.Timer;

/**
 * Created by Dan on 11/12/2015.
 */

public class DucksAutonomous extends AutonomousCommands{
    @Override
    public void runOpMode() throws InterruptedException{
        waitOneFullHardwareCycle();
        winchwheelMC=hardwareMap.dcMotorController.get("winchwheelMC");
        leftsweepMC=hardwareMap.dcMotorController.get("leftsweepMC");
        rightpivotMC=hardwareMap.dcMotorController.get("rightpivotMC");
        gyro=hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        while(gyro.isCalibrating()){
            telemetry.addData("lol", "lol");
        }
        waitForStart();
        sleep(500);
        goForward(.8, 500);
        sleep(300);
        turnRight(40,.5);
        sleep(200);
        goForward(.8,2100);
        sleep(200);
        turnRight(85,.5);
        sleep(300);
        goForward(.8,1500);
        leftsweepMC.setMotorPower(LEFT,0);
        rightpivotMC.setMotorPower(RIGHT,0);
    }
}