package axthrix.world.types.block.production;

import arc.Core;
import arc.struct.Seq;
import axthrix.world.types.AxFaction;
import axthrix.world.util.AxStats;
import mindustry.Vars;
import multicraft.MultiCrafter;

import static mindustry.Vars.state;

public class AxMulticrafter extends MultiCrafter {
    public Seq<AxFaction> faction = new Seq<>();
    public boolean blackListFactions = false;
    public AxMulticrafter(String name) {super(name);}

    @Override
    public void setStats() {
        super.setStats();

        if(faction.any()){
            stats.add(AxStats.faction, Core.bundle.get("team." +  faction.peek().name));
        }
    }
    public boolean partOfPlayerFaction()
    {
        if (blackListFactions)
            return faction.count(f -> f.partOf(Vars.player.team())) == 0;
        return faction.size == 0 || faction.count(f -> f.partOf(Vars.player.team())) > 0;
    }

    @Override
    public boolean isVisible(){
        return state.rules.editor || (partOfPlayerFaction() && !isHidden() && (!state.rules.hideBannedBlocks || !state.rules.isBanned(this)));
    }

    @Override
    public boolean isPlaceable(){
        return Vars.net.server() || (!state.rules.isBanned(this) || state.rules.editor) && supportsEnv(state.rules.env);
    }
}
