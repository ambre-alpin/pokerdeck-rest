/*
 * From https://technologyconversations.com/2014/03/26/application-development-back-end-solution-with-java/
 * https://github.com/vfarcic/TechnologyConversationsBooks/blob/master/src/main/java/com/technologyconversations/ 
 */

package myhomework.pokerdeck.server;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;

public class StandaloneHttpServer extends Server {

	
	@Override
    public boolean getFileCacheEnabled() {
        return true;
    }

    @Override
    public HttpHandler getHttpHandler() {
        return new CLStaticHttpHandler(StandaloneHttpServer.class.getClassLoader(), "webapp/");
    }
}
