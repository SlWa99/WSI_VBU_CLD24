## Task 1 Deployment of a simple web application

In this task you will create a simple web application. You will first
test it on your local machine using a web application server included
in the SDK, then you will deploy it on Google App Engine.

Create a simple web application as follows.

1. Go to IntelliJ.

2. In the **Welcome to IntelliJ** window, click on **Create a new
   project**, choose **Maven Archetype**

   - **Name:** cld-gae
   - **JDK:** select the JDK 11 installed previously
   - **Catalog:** Internal (by default)

   * Add an Archtype

      - **GroupId:** com.google.appengine.archetypes -- not displayed ???
      - **ArtifactId:** appengine-standard-archetype
      - **Version:** 1.0.2

   Advanced Settings:

   - **GroupId**: **ch.heigvd.cld.lab**
   - **ArtifactId**: **LabGAE**

   Make sure to select the **Java 11** SDK. (Other Java versions, notably Java 17 will not work correctly with our configuration.)

   Then **Create**

   - The wizard creates a complete project with a directory structure
     and a number of files. It may take some times to
     generate. IntelliJ detect the framework, you must click on
     **auto import** and accept the configuration of the detected
     frameworks.

3. In the `pom.xml` file add
   ```
   <appengine.sdk.version>1.9.79</appengine.sdk.version>  !!! May day
   ```
   at the end of the properties list to set the **Google SDK version**. You must
   also set the version of the **maven-deploy-plugin** (line 162) to
   `3.0.0-M2`. Set the maven compiler and target to 11:  !!!
   ```
   <maven.compiler.source>11</maven.compiler.source>
   <maven.compiler.target>11</maven.compiler.target>
   ```

4. You must right click on the project root, click on **Open    
   Project Settings**, click on **Modules** and set the language level to 11.

5. Inspect the Servlet the wizard has created: Open the `src`  
   folder that contains the Java source code. Open the
   `HelloAppEngine` file. What does the code do?

   ```
*Réponse Val*   
**Package et imports** :  
Le package ch.heigvd.cld.lab est défini pour cette classe. Des importations de classes nécessaires sont incluses, notamment SystemProperty de Google App Engine et des classes Java Servlet. 
 
**Servlet HelloAppEngine** :  
Cette classe hérite de HttpServlet, ce qui en fait une servlet qui peut répondre aux requêtes HTTP.  La servlet est annotée avec @WebServlet, ce qui indique au conteneur de servlets comment mapper les requêtes HTTP à cette servlet. La servlet répond aux requêtes GET dans la méthode doGet. La réponse HTTP contient un message simple qui inclut la version de Java utilisée par l'application (SystemProperty.version.get()) et la version de la spécification Java (java.specification.version).  
 
**Méthode getInfo** :  
Une méthode statique getInfo est définie, qui renvoie des informations sur la version de Java, le système d'exploitation et le nom d'utilisateur.
   ```

6. Note the annotation starting with `@WebServlet` in front of the
   Servlet. It maps the route `hello` to the Servlet. The mappings are needed by the web application server to route the incoming HTTP requests to the right Servlets.

7. In the `webapp/WEB-INF` directory, you have a file called
   `web.xml`. What information does it contain? And what is its use ?

   ```
*Réponse Val*   
Ce fichier web.xml est utilisé pour configurer des paramètres et des comportements spécifiques de notre application web Java, comme la page d'accueil par défaut. Il est utilisé par le serveur d'application lors du déploiement de l'application web pour configurer le comportement de l'application.

Voici ce que chaque partie de ce fichier signifie et son utilisation :

?xml version="1.0" encoding="utf-8"? : Cette déclaration indique la version XML utilisée et l'encodage des caractères du fichier.  
web-app : C'est l'élément racine du fichier web.xml et il définit le contexte de l'application web.  
xmlns="http://xmlns.jcp.org/xml/ns/javaee" : Cet attribut déclare l'espace de noms XML utilisé dans ce document, qui est défini par Java EE.  
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" : Cet attribut déclare un autre espace de noms XML utilisé pour référencer le schéma XML.  
xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" : Cet attribut indique l'emplacement du schéma XML utilisé pour valider ce fichier.  
version="3.1" : Spécifie la version du descripteur de déploiement de l'application web.  
welcome-file : Cet élément spécifie le fichier qui sera affiché par défaut lorsque l'utilisateur accède à la racine de l'application web. Dans cet exemple, le fichier index.jsp est défini comme page d'accueil par défaut.
   ```

8. Inspect the Google App Engine configuration file
   `appengine-web.xml` in `webapp/WEB-INF`. What information does it contain?

   ```
*Réponse Val*   
Le fichier appengine-web.xml définit les configurations de base nécessaires pour exécuter une application Java sur Google App Engine, y compris la version de la runtime, les paramètres de thread-safe et les propriétés système Java personnalisées.
   ```

9. Edit the Google App Engine configuration file as follows:

   - Set the `<runtime>` to `java11`.
   - Add `<app-engine-apis>true</app-engine-apis>`.

10. In the `webapp` directory you have also the file called
    `index.jsp` spotted in `web.xml`. What is its use ?

    ```
*Réponse Val*   
Le fichier index.jsp est généralement utilisé comme la page d'accueil par défaut d'une application web. Lorsqu'un utilisateur accède à la racine de l'application, le conteneur de servlets redirige automatiquement vers index.jsp s'il est configuré comme la page d'accueil dans le fichier web.xml.
    ```

---

### Test the web application on your local machine using an application server embedded in the Google Cloud SDK as follows:

1. Add Google App Engine support by going to **Tools** > **Gemini + Google Cloud Code** > **App Engine** > **Add App Engine Support** > **Google App Engine Standard**.

