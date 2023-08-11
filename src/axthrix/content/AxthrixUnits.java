package axthrix.content;

import arc.graphics.*;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Tmp;
import axthrix.world.types.ai.AttackDroneAI;
import axthrix.world.types.bulletypes.SpiralPattern;
import axthrix.world.types.unittypes.AxUnitType;
import axthrix.world.types.unittypes.MountUnitType;
import mindustry.entities.abilities.*;
import axthrix.world.types.abilities.*;
import axthrix.world.types.bulletypes.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.*;

import mindustry.entities.pattern.ShootSpread;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.unit.*;
import mindustry.content.*;

import java.util.ArrayList;

import static mindustry.Vars.content;
import static mindustry.Vars.tilePayload;

public class AxthrixUnits {
    public static UnitType
    //Axthrix  |6 trees|
        //Ground
            //Assault Hovers |SubAtomic|
                quark, electron, baryon, hadron, photon,
                /*
                quark: duo helix 1 red 1 blue
                electron: bendy homing bullet
                baryon: Tri Helix, explode on contact
                hadron: one bullets that explodes into two on contact
                photon: wip
                */
            //Support Walkers |Protect|
                barrier, blockade, palisade, parapet, impediment,
            //Specialist Tanks |Energy/Gem|
               anagh,akshaj,amitojas,agnitejas,ayustejas,
        //Air
            //Assault Helicopters |Storm/Thunder|
                rai,zyran,tufani,styrmir,corentin,
            //Support Airships |safety|
                naji,haven,nagiah,abhayad,sosthenes,
            //Specialist Flying Mounts |carry|
                amos,aymoss,amalik,anuvaha,ambuvahini,
                //TX
                arcalishion,
    //Raodon |3 trees|
        //Assault Walker |Power|
            asta,adira,allura,andrea,athena,
        //Support Tank  |Wealth|
            danu,dorit,duarte,dhanya,dhanashri,
        //Specialist aircraft |Fame|
            efim,estes,elmena,evdoxia,estanislao,
    //Ikatusa |undetermined|

    //Core Units |8 units|

