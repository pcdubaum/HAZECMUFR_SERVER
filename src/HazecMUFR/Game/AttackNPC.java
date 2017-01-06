package HazecMUFR.Game;

import java.util.Arrays;
import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSVariableException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AttackNPC  extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		
		User user = getApi().getUserById(params.getInt("id"));
		int damage = params.getInt("da");
		ISFSObject response = new SFSObject();
		
		//List<UserVariable> vars = user.getVariables();
		
		UserVariable hp = user.getVariable("hp");
		UserVariable alive = user.getVariable("al");
		UserVariable level = user.getVariable("lvl");
		
		int result = hp.getIntValue() - damage;
		
		List<UserVariable> uVars;
		
		if(result <= 0)
		{
			hp = new SFSUserVariable("hp", 0);
			alive = new SFSUserVariable("al", false);
			uVars = Arrays.asList(hp, alive);
			
			// Set Vars
			 getApi().setUserVariables(user, uVars);
			
		     response.putInt("xp", 23);
		     
		     int thisXP = sender.getVariable("exp").getIntValue() + 16;
		     SFSUserVariable updatedXP = new SFSUserVariable("exp", thisXP);
		     
		     int thisLevel =sender.getVariable("lvl").getIntValue();
		     int nextLevel = thisLevel + 8;
		     nextLevel *= thisLevel * thisLevel * 10;
		     
		     SFSUserVariable updatedLevel = new SFSUserVariable("lvl", thisLevel);
		     SFSUserVariable restingPoints = new SFSUserVariable("poi", sender.getVariable("poi").getIntValue());
		     
		     if(thisXP >= nextLevel )
		     {
		    	 updatedLevel = new SFSUserVariable("lvl", thisLevel + 1);
		    	 restingPoints = new SFSUserVariable("poi", sender.getVariable("poi").getIntValue() + 5);
		     }
		     
		     try {
		    	// Send back to requester
			    send("doAtt", response, sender);
				sender.setVariables(Arrays.asList(updatedXP, updatedLevel, restingPoints));
				
			} catch (SFSVariableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        
		     
		}
		else
		{
			hp = new SFSUserVariable("hp", result);
			alive = new SFSUserVariable("al", true);
			uVars = Arrays.asList(hp, alive);
			
			// Set Vars
			 getApi().setUserVariables(user, uVars);
		}
		
		
	  
	}
}
