package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Main", group = "TeleOp")
public class Main extends OpMode {

    DcMotor frontLeft, frontRight, rearLeft, rearRight, lift1, lift2;
    Servo wrist, ankle, extend1, extend2, clawout, clawin;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        lift1 = hardwareMap.get(DcMotor.class, "lift1");
        lift2 = hardwareMap.get(DcMotor.class, "lift2");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        lift2.setDirection(DcMotorSimple.Direction.REVERSE);


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        wrist = hardwareMap.get(Servo.class, "wrist");
        clawout = hardwareMap.get(Servo.class, "clawout");
        clawin = hardwareMap.get(Servo.class, "clawin");
        ankle = hardwareMap.get(Servo.class,"ankle");
        extend1 = hardwareMap.get(Servo.class,"extend1");
        extend2 = hardwareMap.get(Servo.class,"extend2");


        wrist.setPosition(0);
        clawout.setPosition(0);
        clawin.setPosition(0);
        ankle.setPosition(0.6);
        extend1.setPosition(0);
        extend2.setPosition(0.21);
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

        lift1.setPower(gamepad2.left_stick_y * .5);
        lift2.setPower(gamepad2.left_stick_y * .5);

       if(gamepad2.right_trigger != 0){
           clawout.setPosition(0.2);
       }
        if(gamepad2.right_trigger == 0){
            clawout.setPosition(0);
        }

        if(gamepad2.left_trigger != 0){
            clawin.setPosition(0.2);
        }
        if(gamepad2.left_trigger == 0){
            clawin.setPosition(0);
        }

        if (gamepad2.a){
            wrist.setPosition(0.4);
        }
        if (gamepad2.x){
            wrist.setPosition(0);
        }

        if (gamepad2.b){
            ankle.setPosition(0.6);
        }
        if (gamepad2.y){
            ankle.setPosition(0);
        }

        if(gamepad2.right_stick_y < 0){
            extend1.setPosition(0.21);
        }
        if(gamepad2.right_stick_y >= 0){
            extend1.setPosition(0);
        }

        if(gamepad2.right_stick_y < 0){
            extend2.setPosition(0);
        }
        if(gamepad2.right_stick_y >= 0){
            extend2.setPosition(0.21);
        }

    }

    @Override
    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        rearLeft.setPower(0);
        rearRight.setPower(0);
        lift1.setPower(0);
        lift2.setPower(0);

    }
}