    //Legends |undetermined|
        //yin and yang tree
            spate, influx,
//testing
    test1
            ;
    public static void load(){
        quark = new AxUnitType("quark") {{
            localizedName = "Quark";
            constructor = ElevationMoveUnit::create;
            flying = false;
            speed = 8.3f/7.5f;
            drag = 0.13f;
            hitSize = 10f;
            health = 275;
            armor = 3;
            range = 8 * 26;
            accel = 0.6f;
            rotateSpeed = 3.3f;
            faceTarget = true;
            hovering = true;
            parts.add(
            new RegionPart("-blade"){{
                mirror = under = true;
                weaponIndex = 0;
                moveY = -2;
                moveX = -2;
            }},
            new HoverPart(){{
                x = 0f;
                y = 0f;
                mirror = false;
                radius = 18f;
                phase = 60f;
                stroke = 5f;
                layerOffset = -0.05f;
                color = Color.valueOf("de9458");
            }},
            new HaloPart(){{
                progress = PartProgress.warmup.delay(0.6f);
                weaponIndex = 0;
                color = Color.valueOf("de9458");
                sides = 10;
                hollow = true;
                shapes = 2;
                stroke = 0.2f;
                strokeTo = 0.8f;
                radius = 2f;
                haloRadius = 9f;
                haloRotateSpeed = 4;
                layer = Layer.effect;
                y = 0;
                x = 0;
            }});

            weapons.add(new Weapon(){{
                mirror = false;
                minWarmup = 0.8f;
                x = 0;
                y = 0;
                reload = 60f/0.8f;
                shootY = 2f;
                shoot = new SpiralPattern(1f, 2){{
                    shots = 3;
                }};
                bullet = new BasicBulletType(3.5f, 30){{
                    width = 1;
                    height = 1;
                    lifetime = 80;
                    keepVelocity = false;
                    trailColor = backColor = lightColor = Color.valueOf("683b3d");
                    frontColor = Color.valueOf("de9458");
                    trailLength = 12;
                    trailChance = 0f;
                    trailWidth = 0.7f;
                    despawnEffect = hitEffect = Fx.none;
                }};
            }});
        }};
        //support walkers
        barrier = new AxUnitType("barrier"){{
           outlineColor = Pal.darkOutline;           
           speed = 0.55f;
           hitSize = 6f;
           health = 340;
           armor = 2f;
           canBoost = true;
           boostMultiplier = 2.5f;
           constructor = MechUnit::create;
           factions.add(AxFactions.axthrix);
           weapons.add(new Weapon("puw"){{
                shootSound = Sounds.sap;
                shootY = 2f;
                x = 0f;
                y = 0f;
                mirror = false;
                top = false;
                reload = 200;
                heatColor = Pal.heal;
                immunities.add(AxthrixStatus.nanodiverge);
                bullet = new BasicBulletType(){{
                    homingRange = 40f;
                    homingPower = 4f;
                    homingDelay = 5f;
                    width = 0.5f;
                    height = 0.5f;
                    damage = 2;
                    lifetime = 40;
                    speed = 3;
                    healPercent = 1;
                    collidesTeam = true;
                    trailEffect = Fx.none;
                    trailInterval = 3f;
                    trailParam = 4f;
                    trailColor = Pal.heal;
                    trailLength = 4;
                    trailWidth = 0.5f;
                    status = AxthrixStatus.nanodiverge;
                    backColor = Pal.heal;
                    frontColor = Color.white;
                }};
            }});    

            abilities.add(new ForceFieldAbility(20f, 0.1f, 100f, 40f * 6));
        }};

        blockade = new AxUnitType("blockade"){{
           outlineColor = Pal.darkOutline;
           armor = 5f;
           speed = 0.7f;
           hitSize = 11f;
           health = 650;
           buildSpeed = 2f;
           canBoost = true;
           boostMultiplier = 1.5f;
           constructor = MechUnit::create;
           factions.add(AxFactions.axthrix);

            abilities.add(new ShieldArcAbility(){{
                region = "aj-blockade-shield";
                radius = 30f;
                angle = 45f;
                y = -24f;
                regen = 0.6f;
                cooldown = 200f;
                max = 600f;
                width = 6f;
                whenShooting = false;
            }});

            weapons.add(new Weapon("aj-nano-launcher"){{
                shootSound = Sounds.blaster;
                shootWarmupSpeed = 0.06f;
                minWarmup = 0.9f;
                x = 6;
                y = 0;
                shootX = 2f;
                shootY = -1f;
                mirror = true;
                top = false;
                reload = 40;
                inaccuracy = 5;
                heatColor = Pal.heal;
                immunities.add(AxthrixStatus.nanodiverge);
                parts.add(
                new RegionPart("-shell"){{
                    progress = PartProgress.warmup;
                    heatProgress = PartProgress.warmup;
                    heatColor = Pal.heal;
                    mirror = false;
                    under = false;
                    moveX = 2f;
                    moves.add(new PartMove(PartProgress.recoil, -1f, 1f, -25f)); 
                }},
                new RegionPart("-bar"){{
                    progress = PartProgress.warmup;
                    heatColor = Pal.heal;
                    layerOffset = -0.5f;
                    mirror = false;
                    under = true;
                    moveX = 2f;
                }});

                bullet = new BasicBulletType(){{
                    speed = 0f;
                    keepVelocity = false;
                    collidesAir = false;
                    spawnUnit = new MissileUnitType("nano-missile"){{
                        targetAir = false;
                        speed = 2.3f;
                        maxRange = 6f;
                        lifetime = 60f * 1.4f;
                        outlineColor = Pal.darkOutline;
                        engineColor = trailColor = Pal.heal;
                        engineLayer = Layer.effect;
                        health = 45;
                        loopSoundVolume = 0.1f;

                        weapons.add(new Weapon(){{
                            shootCone = 360f;
                            mirror = false;
                            reload = 1f;
                            shootOnDeath = true;
                            bullet = new ExplosionBulletType(80f, 25f){{
                                shootEffect = Fx.massiveExplosion;
                                fragBullets = 20;
                                fragBullet = new BasicBulletType(5.5f, 50){{
                                    homingRange = 40f;
                                    homingPower = 4f;
                                    homingDelay = 5f;
                                    width = 0.5f;
                                    height = 0.5f;
                                    damage = 1;
                                    lifetime = 40;
                                    speed = 1;
                                    healPercent = 1;
                                    collidesTeam = true;
                                    trailEffect = Fx.none;
                                    trailInterval = 3f;
                                    trailParam = 4f;
                                    trailColor = Pal.heal;
                                    trailLength = 4;
                                    trailWidth = 0.5f;
                                    status = AxthrixStatus.nanodiverge;
                                    backColor = Pal.heal;
                                    frontColor = Color.white;
                                }};
                            }};
                        }});
                    }};    
                }};
            }});
        }}; 

        palisade = new AxUnitType("palisade"){{
           outlineColor = Pal.darkOutline;
           armor = 12f;
           speed = 0.8f;
           hitSize = 13;
           health = 4050;
           buildSpeed = 3f;
           canBoost = true;
           boostMultiplier = 1.5f;
           constructor = MechUnit::create;
           factions.add(AxFactions.axthrix);

            abilities.add(new ShieldArcAbility(){{
                region = "aj-palisade-shield";
                radius = 35f;
                y = -24f;
                angle = 50f;
                regen = 0.6f;
                cooldown = 200f;
                max = 800f;
                width = 8f;
                whenShooting = false;
            }});

            weapons.add(new Weapon("aj-recursor"){{
                shootStatus = AxthrixStatus.vindicationI;
                shootStatusDuration = 250f;
                shootSound = Sounds.shockBlast;
                shootWarmupSpeed = 0.06f;
                minWarmup = 0.9f;
                x = 8f;
                y = 0.5f;
                shootX = 4f;
                shootY = -2f;
                mirror = true;
                recoil = 5f;
                top = false;
                reload = 120;
                inaccuracy = 50;
                shoot.shots = 60;
                shoot.shotDelay = 0;
                heatColor = Pal.heal;
                immunities.add(AxthrixStatus.nanodiverge);
                parts.add(
                new RegionPart("-pin"){{
                    progress = PartProgress.warmup;
                    heatProgress = PartProgress.recoil;
                    heatColor = Pal.heal;
                    mirror = false;
                    under = true;
                    moveX = 0f; 
                }},
                new RegionPart("-barrel"){{
                    progress = PartProgress.warmup;
                    heatProgress = PartProgress.recoil;
                    heatColor = Pal.heal;
                    mirror = false;
                    under = false;
                    moveX = 3f;
                    moveY = -1f;
                    moveRot = -15f;
                    children.add(new RegionPart("-mount"){{
                        progress = PartProgress.warmup;
                        mirror = false;
                        under = true;
                        layerOffset = -2f;
                        moveY = 0f;
                        moveX = 0f;
                    }});
                }});

                bullet = new BasicBulletType(2f, 9){{
                    homingRange = 40f;
                    homingPower = 4f;
                    homingDelay = 5f;
                    width = 0.5f;
                    height = 0.5f;
                    damage = 8;
                    lifetime = 20;
                    speed = 3;
                    healPercent = 1;
                    collidesTeam = true;
                    trailEffect = Fx.none;
                    trailInterval = 3f;
                    trailParam = 4f;
                    trailColor = Pal.heal;
                    trailLength = 4;
                    trailWidth = 0.5f;
                    status = AxthrixStatus.nanodiverge;
                    backColor = Pal.heal;
                    frontColor = Color.white;
                }};
            }});
        }}; 

        parapet = new AxUnitType("parapet"){{
           outlineColor = Pal.darkOutline;
           armor = 17f;           
           speed = 0.70f;
           hitSize = 24f;
           health = 8600;
           buildSpeed = 4f;
           canBoost = true;
           boostMultiplier = 1.5f;
           constructor = MechUnit::create;
           factions.add(AxFactions.axthrix);

            abilities.add(new ShieldArcAbility(){{
                region = "aj-parapet-shield";
                radius = 30f;
                angle = 100f;
                y = -22f;
                regen = 0.6f;
                cooldown = 200f;
                max = 1000f;
                width = 10f; 
                whenShooting = false;           
            }});

            weapons.add(new Weapon("aj-hammer-shotgun"){{
                shootSound = Sounds.shockBlast;
                shootStatus = AxthrixStatus.vindicationII;
                shootStatusDuration = 450f;
                shootWarmupSpeed = 0.06f;
                minWarmup = 0.9f;
                top = false;
                x = 12;
                y = 0;
                mirror = true;
                alternate = false;
                reload = 220;
                inaccuracy = 50;
                shoot.shots = 80;
                shoot.shotDelay = 1;
                heatColor = Pal.heal;
                immunities.add(AxthrixStatus.nanodiverge);
                parts.add(
                new RegionPart("-blade"){{
                    progress = PartProgress.warmup;
                    heatProgress = PartProgress.warmup;
                    heatColor = Pal.heal;
                    mirror = false;
                    under = false;
                    moveX = 2f;
                    moves.add(new PartMove(PartProgress.recoil, -1f, 1f, -25f));
                    children.add(new RegionPart("-piston"){{
                        progress = PartProgress.warmup;
                        heatProgress = PartProgress.warmup;
                        heatColor = Pal.heal;
                        mirror = false;
                        under = false;
                        moveY = 2f;
                        moveX = 0f;
                        moves.add(new PartMove(PartProgress.recoil, 0f, -4f, 0f));
                    }}); 
                }});

                bullet = new BasicBulletType(2f, 9){{
                    homingRange = 40f;
                    homingPower = 4f;
                    homingDelay = 5f;
                    width = 0.5f;
                    height = 0.5f;
                    damage = 18;
                    lifetime = 40;
                    speed = 3;
                    healPercent = 1;
                    collidesTeam = true;
                    trailEffect = Fx.none;
                    trailInterval = 3f;
                    trailParam = 4f;
                    trailColor = Pal.heal;
                    trailLength = 4;
                    trailWidth = 0.5f;
                    status = AxthrixStatus.nanodiverge;
                    backColor = Pal.heal;
                    frontColor = Color.white;
                }};
            }});
            weapons.add(new Weapon("aj-trombone"){{
                shootSound = Sounds.plasmaboom;
                shootWarmupSpeed = 0.06f;
                minWarmup = 0.9f;
                top = true;
                layerOffset = 0.001f;
                x = 8.2f;
                y = -3f;
                rotate = true;
                mirror = true;
                reload = 80;
                inaccuracy = 10;
                shoot.shots = 3;
                shoot.shotDelay = 5;
                immunities.add(AxthrixStatus.vibration);
                parts.add(
                new RegionPart("-shell"){{
                    progress = PartProgress.warmup;
                    heatProgress = PartProgress.warmup;
                    heatColor = Pal.heal;
                    mirror = true;
                    under = false;
                    moveX = -1.5f;
                    moveY = -2f;
                    moveRot = -15f;
                    moves.add(new PartMove(PartProgress.recoil, 1f, -2f, -5f));
                    children.add(new RegionPart("-bar"){{
                        progress = PartProgress.warmup;
                        heatProgress = PartProgress.recoil;
                        heatColor = Pal.heal;
                        mirror = false;
                        under = true;
                        moveY = 0f;
                        moveX = 0f;
                        layerOffset = -1f;
                    }});
                    children.add(new RegionPart("-piston"){{
                        progress = PartProgress.warmup;
                        heatProgress = PartProgress.recoil;
                        heatColor = Pal.heal;
                        mirror = false;
                        under = true;
                        moveY = 1.5f;
                        moveX = 0f;
                        moves.add(new PartMove(PartProgress.recoil, 0f, -3.5f, 0f));
                    }}); 
                }});
                bullet = new SonicBulletType(){{
                    damage = 150;
                    width = 12f;
                    height = 6f;
                }};
            }});        
        }}; 
        impediment = new AxUnitType("impediment"){{
           outlineColor = Pal.darkOutline;
           armor = 25f;           
           speed = 0.60f;
           health = 14460;
           buildSpeed = 4f;
           constructor = MechUnit::create;
           factions.add(AxFactions.axthrix);

            abilities.add(new ShieldArcAbility(){{
                region = "aj-impediment-shield";
                radius = 40f;
                angle = 100f;
                y = -22f;
                regen = 0.6f;
                cooldown = 200f;
                max = 1000f;
                width = 14f;
                whenShooting = false;           
            }});

            abilities.add(new NanobotStormAbility());

            weapons.add(new Weapon("aj-tuba"){{
                shootSound = Sounds.plasmaboom;
                shootStatus = AxthrixStatus.vindicationIII;
                shootStatusDuration = 200f;
                shootWarmupSpeed = 0.06f;
                minWarmup = 0.9f;
                top = true;
                x = 16;
                y = 0f;
                shootX = -24f;
                shootY = 12f;
                mirror = true;
                reload = 80;
                inaccuracy = 10;
                shoot.shots = 4;
                shoot.shotDelay = 5;
                immunities.add(AxthrixStatus.vibration);
                layerOffset = 0.2f;
                parts.add(
                new RegionPart("-arm"){{
                    progress = PartProgress.warmup;
                    heatProgress = PartProgress.warmup.delay(0.6f);
                    heatColor = Pal.heal;
                    mirror = false;
                    under = false;
                    x = -17f;
                    moveX = -1.5f;
                    moveY = -2f;
                    moveRot = -15f;
                    moves.add(new PartMove(PartProgress.recoil, 0f,  1f, -5f));
                    children.add(new ShapePart(){{
                        progress = PartProgress.warmup.delay(0.6f);
                        rotateSpeed = -5;
                        color = Pal.heal;
                        sides = 8;
                        hollow = true;
                        stroke = 0f;
                        strokeTo = 1.6f;
                        radius = 6f;
                        layer = Layer.effect;
                        y = -5;
                        x = -22;
                    }});
                    children.add(new ShapePart(){{
                        progress = PartProgress.warmup.delay(0.6f);
                        rotateSpeed = 4;
                        color = Pal.heal;
                        sides = 6;
                        hollow = true;
                        stroke = 0f;
                        strokeTo = 1.6f;
                        radius = 4f;
                        layer = Layer.effect;
                        y = -5;
                        x = -22;
                    }});
                    children.add(new ShapePart(){{
                        progress = PartProgress.warmup.delay(0.6f);
                        rotateSpeed = -5;
                        color = Pal.heal;
                        sides = 20;
                        hollow = true;
                        stroke = 0f;
                        strokeTo = 1.6f;
                        radius = 9f;
                        layer = Layer.effect;
                        y = -5;
                        x = -22;
                    }});
                    children.add(new HaloPart(){{
                        progress = PartProgress.warmup.delay(0.6f);
                        color = Pal.heal;
                        sides = 8;
                        hollow = true;
                        shapes = 5;
                        stroke = 0f;
                        strokeTo = 4f;
                        radius = 1f;
                        haloRadius = 7f;
                        haloRotateSpeed = 1;
                        layer = Layer.effect;
                        y = -5;
                        x = -22;
                    }});
                    children.add(new RegionPart("-plate"){{
                        progress = PartProgress.warmup;
                        heatProgress = PartProgress.warmup.delay(0.6f);
                        heatColor = Pal.heal;
                        mirror = false;
                        under = true;
                        moveY = 2f;
                        moveX = -11f;
                        children.add(new RegionPart("-wing"){{
                            progress = PartProgress.warmup;
                            heatProgress = PartProgress.warmup.delay(0.6f);
                            heatColor = Pal.heal;
                            mirror = false;
                            under = true;
                            moveY = -3f;
                            moveX = -6f;
                            moveRot = -10f;
                            moves.add(new PartMove(PartProgress.recoil, 0f, -2f, -10f));
                        }});
                    }});
                }});
                bullet = new SonicBulletType(){{
                    damage = 200;
                    width = 9f;
                    height = 4.5f;
                    shrinkY = -0.6f;
                    shrinkX = -1.8f;
                 }};
            }});
            weapons.add(new Weapon("aj-pod"){{
                shootCone = 360f;
                shootSound = Sounds.blaster;
                shootWarmupSpeed = 0.06f;
                minWarmup = 0.9f;
                baseRotation = 180f;
                top = false;
                x = 0;
                y = -1;
                shootY = -6f;
                mirror = false;
                reload = 1020;
                inaccuracy = 40;
                shoot.shots = 60;
                shoot.shotDelay = 2;
                heatColor = Pal.heal;
                layerOffset = -2f;
                immunities.add(AxthrixStatus.nanodiverge);
                

                bullet = new BasicBulletType(){{
                    speed = 0f;
                    keepVelocity = false;
                    collidesAir = false;
                    spawnUnit = new MissileUnitType("nano-swarmer"){{
                        targetAir = true;
                        speed = 4f;
                        maxRange = 14f;
                        lifetime = 120f * 1.6f;
                        outlineColor = Pal.darkOutline;
                        engineColor = trailColor = Pal.heal;
                        engineLayer = Layer.effect;
                        health = 45;
                        loopSoundVolume = 0.1f;

                        weapons.add(new Weapon(){{
                            shootCone = 360f;
                            mirror = false;
                            reload = 1f;
                            shootOnDeath = true;
                            bullet = new ExplosionBulletType(120f, 85f){{
                                shootEffect = Fx.massiveExplosion;
                            }};
                        }});
                    }};    
                }};
            }});    
        }};
        //Special Flying Mount
        //amos,aymoss,amalik,anuvaha,ambuvahini,
        amos = new MountUnitType("amos")
        {{
            localizedName = "[#a52ac7]Amos";
            description = """
                          [#a52ac7]Can pick up and use any 1x1 Item Turret.
                          Unit Item Storage will restock current attached turret.[]
                          [#800000]Only the first turret picked up will be operational.
                          """;

            constructor = PayloadUnit::create;
            health = 600;
            armor = 2;
            faceTarget = true;
            factions.add(AxFactions.axthrix);
            flying = true;
            hitSize = 2*2;
            engineColor = Color.valueOf("a52ac7");
            itemCapacity = 300;
            itemOffsetY = 6;
            speed = 20f / 7.5f;
            rotateSpeed = 9 / 7.5f;
            accel = 0.08f;
            drag = 0.014f;
            strafePenalty = 1;
            lowAltitude = true;
            pickupUnits = false;
            omniMovement = false;
            payloadCapacity = tilePayload * (1 * 1);
            range = 12*8;
            engineSize = 0;
            engines = Seq.with(
                    new UnitEngine(0,-14,6,-90),
                    new UnitEngine(-10,-10,6,180+45),
                    new UnitEngine(10,-10,6,270+45)
            );
        }};
        aymoss = new MountUnitType("aymoss")
        {{
            localizedName = "[#a52ac7]Aymoss";
            description = """
                          [#a52ac7]Can pick up and use any 2x2 Item Turret or smaller.
                          Unit Item Storage will restock current attached turret.[]
                          [#800000]Only the first turret picked up will be operational.
                          """;

            constructor = PayloadUnit::create;
            health = 1800;
            armor = 5;
            faceTarget = true;
            factions.add(AxFactions.axthrix);
            flying = true;
            hitSize = 4*4;
            engineColor = Color.valueOf("a52ac7");
            itemCapacity = 600;
            itemOffsetY = 6;
            speed = 18f / 7.5f;
            rotateSpeed = 8 / 7.5f;
            accel = 0.07f;
            drag = 0.015f;
            strafePenalty = 1;
            lowAltitude = true;
            pickupUnits = false;
            omniMovement = false;
            payloadCapacity = tilePayload * (2 * 2);
            range = 12*8;
            engineSize = 0;
            engines = Seq.with(
                    new UnitEngine(0,-14,6,-90),
                    new UnitEngine(-10,-10,6,180+45),
                    new UnitEngine(10,-10,6,270+45)
            );
        }};
        amalik = new MountUnitType("amalik")
        {{
            localizedName = "[#a52ac7]Amalik";
            description = """
                          [#a52ac7]Can pick up and use any 3x3 Item Turret or smaller.
                          Unit Item Storage will restock current attached turret.[]
                          [#800000]Only the first turret picked up will be operational.
                          """;

            constructor = PayloadUnit::create;
            health = 5400;
            armor = 8;
            faceTarget = true;
            factions.add(AxFactions.axthrix);
            flying = true;
            hitSize = 6*6;
            engineColor = Color.valueOf("a52ac7");
            itemCapacity = 1200;
            itemOffsetY = 6;
            speed = 16f / 7.5f;
            rotateSpeed = 7 / 7.5f;
            accel = 0.06f;
            drag = 0.016f;
            strafePenalty = 1;
            lowAltitude = true;
            pickupUnits = false;
            omniMovement = false;
            payloadCapacity = tilePayload * (3 * 3);
            range = 12*8;
            engineSize = 0;
            engines = Seq.with(
                    new UnitEngine(0,-14,6,-90),
                    new UnitEngine(-10,-10,6,180+45),
                    new UnitEngine(10,-10,6,270+45)
            );
        }};
        anuvaha = new MountUnitType("anuvaha")
        {{
            localizedName = "[#a52ac7]Anuvaha";
            description = """
                          [#a52ac7]Can pick up and use any 4x4 Item Turret or smaller.
                          Unit Item Storage will restock current attached turret.[]
                          [#800000]Only the first turret picked up will be operational.
                          """;

            constructor = PayloadUnit::create;
            health = 16200;
            armor = 11;
            faceTarget = true;
            factions.add(AxFactions.axthrix);
            flying = true;
            hitSize = 8*8;
            engineColor = Color.valueOf("a52ac7");
            itemCapacity = 2400;
            itemOffsetY = 6;
            speed = 14f / 7.5f;
            rotateSpeed = 6 / 7.5f;
            accel = 0.05f;
            drag = 0.017f;
            strafePenalty = 1;
            lowAltitude = true;
            pickupUnits = false;
            omniMovement = false;
            payloadCapacity = tilePayload * (4 * 4);
            range = 12*8;
            engineSize = 0;
            engines = Seq.with(
                    new UnitEngine(0,-14,6,-90),
                    new UnitEngine(-10,-10,6,180+45),
                    new UnitEngine(10,-10,6,270+45)
            );
        }};
        ambuvahini = new MountUnitType("ambuvahini")
        {{
            localizedName = "[#a52ac7]Ambuvahini";
            description = """
                          [#a52ac7]Can pick up and use any 5x5 Item Turret or smaller.
                          Unit Item Storage will restock current attached turret.[]
                          [#800000]Only the first turret picked up will be operational.
                          """;

            constructor = PayloadUnit::create;
            health = 32500;
            armor = 26;
            faceTarget = true;
            factions.add(AxFactions.axthrix);
            flying = true;
            hitSize = 10*10;
            engineColor = Color.valueOf("a52ac7");
            itemCapacity = 4800;
            itemOffsetY = 6;
            speed = 12f / 7.5f;
            rotateSpeed = 5 / 7.5f;
            accel = 0.04f;
            drag = 0.018f;
            strafePenalty = 1;
            lowAltitude = true;
            pickupUnits = false;
            omniMovement = false;
            payloadCapacity = tilePayload * (5 * 5);
            range = 12*8;
            engineSize = 0;
            engines = Seq.with(
                    new UnitEngine(0,-14,6,-90),
                    new UnitEngine(-10,-10,6,180+45),
                    new UnitEngine(10,-10,6,270+45)
            );
        }};
        arcalishion = new MountUnitType("arcalishion")//Todo Might have weapons or abilities
        {{
            localizedName = "[#a52ac7]Arcalishion";
            description = """
                          [orange]|Teir Xalibur Unit|
                          (This Means This Is A Boss)[]
                          --------------------------------------------------
                          [#a52ac7]Can pick up and use any 6x6 Item Turret or smaller.
                          Unit Item Storage will restock current attached turret.[]
                          [#800000]Only the first turret picked up will be operational.
                          """;

            constructor = PayloadUnit::create;
            health = 65000;
            armor = 39;
            faceTarget = true;
            factions.add(AxFactions.axthrix);
            flying = true;
            hitSize = 12*12;
            engineColor = Color.valueOf("a52ac7");
            itemCapacity = 10000;
            itemOffsetY = 20;
            speed = 8f / 7.5f;
            rotateSpeed = 4 / 7.5f;
            accel = 0.03f;
            drag = 0.019f;
            strafePenalty = 1;
            lowAltitude = true;
            pickupUnits = false;
            omniMovement = false;
            payloadCapacity = tilePayload * (6 * 6);
            weapons.add(
                    new Weapon(){
                        float rangeWeapon = 520f;

                        @Override
                        public void draw(Unit unit, WeaponMount mount){
                            float z = Draw.z();

                            Tmp.v1.trns(unit.rotation, y);
                            float f = 1 - mount.reload / reload;
                            float rad = 12f;

                            float f1 = Mathf.curve(f,  0.4f, 1f);
                            Draw.z(Layer.bullet);
                            Draw.color(heatColor);
                            for(int i : Mathf.signs){
                                for(int j : Mathf.signs){
                                    DrawFunc.tri(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, f1 * rad / 3f + Mathf.num(j > 0) * 2f * (f1 + 1) / 2, (rad * 3f + Mathf.num(j > 0) * 20f) * f1, j * Time.time + 90 * i);
                                }
                            }

                            TextureRegion arrowRegion = NHContent.arrowRegion;

                            Tmp.v6.set(mount.aimX, mount.aimY).sub(unit);
                            Tmp.v2.set(mount.aimX, mount.aimY).sub(unit).nor().scl(Math.min(Tmp.v6.len(), rangeWeapon)).add(unit);

                            for (int l = 0; l < 4; l++) {
                                float angle = 45 + 90 * l;
                                for (int i = 0; i < 4; i++) {
                                    Tmp.v3.trns(angle, (i - 4) * tilesize + tilesize).add(Tmp.v2);
                                    float fS = (100 - (Time.time + 25 * i) % 100) / 100 * f1 / 4;
                                    Draw.rect(arrowRegion, Tmp.v3.x, Tmp.v3.y, arrowRegion.width * fS, arrowRegion.height * fS, angle + 90);
                                }
                            }

                            Lines.stroke((1.5f + Mathf.absin( Time.time + 4, 8f, 1.5f)) * f1, heatColor);
                            Lines.square(Tmp.v2.x, Tmp.v2.y, 4 + Mathf.absin(8f, 4f), 45);

                            Lines.stroke(rad / 2.5f * mount.heat, heatColor);
                            Lines.circle(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, rad * 2 * (1 - mount.heat));

                            Draw.color(heatColor);
                            Fill.circle(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, f * rad);
                            Lines.stroke(f * 1.5f);
                            DrawFunc.circlePercentFlip(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, f * rad + 5, Time.time, 20f);
                            Draw.color(Color.white);
                            Fill.circle(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, f * rad * 0.7f);

                            Draw.z(z);
                        }

                        @Override
                        protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation){
                            shootSound.at(shootX, shootY, Mathf.random(soundPitchMin, soundPitchMax));

                            BulletType ammo = bullet;
                            float lifeScl = ammo.scaleLife ? Mathf.clamp(Mathf.dst(shootX, shootY, mount.aimX, mount.aimY) / ammo.range) : 1f;

                            Tmp.v6.set(mount.aimX, mount.aimY).sub(unit);
                            Tmp.v1.set(mount.aimX, mount.aimY).sub(unit).nor().scl(Math.min(Tmp.v6.len(), rangeWeapon)).add(unit);

                            Bullet b = bullet.create(unit, unit.team, Tmp.v1.x, Tmp.v1.y, 0);
                            b.vel.setZero();
                            b.set(Tmp.v1);
                            unit.apply(shootStatus, shootStatusDuration);

                            if(Vars.headless)return;
                            Vec2 vec2 = new Vec2().trns(unit.rotation, y).add(unit);
                            PosLightning.createEffect(vec2, b, NHColor.thurmixRed, 3, 2.5f);
                            for(int i = 0; i < 5; i++){
                                Time.run(i * 6f, () -> {
                                    NHFx.chainLightningFade.at(vec2.x, vec2.y, Mathf.random(8, 14), NHColor.thurmixRed, b);
                                });
                            }

                            ammo.shootEffect.at(shootX, shootY, rotation);
                            ammo.smokeEffect.at(shootX, shootY, rotation);
                        }

                        {
                            y = 25.425f;
                            x = 0;
                            shootY = 0;
                            shoot = new ShootPattern();
                            reload = 900f;
                            rotateSpeed = 100f;
                            rotate = true;
                            top = false;
                            mirror = alternate = predictTarget = false;
                            heatColor = NHColor.thurmixRed;
                            shootSound = NHSounds.hugeShoot;
                            bullet = new EffectBulletType(480f){
                                @Override
                                protected float calculateRange(){
                                    return rangeWeapon;
                                }

                                @Override
                                public void despawned(Bullet b){
                                    super.despawned(b);

                                    Vec2 vec = new Vec2().set(b);

                                    float damageMulti = b.damageMultiplier();
                                    Team team = b.team;
                                    for(int i = 0; i < splashDamageRadius / (tilesize * 3.5f); i++){
                                        int finalI = i;
                                        Time.run(i * despawnEffect.lifetime / (splashDamageRadius / (tilesize * 2)), () -> {
                                            Damage.damage(team, vec.x, vec.y, tilesize * (finalI + 6), splashDamage * damageMulti, true);
                                        });
                                    }

                                    Units.nearby(team, vec.x, vec.y, splashDamageRadius * 2, u -> {
                                        u.heal((1 - u.healthf()) / 3 * u.maxHealth());
                                        u.apply(StatusEffects.overclock, 360f);
                                    });

                                    Units.nearbyEnemies(team, vec.x, vec.y, splashDamageRadius * 2, u -> {
                                        u.apply(NHStatusEffects.scannerDown, 600f);
                                    });

                                    if(!NHSetting.enableDetails())return;
                                    float rad = 120;
                                    float spacing = 2.5f;

                                    for(int k = 0; k < (despawnEffect.lifetime - NHFx.chainLightningFadeReversed.lifetime) / spacing; k++){
                                        Time.run(k * spacing, () -> {
                                            for(int j : Mathf.signs){
                                                Vec2 v = Tmp.v6.rnd(rad * 2 + Mathf.random(rad * 4)).add(vec);
                                                (j > 0 ? NHFx.chainLightningFade : NHFx.chainLightningFadeReversed).at(v.x, v.y, 12f, hitColor, vec);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void update(Bullet b){
                                    float rad = 120;

                                    Effect.shake(8 * b.fin(), 6, b);

                                    if(b.timer(1, 12)){
                                        Seq<Teamc> entites = new Seq<>();

                                        Units.nearbyEnemies(b.team, b.x, b.y, rad * 2.5f * (1 + b.fin()) / 2, entites::add);

                                        Units.nearbyBuildings(b.x, b.y, rad * 2.5f * (1 + b.fin()) / 2, e -> {
                                            if(e.team != b.team)entites.add(e);
                                        });

                                        entites.shuffle();
                                        entites.truncate(15);

                                        for(Teamc e : entites){
                                            PosLightning.create(b, b.team, b, e, lightningColor, false, lightningDamage, 5 + Mathf.random(5), PosLightning.WIDTH, 1, p -> {
                                                NHFx.lightningHitSmall.at(p.getX(), p.getY(), 0, lightningColor);
                                            });
                                        }
                                    }

                                    if(NHSetting.enableDetails() && b.lifetime() - b.time() > NHFx.chainLightningFadeReversed.lifetime)for(int i = 0; i < 2; i++){
                                        if(Mathf.chanceDelta(0.2 * Mathf.curve(b.fin(), 0, 0.8f))){
                                            for(int j : Mathf.signs){
                                                Sounds.spark.at(b.x, b.y, 1f, 0.3f);
                                                Vec2 v = Tmp.v6.rnd(rad / 2 + Mathf.random(rad * 2) * (1 + Mathf.curve(b.fin(), 0, 0.9f)) / 1.5f).add(b);
                                                (j > 0 ? NHFx.chainLightningFade : NHFx.chainLightningFadeReversed).at(v.x, v.y, 12f, hitColor, b);
                                            }
                                        }
                                    }

                                    if(b.fin() > 0.05f && Mathf.chanceDelta(b.fin() * 0.3f + 0.02f)){
                                        NHSounds.blaster.at(b.x, b.y, 1f, 0.3f);
                                        Tmp.v1.rnd(rad / 4 * b.fin());
                                        NHFx.shuttleLerp.at(b.x + Tmp.v1.x, b.y + Tmp.v1.y, Tmp.v1.angle(), hitColor, Mathf.random(rad, rad * 3f) * (Mathf.curve(b.fin(Interp.pow2In), 0, 0.7f) + 2) / 3);
                                    }
                                }

                                @Override
                                public void draw(Bullet b){
                                    float fin = Mathf.curve(b.fin(), 0, 0.02f);
                                    float f = fin * Mathf.curve(b.fout(), 0f, 0.1f);
                                    float rad = 120;

                                    float z = Draw.z();

                                    float circleF = (b.fout(Interp.pow2In) + 1) / 2;

                                    Draw.color(hitColor);
                                    Lines.stroke(rad / 20 * b.fin());
                                    Lines.circle(b.x, b.y, rad * b.fout(Interp.pow3In));
                                    Lines.circle(b.x, b.y, b.fin(Interp.circleOut) * rad * 3f * Mathf.curve(b.fout(), 0, 0.05f));

                                    Rand rand = NHFunc.rand;
                                    rand.setSeed(b.id);
                                    for(int i = 0; i < (int)(rad / 3); i++){
                                        Tmp.v1.trns(rand.random(360f) + rand.range(1f) * rad / 5 * b.fin(Interp.pow2Out), rad / 2.05f * circleF + rand.random(rad * (1 + b.fin(Interp.circleOut)) / 1.8f));
                                        float angle = Tmp.v1.angle();
                                        DrawFunc.tri(b.x + Tmp.v1.x, b.y + Tmp.v1.y, (b.fin() + 1) / 2 * 28 + rand.random(0, 8), rad / 16 * (b.fin(Interp.exp5In) + 0.25f), angle);
                                        DrawFunc.tri(b.x + Tmp.v1.x, b.y + Tmp.v1.y, (b.fin() + 1) / 2 * 12 + rand.random(0, 2), rad / 12 * (b.fin(Interp.exp5In) + 0.5f) / 1.2f, angle - 180);
                                    }

                                    Angles.randLenVectors(b.id + 1, (int)(rad / 3), rad / 4 * circleF, rad * (1 + b.fin(Interp.pow3Out)) / 3, (x, y) -> {
                                        float angle = Mathf.angle(x, y);
                                        DrawFunc.tri(b.x + x, b.y + y, rad / 8 * (1 + b.fout()) / 2.2f, (b.fout() * 3 + 1) / 3 * 25 + rand.random(4, 12) * (b.fout(Interp.circleOut) + 1) / 2, angle);
                                        DrawFunc.tri(b.x + x, b.y + y, rad / 8 * (1 + b.fout()) / 2.2f, (b.fout() * 3 + 1) / 3 * 9 + rand.random(0, 2) * (b.fin() + 1) / 2, angle - 180);
                                    });

                                    Drawf.light(b.x, b.y, rad * f * (b.fin() + 1) * 2, Draw.getColor(), 0.7f);

                                    Draw.z(Layer.effect + 0.001f);
                                    Draw.color(hitColor);
                                    Fill.circle(b.x, b.y, rad * fin * circleF / 2f);
                                    Draw.color(NHColor.thurmixRedDark);
                                    Fill.circle(b.x, b.y, rad * fin * circleF * 0.75f / 2f);
                                    Draw.z(Layer.bullet - 0.1f);
                                    Draw.color(NHColor.thurmixRedDark);
                                    Fill.circle(b.x, b.y, rad * fin * circleF * 0.8f / 2f);
                                    Draw.z(z);
                                }

                                {
                                    hittable = false;
                                    collides = false;
                                    collidesTiles = collidesAir = collidesGround = true;
                                    speed = 100;

                                    despawnHit = true;
                                    keepVelocity = false;

                                    splashDamageRadius = 800f;
                                    splashDamage = 800f;

                                    lightningDamage = 200f;
                                    lightning = 36;
                                    lightningLength = 60;
                                    lightningLengthRand = 60;

                                    rangeWeapon = 400f;
                                    hitShake = despawnShake = 40f;
                                    drawSize = clipSize = 800f;
                                    hitColor = lightColor = trailColor = lightningColor = NHColor.thurmixRed;

                                    fragBullets = 22;
                                    fragBullet = NHBullets.collapserBullet;
                                    hitSound = NHSounds.hugeBlast;
                                    hitSoundVolume = 4f;

                                    fragLifeMax = 1.1f;
                                    fragLifeMin = 0.7f;
                                    fragVelocityMax = 0.6f;
                                    fragVelocityMin = 0.2f;

                                    status = NHStatusEffects.weak;
                                    statusDuration = 3000f;

                                    shootEffect = NHFx.lightningHitLarge(hitColor);

                                    hitEffect = NHFx.hitSpark(hitColor, 240f, 220, 900, 8, 27);
                                    despawnEffect = NHFx.collapserBulletExplode;
                                }
                            };
                        }
                    }
            );
            range = 12*8;
            engineSize = 0;
            engines = Seq.with(
                    new UnitEngine(-30,-70,14,-90),
                    new UnitEngine(30,-70,14,-90),

                    new UnitEngine(-70,-35,8,180+45),
                    new UnitEngine(70,-35,8,270+45),

                    new UnitEngine(-60,29,10,180+45),
                    new UnitEngine(60,29,10,270+45)
            );
        }};
        //legends
        //yin and yang tree
        spate = new UnitType("spate"){{//Todo Missile weapon
           outlineColor = Pal.darkOutline;
           flying = true;           
           speed = 2f;
           hitSize = 6f;
           health = 340;
           armor = 2f;
           constructor = UnitEntity::create;
           weapons.add(new Weapon("puw"){{
                shootY = 2f;
                x = 1f;
                y = 0f;
                mirror = true;
                reload = 10;
                top = false;
                heatColor = Pal.heal;
                bullet = new BasicBulletType(){{
                    damage = 40;
                    lifetime = 60;
                    speed = 5;
                }};
            }});  

            abilities.add(new SStatusFieldAbility(AxthrixStatus.precludedA, 160f, 140f, 100f){{
                atNotShoot = true;
            }});
        }};
        influx = new UnitType("influx"){{//Todo Cannon weapon
           outlineColor = Pal.darkOutline;         
           speed = 2f;
           hitSize = 6f;
           health = 340;
           armor = 2f;
           faceTarget = false;
           crushDamage = 500f;
           constructor = TankUnit::create;
           weapons.add(new Weapon("puw"){{
                rotate = true;
                rotateSpeed = 3;
                shootY = 2f;
                x = 1f;
                y = 0f;
                mirror = false;
                reload = 10;
                top = true;
                heatColor = Pal.heal;
                bullet = new BasicBulletType(){{
                    damage = 40;
                    lifetime = 60;
                    speed = 5;
                }};
            }});  

            abilities.add(new SStatusFieldAbility(AxthrixStatus.precludedX, 160f, 140f, 100){{
                onShoot = true;
            }});
        }};
        test1 = new UnitType("test1")
        {{
            localizedName = "[#800000]TEST";

            constructor = UnitEntity::create;
            health = 6000;
            armor = 2;
            faceTarget = true;
            flying = true;
            hitSize = 2*2;
            engineColor = Color.valueOf("95abd9");
            itemCapacity = 300;
            itemOffsetY = 6;
            speed = 20f / 7.5f;
            strafePenalty = 1;
            drag = 0.8f;
            lowAltitude = true;
            omniMovement = false;
            range = 12*8;
            engineSize = 0;
            engines = Seq.with(
                    new UnitEngine(0,-14,6,-90),
                    new UnitEngine(-10,-10,6,180+45),
                    new UnitEngine(10,-10,6,270+45)
            );
            weapons.add(new Weapon(){{
                shootY = 2f;
                x = 1f;
                y = 0f;
                mirror = false;
                reload = 200;
                top = true;
                bullet = new BasicBulletType(){{
                    damage = 400;
                    lifetime = 1200;
                    speed = 5;
                }};
            }});
            for(float i : Mathf.signs) {
                abilities.add(
                    new DroneControlAbility() {{

                        rallyPos.add(new Vec2(38f * i, 8f));
                        rallyPos.add(new Vec2(20f * i, 20f));
                        spawnX = 48 / 4f * i;
                        spawnY = 7 / -4f;;
                        unitSpawn = AxthrixDrones.basicFlame;
                        constructTime = 60 * 5f;
                        setController(returnOwner(), "AttackDroneAI");
                        autoRelease = true;
                    }});
                }
            }
        };
    }
}        