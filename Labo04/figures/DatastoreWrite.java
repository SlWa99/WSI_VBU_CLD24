package ch.heigvd.cld.lab;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@WebServlet(name = "DatastoreWrite", value = "/datastorewrite")
public class DatastoreWrite extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter pw = resp.getWriter();
        pw.println("Writing entity to datastore.");

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Retrieving the entity kind and key from the request parameters
        String kind = request.getParameter("_kind");
        String keyName = request.getParameter("_key");

        if (kind == null) {
            pw.println("Error: No kind specified for the entity.");
            return; // Early return if no kind is specified
        }

        // Determine the key for the new entity
        Entity entity;
        if (keyName != null) {
            Key key = KeyFactory.createKey(kind, keyName);
            entity = new Entity(key);
        } else {
            entity = new Entity(kind);  // Let Datastore generate the key
        }

        // Adding properties to the entity from all other parameters
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (!paramName.equals("_kind") && !paramName.equals("_key")) {
                String paramValue = request.getParameter(paramName);
                entity.setProperty(paramName, paramValue);
            }
        }

        // Save the entity to the datastore
        datastore.put(entity);
        pw.println("Entity of kind '" + kind + "' saved with properties. Key: " + entity.getKey());
    }
}
