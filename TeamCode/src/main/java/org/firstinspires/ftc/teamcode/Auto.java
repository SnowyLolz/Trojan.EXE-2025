package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.bosch.BNO055IMU;

@Autonomous(name = "Auto", group = "Autonomous")
public class Auto extends LinearOpMode {

    // Drivetrain motors
    private DcMotor frontLeft, frontRight, rearLeft, rearRight;

    // Auxiliary motors
    private DcMotor slider, arm;

    // Odometry encoders
    private DcMotor forwardEncoder, strafeEncoder;

    // Gyro (IMU)
    private BNO055IMU imu;

    // Encoder counts per cm
    private static final double COUNTS_PER_CM = 625; // Adjust based on your encoder and wheel setup
    private static final double ROTATION_GAIN = 0.02; // Tuning constant for rotation correction

    @Override
    public void runOpMode() {
        // Initialize drivetrain motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);

        // Initialize auxiliary motors
        slider = hardwareMap.get(DcMotor.class, "slider");
        arm = hardwareMap.get(DcMotor.class, "arm");

        // Initialize odometry encoders
        forwardEncoder = hardwareMap.get(DcMotor.class, "forwardEncoder");
        strafeEncoder = hardwareMap.get(DcMotor.class, "strafeEncoder");

        forwardEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        strafeEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        forwardEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        strafeEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize IMU
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        // Wait for start
        waitForStart();

        if (opModeIsActive()) {
            // Example usage of the functions
            moveForward(20); // Move forward 50 cm
            waitFor(2);
            strafeRight(-30); // Strafe right 30 cm
            moveForward(-10);
            strafeRight(50);
        }
    }

    // Function to move forward or backward
    private void moveForward(double distanceCm) {
        int targetCounts = (int) (distanceCm * COUNTS_PER_CM);
        int startPosition = forwardEncoder.getCurrentPosition();

        while (opModeIsActive() && Math.abs(forwardEncoder.getCurrentPosition() - startPosition) < Math.abs(targetCounts)) {
            double power = distanceCm > 0 ? 0.5 : -0.5;
            setMotorPowers(power, power, power, power);
            telemetry.addData("Forward Distance", forwardEncoder.getCurrentPosition() - startPosition);
            telemetry.update();
        }
        stopMotors();
    }

    // Function to strafe left or right
    private void strafeRight(double distanceCm) {
        int targetCounts = (int) (distanceCm * COUNTS_PER_CM);
        int startPosition = strafeEncoder.getCurrentPosition();

        while (opModeIsActive() && Math.abs(strafeEncoder.getCurrentPosition() - startPosition) < Math.abs(targetCounts)) {
            double power = distanceCm > 0 ? 0.5 : -0.5;
            setMotorPowers(power, -power, -power, power);
            telemetry.addData("Strafe Distance", strafeEncoder.getCurrentPosition() - startPosition);
            telemetry.update();
        }
        stopMotors();
    }

    // Function to rotate clockwise or counterclockwise
    private void rotate(double angleDegrees) {
        double targetAngle = imu.getAngularOrientation().firstAngle + Math.toRadians(angleDegrees);
        while (opModeIsActive() && Math.abs(imu.getAngularOrientation().firstAngle - targetAngle) > Math.toRadians(2)) {
            double error = targetAngle - imu.getAngularOrientation().firstAngle;
            double power = ROTATION_GAIN * error;
            setMotorPowers(-power, power, -power, power);
            telemetry.addData("Heading", imu.getAngularOrientation().firstAngle);
            telemetry.update();
        }
        stopMotors();
    }

    // Function to wait for a specified time
    private void waitFor(double seconds) {
        double startTime = getRuntime();
        while (opModeIsActive() && getRuntime() - startTime < seconds) {
            telemetry.addData("Waiting", "%.1f seconds", seconds - (getRuntime() - startTime));
            telemetry.update();
        }
    }

    // Helper function to set motor powers
    private void setMotorPowers(double fl, double fr, double bl, double br) {
        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        rearLeft.setPower(bl);
        rearRight.setPower(br);
    }

    // Helper function to stop all motors
    private void stopMotors() {
        setMotorPowers(0, 0, 0, 0);
    }
}
