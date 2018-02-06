/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team0000.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.*;

public class Robot extends IterativeRobot {
	
	// Define the motor controllers and components used in the robot, assigning a RoboRIO channel for each controller...
	//
	// DriveBase...
	//
	private SpeedController leftDrive1 = new Spark(0);
	private SpeedController leftDrive2 = new Spark(1);
	private SpeedController leftDrive = new SpeedControllerGroup(leftDrive1, leftDrive2);
	private SpeedController rightDrive1 = new Spark(2);
	private SpeedController rightDrive2 = new Spark(3);
	private SpeedController rightDrive = new SpeedControllerGroup(rightDrive1, rightDrive2);
	private DifferentialDrive drive = new DifferentialDrive(leftDrive, rightDrive);
	//
	// Other motor controllers, e.g. for a grabber...
	//
	private SpeedController grabber = new VictorSP(4);
	
	// Define the components used to input info to the robot...
	//
	// User input...
	//
	private Joystick joystick = new Joystick(0);
	private XboxController xbox = new XboxController(1);
	//
	// Sensor switches on the robot, with their channels...
	//
	private DigitalInput grabbedSensor = new DigitalInput(0);
	
	// Remaining items to help control the robot...
	//
	private Timer autonomousDriveTimer = new Timer();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
	}

	/**
	 * This function is called at the start of the autonomous section.
	 */
	@Override
	public void autonomousInit() {
		autonomousDriveTimer.reset();
		autonomousDriveTimer.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		driveForwardIfRequired();
	}
	
	/**
	 * If the timer hasn't reached some limit, then drive straight forward.
	 */
	private void driveForwardIfRequired() {
		// Drive forward while in autonomous period, but only for the time to allow robot to do its work...
		if (autonomousDriveTimer.get() < 1.5 /* seconds */) {
			drive.tankDrive(1, 1);
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		driveRobot();
		moveGrabber();
	}
	
	/**
	 * Drive the robot in the direction provided by the Joystick.
	 */
	private void driveRobot() {
		double speed = joystick.getY();
		double direction = joystick.getX();
		drive.arcadeDrive(speed, direction);
	}
	
	/**
	 * Move the grabber in the direction requested by the Xbox, but only if the limit switch hasn't been hit.
	 */
	private void moveGrabber() {
		double grabberSpeed = 0.75;
		
		boolean grab = xbox.getAButton();
		boolean release = xbox.getBButton();
		
		if (grab && !release) {

			boolean grabbed = grabbedSensor.get();
			if (grabbed) {
				grabber.set(0);
			} else {
				grabber.set(grabberSpeed);
			}

		} else if (release && !grab) {
		
			grabber.set(-grabberSpeed);
		
		} else {
		
			grabber.set(0);
		
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
