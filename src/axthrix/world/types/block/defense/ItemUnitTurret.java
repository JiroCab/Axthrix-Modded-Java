package axthrix.world.types.block.defense;

import arc.Core;
import arc.Events;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.scene.style.Drawable;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.TypeIO;
import mindustry.logic.LAccess;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.UnitPayload;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.Stat;
import axthrix.content.AxthrixFx;
import axthrix.world.types.bulletypes.SpawnHelperBulletType;
import axthrix.world.types.unittypes.DroneUnitType;

import java.util.Arrays;
import java.util.Objects;

import static mindustry.Vars.*;

/*The cross bread of a Turret and Unit factory, for the sake of being different*/
public class ItemUnitTurret extends AxItemTurret {
    /*common required items for all unit types*/
    public ItemStack[] requiredAlternate = ItemStack.with(Items.copper, 20, Items.silicon, 15);
    /*Minimum Items needed*/
    /*Parameters when failing to make a unit*/
    public ItemStack[] requiredItems = ItemStack.with(Items.copper, 20, Items.silicon, 15);
    public Sound failedMakeSound = Sounds.dullExplosion;
    public float failedMakeSoundPitch = 0.7f, getFailedMakeSoundVolume = 1f;
    public Effect failedMakeFx = AxthrixFx.failedMake;
    public TextureRegion bottomRegion;
    /*Hovering Shows the unit creation*/
    public boolean hoverShowsSpawn = false, payloadExitShow = true, drawOnTarget = false, arrowShootPos = false;
    /*Aim at the rally point*/
    public boolean rallyAim = true;
    /*Aim for closest liquid*/
    public boolean liquidAim = false;

    //For Shooting whatever is in playload as a bullet
    BulletType fallbackBullet;
    public float payloadSpeed = 0.7f, payloadRotateSpeed = 5f;
    public Block alternate;


    /*Todo:  tier/unit switch when a component block is attached (t4/5 erekir) */

    public ItemUnitTurret(String name){
        super(name);
        commandable = configurable = outputsPayload = clearOnDoubleTap = true;
        playerControllable = false;
        shootSound = Sounds.respawn;
        drawer = new DrawDefault();
        fogRadius = -1;
        range = 0f;
        config(UnitCommand.class, (ItemUnitTurretBuild build, UnitCommand command) -> build.command = command);
        configClear((ItemUnitTurretBuild build) -> build.command = null);
    }

    public void setBars(){
        super.setBars();
        addBar("bar.progress", (ItemUnitTurretBuild entity) -> new Bar("bar.progress", Pal.ammo,() -> entity.reloadCounter / reload));

        /*TODO: Takes a bit to update*/
        addBar("units", (ItemUnitTurretBuild e) -> e.peekAmmo() == null || e.peekAmmo().spawnUnit != null &&  !e.peekAmmo().spawnUnit.useUnitCap ? null : new Bar(() ->
                e.peekAmmo().spawnUnit == null ? "[lightgray]" + Iconc.cancel :
                        Core.bundle.format("bar.unitcap",
                                !Objects.equals(Fonts.getUnicodeStr(e.peekAmmo().spawnUnit.name), "") ? Fonts.getUnicodeStr(e.peekAmmo().spawnUnit.name) : Iconc.units,
                                e.team.data().countType(e.peekAmmo().spawnUnit),
                                Units.getStringCap(e.team)
                        ),
                () -> Pal.power,
                () -> e.peekAmmo() == null ? 0f : e.peekAmmo().spawnUnit == null ? 0f : (float) e.team.data().countType(e.peekAmmo().spawnUnit) / Units.getCap(e.team)
        ));

    }

    @Override
    public void load(){
        bottomRegion = Core.atlas.find(name + "-bottom");
        super.load();
    }

