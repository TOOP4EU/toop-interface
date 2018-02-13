package eu.toop.iface.mockup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.helger.asic.AsicReaderFactory;
import com.helger.asic.AsicWriterFactory;
import com.helger.asic.IAsicReader;
import com.helger.asic.IAsicWriter;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.serialize.SerializationHelper;

public class TOOPMessageBundleBuilder {
	private final TOOPMessageBundle toopMessageBundle;

	public TOOPMessageBundleBuilder() {
		this.toopMessageBundle = new TOOPMessageBundle();
	}

	public TOOPMessageBundleBuilder setMSDataRequest(final MSDataRequest msDataRequest) {
		this.toopMessageBundle.setMsDataRequest(msDataRequest);
		return this;
	}

	public TOOPMessageBundleBuilder setTOOPDataRequest(final TOOPDataRequest toopDataRequest) {
		this.toopMessageBundle.setToopDataRequest(toopDataRequest);
		return this;
	}

	public TOOPMessageBundle sign(final OutputStream archiveOutput, final File keystoreFile,
			final String keystorePassword, final String keyPassword) throws IOException {

		final AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();
		final IAsicWriter asicWriter = asicWriterFactory.newContainer(archiveOutput);

		if (toopMessageBundle.getMsDataRequest() != null) {
			final byte[] msDataRequestBytes = SerializationHelper
					.getSerializedByteArray(toopMessageBundle.getMsDataRequest());
			asicWriter.add(new ByteArrayInputStream(msDataRequestBytes), "MSDataRequest", CMimeType.APPLICATION_XML);
		}
		if (toopMessageBundle.getToopDataRequest() != null) {
			final byte[] toopDataRequestBytes = SerializationHelper
					.getSerializedByteArray(toopMessageBundle.getToopDataRequest());
			asicWriter.add(new ByteArrayInputStream(toopDataRequestBytes), "TOOPDataRequest",
					CMimeType.APPLICATION_XML);
		}

		asicWriter.sign(keystoreFile, keystorePassword, keyPassword);

		return toopMessageBundle;
	}

	public TOOPMessageBundle parse(final InputStream archiveInput) throws IOException {
		try (final IAsicReader asicReader = AsicReaderFactory.newFactory().open(archiveInput)) {
			String entryName;
			while ((entryName = asicReader.getNextFile()) != null) {
				if (entryName.equals("MSDataRequest")) {
					try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
						asicReader.writeFile(bos);
						final MSDataRequest msDataRequest = (MSDataRequest) SerializationHelper
								.getDeserializedObject(bos.toByteArray());
						toopMessageBundle.setMsDataRequest(msDataRequest);
					}
				} else if (entryName.equals("TOOPDataRequest")) {
					try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
						asicReader.writeFile(bos);
						final TOOPDataRequest toopDataRequest = (TOOPDataRequest) SerializationHelper
								.getDeserializedObject(bos.toByteArray());
						toopMessageBundle.setToopDataRequest(toopDataRequest);
					}
				}
			}
		}

		return toopMessageBundle;
	}
}
