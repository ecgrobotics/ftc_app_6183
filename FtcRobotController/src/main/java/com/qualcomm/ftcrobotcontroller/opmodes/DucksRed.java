package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Justin on 2/17/2016.
 */
public class DucksRed extends AutonomousCommands{
    public void runOpMode() throws InterruptedException{
        waitOneFullHardwareCycle();
        winchwheelMC = hardwareMap.dcMotorController.get("winchwheelMC");
        leftsweepMC = hardwareMap.dcMotorController.get("leftsweepMC");
        rightpivotMC = hardwareMap.dcMotorController.get("rightpivotMC");
        right=hardwareMap.dcMotor.get("right");
        left=hardwareMap.dcMotor.get("left");
        climbersLeft=hardwareMap.servo.get("climbersLeft");
        climbersRight=hardwareMap.servo.get("climbersRight");
        tray=hardwareMap.servo.get("tray");
        plow=hardwareMap.servo.get("plow");
        leftsweepMC.setMotorChannelMode(LEFT, DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightpivotMC.setMotorChannelMode(RIGHT, DcMotorController.RunMode.RUN_USING_ENCODERS);
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        while (gyro.isCalibrating()) {
            telemetry.addData("lol", "lol");
        }
        //turn left onto ramp
        waitForStart();
        plow.setPosition(PLOWDOWN);
        encoderForward(60,.4);
        encoderBackward(5,.4);
        turnLeft(90,.3);
        plow.setPosition(PLOWUP);
        encoderForward(30,.3);
        stop();
    }
}
