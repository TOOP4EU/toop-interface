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
					.setMSDataResponse(new MSDataResponse("AAA111"))
					.setTOOPDataResponse(new TOOPDataResponse("BBB222"))
					.sign(archiveOutput, keystore, keystorePassword, keystoreKeyPassword);
			archiveOutput.flush();

			try (final NonBlockingByteArrayInputStream archiveInput = archiveOutput.getAsInputStream()) {
				final TOOPMessageBundle bundleRead = new TOOPMessageBundleBuilder().parse(archiveInput);

				assertTrue(bundleRead.getMsDataRequest().identifier.equals("ABC123"),
						"MSDataRequest did not arrive safely");
				assertTrue(bundleRead.getToopDataRequest().identifier.equals("DEF456"),
						"ToopDataRequest did not arrive safely");
				assertTrue(bundleRead.getMsDataResponse().identifier.equals("AAA111"),
						"MSDataResponse did not arrive safely");
				assertTrue(bundleRead.getToopDataResponse().identifier.equals("BBB222"),
						"MSDataResponse did not arrive safely");
			}
		}
	}
}
