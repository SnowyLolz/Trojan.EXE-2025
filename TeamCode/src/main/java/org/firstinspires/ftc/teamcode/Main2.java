package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Main(reversed motor)", group = "TeleOp")
public class Main2 extends OpMode {

    DcMotor frontLeft, frontRight, rearLeft, rearRight, arm, arm2, slider;
    Servo wrist, claw;
    double wristPOS;
    int mode;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        arm = hardwareMap.get(DcMotor.class, "arm");
        arm2 = hardwareMap.get(DcMotor.class, "arm2");
        slider = hardwareMap.get(DcMotor.class, "slider");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        arm2.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        wrist = hardwareMap.get(Servo.class, "wrist");

        claw = hardwareMap.get(Servo.class, "claw");


        wristPOS = 0.3;
        wrist.setPosition(0.42);
        claw.setPosition(0);
        mode = 1;
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        double frontLeftPower = y + x + rx;
        double frontRightPower = y - x - rx;
        double rearLeftPower = y - x + rx;
        double rearRightPower = y + x - rx;

        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower),
                Math.max(Math.abs(rearLeftPower), Math.abs(rearRightPower))));
        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            rearLeftPower /= max;
            rearRightPower /= max;
        }

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        rearLeft.setPower(rearLeftPower);
        rearRight.setPower(rearRightPower);

        slider.setPower(gamepad2.right_stick_y * .5);

        if(mode == 1) {
            if (gamepad2.left_stick_y == 0) {
                arm.setPower(0.15);
                arm2.setPower(0.15);
            } else if (gamepad2.left_stick_y < 0) {
                arm.setPower(gamepad2.left_stick_y * 0.05);
                arm2.setPower(gamepad2.left_stick_y * 0.05);

            } else if (gamepad2.left_stick_y > 0) {
                arm.setPower(gamepad2.left_stick_y * 0.65);
                arm2.setPower(gamepad2.left_stick_y * 0.65);

            }
        }

        if(mode == 2) {
            if (gamepad2.left_stick_y == 0) {
                arm.setPower(0.1);
                arm2.setPower(0.1);
            } else if (gamepad2.left_stick_y < 0) {
                arm.setPower(gamepad2.left_stick_y * 0.6);
                arm.setPower(gamepad2.left_stick_y * 0.6);
            } else if (gamepad2.left_stick_y > 0) {
                arm.setPower(gamepad2.left_stick_y * 0.8);
                arm.setPower(gamepad2.left_stick_y * 0.8);
            }
        }

        if (gamepad2.y) mode = 2;
        if (gamepad2.b) mode = 1;

        if(gamepad2.right_trigger != 0){
            claw.setPosition(0.2);
        }
        if(gamepad2.right_trigger == 0){
            claw.setPosition(0);
        }

        if (gamepad2.a){
            wrist.setPosition(wristPOS);
        }
        if (gamepad2.x){
            wrist.setPosition(0);
        }

        telemetry.addData("Mod", mode);
        telemetry.update();
    }

    @Override
    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        rearLeft.setPower(0);
        rearRight.setPower(0);
        arm.setPower(0);
        slider.setPower(0);
    }
}