    @Override
    public TextureRegion[] icons(){
        if(!(drawer instanceof  DrawDefault))return drawer.finalIcons(this);
        else {
            //use team region in vanilla team blocks
            TextureRegion r = variants > 0 ? Core.atlas.find(name + "1") : region;
            return teamRegion.found() && minfo.mod == null ? new TextureRegion[]{bottomRegion, r, teamRegions[Team.sharded.id]} : new TextureRegion[]{bottomRegion, r};
        }
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out){
        if(!(drawer instanceof DrawDefault))drawer.getRegionsToOutline(this, out);
        else {generatedIcons = null;}
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.remove(Stat.ammo);
        stats.remove(Stat.reload);
        stats.remove(Stat.targetsAir);
        stats.remove(Stat.inaccuracy);
        stats.remove(Stat.targetsGround);

        if(range <= 1)stats.remove(Stat.shootRange);
        stats.add(Stat.output, table -> this.ammoTypes.each((item, bul) -> {
            UnitType displayUnit = bul.spawnUnit;
            if(displayUnit == null) return;
            table.row();
            table.table(Styles.grayPanel, b -> {
                if(!displayUnit.isBanned()) b.image(displayUnit.fullIcon).size(40).pad(10f).left().scaling(Scaling.fit);
                else b.image(Icon.cancel.getRegion()).color(Pal.remove).size(40).pad(10f).left().scaling(Scaling.fit);

                b.table(info -> {
                    if(item != null) info.table(title -> {
                        title.image(item.fullIcon).size(3 * 8).left().scaling(Scaling.fit).top();
                        title.add(item.localizedName).left().top();
                    }).left().row();
                    info.add(displayUnit.localizedName).left().row();
                    if (Core.settings.getBool("console")) info.add(displayUnit.name).left().color(Color.lightGray);
                });
                b.button("?", Styles.flatBordert, () -> ui.content.show(displayUnit)).size(40f).pad(10).right().grow().visible(displayUnit::unlockedNow);
            }).growX().pad(5).row();
        }));
    }

    @Override
    public void init(){
        if(requiredItems.length >= 1) for (ItemStack i : requiredItems) consumeItem(i.item, i.amount);

        /*Todo: Hover icon for the unit (UnitFactory)*/
        super.init();
    }

    public class ItemUnitTurretBuild<T extends UnitPayload> extends ItemTurretBuild{
        public @Nullable Vec2 commandPos;
        public float time, speedScl;
        public int direction = -1;
        public @Nullable UnitPayload payload;
        public Vec2 payVector = new Vec2();
        public @Nullable UnitCommand command;

        @Override
        public boolean acceptItem(Building source, Item item){
            return ((ammoTypes.get(item) != null && totalAmmo + ammoTypes.get(item).ammoMultiplier <= maxAmmo && !ammoTypes.get(item).spawnUnit.isBanned())
                     || (Arrays.stream(requiredItems).anyMatch( i -> item == i.item) && items.get(item) < getMaximumAccepted(item)));
        }


        @Override
        public int acceptStack(Item item, int amount, Teamc source){
            if(acceptItem(self(), item) && block.hasItems && (source == null || source.team() == team)){
                return Math.min(getMaximumAccepted(item) - items.get(item), amount);
            } else return super.acceptStack(item, amount, source);
        }

        @Override
        public void handleItem(Building source, Item item){
            if (Arrays.stream(requiredItems).noneMatch(i -> item == i.item)) {
                super.handleItem(source, item);
            } else if(items.get(item) < getMaximumAccepted(item)) {
                items.add(item, 1);
            }
        }

        @Override
        public void updateTile(){
            speedScl = Mathf.lerpDelta(speedScl, 1f, 0.05f);
            time += edelta() * speedScl * Vars.state.rules.unitBuildSpeed(team);

            moveOutPayload();
            super.updateTile();

            if(direction == -1 && payload != null){
                BulletType nya = new SpawnHelperBulletType(){{
                    shootEffect = Fx.shootBig;
                    ammoMultiplier = 1f;
                    reloadMultiplier = 0.75f;
                    spawnUnit = payload.unit.type;
                }};
                shootRegular(nya, shootCreatable(nya), false);
                payload = null;
            }
        }

