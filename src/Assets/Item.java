package Assets;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.smartfoxserver.v2.api.ISFSMMOApi;
import com.smartfoxserver.v2.mmo.IMMOItemVariable;
import com.smartfoxserver.v2.mmo.MMOItem;
import com.smartfoxserver.v2.mmo.MMOItemVariable;

import HazecMUFR.Game.ServerOnlyConstants;

public class Item extends MMOItem{
	

	    
	public enum EquipmentType 
	{
		None (0),
		Weapon_MainHand(1),
		Weapon_OffHand (2),
		Head (3),
		Necklace (4),
		Shoulders (5),
		Chest (6),
		Bracers (7),
		Gloves (8),
		Belt (9),
		Pants (10),
		Boots (11),
		Trinket (12),
        Wings (13),
        Jewels (14),
        Consumable (15),
        Bowns (16),
        Staffs (17),
		Spear(18);
        
        private final int type;   // in kilograms
        
	    EquipmentType(int itemType) {
	        this.type = itemType;
	    }
	}
	
	private int dbid;
	private EquipmentType type;
	private int itemNumber;
	private int rank;
	private int itemLevel;
	private int MinDamage;
	private int MaxDamage;
    private float AttackSpeed;
    private int Block;
	private int Armor;
	private boolean luck;
	private int option1;
	private int option2;
	private int option3;
	private int option4;
	private int position;
	private int quantity;
	private int slotNumber;
	public int upgradeLevel;
	
	public Item(EquipmentType type, int number, int id, int rank, boolean luck, int options, int pos)
	{
		this.dbid = id;
		this.type = type;
		this.itemNumber = number;
		this.itemLevel = 3;
		this.position = pos;
		
		// Prepare a list of variables for the MMOItem
	    List<IMMOItemVariable> variables = new LinkedList<IMMOItemVariable>();
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemId, this.dbid) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemNumber, this.itemNumber) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemType, this.type.type) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemLevel, this.getItemLevel()) );
	}
	
	public Item(int type, int number, int id, int rank, Integer integer, int option1, int option2, int option3, int option4, int pos, int quantidade, int slot, int uplevel)
	{
		this.dbid = id;
		this.type = EquipmentType.values()[type];
		this.itemNumber = number;
		this.rank = rank;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
		this.option4 = option4;
		this.quantity = quantidade;
		this.itemLevel = uplevel;
		this.position = pos;
		this.slotNumber = slot;
		this.upgradeLevel = uplevel;
		
		// Prepare a list of variables for the MMOItem
	    List<IMMOItemVariable> variables = new LinkedList<IMMOItemVariable>();
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemId, this.dbid) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemNumber, this.itemNumber) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemType, this.type.type) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemLevel, this.getItemLevel()) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemQuantity, this.getItemLevel()) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemSlot, this.getItemLevel()) );
	}
	
	public Item(EquipmentType type, int number, int id, int options, int level)
	{
		this.dbid = id;
		this.type = type;
		this.itemNumber = number;
		this.itemLevel = level;
		
		// Prepare a list of variables for the MMOItem
	    List<IMMOItemVariable> variables = new LinkedList<IMMOItemVariable>();
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemId, this.dbid) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemNumber, this.itemNumber) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemType, this.type.type) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemLevel, this.getItemLevel()) );
	}
	
	public Item(Item item)
	{
		this.type = item.type;
		this.itemNumber = item.itemNumber;
		this.dbid = item.dbid;
		this.setrank(item.getrank());
		this.setItemLevel(item.getItemLevel());
		this.MinDamage = item.MinDamage;
		this.MaxDamage = item.MaxDamage;
		this.AttackSpeed = item.AttackSpeed;
		this.Block = item.Block;
		this.Armor = item.Armor;
		this.luck = item.luck;
		this.option1 = item.option1;
		this.option2 = item.option2;
		this.option3 = item.option3;
		this.option4 = item.option4;
		
		// Prepare a list of variables for the MMOItem
	    List<IMMOItemVariable> variables = new LinkedList<IMMOItemVariable>();
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemId, this.dbid) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemNumber, this.itemNumber) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemType, this.type.type) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemLevel, this.getItemLevel()) );
	}
	
	public void setVariables(ISFSMMOApi mmoApi)
	{
		List<IMMOItemVariable> variables = new LinkedList<IMMOItemVariable>();
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemId, this.dbid) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemNumber, this.itemNumber) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemType, this.type.type) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemLevel, this.getItemLevel()) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemrank, this.getrank()) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemLuck, makeLuck() ));
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemOption1, makeOptions() ));
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemOption2, this.option2) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemOption3, this.option3) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemOption4, this.option4) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemQuantity, this.quantity) );
	    variables.add( new MMOItemVariable(ServerOnlyConstants.serverItemSlot, this.slotNumber) );
	    
	    mmoApi.setMMOItemVariables(this, variables);
	}
	
	public int makeGoldCoins(int coins)
	{
		int _return = coins;
		
		Random rnd = new Random();
		_return += rnd.nextInt(21) / 100;
		
		return _return;
	}
	
	public int makeOptions()
	{
		Random rnd = new Random();
		int percen = rnd.nextInt(99);
		int opt = 0;
		
		if(percen == 99)
			opt = 3;
		else if(percen >= 90)
		{
			opt = 2;
		}else if(percen >= 75)
		{
			opt = 1;
		}
		
		return opt;
	}
	
	public int makeLuck()
	{
		Random rnd = new Random();
		int percen = rnd.nextInt(99);
		int opt = 0;
		
		if(percen >= 80)
			opt = 1;
		
		return opt;
	}

	public EquipmentType getType() {
		return type;
	}
	
	public int getTypeasInt() {
		return type.type;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public int getItemDbId() {
		return dbid;
	}
	
	public void setItemDbId(int dbidaux) {
		dbid = dbidaux;
	}

	private void setrank(int rank) {
		this.rank = rank;
	}

	public int getrank() {
		return rank;
	}

	public boolean isLuck() {
		return luck;
	}

	public void setLuck(boolean luck) {
		this.luck = luck;
	}

	public int getOption1() {
		return option1;
	}

	public void setOption1(int option) {
		this.option1 = option;
	}
	
	public int getOption2() {
		return option2;
	}

	public void setOption2(int option) {
		this.option2 = option;
	}
	
	public int getOption3() {
		return option3;
	}

	public void setOption3(int option) {
		this.option3 = option;
	}
	
	public int getOption4() {
		return option4;
	}

	public void setOption4(int option) {
		this.option4 = option;
	}

	public int getItemLevel() {
		return itemLevel;
	}
	
	
	public int getItemQuantity() {
		return quantity;
	}
	
	public void setItemQuantity(int quantit) {
		quantity = quantit;
	}
	
	public int getItemSlot() {
		return slotNumber;
	}
	
	public void setItemSlot(int slot) {
		slotNumber = slot;
	}

	public void setItemLevel(int itemLeve) {
		itemLevel = itemLeve;
	}

	public int getPos() {
		// TODO Auto-generated method stub
		return position;
	}


}
