package data.hullmods;


import java.util.List;
import java.util.Objects;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;


import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.WeaponAPI;

public class utafae_GravitonFieldGenerator extends BaseHullMod {

	public static float MANEUVER_BONUS = 25f;
	public static float SPEED_BONUS = 10f;


	float getRange(HullSize hullSize){

		switch (hullSize) {
			case FRIGATE:
				return 300;

			case DESTROYER:
				return 600;

			case CRUISER:
				return 900;

			case CAPITAL_SHIP:
				return 1200;
		
			default:
				return 0;

		}

	}



	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + getRange(HullSize.FRIGATE);
		if (index == 1) return "" + getRange(HullSize.DESTROYER);
		if (index == 2) return "" + getRange(HullSize.CRUISER);
		if (index == 3) return "" + getRange(HullSize.CAPITAL_SHIP);
		if (index == 4) return "" + (int)Math.round(MANEUVER_BONUS) + "%";
		if (index == 5) return "" + (int)Math.round(SPEED_BONUS) + "%";

		return null;
	}
	


	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		
		
		ship.addListener(new GravitonFieldModifier(getRange(ship.getHullSize()), MANEUVER_BONUS, SPEED_BONUS, ship));
	}




	public static class GravitonFieldModifier implements AdvanceableListener{



		
		CombatEngineAPI com = Global.getCombatEngine();

		


		boolean checkMode(){
			if(Global.getSector().getCampaignUI().getCurrentCoreTab() != CoreUITabId.REFIT){
				return false;
			}

			if (Global.getCurrentState() != GameState.COMBAT){
				return false;
			}
			if (!com.isInCampaign() && !com.isMission()) {
				return false; // Not in real combat
			}

			return true;
		}
	
		float range, maneuver, speed;
		ShipAPI ship;
	
		public GravitonFieldModifier(float range, float maneuver, float speed, ShipAPI ship){
			this.range = range;
			this.maneuver = maneuver;
			this.speed = speed;
			this.ship =  ship;
		}
	

		

	
		List<ShipAPI> ships;
		List<DamagingProjectileAPI> projectiles;
	
		List<ShipAPI> modified;
	
		public void advance(float amount){

			if(checkMode()){
				
			
						
	
			ships = com.getShips();
			projectiles = com.getProjectiles();
	
	
	
			for (ShipAPI ship : ships) {
				
				if(ship.isAlive() && !Objects.equals(ship.getId(), ship.getId())){	//excludes dead or player-controlled ships
					float distance = Vector2f.sub(ship.getCopyLocation(), ship.getCopyLocation(), null).length();

					
					if(distance <= range && !checkIfModified(ship.getId())){	//finds unmodified ships within range
						applyEffects(ship, false);
						modified.add(ship);
					}
					else if(distance >= range && checkIfModified(ship.getId())){	//finds modified ships outside of the range
						applyEffects(ship, true);
						modified.remove(ship);
					}
					
				}
				else{
					if(checkIfModified(ship.getId())){  //removes dead ships from modified list
						modified.remove((ship));
					}
				}
			}
	

			for (DamagingProjectileAPI pro : projectiles) {	
				if(com.isEntityInPlay(pro)){
					float distance = Vector2f.sub(pro.getLocation(), ship.getLocation(), null).length();

					if(distance <= range){
						adjustProjectile(pro);
					}
					

				}
				
			}
			}
	
		}
	
		boolean checkIfModified(String modId){
			
			if(modified.isEmpty()){
				return false;
			}

			if(true){
				for (ShipAPI ship : modified) {
					if(Objects.equals(ship.getId(), modId)){
						return true;
					}
				}
			}
			
	
			return false;
		}
	

		
		

		void applyEffects(ShipAPI ship, boolean unapply){
	
			int flipper;
	
			boolean ally = ship.isAlly();
	
			if(ally){
				flipper = 1;
			}else{
				flipper = -1;
			}
	
	
			MutableShipStatsAPI stats = ship.getMutableStats();
			String id = "Graviton_Field_Modifier" + ship.getId();
	
			if(unapply){
				stats.getAcceleration().unmodify(id);
				stats.getDeceleration().unmodify(id);
				stats.getTurnAcceleration().unmodify(id);
				stats.getMaxTurnRate().unmodify(id);
	
				stats.getMaxSpeed().unmodify(id); 
			}
			else{
				stats.getAcceleration().modifyPercent(id, maneuver * flipper);
				stats.getDeceleration().modifyPercent(id, maneuver * flipper);
				stats.getTurnAcceleration().modifyPercent(id, maneuver * 2f * flipper);
				stats.getMaxTurnRate().modifyPercent(id, maneuver * flipper);
	
				stats.getMaxSpeed().modifyPercent(id, speed * flipper); 
			}
	
			
	
		}
	
	}
	
	


	static public void adjustProjectile(DamagingProjectileAPI proj) {
		if (proj == null || proj.getVelocity() == null || proj.getVelocity().length() == 0f) return;
	
		boolean ally;
		ally = proj.getSource().isAlly();


		Vector2f velocity = proj.getVelocity();
		float currentSpeed = velocity.length();
		Vector2f direction = new Vector2f(velocity);
		direction.normalise();

		float max = proj.getWeapon().getProjectileSpeed();

		if(ally && currentSpeed < max * SPEED_BONUS){

			// Multiply current speed
		
	
		float newSpeed = currentSpeed * SPEED_BONUS;
		Vector2f newVelocity = new Vector2f(direction.x * newSpeed, direction.y * newSpeed);
		velocity.set(newVelocity);
	
		// If it's a missile, increase turn rate
		if (proj instanceof MissileAPI) {
			MissileAPI missile = (MissileAPI) proj;
	
			missile.setAngularVelocity(missile.getAngularVelocity() * MANEUVER_BONUS);

		}
		}
		else if(!ally && currentSpeed > max * (1f - (SPEED_BONUS/100))){

		float newSpeed = currentSpeed * (1f - (SPEED_BONUS/100));
		Vector2f newVelocity = new Vector2f(direction.x * newSpeed, direction.y * newSpeed);
		velocity.set(newVelocity);
	
		// If it's a missile, decrease turn rate
		if (proj instanceof MissileAPI) {
			MissileAPI missile = (MissileAPI) proj;
	
			missile.setAngularVelocity(missile.getAngularVelocity() * (1f - (MANEUVER_BONUS/100)));

		}
		}

		
	}
}












