package axthrix.world.types.block.defense;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.util.Strings;
import axthrix.world.util.AxStats;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;


public class PowerAcceleratedTurret extends AxPowerTurret{
    public float acceleratedDelay = 120, acceleratedBonus = 1.5f;
    public int acceleratedSteps = 1;

    public float burnoutDelay = 240, cooldownDelay = 120;
    public boolean burnsOut = true;

    public PowerAcceleratedTurret(String name){
        super(name);
    }
    

    @Override
    public void setBars(){
        super.setBars();
        if(acceleratedBonus == 1){
            if(burnsOut){
                addBar("aj-heat", (PowerAcceleratedTurretBuild entity) -> new Bar(
                        () -> entity.accelCount > acceleratedSteps ? Core.bundle.format("bar.aj-overheated") : Core.bundle.format("bar.aj-heat", Strings.autoFixed((entity.accelBoost - 1) * 100f, 2)),
                        () -> entity.accelCount > acceleratedSteps ? Pal.remove : Color.orange,
                        entity::heatf
                ));
            }
        }else{
            addBar("aj-phases", (PowerAcceleratedTurretBuild entity) -> new Bar(
                    () -> entity.accelCount > acceleratedSteps ? Core.bundle.format("bar.aj-overheated") : Core.bundle.format("bar.aj-phases", Strings.autoFixed((entity.accelBoost - 1) * 100f, 2)),
                    () -> entity.accelCount > acceleratedSteps ? Pal.remove : Pal.techBlue,
                    entity::boostf
            ));
        }

    }
    @Override
    public void setStats(){
        super.setStats();
        if(acceleratedBonus != 1){
            stats.add(AxStats.maxFireRateBonus, 60.0F / reload * (float)shoot.shots * (acceleratedBonus * acceleratedSteps - 1), StatUnit.perSecond);
            stats.add(AxStats.timeForMaxBonus, (acceleratedDelay * acceleratedSteps) / 60, StatUnit.seconds);
        }
        if (burnsOut){
            stats.add(AxStats.overheat, burnoutDelay / 60, StatUnit.seconds);
            stats.add(AxStats.timeToCool, cooldownDelay / 60, StatUnit.seconds);
        }
        if (this.coolant != null) {
            this.stats.remove(Stat.booster);
            this.stats.add(Stat.booster, StatValues.boosters(this.reload, this.coolant.amount, this.coolantMultiplier, true, (l) -> {
                return l.coolant && this.consumesLiquid(l);
            }));
        }
    }

    public class PowerAcceleratedTurretBuild extends PowerTurretBuild{
        public float accelBoost, accelCounter;
        public int accelCount;

        @Override
        public void updateTile(){
            super.updateTile();

            if(accelCount > acceleratedSteps){
                accelCounter += edelta();
                if(accelCounter >= cooldownDelay){
                    coolantMultiplier = 5;
                    super.updateCooling();
                    accelCount = 0;
                    accelBoost = 1;
                    accelCounter %= cooldownDelay;
                }
            }else if(isShooting()){
                accelCounter += edelta(); 
                if(accelCount < acceleratedSteps && accelCounter >= acceleratedDelay){
                    accelBoost += (acceleratedBonus - 1);
                    accelCount++;
                    accelCounter %= acceleratedDelay;
                }else if(burnsOut && accelCounter >= burnoutDelay){
                    accelBoost = 0;
                    coolantMultiplier = 0;
                    accelCount++;
                    accelCounter %= burnoutDelay;
                }
            }else{
                accelCount = 0;
                accelCounter = 0;
                accelBoost = 1;
                coolantMultiplier = 5;
                super.updateCooling();
            }
        }

        @Override
        protected void updateReload(){
            float multiplier = hasAmmo() ? peekAmmo().reloadMultiplier : 1f;
            reloadCounter += delta() * multiplier * accelBoost * baseReloadSpeed();

            reloadCounter = Math.min(reloadCounter, reload);
        }
        
        public float boostf(){
            if(accelCount > acceleratedSteps) return 1 - (accelCounter / cooldownDelay);
            return Mathf.clamp((float)accelCount / acceleratedSteps);
        }
        public float heatf(){
            if(accelCount > acceleratedSteps) return 1 - (accelCounter / cooldownDelay);
            return accelCounter / burnoutDelay;
        }
    }
}