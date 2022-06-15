package frc.team4481.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team4481.frclibrary4481.auto.AllianceColor;
import frc.team4481.frclibrary4481.subsystems.SubsystemControllerBase;

public class ShooterController extends SubsystemControllerBase {
    private boolean secondBallMismatch;
    private boolean reverse;
    private boolean colorFilterActive = false;
    private boolean shootActive;
    private boolean outtakeActive;
    private boolean storageActive;
    private boolean hoodComplete;
    private double shotDistance = 0;
    private int numberOfShots;
    private boolean visionAtSetpoint = false;
    private boolean atSetpoint;
    private AllianceColor allianceColor;

    public void setAllianceColor(AllianceColor pColor) { allianceColor = pColor;}

    public AllianceColor getAllianceColor() {
        return allianceColor;
    }

    public void setAtSetpoint(boolean pAtSetpoint) {
        atSetpoint = pAtSetpoint;
    }

    public boolean getAtSetpoint(){
        return atSetpoint;
    }

    public void setColorFilterActive(boolean pColorFilterActive) {
        colorFilterActive = pColorFilterActive;
    }

    public boolean getColorFilterActive() {
        return colorFilterActive;
    }

    public void setSecondBallMismatch(boolean pSecondBallMismatch) {
        secondBallMismatch = pSecondBallMismatch;
    }

    public boolean getSecondBallMismatch() {
        return secondBallMismatch;
    }

    public void setReverse(boolean pReverse) {
        reverse = pReverse;
    }

    public boolean getReverse() {
        return reverse;
    }

    public void setShootActive(boolean pShootActive) {
        shootActive = pShootActive;
    }

    public boolean getShootActive() {
        return shootActive;
    }

    public void setOuttakeActive(boolean pOuttakeActive) {
        outtakeActive = pOuttakeActive;
    }

    public boolean getOuttakeActive() {
        return outtakeActive;
    }

    public void setStorageActive(boolean pStorageActive) {
        storageActive = pStorageActive;
    }

    public boolean getStorageActive() {
        return storageActive;
    }

    public void setNumberOfShots(int pNumberOfShots) {
        numberOfShots = pNumberOfShots;
    }

    public int getNumberOfShots() {
        return numberOfShots;
    }

    public void setShotDistance(double pShotDistance) {
        shotDistance = pShotDistance;
    }

    public double getShotDistance() {
        return shotDistance;
    }

    public void setHoodComplete(boolean pHoodComplete) {
        hoodComplete = pHoodComplete;
    }

    public boolean getHoodComplete() {
        return hoodComplete;
    }
    public void setVisionAtSetpoint(boolean pVisionAtSetpoint) {
        visionAtSetpoint = pVisionAtSetpoint;
    }
    public boolean isVisionAtSetpoint() {
        return visionAtSetpoint;
    }

    private ControlState currentControlState = ControlState.DISABLED;

    public enum ControlState {
        DISABLED,
        ENABLED,
    }

    public void setControlState(ControlState currentControlState) {
        this.currentControlState = currentControlState;
    }

    public ControlState getControlState() {
        return currentControlState;
    }

    private ShootState currentShootState = ShootState.HIGH2X;
    private ShootState desiredShootState = ShootState.HIGH2X;

    public enum ShootState {
        HIGH2X,
        STEAL,
        LOW2X,
        AUTOMATIC,
        FILTER,
    }

    public void setShootState(ShootState pShootState) {
        this.currentShootState = pShootState;
    }

    public ShootState getShootState() {
        return currentShootState;
    }

    public void setDesiredShootState(ShootState pDesiredShootState) {
        this.desiredShootState = pDesiredShootState;
    }

    public ShootState getDesiredShootState() {
        return desiredShootState;
    }

    private reverseState currentReverseState = reverseState.FULL;

    public enum reverseState {
        SINGLE,
        FULL
    }

    public void setReverseState(reverseState pReverseState) {
        this.currentReverseState = pReverseState;
    }

    public reverseState getReverseState() {
        return currentReverseState;
    }
}
