package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

import org.lwjgl.util.vector.Vector2f;
import com.fs.starfarer.api.Global;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.fs.starfarer.api.graphics.utafae_DistortionConeEffect;




//import javax.swing.text.html.HTMLDocument.Iterator;

public class utafae_GravitonWaveCannonStats extends BaseShipSystemScript {


    // Configurable cone parameters
    private static final float CONE_ANGLE_DEGREES = 120f;
    private static final float CONE_RANGE = 800f;

	private static final float PUSH_SPEED = 3500f;

    private boolean hasFired = false;

    @Override
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        if (state == State.ACTIVE && !hasFired) {
            CombatEntityAPI ship = stats.getEntity();
            if (!(ship instanceof ShipAPI)) return;

            ShipAPI sourceShip = (ShipAPI) ship;


			// Draw the visual cone using particles
		final utafae_DistortionConeEffect coneEffect = new utafae_DistortionConeEffect();
			if (state == State.ACTIVE || state == State.IN) {
				coneEffect.update((ShipAPI)ship, 800f, 120f, 1f, 0.3f, Global.getCombatEngine().getElapsedInLastFrame());
			}

            applyConeEffect(sourceShip);
            hasFired = true;
        }

        if (state == State.OUT) {
            hasFired = false; // Reset for next activation
        }
    }

    private void applyConeEffect(ShipAPI sourceShip) {
		CombatEngineAPI engine = Global.getCombatEngine();
	
		Vector2f sourceLoc = sourceShip.getLocation();
		float facing = sourceShip.getFacing();
	

		Color color = new Color(209, 117, 109, 255);
		

		
		//drawVisualCone(engine, sourceLoc, facing, CONE_RANGE, CONE_ANGLE_DEGREES, 12, color);
	
		List<ShipAPI> ships = engine.getShips();
		List<DamagingProjectileAPI> projs = engine.getProjectiles();

		
		for (DamagingProjectileAPI pro : projs) {
			if(isTarget(pro, facing, sourceLoc)){
				coneEffect(engine, pro, sourceShip);
			}
		}

		for (ShipAPI ship : ships) {
			if(isTarget(ship, facing, sourceLoc) && ship != sourceShip){
				coneEffect(engine, ship, sourceShip);
			}
		}


	}

	boolean isTarget(CombatEntityAPI ent, float facing, Vector2f shipLoc){

		float distance = Vector2f.sub(ent.getLocation(), shipLoc, null).length();
        if (distance > CONE_RANGE) return false;;

        float angleTo = vectorAngle(shipLoc, ent.getLocation());
        float angleDiff = Math.abs(angleDifference(facing, angleTo));
        if (angleDiff > CONE_ANGLE_DEGREES / 2f) return false;

		return true;
	}

	private void coneEffect(CombatEngineAPI engine, CombatEntityAPI target, ShipAPI sourceShip){

		Vector2f sourceLoc = sourceShip.getLocation();

		engine.applyDamage(target, target.getLocation(), 200, DamageType.KINETIC, 0, false, false, sourceShip);

			Vector2f pos = target.getLocation();
			Vector2f direction = getOppositeDirectionVectorFromSource(target, sourceShip);

			

				Vector2f move = new Vector2f(direction);
				move.scale((PUSH_SPEED * target.getMass() )* engine.getElapsedInLastFrame());


				Vector2f push = Vector2f.add(target.getVelocity(), move, null);
				target.getVelocity().set(push);

				engine.addHitParticle(target.getLocation(), new Vector2f(), 30f, 1f, 0.3f, Color.RED);
	}

	private void drawVisualCone(CombatEngineAPI engine, Vector2f origin, float angle, float range, float arcWidthDeg, int segments, Color color) {
		float step = arcWidthDeg / (float) segments;
		float startAngle = angle - arcWidthDeg / 2f;
	
		for (int i = 0; i <= segments; i++) {
			float currentAngle = startAngle + step * i;
			Vector2f dir = Misc.getUnitVectorAtDegreeAngle(currentAngle);
			Vector2f point = new Vector2f(origin.x + dir.x * range, origin.y + dir.y * range);
	
			engine.addSmoothParticle(
					point,
					new Vector2f(), // no velocity
					40f,            // size
					0.7f,           // brightness
					0.3f,           // duration
					color
			);
		}
	}
	

   

    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        // No persistent stat modifications
    }


	public static float getAngleDifference(float angle1, float angle2) {
        // Normalize the angles to be within the range [0, 360)
        angle1 = angle1 % 360;
        angle2 = angle2 % 360;

        // Calculate the difference
        float difference = Math.abs(angle1 - angle2);

        // Ensure the difference is the smallest angle between the two
        if (difference > 180) {
            difference = 360 - difference;
        }

        return difference;
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


	public static Vector2f getOppositeDirectionVectorFromSource(CombatEntityAPI target, ShipAPI source) {
        
        if (source== null || target == null) return new Vector2f(0f, 0f);

        Vector2f targetLoc = target.getLocation();
        Vector2f sourceLoc = source.getLocation();

        // Calculate the angle from target to player
        float angleToSource = (float) Math.toDegrees(Math.atan2(
                sourceLoc.y - targetLoc.y,
                sourceLoc.x - targetLoc.x));

        // Opposite angle (180° rotated)
        float oppositeAngle = angleToSource + 180f;

        // Convert to radians for sin/cos
        double radians = Math.toRadians(oppositeAngle);

        // Return the unit vector in that direction
        return new Vector2f((float) Math.cos(radians), (float) Math.sin(radians));
    }

}