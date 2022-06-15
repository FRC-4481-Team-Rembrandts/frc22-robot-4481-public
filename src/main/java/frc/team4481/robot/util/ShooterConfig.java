package frc.team4481.robot.util;

import java.util.ArrayList;
import java.util.List;

public class ShooterConfig {
    private List<ShooterPreset> shooterConfigs;

    public ShooterConfig(){
        shooterConfigs = new ArrayList<>();
    }

    public ShooterConfig(ArrayList<ShooterPreset> pShooterConfigs){
        this.shooterConfigs = pShooterConfigs;
    }

    public List<ShooterPreset> getShooterConfigs() {
        return shooterConfigs;
    }
}