2. Create a Local server: **Tools** > **Gemini + Google Cloud Code** > **App Engine** > **Run on a local App Engine Server dev server**. IntelliJ will ask you to create a run configuration. In the 'Application server' field, select **Google App Engine Standard Local Server**.  In the 'Artefact to deploy' field, select 'LabGAE:war exploded' (or the corresponding name, according to your project configuration). Then, click on **Apply** and **OK**.

3. Go to the upper right corner of the IDE. You have a list of build destinations. Set the list to **Google App Engine Standard Local Server**. Run the project with the play button on the right. Normally your default browser open automatically on the server home page.

4. In this page you can see the list of the servlet available. If you click on the servlet link you can see the result of the servlet execution.

5. Observe the lower pane of the IntelliJ window: the **Run** view should open. It shows the local web application server executing. You can stop by clicking the red stop button. If everything went OK the message **INFO: Dev App Server is now running** is displayed with two urls. The first is the server home page and the second is the admin interface where you can find the datastore.

6. When you have finished testing, click the red square in the **Services** view or in the upper right corner.

---

### Deploy the web application on Google App Engine as follows:

1.  Log into the Cloud Platform console:
    <https://console.cloud.google.com/>. Create a new project called
    `labgae`. Let the location parameter to default.

2.  In IntelliJ go to the list of build destinations, select **Edit
    Configurations** and select **Google App Engine Deployment**.

    - In the 'Server' field, select **Create new**.
    - In the deployment list, select **Maven build: LabGAE**.
    - In the project list, select your Google Cloud project you
      created on the platform. If you don't have your google account
      set, you must do that now. Then you must click on **OK**.
    - Normally an error message appears beside **region** ("An App Engine application does not exist for this project ..."). You must click on **Click here** to ask the installation on the remote server. In the list select `europe-west` and click on **OK**. In may takes a few minutes to be generate. Then click on **Apply** and **OK**.
    - Note: If you have an error 'Read access to project 'labgae-123456' was denied: please check billing account associated and retry', this means that you haven't linked your account with the provided student credit. In which case, you need to make sure that you have applied the student credit to your account. Then, go to the Cloud Platform console (<https://console.cloud.google.com/>) and select your project. In the left menu, go to **Billing** and select the student credit. Then, click on **Link a billing account** and select the student credit. After that, you should be able to deploy your application.
    - Note: If you have a 'Resource already exist' error, wait for a few minutes for your instance to be fully created (see <https://console.cloud.google.com/appengine/start>). Then, if the error persists, click on your project name and reselect it. This will refresh IntelliJ's cache and the error should disappear.

3.  Deploy the web application to the cloud:

        - Go to the upper right corner of the IDE. You have a list of
          build destinations. Set the list to **Google App Engine
          Deployment** Run the project with the play button on the right.

          **Note**: You might need to grant `Storage Object Viewer` permission to your service account (the service account is _not_ your own account. It is a special account linked to your project used by Google. It can be found in the logs if you try to deploy for the first time. More information here <https://cloud.google.com/iam/docs/service-accounts>). To achieve this, go to **Cloud Storage** service and select the bucket that requires to set the permission. In the **Permission** tab, add a new permission where **New principals** is the service account for your project (given in the logs), and the role is `Storage Object Viewer`. Re-run your build configuration and the application should be deployed.

        - Observe the messages of the deployment process in the **Run**
          view, then the **Services** view. You should see a message like this Deployed service [default] to https://20200406t150557-dot-labgae.appspot.com]`. Click on the link to verify that it works.

---

### Test the web application on App Engine:

1.  In the Cloud Platform console navigate to the App Engine
    Dashboard. On the right side you should see a link like this
    `labgae.appspot.com`. This link points to your deployed
    web app. Click it to see your app running on App Engine.

2.  Navigate to the Servlet and reload it a few times in your browser
    to generate some traffic. Return to the App Engine
    Dashboard. After some time you should see your requests appear in the dashboard graph.

Deliverables:

- What does the sample code generated by the wizard do? Explain what
  you see in the **Java class files**, **web.xml**,
  **appengine-web.xml** and **index.jsp** files in a few sentences.


```
```
package ch.heigvd.cld.lab;

import com.google.appengine.api.utils.SystemProperty;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloAppEngine", value = "/hello")
public class HelloAppEngine extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    Properties properties = System.getProperties();

    response.setContentType("text/plain");
    response.getWriter().println("Hello App Engine - Standard using "
        + SystemProperty.version.get() + " Java " + properties.get("java.specification.version"));
  }

  public static String getInfo() {
    return "Version: " + System.getProperty("java.version")
          + " OS: " + System.getProperty("os.name")
          + " User: " + System.getProperty("user.name");
  }

}
```

```
<?xml version="1.0" encoding="utf-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" version="3.1">
  <welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
  </welcome-file-list>
</web-app>
```

```
<?xml version="1.0" encoding="utf-8"?>
<appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
<runtime>java11</runtime>
<threadsafe>true</threadsafe>
<system-properties>
    <property name="java.util.logging.config.file" value="WEB-INF/logging.properties"/>
</system-properties>
<app-engine-apis>true</app-engine-apis>
</appengine-web-app>
       
```

```
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ch.heigvd.cld.lab.HelloAppEngine" %>
<html>
<head>
  <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
  <title>Hello App Engine Standard Java 8</title>
</head>
<body>
    <h1>Hello App Engine -- Java 8!</h1>

  <p>This is <%= HelloAppEngine.getInfo() %>.</p>
  <table>
    <tr>
      <td colspan="2" style="font-weight:bold;">Available Servlets:</td>
    </tr>
    <tr>
      <td><a href='/hello'>Hello App Engine</a></td>
    </tr>
  </table>

</body>
</html>

```
