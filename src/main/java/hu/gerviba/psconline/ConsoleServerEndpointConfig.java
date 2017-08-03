package hu.gerviba.psconline;
 
import javax.websocket.server.ServerEndpointConfig.Configurator;
import hu.gerviba.psconline.ConsoleServerEndpoint;

public class ConsoleServerEndpointConfig extends Configurator {
 
    private static ConsoleServerEndpoint server = new ConsoleServerEndpoint();
 
    @SuppressWarnings("unchecked")
	@Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) server;
    }
}