package main.saveAndLoad;

import java.io.File;
import java.util.List;

import gameWorld.Controller;
import gameWorld.Room;
import gameWorld.characters.*;
import gameWorld.gameObjects.Floor;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.Item.Type;
import gameWorld.gameObjects.containers.Cabinet;
import gameWorld.gameObjects.containers.Container;
import gameWorld.gameObjects.weapons.Weapon;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A save manager to store or load the state of a game controller from file
 * Saves the game state to an XML file
 * XML file contains a bunch of XML elements
 * If object is "contained" inside another parent object,
 * element is also contained inside the parent element, i.e.
 * 	a Room object has a Player object in it
 * 	a Room XML element has a Player XML element in it
 * 
 * @author Carl
 * Edited by Jah Seng Lee
 */
public final class SaveManager {
	public static String SAVE_DIR = "";
	private static Document doc;
	
	private SaveManager(){
		//prevent instantiation
	}
	
	/**
	 * Save the game to file
	 * @param controller The controller to save
	 * @param name File name
	 */
	public static void saveGame(Controller controller, String name){
		try {
			//Create the xml document
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.newDocument();
			
			//Create root element
			Element root = doc.createElement("Controller");
			doc.appendChild(root);
			
			saveRooms(controller, root);
			
			//Save the file to disk
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource src = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(SAVE_DIR+name));
			
			transformer.transform(src, result);
			
		} catch (ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void saveRooms(Controller controller, Element root) {
		for(Room r : controller.getRooms()){
			Element room = doc.createElement("Room");
			root.appendChild(room);
			room.setAttribute("id", r.getName());
			
			//Write players in room
			savePlayers(room, r);
			
			//TODO: save NPC players positions and types
			
			//save room contents
			saveCabinets(room, r);
			saveRoomContents(room, r);
		}
	}

	/**
	 * Save all items stored inside cabinets
	 * The saveRoomContents() method is unable to safely parse cabinets
	 * Thus, this method is needed to properly store cabinets states
	 * 
	 * @param room
	 * @param r
	 */
	private static void saveCabinets(Element room, Room r) {
		Item[][] contents = r.getContents();
		
		for(int i = 0; i < contents.length; i++){
			for(int j = 0; j < contents[0].length; j++){
				if(!(contents[i][j] instanceof Cabinet)) continue;
				
				Cabinet c = (Cabinet)contents[i][j];
				
				Element e = doc.createElement("Cabinet");
				saveContainerItems(e, c);
				e.setAttribute("x", Integer.toString(i));
				e.setAttribute("y", Integer.toString(j));
				room.appendChild(e);
			}
		}
	}

	/**
	 * Save dynamic items in the room
	 * All pick up items saved as a "Item" tag inside the room tag
	 * All container items saved as a "Container" tag inside the room tag
	 * 		"Container" tags may have an "Item" tag as a child
	 * 
	 * @param room
	 * @param r
	 */
	private static void saveRoomContents(Element room, Room r) {
		Item[][] contents = r.getContents();
		
		for(int i = 0; i < contents.length; i++){
			for(int j = 0; j < contents[0].length; j++){
				if(!(contents[i][j] instanceof Floor)) continue;
				
				Item item = ((Floor)contents[i][j]).getItem();
				if(item == null){ //no item in this position 
					continue;	
				}
				else{
					//write the item to the XML file
					Element e;
					
					if(item instanceof Container){
						//Save container
						e = doc.createElement("Container");
						e.setAttribute("type", item.getType().name());
						e.setAttribute("x", Integer.toString(i));
						e.setAttribute("y", Integer.toString(j));
						room.appendChild(e);
						
						//store inner child item(s)
						saveContainerItems(e, item);
					}
					else{//not a container, store the item
						e = doc.createElement("Item");
						e.setAttribute("type", item.getType().name());
						//if type is weapon, save the type of weapon
						if(item.getType() == Type.Weapon){
							e.setAttribute("weaponType", 
									((Weapon)item).getWeaponType().name());
						}
						e.setAttribute("x", Integer.toString(i));
						e.setAttribute("y", Integer.toString(j));
						room.appendChild(e);
					}
				}
			}
		}
	}

	/**
	 * Save all the items inside a container
	 * If no items are present, no XML element is created
	 * 
	 * @param e
	 * @param item
	 */
	private static void saveContainerItems(Element e, Item item) {
		List<Item> items = ((Container)item).getContents();
		
		Element innerItem;
		for(Item i: items){
			innerItem = doc.createElement("Item");
			innerItem.setAttribute("type", i.getType().name());
			e.appendChild(innerItem);
		}
	}

	/**
	 * Write Player objects into XML file
	 * Player tags should contain:
	 * 	-inventory tag
	 * Player should have attributes:
	 * 	-x, y position
	 * 	-type(DavePlayer, PondyPlayer, etc)
	 * 	-weapon
	 * 	-health
	 * 
	 * @param room
	 * @param r
	 */
	private static void savePlayers(Element room, Room r) {
		for (Player p : r.getPlayers()){
			Element player = doc.createElement("Player");
			room.appendChild(player);
			
			player.setAttribute("type", p.getType().name());
			player.setAttribute("x", Integer.toString(p.getX()));
			player.setAttribute("y", Integer.toString(p.getY()));
			
			//TODO: Save player Weapon
			saveWeapon(p, player);
			
			//Write items player currently holds
			saveInventory(p, player);
		}
	}
	
	private static void saveWeapon(Player p, Element player){
		//initialise weapon
		Element weapon = doc.createElement("Weapon");
		player.appendChild(weapon);
		
		weapon.setAttribute("weaponType", p.getWeapon().getWeaponType().name());
		
	}
	
	private static void saveInventory(Player p, Element player){
		
		//put all the items inside player element
		Item[] items = p.getInventory();
		
		for(int i = 0; i < items.length; i++){
			if(items[i] != null){
				Element inventoryItem = doc.createElement("Item");
				inventoryItem.setAttribute("type", items[i].getType().name());
				inventoryItem.setAttribute("index", Integer.toString(i));
				player.appendChild(inventoryItem);
			}
		}
	}
}
