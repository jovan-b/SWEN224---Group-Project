package main.saveAndLoad;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gameObjects.Room;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import main.Controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import characters.*;

/**
 * A save manager to store or load the state of a game controller from file
 * @author Carl
 *
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
			
			//Write rooms to file
			for(Room r : controller.getRooms()){
				Element room = doc.createElement("Room");
				root.appendChild(room);
				room.setAttribute("id", r.getName());
				
				//Write players in room
				for (Player p : r.getPlayers()){
					Element player = doc.createElement("Player");
					room.appendChild(player);
					
					player.setAttribute("type", p.getType().toString());
					player.setAttribute("x", Integer.toString(p.getX()));
					player.setAttribute("y", Integer.toString(p.getY()));
				}
			}
			
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
	
	public static void loadGame(Controller controller, String name){
		//TODO: refactor this method into something readable
		try {
			File file = new File(SAVE_DIR+name);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			
			doc.getDocumentElement().normalize();
			
			//Read rooms
			NodeList list = doc.getElementsByTagName("Room");
			
			List<Room> rooms = new ArrayList<>();
			List<Player> players = new ArrayList<>();
			
			for (int i = 0; i < list.getLength(); i++){
				Node node = (Node) list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE){
					Element e = (Element) node;
					
					//read players
					NodeList playerlist = doc.getElementsByTagName("Player");
					
					//create a room object from the node
					Room r = new Room(e.getAttribute("id"), controller);
					
					for (int j = 0; j < list.getLength(); j++){
						Node pnode = (Node) list.item(j);
						if(node.getNodeType() == Node.ELEMENT_NODE){
							Element pelement = (Element) pnode;	//player element
							Player p;
							
							//create a player object from the node
							switch(Integer.parseInt(pelement.getAttribute("Type"))){
							case(1):	//PondyPlayer
								p = new PondyPlayer(r, Integer.parseInt(pelement.getAttribute("x")), 
										Integer.parseInt(pelement.getAttribute("y")));
								break;
							default:	//DavePlayer
								p = new DavePlayer(r, Integer.parseInt(pelement.getAttribute("x")), 
										Integer.parseInt(pelement.getAttribute("y")));
								break;
							}
							
							//add player object to room object
							r.addPlayer(p);
							
							//add player object to list of players
							players.add(p);
						}
					}
					//add room to list of rooms
					rooms.add(r);
				}
			}
			
			//TODO: add list of rooms to controller
			//TODO: add list of players to the controller

		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
