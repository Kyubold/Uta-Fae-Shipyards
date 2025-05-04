package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

public class utafae_GravitySurgeStats extends BaseShipSystemScript {


	private static final float PUSH_SPEED = 700f;
	private static final float agility_bonus = 350f;

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {


		ShipAPI ship = (ShipAPI) stats.getEntity();
		if (ship == null || !ship.isAlive()) return;

		CombatEngineAPI engine = Global.getCombatEngine();

		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getAcceleration().modifyPercent(id, agility_bonus * effectLevel);
			stats.getDeceleration().modifyPercent(id, agility_bonus * effectLevel);
			stats.getTurnAcceleration().modifyPercent(id, agility_bonus * effectLevel);
			stats.getMaxTurnRate().modifyPercent(id, agility_bonus);
		}
		
		Vector2f mouse = ship.getMouseTarget();
		Vector2f pos = ship.getLocation();
		Vector2f direction = Vector2f.sub(mouse, pos, null);
		direction.normalise();

		Vector2f move = new Vector2f(direction);
		move.scale(PUSH_SPEED * engine.getElapsedInLastFrame());

		Vector2f push = Vector2f.add(ship.getVelocity(), move, null);
		ship.getVelocity().set(push);
		
	}

	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getDeceleration().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("improved maneuverability", false);
		} else if (index == 1) {
			return new StatusData("placeholder_text", false);
		}
		return null;
	}
}
