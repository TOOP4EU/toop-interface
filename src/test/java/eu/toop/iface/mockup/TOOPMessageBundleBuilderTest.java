package eu.toop.iface.mockup;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;

public class TOOPMessageBundleBuilderTest {
	@Test
	public void testTOOPMessagebundleBuilder() throws IOException {
		final File keystore = new File("src/test/resources/demo-keystore.jks");
		final String keystorePassword = "password";
		final String keystoreKeyPassword = "password";

		try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream()) {
			new TOOPMessageBundleBuilder().setMSDataRequest(new MSDataRequest("ABC123"))
					.setTOOPDataRequest(new TOOPDataRequest("DEF456"))
					.sign(archiveOutput, keystore, keystorePassword, keystoreKeyPassword);
			archiveOutput.flush();

			try (final NonBlockingByteArrayInputStream archiveInput = archiveOutput.getAsInputStream()) {
				final TOOPMessageBundle bundleRead = new TOOPMessageBundleBuilder().parse(archiveInput);

				assertTrue(bundleRead.getMsDataRequest().identifier.equals("ABC123"), "MSDataRequest arrived safely");
				assertTrue(bundleRead.getToopDataRequest().identifier.equals("DEF456"), "ToopDataRequest arrived safely");
			}
		}
	}
}
