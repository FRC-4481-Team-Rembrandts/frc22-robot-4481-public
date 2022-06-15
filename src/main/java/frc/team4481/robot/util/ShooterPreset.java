package frc.team4481.robot.util;

/**
 * Shooter preset which is used in the vision lookup table
 */
public class ShooterPreset implements Comparable<ShooterPreset> {
    private double hoodEjectAngle;
    private double flywheelSpeed;
    private double topRollerPercentage;
    private double distance;

    public ShooterPreset(double pHoodEjectAngle,
                         double pFlywheelSpeed,
                         double pTopRollerPercentage,
                         double pDistance){
        this.hoodEjectAngle = pHoodEjectAngle;
        this.flywheelSpeed = pFlywheelSpeed;
        this.topRollerPercentage = pTopRollerPercentage;
        this.distance = pDistance;
    }

    public double getHoodEjectAngle() {
        return hoodEjectAngle;
    }

    public double getFlywheelSpeed(){
        return flywheelSpeed;
    }

    public double getTopRollerPercentage(){
        return topRollerPercentage;
    }

    public double getDistance(){
        return distance;
    }


    public void setHoodEjectAngle(double pHoodEjectAngle) {
        this.hoodEjectAngle = pHoodEjectAngle;
    }

    public void setFlywheelSpeed(double pFlywheelSpeed) {
        this.flywheelSpeed = pFlywheelSpeed;
    }

    public void setTopRollerPercentage(double pTopRollerPercentage) {
        this.topRollerPercentage = pTopRollerPercentage;
    }

    public void setDistance(double pDistance) {
        this.distance = pDistance;
    }


    @Override
    public int compareTo(ShooterPreset pPreset) {
        return Double.compare(this.getDistance(), pPreset.getDistance());
    }
}
