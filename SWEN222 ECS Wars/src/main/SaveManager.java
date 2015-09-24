package main;

import java.io.File;

import gameObjects.Room;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
			
			//Create children elements
			Element roomParent = doc.createElement("Rooms");
			root.appendChild(roomParent);
			
			//Write rooms to file
			for(Room r : controller.rooms){
				roomParent.setAttribute("id", r.getName());
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
	
	public static Controller loadGame(String name){
		return null;
	}
}