        @Override
        public boolean hasAmmo(){
            if(payload != null) return false;
            if(!hasReqItems()) return false;
            return super.hasAmmo();
        }

        public boolean hasReqItems(){
            for (ItemStack req : requiredItems) {
                if(items.get(req.item) >= req.amount) continue;
                return false;
            }
            return true;
        }

        public boolean shootCreatable (BulletType type){
            return !type.spawnUnit.isBanned() && (type.spawnUnit.unlockedNowHost() && state.isCampaign() || !state.isCampaign());
        }

        @Override
        protected void shoot(BulletType type){
            boolean creatable = shootCreatable(type);
            if(direction != -1){
                shootPayload(type, true);
            } else{
                if(payload != null) payload = null;
                shootRegular(type, creatable, true);
            }

            if(consumeAmmoOnce) useAmmo();
        }

        protected void shootPayload(BulletType type, boolean consume){
            if(type.spawnUnit == null) return;
            if(payload == null) {
                payload = new UnitPayload(type.spawnUnit.create(team));
                Unit p = (payload).unit;
                if (commandPos != null && p.isCommandable()) {
                    p.command().commandPosition(commandPos);
                }
                if (p.isCommandable() && command != null) {
                    p.command().command(command);
                }
                Events.fire(new EventType.UnitCreateEvent(p, this));
                payVector.setZero();
                if(consume) consume();
            }
            moveOutPayload();
        }

        protected void shootRegular(BulletType type, boolean creatable, boolean consume){
            boolean spawn =creatable && (state.rules.waveTeam == this.team || (type.spawnUnit.useUnitCap && this.team.data().countType(type.spawnUnit) < this.team.data().unitCap) || !type.spawnUnit.useUnitCap);
            if(spawn){
                /*don't create the unit if it's banned or at unit cap*/
                if(consume)consume();
                float rot = direction == -1 ? rotation - 90 : (direction -1) * 90,
                        bulletX = x + Angles.trnsx(rot, shootX, shootY),
                        bulletY = y + Angles.trnsy(rot, shootX, shootY);

                if (shoot.firstShotDelay > 0) {
                    chargeSound.at(bulletX, bulletY, Mathf.random(soundPitchMin, soundPitchMax));
                    type.chargeEffect.at(bulletX, bulletY, rot);
                }

                shoot.shoot(barrelCounter, (xOffset, yOffset, angle, delay, mover) -> {
                    queuedBullets++;
                    if (delay > 0f) {
                        Time.run(delay, () -> bullet(type, xOffset, yOffset, angle, mover));
                    } else {
                        bullet(type, xOffset, yOffset, angle, mover);
                    }
                }, () -> barrelCounter++);
            }else {
                failedMakeFx.create(x, y, rotation -90, Pal.plasticSmoke, null);
                failedMakeSound.at(x, y, failedMakeSoundPitch, getFailedMakeSoundVolume);
            }
        }

        public void moveOutPayload(){
            if(payload == null) return;

            updatePayload();

            Vec2 dest = Tmp.v1.trns((direction + 1) * 90, size * tilesize / 2f);
            payloadRotation = Angles.moveToward(payloadRotation, rotdeg(), payloadRotateSpeed * delta());
            payVector.approach(dest, payloadSpeed * delta());

            int trns = this.block.size / 2 + 1;
            Building front = this.nearby(Geometry.d4((direction + 1)).x * trns, Geometry.d4((direction + 1)).y * trns);
            boolean canDump = front == null || !front.tile().solid(),
                        canMove = front != null && (front.block.outputsPayload || front.block.acceptsPayload);

            if(canDump && !canMove) pushOutput(payload, 1f - (payVector.dst(dest) / (size * tilesize / 2f)));

            if(payVector.within(dest, 0.001f)){
                payVector.clamp(-size * tilesize / 2f, -size * tilesize / 2f, size * tilesize / 2f, size * tilesize / 2f);

                if(canMove){
                    if(movePayload(payload)){
                        payload = null;
                    }
                }else if(canDump) dumpPayload();
            }
        }

