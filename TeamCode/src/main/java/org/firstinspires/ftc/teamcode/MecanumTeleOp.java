package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Mecanum TeleOp", group = "TeleOp")
public class MecanumTeleOp extends LinearOpMode {

    // Drive motors
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    // Driving settings
    private static final double NORMAL_SPEED = 1.0;
    private static final double SLOW_SPEED = 0.35;
    private static final double STRAFE_MULTIPLIER = 1.1;

    @Override
    public void runOpMode() {

        // =====================================
        // 1. INITIALIZE MOTORS
        // =====================================

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Motor directions (verify on your robot)
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        // Run motors using direct power control
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Brake when power is zero
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Make sure motors start stopped
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        telemetry.addLine("Mecanum Drive Initialized");
        telemetry.addLine("Press START to drive");
        telemetry.update();

        // =====================================
        // 2. WAIT FOR START
        // =====================================

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        // =====================================
        // 3. MAIN TELEOP LOOP
        // =====================================

        while (opModeIsActive()) {

            // Read controller input
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // Improve strafing response
            x *= STRAFE_MULTIPLIER;

            // =====================================
            // 4. SPEED CONTROL
            // =====================================

            // Hold right bumper for slow mode
            double speedMultiplier;

            if (gamepad1.right_bumper) {
                speedMultiplier = SLOW_SPEED;
            } else {
                speedMultiplier = NORMAL_SPEED;
            }

            // =====================================
            // 5. MECANUM DRIVE CALCULATIONS
            // =====================================

            // Normalize motor powers
            double denominator = Math.max(
                    Math.abs(y) + Math.abs(x) + Math.abs(rx),
                    1.0
            );

            // Calculate each motor's power
            double flPower = (y + x + rx) / denominator;
            double frPower = (y - x - rx) / denominator;
            double blPower = (y - x + rx) / denominator;
            double brPower = (y + x - rx) / denominator;

            // =====================================
            // 6. APPLY SPEED MULTIPLIER
            // =====================================

            flPower *= speedMultiplier;
            frPower *= speedMultiplier;
            blPower *= speedMultiplier;
            brPower *= speedMultiplier;

            // =====================================
            // 7. SEND POWER TO MOTORS
            // =====================================

            frontLeft.setPower(flPower);
            frontRight.setPower(frPower);
            backLeft.setPower(blPower);
            backRight.setPower(brPower);

            // =====================================
            // 8. TELEMETRY
            // =====================================

            telemetry.addData("Forward / Backward", y);
            telemetry.addData("Strafe", x);
            telemetry.addData("Rotation", rx);

            telemetry.addData(
                    "Drive Mode",
                    gamepad1.right_bumper ? "SLOW" : "NORMAL"
            );

            telemetry.addData("Front Left Power", flPower);
            telemetry.addData("Front Right Power", frPower);
            telemetry.addData("Back Left Power", blPower);
            telemetry.addData("Back Right Power", brPower);

            telemetry.update();
        }

        // =====================================
        // 9. STOP ALL MOTORS
        // =====================================

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
