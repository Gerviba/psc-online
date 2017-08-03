package hu.gerviba.psconline;

import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/console", configurator = ConsoleServerEndpointConfig.class)
public class ConsoleServerEndpoint {
    
	static {
		Configuration.init();
	}
	
    private static ConcurrentHashMap<String, TerminalInstance> terminals = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
    	terminals.put(session.getId(), new TerminalInstance(session));
        
    }

    @OnClose
    public void onClose(Session session) {
    	terminals.remove(session.getId()).free();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(message))) {
            JsonObject input = jsonReader.readObject();
            TerminalInstance user = terminals.get(session.getId());
            
            switch (input.getString("action")) {
                case "start": {
                	user.sendStatus("Starting application...");
                	user.startApplication(input.getString("code"));
                    break;
                }
                case "write": {
                	user.sendToApp(input.getString("line"));
                	break;
                }
                case "compile": {
                    
                    break;
                }
                case "save": {
                    
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
    
}
