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
        left.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        while (gyro.isCalibrating()) {
            telemetry.addData("lol", "lol");
        }
        waitForStart();
        plow.setPosition(PLOWDOWN);
        climbersLeft.setPosition(.3);
        climbersRight.setPosition(1);
        tray.setPosition(.5);

        //drive to bucket
        int target=left.getCurrentPosition()-Counts(130);
        while(left.getCurrentPosition() >= target) {
            right.setPower(.2);
            left.setPower(-.3);
            telemetry.addData("target",target);
            telemetry.addData("Left Enc: ", left.getCurrentPosition());
            waitOneFullHardwareCycle();
        }
        left.setPower(0);
        right.setPower(0);

        //turn towards bucket
        sleep(500);
        turnLeft(30,.2);
        sleep(500);

        //winch down in front of bucket
        rightpivotMC.setMotorPower(WINCHPIVOT, -.2);
        sleep(800);
        rightpivotMC.setMotorPower(WINCHPIVOT, 0);

        //winch out
        winchwheelMC.setMotorPower(WINCH, .9);
        winchwheelMC.setMotorPower(WINCHWHEEL, -1);
        sleep(4000);
        winchwheelMC.setMotorPower(WINCH, 0);
        winchwheelMC.setMotorPower(WINCHWHEEL, 0);
        sleep(500);

        //winch down over bucket
        rightpivotMC.setMotorPower(WINCHPIVOT, -.1);
        sleep(500);
        rightpivotMC.setMotorPower(WINCHPIVOT, 0);

        //winch in over bucket, dump climbers
        winchwheelMC.setMotorPower(WINCH, -.5);
        winchwheelMC.setMotorPower(WINCHWHEEL, .5);
        sleep(6000);
        winchwheelMC.setMotorPower(WINCH, 0);
        winchwheelMC.setMotorPower(WINCHWHEEL, 0);
        sleep(500);

        //winch out after dumping
        winchwheelMC.setMotorPower(WINCH, 1);
        winchwheelMC.setMotorPower(WINCHWHEEL, -1);
        sleep(1000);
        winchwheelMC.setMotorPower(WINCH, 0);
        winchwheelMC.setMotorPower(WINCHWHEEL, 0);

        //winch up after dumping
        rightpivotMC.setMotorPower(WINCHPIVOT, .1);
        sleep(300);
        rightpivotMC.setMotorPower(WINCHPIVOT, 0);

        //winch in after dumping
        winchwheelMC.setMotorPower(WINCH, -1);
        winchwheelMC.setMotorPower(WINCHWHEEL, 1);
        sleep(2000);
        winchwheelMC.setMotorPower(WINCH, 0);
        winchwheelMC.setMotorPower(WINCHWHEEL, 0);


    }
}
