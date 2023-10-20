package client.test;

import client.core.Client;
import client.test.core.parser.TestParser;
import config.ConfigHandler;

/**
 * @author Tomasz Zbroszczyk on 19.10.2023
 * @version 1.0
 */
public class ClientTest extends Client {

    protected static String test_dir;

    public ClientTest(ConfigHandler configHandler) {
        super(configHandler);
        test_dir = configHandler.getString("test_dir");
    }

    @Override
    public void startClient() {
        System.out.println("Client started on: " + ip + ":" + port);
        System.out.println("All the commands are being run from the tests.t file");
        System.out.println();
        TestParser testParser = new TestParser(test_dir);
        testParser.parse();
        System.exit(0);
    }

}
