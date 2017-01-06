package Assets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.smartfoxserver.v2.api.ISFSApi;
import com.smartfoxserver.v2.api.ISFSMMOApi;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.entities.variables.Variables;
import com.smartfoxserver.v2.mmo.IMMOItemVariable;
import com.smartfoxserver.v2.mmo.MMOItemVariable;
import com.smartfoxserver.v2.mmo.MMORoom;
import com.smartfoxserver.v2.mmo.Vec3D;

import Assets.Item.EquipmentType;
import HazecMUFR.Game.ServerOnlyConstants;

public class Monster{
	
	public enum MovimentToBePeformed 
	{
		Up (0),
		Down(1),
		Rigth (2),
		Left (3),
		UpperLeft (4),
		UpperRight (5),
		BottomLeft (6),
		BottomRight (7);
		
		private final int moviment;   // in kilograms
        
		MovimentToBePeformed(int moveType) {
	        this.moviment = moveType;
	    }
	}
	
	ISFSObject response = new SFSObject();
		
	private String name;
	private int monsterId;
	private boolean alive;
	private int timer;
	private int movimentTimer;
	private int attackTimer;
	private int maxHp;
	private int hp;
	private int hpRegen;
	private int maxMana;
	private int mana;
	private int manaRegen;
	private int level;
	private int rank;
	private int minAttack;
	private int maxAttack;
	private int defense;
	private int initialX;
	private int initialZ;
	private int finalX;
	private int finalZ;
	private int skill;
	private int levelRange;
	private int spawnTime;
	private Item assignedItem;
	private List<Item> avaiableIten = new ArrayList<Item>();
	private List<UserVariable> uVars;
	private Vec3D[] positions;
	private Vec3D lastPositions;
	private int positionIndex;
	private boolean positinControll = false;
		
	public Monster(int monsterId, boolean alive, int timer,int	movimentTimer,	int maxHp,	int hpRegen, int maxMana, int manaRegen,
			int level, int rank, int minAttack,int maxAttack,int defense, int initialX, int initialZ, int finalX, int finalZ, int skill, 
			int levelRange, int spawnTime, String name)
	{
		this.name = name;
		this.monsterId = monsterId;
		this.alive = alive;
		this.timer = timer;
		this.movimentTimer = movimentTimer;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.hpRegen = hpRegen;
		this.maxMana = maxMana;
		this.mana = maxMana;
		this.manaRegen = manaRegen;
		this.level = level;
		this.rank = rank;
		this.minAttack = minAttack;
		this.maxAttack = maxAttack;
		this.defense = defense;
		this.initialX = initialX;
		this.initialZ = initialZ;
		this.finalX = finalX;
		this.finalZ = finalZ;
		this.skill = skill;
		this.levelRange = levelRange;
		this.spawnTime = spawnTime;
		this.positions = new Vec3D[15];
		this.positionIndex = 0;
		this.positinControll = false;
		
		UpdateVariables();
	}
	
	public Monster(Monster otherMonster)
	{
		//avaiableIten
		
		this.name = otherMonster.name;
		this.monsterId = otherMonster.monsterId;
		this.alive = otherMonster.alive;
		this.timer = otherMonster.timer;
		this.movimentTimer = otherMonster.movimentTimer;
		this.maxHp = otherMonster.maxHp;
		this.hp = otherMonster.maxHp;
		this.hpRegen = otherMonster.hpRegen;
		this.maxMana = otherMonster.maxMana;
		this.mana = otherMonster.maxMana;
		this.manaRegen = otherMonster.manaRegen;
		this.level = otherMonster.level;
		this.rank = otherMonster.rank;
		this.minAttack = otherMonster.minAttack;
		this.maxAttack = otherMonster.maxAttack;
		this.defense = otherMonster.defense;
		this.initialX = otherMonster.initialX;
		this.initialZ = otherMonster.initialZ;
		this.finalX = otherMonster.finalX;
		this.finalZ = otherMonster.finalZ;
		this.skill = otherMonster.skill;
		this.levelRange = otherMonster.levelRange;
		this.spawnTime = otherMonster.spawnTime;
		this.avaiableIten = otherMonster.avaiableIten;
		this.positions = new Vec3D[15];
		this.positionIndex = 0;
		this.positinControll = false;
		
		UpdateVariables();
	}
	
	///
	public void UpdateVariables(){
		
		if(hp <= 0)
			alive = false;
		
		uVars = Arrays.asList((UserVariable) 
				new SFSUserVariable("npc", true),
				new SFSUserVariable("et", this.monsterId), 
				new SFSUserVariable("al", this.alive),
				new SFSUserVariable("hp", this.hp));
	}
	
