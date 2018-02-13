package eu.toop.iface.mockup;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TOOPMessageBundleBuilderTest {
    @Test
    public void testTOOPMessagebundleBuilder () {

        File keystore = new File("src/test/resources/demo-keystore.jks");
        String keystorePassword = "password";
        String keystoreKeyPassword = "password";

        ByteArrayOutputStream archiveOutput = new ByteArrayOutputStream();
        try {
            TOOPMessageBundle bundle = new TOOPMessageBundleBuilder()
                    .setMSDataRequest(new MSDataRequest("ABC123"))
                    .setTOOPDataRequest(new TOOPDataRequest("DEF456"))
                    .sign(archiveOutput, keystore, keystorePassword, keystoreKeyPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream archiveInput = new ByteArrayInputStream(archiveOutput.toByteArray());
        try {
            TOOPMessageBundleBuilder bundleBuilder = new TOOPMessageBundleBuilder();
            TOOPMessageBundle bundle = bundleBuilder.parse(archiveInput);

            assertTrue(bundle.getMsDataRequest().identifier.equals("ABC123"), "MSDataRequest arrived safely");
            assertTrue(bundle.getMsDataRequest().identifier.equals("DEF456"), "MSDataRequest arrived safely");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