        @Override
        public boolean movePayload(Payload todump) {
            int trns = this.block.size / 2 + 1;
            Tile next = this.tile.nearby(Geometry.d4(direction + 1).x * trns, Geometry.d4(direction + 1).y * trns);
            if (next != null && next.build != null && next.build.team == this.team && next.build.acceptPayload(this, todump)) {
                next.build.handlePayload(this, todump);
                return true;
            } else {
                return false;
            }
        }

        public void pushOutput(Payload payload, float progress){
            float thresh = 0.55f;
            if(progress >= thresh){
                boolean legStep = payload instanceof UnitPayload u && u.unit.type.allowLegStep;
                float size = payload.size(), radius = size/2f, x = payload.x(), y = payload.y(), scl = Mathf.clamp(((progress - thresh) / (1f - thresh)) * 1.1f);

                Groups.unit.intersect(x - size/2f, y - size/2f, size, size, u -> {
                    float dst = u.dst(payload);
                    float rs = radius + u.hitSize/2f;
                    if(u.isGrounded() && u.type.allowLegStep == legStep && dst < rs)
                        u.vel.add(Tmp.v1.set(u.x - x, u.y - y).setLength(Math.min(rs - dst, 1f)).scl(scl));
                });
            }
        }

        public void dumpPayload(){
            //translate payload forward slightly
            float tx = Angles.trnsx(payload.rotation(), 0.1f), ty = Angles.trnsy(payload.rotation(), 0.1f);
            payload.set(payload.x() + tx, payload.y() + ty, payload.rotation());

            if(payload.dump()) payload = null;
            else payload.set(payload.x() - tx, payload.y() - ty, payload.rotation());
        }

        public void updatePayload(){
            if(payload != null) payload.set(x + payVector.x, y + payVector.y, (direction + 1) * 90);
        }

        @Override
        public void draw(){
            if(!(drawer instanceof DrawDefault)){
                super.draw();
            }else{
                float rot = direction == -1 ? rotation -90: direction * 90;
                Draw.rect(bottomRegion, x, y);
                if (peekAmmo() != null && peekAmmo().spawnUnit != null) {
                    UnitType unt = peekAmmo().spawnUnit;
                    if (shootCreatable(peekAmmo())) {
                        Draw.draw(Layer.blockOver, () -> Drawf.construct(this, unt, rot, reloadCounter / reload, speedScl, time));
                    } else {
                        Draw.draw(Layer.blockOver, () -> {
                            Draw.alpha(reloadCounter / reload);
                            Draw.rect(unt.fullIcon, x, y, rot);

                            Draw.color(Pal.accent);
                            Draw.alpha((reloadCounter / reload) / 1.2f);
                            Lines.lineAngleCenter(this.x + Mathf.sin(this.time, 20f, (this.block.size * tilesize - 4f) / 4f), this.y, 90, this.block.size * tilesize - 4f);
                            Draw.reset();

                            Draw.color(Pal.remove, Math.min(reloadCounter / reload, 0.8f));
                            Draw.rect(Icon.warning.getRegion(), x, y);
                            Draw.reset();
                        });
                    }
                }
                Draw.z(Layer.blockOver + 0.1f);
                if(payload != null){
                    payload.draw();
                }
                Draw.rect(region, x, y);
            }
        }

        @Override
        public void drawSelect(){
            /*instead of dealing/trying to make the block itself rotate, we have this*/
            Lines.stroke(1f, team.color);
            Draw.color(team.color, 0.8f);

            float sx = arrowShootPos ? shootX : 0f, sy = arrowShootPos ? shootY : 6f,
                    rot = direction == -1 ? rotation - 90 : direction * 90,
                    squareX = x + Angles.trnsx(rot , sx, sy), squareY = y + Angles.trnsy(rot, sx, sy);
            if(hoverShowsSpawn && direction == -1){
                Lines.square(squareX, squareY + 0.5f, 3.5f, Time.time * 0.5f);
            } else if(payloadExitShow && direction != -1){
                TextureRegion regionArrow = Core.atlas.find("place-arrow");

                Draw.rect(regionArrow, squareX, squareY -1f, (float) regionArrow.width / size, (float) regionArrow.height / size,direction * 90);
            }
            Draw.reset();
            if(drawOnTarget && target != null){
                Drawf.target(target.getX(), target.getY(), 7f * Interp.swingIn.apply(1f), this.team().color);
            }

            super.drawSelect();
        }

