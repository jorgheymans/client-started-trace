package testclient;

/**
 * @author <a href="mailto:jorg.heymans@ext.ec.europa.eu">Jorg Heymans</a>
 * @version $Id$
 */
public class StartServers {

  public static void main(String[] args) {
    Server server0 = new Server("server0", 4220);
    new Thread(server0).start();
    Server server1 = new Server("server1", 4221);
    new Thread(server1).start();
  }
}
