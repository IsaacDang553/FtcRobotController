package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class VariablePractice extends OpMode {
    public void init() {
        int motorAngle = 23014;
        double motorSpeed = 0.75;
        boolean clawClosed = true;
        String name = "Isaac";

        telemetry.addData("Motor Angle is (between 0-180)", motorAngle);
        telemetry.addData("Motor Speed is", motorSpeed);
        telemetry.addData("Claw closed", clawClosed);
        telemetry.addData("Name is", name);
    }

    public void loop() {



    }
}