	///
	public void AddItemToAvaiablesItems(Item item)
	{
		Item itemToAdd = null;
		
		// We don't need Jewel or Consumable item for now
		if (item.getType() != EquipmentType.Consumable
				&& item.getType() != EquipmentType.Jewels) {
			
			
			// If we have a exactly item level
			// Our item level will be equal to 0
			if (item.getItemLevel() <= level) {
				
				itemToAdd = new Item((item));
				
				int aux = item.getItemLevel() - levelRange;
				
				if(item.getItemLevel() > level - levelRange)
				{
					itemToAdd.upgradeLevel = aux;
					
					if(aux >= 4){
						itemToAdd.upgradeLevel = 4;
					}
				}
				
				// Add Item
				avaiableIten.add(itemToAdd);
			} 
			// Our item level will be greater then 0
			// Masimun item upgradelevel = 4
			else if (item.getItemLevel() > level ) {
				
				itemToAdd = new Item(item);
				
				if(level - item.getItemLevel() <= 3 && level - item.getItemLevel() > 0)
				{
					itemToAdd.upgradeLevel = level - item.getItemLevel();
					
				}else if(level - item.getItemLevel() > 4){
					itemToAdd.upgradeLevel = 4;
				}
			} 
		} 
	}
	
	public void GetNewItem(int dbid) {
		Random rnd = new Random();
		Item newItem = null;

		int rndItemChoice = 0;
		rndItemChoice = rnd.nextInt(1000);

		// New Item (Weapon, Shield or Set Item)
		if (rndItemChoice >= 700) {
			newItem = new Item((avaiableIten.get(rnd.nextInt(avaiableIten.size()))));
			newItem.setItemDbId(dbid);
			SetItemOptions(newItem);
			SetItemLuck(newItem);
		}
		else{
			//newItem = new Item(avaiableIten.get[0]);
		}

		assignedItem = newItem;
	}
	

	private void SetItemOptions(Item newItem) {
		Random rnd = new Random();
		
		int rndItemChoice = 0;
		rndItemChoice = rnd.nextInt(1000);	
		
		if (rndItemChoice <= 25) {
			newItem.setOption1(4);
		}else if(rndItemChoice <= 100){
			newItem.setOption1(3);
		}else if(rndItemChoice <= 200){
			newItem.setOption1(2);
		}else if(rndItemChoice <= 400){
			newItem.setOption1(1);
		}else{
			newItem.setOption1(0);
		}
	}
	
	private void SetItemLuck(Item newItem) {
		Random rnd = new Random();
		
		int rndItemChoice = 0;
		rndItemChoice = rnd.nextInt(1000);	
		
		if (rndItemChoice <= 25) {
			newItem.setLuck(true);
		}else{
			newItem.setLuck(false);
		}
	}
	
	private void SetItemRarit(Item newItem) {
		Random rnd = new Random();
		
		int rndItemChoice = 0;
		rndItemChoice = rnd.nextInt(1000);	
		
		if (rndItemChoice <= 5) {
			newItem.setLuck(true);
		}else{
			newItem.setLuck(false);
		}
	}
	
	public boolean CheckDie(int hp, ISFSMMOApi mmoapi, ISFSApi api, User npc, float posx, float posz, Room room)
	{
		if (hp <= 0) {
			if (IsAlive() && timer != 0) {
				timer = 0;
				if (assignedItem != null) {
					
					// Prepare a list of variables for the MMOItem
					List<IMMOItemVariable> variables = new	LinkedList<IMMOItemVariable>();
					
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemId, assignedItem.getId()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemNumber, assignedItem.getItemNumber()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemType, assignedItem.getTypeasInt()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemLevel, assignedItem.getItemLevel()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemOption1, assignedItem.getOption1()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemOption2, assignedItem.getOption2()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemOption3, assignedItem.getOption3()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemOption4, assignedItem.getOption4()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemrank, assignedItem.getrank()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemLuck, assignedItem.isLuck()));
					variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemQuantity, assignedItem.getItemQuantity()));
										
					assignedItem.setVariables(variables);
					
					DropAssignedItem(mmoapi, posx, posz, room);
					
