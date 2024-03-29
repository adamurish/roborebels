/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.smartdashboard.SendableGyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author dmw
 */
public class RRTestThread extends Thread 
{
    private RRDrive                 drive;
//    private RRDriveSendable         drive;
//    private SendableGyro            gyro;
    
//    private double lastTickTime;
    
    public RRTestThread(RRDriveSendable d)
    {
        drive = d;
        
    }
    
    public RRTestThread()
    {
        drive = new RRDrive(2, 1, 14, 13, 12, 11);
        
//        drive = new RRDriveSendable(2, 1, 14, 13, 12, 11);
//        gyro = new SendableGyro(1);
        
        
        
        
//        lastTickTime = Timer.getFPGATimestamp();
    }
    
    public void run()
    {
        double currentTime = 0.0;
        
        while (true)
        {
//            currentTime = Timer.getFPGATimestamp();
//            
//            if ( currentTime - lastTickTime >= 0.02 )
//            {
//                drive.drive(false);
//                lastTickTime = Timer.getFPGATimestamp();
//                System.out.println("+++ driving from thread... " + currentTime);
//            }
//            else
//            {
//                System.out.println("*** not driving at this moment!");
//            }
            
            
            /*  This method of driving, then sleeping for 20 milliseconds works
             *  just as well as the above method, and is less complicated.
             */
            drive.drive(false);
            
//            SmartDashboard.putData("RRDriveSendable", drive);
//            SmartDashboard.putData("Gyro", gyro);
            
            try {
                RRTestThread.sleep(20);
            } catch (InterruptedException ex) {
                System.err.println("RRTestThread::run() - Interrupted Exception!");
            }
        }
    }
}
