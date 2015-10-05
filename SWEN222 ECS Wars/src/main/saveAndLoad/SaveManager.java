package main.saveAndLoad;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gameWorld.Controller;
import gameWorld.Room;
import gameWorld.characters.*;
import gameWorld.characters.nonplayer.NonPlayer;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
			Document doc = builder.newDocument();
			
			//Create root element
			Element root = doc.createElement("Controller");
			doc.appendChild(root);
			
			saveRooms(controller, doc, root);
			
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

	private static void saveRooms(Controller controller, Document doc, Element root) {
		for(Room r : controller.getRooms()){
			Element room = doc.createElement("Room");
			root.appendChild(room);
			room.setAttribute("id", r.getName());
			
			//Write players in room
			savePlayers(room, r, doc);
		}
	}

	private static void savePlayers(Element room, Room r, Document doc) {
		for (Player p : r.getPlayers()){
			Element player = doc.createElement("Player");
			room.appendChild(player);
			
			player.setAttribute("type", p.getType().toString());
			player.setAttribute("x", Integer.toString(p.getX()));
			player.setAttribute("y", Integer.toString(p.getY()));
			
			//Write items player currently holds
			writeInventory(p, player, doc);
		}
	}
	
	private static void writeInventory(Player p, Element player, Document doc){
		//initialise inventory, players only have one
		Element inventory = doc.createElement("Inventory");
		player.appendChild(inventory);
		
		//List the inventory items in order
		//i.e. the first item in inventory should be attibute 0 = [item description]
		for(int i = 0; i < p.getInventory().length; i++){
			if(p.getInventory()[i] != null){
				inventory.setAttribute(Integer.toString(i),	 
						p.getInventory()[i].toString());
			}
		}
	}
}
