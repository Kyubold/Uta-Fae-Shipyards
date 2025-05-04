package com.fs.starfarer.api.graphics;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.Global;
import org.lwjgl.util.vector.Vector2f;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.Random;

public class utafae_DistortionConeEffect {

    private float sweepAngle = 0f;
    private float sweepSpeed = 90f; // degrees per second
    private final Random rand = new Random();

    public void update(ShipAPI ship, float range, float arc, float intensity, float lifetime, float amount) {
        if (ship == null || !ship.isAlive()) return;

        CombatEngineAPI engine = Global.getCombatEngine();
        if (engine == null || engine.isPaused()) return;

        Vector2f origin = ship.getLocation();
        float facing = ship.getFacing();

        // Update the sweep angle over time
        sweepAngle += sweepSpeed * amount;
        if (sweepAngle > arc) sweepAngle -= arc;

        float currentAngle = facing - arc / 2f + sweepAngle;

        // Spawn a faint jitter effect on the ship
        ship.setJitterUnder(ship, Color.CYAN, intensity * 0.01f, 2, 1f, 4f);

        // Create a fake “sweep” using particles
        Vector2f sweepPoint = pointOnCircle(origin, range * 0.75f, currentAngle);
        Color color = new Color(100, 255, 255, 120);
        engine.addSmoothParticle(sweepPoint, new Vector2f(), 50f, 1f, lifetime, color);

        // Random "burst" inside cone
        if (rand.nextFloat() < 0.03f) {
            float randAngle = facing - arc / 2f + rand.nextFloat() * arc;
            float randDist = range * (0.4f + rand.nextFloat() * 0.5f);
            Vector2f burstLoc = pointOnCircle(origin, randDist, randAngle);
            engine.addHitParticle(burstLoc, new Vector2f(), 75f, 1.5f, 0.4f, Color.WHITE);
        }
    }

    private Vector2f pointOnCircle(Vector2f center, float radius, float angleDeg) {
        double angleRad = Math.toRadians(angleDeg);
        float x = center.x + (float) Math.cos(angleRad) * radius;
        float y = center.y + (float) Math.sin(angleRad) * radius;
        return new Vector2f(x, y);
    }
}
