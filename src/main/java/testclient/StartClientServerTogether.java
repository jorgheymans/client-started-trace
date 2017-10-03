package testclient;

/**
 * @author <a href="mailto:jorg.heymans@ext.ec.europa.eu">Jorg Heymans</a>
 * @version $Id$
 */
public class StartClientServerTogether {

  public static void main(String[] args) {
    StartServers.main(null);
    StartClient.main(null);
  }
}