        @Override
        public Vec2 getCommandPosition(){return commandPos;}

        @Override
        protected void findTarget(){
            if(rallyAim){
                if(commandPos != null) targetPos = commandPos;
                return;
            }
            if(liquidAim){
                target = indexer.findTile(team, x, y, range, t-> !t.checkSolid() && t.floor().isLiquid, false);
            }

            super.findTarget();
        }

        @Override
        public void onCommand(Vec2 tar){commandPos = tar;}

        @Override
        /*Work around for rally point without fully rewriting updateTile*/
        public boolean logicControlled(){return logicControlTime > 0 || commandPos != null;}

        @Override
        public void buildConfiguration(Table table){
            table.background(Styles.black6);

            buildIcon(table, -1, Icon.export);
            buildIcon(table, 0, Icon.up);
            buildIcon(table, 1, Icon.left);
            buildIcon(table, 2, Icon.down);
            buildIcon(table, 3, Icon.right);


            var group = new ButtonGroup<ImageButton>();
            group.setMinCheckCount(0);
            int i = 0, columns = 6;
            if(peekAmmo() != null && peekAmmo().spawnUnit != null && (peekAmmo().spawnUnit instanceof DroneUnitType nyf && !nyf.constructHideDefault) && peekAmmo().spawnUnit.commands.length >1 ){
                var unit = peekAmmo().spawnUnit;
                var list = unit.commands;
                table.row();
                for(var item : list){
                    ImageButton button = table.button(item.getIcon(), Styles.clearNoneTogglei, 40f, () -> {
                        command = item;
                        configure(item);
                        deselect();
                    }).tooltip(item.localized()).group(group).get();

                    button.update(() -> button.setChecked(command == item || (command == null && unit.defaultCommand == item)));

                    if(++i % columns == 0){
                        table.row();
                    }
                }
            }
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            if(block.clearOnDoubleTap){
                if(self() == other){
                    deselect();
                    direction = -1;
                    Call.tileConfig(Vars.player, this, -1);
                    return false;
                }
                return true;
            }
            return self() != other;
        }

        void buildIcon(Table table, int conf, Drawable icon){
            table.button(icon, Styles.clearNoneTogglei, 40f, () -> {
                direction = conf;
                configure(conf);
                deselect();
            }).checked(direction == conf);
        }

        @Override
        public Object senseObject(LAccess sensor){
            if(sensor == LAccess.config) return null;
            if(sensor == LAccess.rotation) return direction == -1 ? rotation : direction * 90f;
            return super.senseObject(sensor);
        }

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.rotation) return direction == -1 ? rotation : direction * 90f;
            if(sensor == LAccess.itemCapacity) return Mathf.round(itemCapacity * state.rules.unitCost(team));
            return super.sense(sensor);
        }

        @Override
        public boolean isShooting(){
            return super.isShooting() && hasAmmo();
        }

        @Override
        protected float baseReloadSpeed(){
            return hasReqItems() ? efficiency : 0f;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            TypeIO.writeVecNullable(write, commandPos);
            write.i(direction);
            Payload.write(payload, write);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            if(revision >= 3){ /*necessary, to prevent map/save corruption */
                commandPos = TypeIO.readVecNullable(read);
            }
            if(revision >=5 ){
                direction = read.i();
                payload = Payload.read(read);
            } else  direction = -1;
        }

        @Override
        public byte version(){
            return 5;
        }
    }

}
