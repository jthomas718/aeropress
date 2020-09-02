import aeropress.ConnectionHandler;
import aeropress.HttpMethod;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

public class ConnectionHandlerTest {

    @Test
    public void methodsToStringTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Set<HttpMethod> methods = new LinkedHashSet<>();
        methods.add(HttpMethod.GET);
        methods.add(HttpMethod.POST);
        methods.add(HttpMethod.DELETE);

        Method method = ConnectionHandler.class.getDeclaredMethod("methodsToString", Set.class);
        method.setAccessible(true);
        String s = (String) method.invoke(null, methods);
        assertEquals("GET, POST, DELETE", s);
    }
}
