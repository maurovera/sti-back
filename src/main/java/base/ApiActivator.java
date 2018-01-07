package base;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Path base de los recursos, todos los demás recursos se econtraran bajo el
 * path '/api'
 *
 * @author mbaez@konecta.com.py
 */
@ApplicationPath("/api")
public class ApiActivator extends Application {
}
