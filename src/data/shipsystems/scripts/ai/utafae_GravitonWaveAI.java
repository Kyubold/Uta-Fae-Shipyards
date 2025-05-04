package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.*;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class utafae_GravitonWaveAI implements ShipSystemAIScript {

    private ShipAPI ship;
    private ShipSystemAPI system;
    private CombatEngineAPI engine;

    private static final float RANGE = 800f;
    private static final float ARC = 120f; // degrees

    

    @Override
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags arg2, CombatEngineAPI engine) {
        this.ship = ship;
        this.system = system;
        this.engine = engine;
    }

    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        if (engine == null || ship == null || !ship.isAlive()) return;
        if (!system.canBeActivated()) return; // don't proceed unless a charge is ready

        List<ShipAPI> ships = engine.getShips();
        List<MissileAPI> missiles = engine.getMissiles();

        boolean hasEnemy = false;
        boolean hasFriendly = false;

        Vector2f shipLoc = ship.getLocation();
        float facing = ship.getFacing();

        // Check ships
        for (ShipAPI other : ships) {
            if (other == ship || !other.isAlive()) continue;
            float distance = Vector2f.sub(other.getLocation(), shipLoc, null).length();
            if (distance > RANGE) continue;

            float angleTo = vectorAngle(shipLoc, other.getLocation());
            float angleDiff = Math.abs(angleDifference(facing, angleTo));
            if (angleDiff > ARC / 2f) continue;

            if (other.getOwner() == ship.getOwner()) {
                hasFriendly = true;
            } else {
                hasEnemy = true;
            }
        }

        // Check missiles
        for (MissileAPI missile : missiles) {
            if (missile.getOwner() == ship.getOwner() || !engine.isEntityInPlay(missile)) continue;

            float distance = Vector2f.sub(missile.getLocation(), shipLoc, null).length();
            if (distance > RANGE) continue;

            float angleTo = vectorAngle(shipLoc, missile.getLocation());
            float angleDiff = Math.abs(angleDifference(facing, angleTo));
            if (angleDiff > ARC / 2f) continue;

            hasEnemy = true;
        }

        if (hasEnemy && !hasFriendly) {
            ship.useSystem(); // trigger the system
        }
    }

    
    public boolean needsTarget() {
        return false;
    }

    
    public ShipAPI pickTarget() {
        return null;
    }

    // Helper: angle between two points (in degrees)
    private float vectorAngle(Vector2f from, Vector2f to) {
        return (float) Math.toDegrees(Math.atan2(to.y - from.y, to.x - from.x));
    }

    // Helper: normalized angle difference
    private float angleDifference(float a1, float a2) {
        float diff = a1 - a2;
        while (diff < -180f) diff += 360f;
        while (diff > 180f) diff -= 360f;
        return diff;
    }

    
}
