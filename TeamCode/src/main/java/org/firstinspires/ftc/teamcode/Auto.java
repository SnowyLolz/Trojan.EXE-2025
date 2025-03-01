package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Auto", group = "Autonomous")
public class Auto extends LinearOpMode {

    private DcMotor frontLeft, frontRight, rearLeft, rearRight,lift1, lift2;

    private Servo clawout, wrist;

    private static final double COUNTS_PER_CM = 800;

    private static final double RAISE_PPR = 538;




    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        lift1 = hardwareMap.get(DcMotor.class, "lift1");
        lift2 = hardwareMap.get(DcMotor.class, "lift2");
        clawout = hardwareMap.get(Servo.class,"clawout");
        wrist = hardwareMap.get(Servo.class,"wrist");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        lift1.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        if (opModeIsActive()) {

            clawout.setPosition(0);
            raise(4);
            moveForward(-16);
            sleep(500);
            lower(1.3);
            sleep(300);
            clawout.setPosition(0.25);
            sleep(500);
            lower(3);

        }
    }

    private void moveForward(double distanceCm) {
        int targetCounts = (int) (distanceCm * COUNTS_PER_CM);
        int startPosition = frontLeft.getCurrentPosition();

        while (opModeIsActive() && Math.abs(frontLeft.getCurrentPosition() - startPosition) < Math.abs(targetCounts)) {
            double power = distanceCm > 0 ? 0.5 : -0.5;
            setMotorPowers(power, power, power, power);
            auxiliaryPowers(0.1,0.1);
            telemetry.addData("Forward Distance", frontLeft.getCurrentPosition() - startPosition);
            telemetry.update();
        }
        stopMotors();
    }

    private void strafeRight(double distanceCm) {
        int targetCounts = (int) (distanceCm * COUNTS_PER_CM);
        int startPosition = frontRight.getCurrentPosition();

        while (opModeIsActive() && Math.abs(frontRight.getCurrentPosition() - startPosition) < Math.abs(targetCounts)) {
            double power = distanceCm > 0 ? 0.5 : -0.5;
            setMotorPowers(power, -power, -power, power);
            telemetry.addData("Strafe Distance", frontRight.getCurrentPosition() - startPosition);
            telemetry.update();
        }
        stopMotors();
    }

    private void rotate(double distanceCm) {

        int targetCounts = (int) (distanceCm * COUNTS_PER_CM);
        int startPosition = rearLeft.getCurrentPosition();

        while (opModeIsActive() && Math.abs(rearLeft.getCurrentPosition() - startPosition) < Math.abs(targetCounts)) {
            double power = distanceCm > 0 ? 0.5 : -0.5;
            setMotorPowers(power, -power, power, -power);
            telemetry.addData("Rotation Distance", rearLeft.getCurrentPosition() - startPosition);
            telemetry.update();
        }

        stopMotors();
    }

    private void raise(double distanceCm) {

        int targetCounts = (int) (distanceCm * RAISE_PPR);
        int startPosition = lift1.getCurrentPosition();

        while (opModeIsActive() && Math.abs(lift1.getCurrentPosition() - startPosition) < Math.abs(targetCounts)) {

            auxiliaryPowers(0.5,0.5);
            telemetry.addData("Raise", lift1.getCurrentPosition() - startPosition);
            telemetry.update();
        }
        stopMotors();
    }

    private void lower(double distanceCm) {

        int targetCounts = (int) (distanceCm * RAISE_PPR);
        int startPosition = lift1.getCurrentPosition();

        while (opModeIsActive() && Math.abs(lift1.getCurrentPosition() - startPosition) < Math.abs(targetCounts)) {

            auxiliaryPowers(-0.5,-0.5);
            telemetry.addData("Lower", lift1.getCurrentPosition() - startPosition);
            telemetry.update();
        }
        stopMotors();
    }


    private void waitFor(double seconds) {
        double startTime = getRuntime();
        while (opModeIsActive() && getRuntime() - startTime < seconds) {
            telemetry.addData("Waiting", "%.1f seconds", seconds - (getRuntime() - startTime));
            telemetry.update();
        }
    }

    private void setMotorPowers(double fl, double fr, double bl, double br) {
        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        rearLeft.setPower(bl);
        rearRight.setPower(br);
    }

    private void auxiliaryPowers(double liftP, double lift2P){

        lift1.setPower(liftP);
        lift2.setPower(lift2P);

    }

    public void Servo(double power, long durationMs, double position) {
        clawout.setPosition(position);
        sleep(200);
    }

    private void stopMotors() {
        setMotorPowers(0, 0, 0, 0);
        auxiliaryPowers(0,0);
    }
}