					assignedItem = null;
				}
				
				List<UserVariable> vars = Arrays.asList(
						(UserVariable) 
						new SFSUserVariable("al", false),
						new SFSUserVariable("hp", 0));
				
				api.setUserVariables(npc, vars);
			}
			
			alive = false;
		}
		
		return alive;
	}
	
	public void DropAssignedItem(ISFSMMOApi api, float posx, float posz, Room room)
	{
		api.setMMOItemPosition(assignedItem, new Vec3D(posx, 1.0f, posz), room);
	}
	
	public boolean IsAlive()
	{
		if(hp > 0)
			return true;
		else
			return false;
	}
	
	public List<UserVariable> GetVariables()
	{
		return uVars;
	}
	
	public List<Item> GetAllAvaiableItems()
	{
		return avaiableIten;
	}
	
	public int GetInitialX()
	{
		return initialX;
	}
	
	public int GetInitialZ()
	{
		return initialZ;
	}
	
	public int GetFinalX()
	{
		return finalX;
	}
	
	public int GetFinalZ()
	{
		return finalZ;
	}
	
	public int GetMaxHP()
	{
		return maxHp;
	}
	
	public void Tick(ISFSApi api, ISFSMMOApi mmoApi, User npc, List<User> users, MMORoom room) {
		
		Random rnd = new Random();
		
		if (!alive) {
			
			timer = timer + 1;
			
		} else {
			
			if(rnd.nextInt(2) == 0)
				
				movimentTimer = movimentTimer + 1;
		}
		
		if (timer == spawnTime * 100) {
			
			// Set NPC variables
			List<UserVariable> vars = Arrays.asList((UserVariable) new SFSUserVariable("al", true),
					new SFSUserVariable("hp", GetMaxHP()));

			//assignedItem = GetNewItem(0);
			hp = maxHp;
			api.setUserVariables(npc, vars);

			timer = 0;
			alive = true;

		}
		
		
		
		if (IsAlive()) {

			float posX = npc.getVariable("x").getDoubleValue().floatValue();
			float posY = 1.0f;
			float posZ = npc.getVariable("z").getDoubleValue().floatValue();
			Vec3D pos = new Vec3D(posX, posY, posZ);
			
			List<User> recipients = users;

			for (User user : recipients) {
				if (!user.isNpc()) {
					
					float uPosX = user.getVariable("x").getDoubleValue().floatValue();
					float uPosZ = user.getVariable("z").getDoubleValue().floatValue();

					float distance = (float) Math.sqrt(Math.pow((double) uPosX - (double) posX, 2)
							+ Math.pow((double) uPosZ - (double) posZ, 2));

					
					if (distance <= 2.0f && attackTimer >= 10 && alive) {
						
						if(user.getVariable("hp").getIntValue() > 0){
						int dmg = rnd.nextInt(maxAttack - minAttack) + minAttack;
						response.putInt("dmg", dmg);
						response.putInt("id", npc.getId());
						response.putInt("id2", user.getId());
						
						
						api.sendExtensionResponse("att", response, user, room, false); 
						
						attackTimer = 0;
						movimentTimer = 0;

						break;
						}
					} else {
						attackTimer++;
					}
				}
			}

			if (movimentTimer >= 20) {

				int willWalkBack = rnd.nextInt(3);
				MovimentToBePeformed walkPos = MovimentToBePeformed.values()[rnd.nextInt(8)];

				//float distance = (float) Math.sqrt(Math.pow((double) initialX - (double) posX, 2)
						//+ Math.pow((double) initialZ - (double) posZ, 2));

				if (positinControll) {
				
					if(positionIndex == 0)
					{
						positinControll = true;
					}
					else
					{
						positionIndex--;
					}
					
					mmoApi.setUserPosition(npc, positions[positionIndex], room);
					
				} else if (willWalkBack == 0) {
					
					switch (walkPos) {
					
					case Up:
						if(posX + 1.0f < 256)
							pos = new Vec3D(posX + 1.0f, 1.0f, posZ);
						break;

					case Down:
						if(posX - 1.0f > 0)
							pos = new Vec3D(posX - 1.0f, 1.0f, posZ);
						break;

					case Left:
						if(posZ + 1.0f < 256)
							pos = new Vec3D(posX, 1.0f, posZ + 1.0f);
						break;

					case Rigth:
						if(posZ - 1.0f > 0)
							pos = new Vec3D(posX, 1.0f, posZ - 1.0f);
						break;

					case UpperLeft:
						if(posX + 1.0f < 256 && posZ + 1.0f < 256)
							pos = new Vec3D(posX + 1.0f, 1.0f, posZ + 1.0f);
						break;

					case BottomRight:
						if(posX - 1.0f > 0 && posZ - 1.0f > 0)
							pos = new Vec3D(posX - 1.0f, 1.0f, posZ - 1.0f);
						break;

					case UpperRight:
						if(posX + 1.0f < 256 && posZ - 1.0f > 0)
						pos = new Vec3D(posX + 1.0f, 1.0f, posZ - 1.0f);
						break;

					case BottomLeft:
						if(posX - 1.0f > 0 && posZ + 1.0f < 256)
						pos = new Vec3D(posX - 1.0f, 1.0f, posZ + 1.0f);
						break;

					}
					
					positions[positionIndex] = new Vec3D(0, 0);
					positions[positionIndex] = pos;
					positionIndex++;
					
					mmoApi.setUserPosition(npc, pos, room);
					
					if(positionIndex == 15)
					{
						positionIndex = 14;
						positinControll = false;	
					}
					
				}

				// trace("1");

				
				// trace(npc.getVariable("x").getDoubleValue().floatValue());
				

				// Set NPC variables
				List<UserVariable> vars2 = Arrays.asList((UserVariable) new SFSUserVariable("x", pos.floatX()),
						new SFSUserVariable("z", pos.floatZ()), new SFSUserVariable("fx", pos.floatX()),
						new SFSUserVariable("fz", pos.floatZ()));

				api.setUserVariables(npc, vars2);
				
				movimentTimer = 0;
			//}
		}
	}
}
}
