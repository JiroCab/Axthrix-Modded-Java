package axthrix.world.types.ai;

import arc.math.Mathf;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.entities.Units;
import mindustry.entities.units.AIController;
import mindustry.gen.Building;
import mindustry.gen.Teamc;

public class UnitHealerAi extends AIController {
    public static float retreatDelay = Time.toSeconds * 3f;
    @Nullable
    Teamc avoid;
    private float retreatTimer;
    public boolean includeBlocks = false;

    @Override
    public void updateTargeting() {
        if(timer.get(timerTarget, 15)){
            Building blk = null;
            //Looking for damaged allied
             var unt = Units.closest(unit.team, unit.x, unit.y, Math.max(unit.type.range, 400f), u -> !u.dead() && u.type != unit.type && u.targetable(unit.team) && u.type.playerControllable && u.health < u.maxHealth,
                    (u, tx, ty) -> (u.maxHealth / u.health) + Mathf.dst2(u.x, u.y, tx, ty) / 6400f);
            if(includeBlocks) {
                 blk = Units.findDamagedTile(unit.team, unit.x, unit.y);
            }

            if(unt != null && blk != null){
                var a = unit.dst(blk); var b = unit.dst(unt);
                if( a<b) target = blk;
                else  target = unt;
            }
            else if(unt != null) target = unt;
            else if (blk != null) target = blk;
        }
    }

    @Override
    public void updateMovement(){
        unloadPayloads();
        if(target != null){
            if(!unit.within(target, unit.type.range)){
                moveTo(target, unit.type.range * 0.9f , 50f);
                unit.controlWeapons(true);
            } else unit.controlWeapons(false);
            unit.lookAt(target);
            retreatTimer = 0f;
        } else {
            if((retreatTimer += Time.delta) >= retreatDelay){
                if(timer.get(timerTarget4, 40)){
                    avoid =  Units.closest(unit.team, unit.x, unit.y, Math.max(unit.type.range, 400f), u -> !u.dead() && u.type != unit.type && u.targetable(unit.team) && u.type.playerControllable,
                            (u, tx, ty) -> -u.maxHealth + Mathf.dst2(u.x, u.y, tx, ty) / 6400f);
                }

                if(avoid != null) moveTo(avoid, unit.type.range);
                else moveTo(unit.closestCore(), unit.type.range);
            }

        }


    }

}
