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

public class ToopMessageBundleBuilder {
	private final ToopMessageBundle toopMessageBundle;

	public ToopMessageBundleBuilder() {
		this.toopMessageBundle = new ToopMessageBundle();
	}

	public ToopMessageBundleBuilder setMSDataRequest(final MSDataRequest msDataRequest) {
		this.toopMessageBundle.setMsDataRequest(msDataRequest);
		return this;
	}

	public ToopMessageBundleBuilder setMSDataResponse(final MSDataResponse msDataResponse) {
		this.toopMessageBundle.setMsDataResponse(msDataResponse);
		return this;
	}

	public ToopMessageBundleBuilder setTOOPDataRequest(final ToopDataRequest toopDataRequest) {
		this.toopMessageBundle.setToopDataRequest(toopDataRequest);
		return this;
	}

	public ToopMessageBundleBuilder setTOOPDataResponse(final ToopDataResponse toopDataResponse) {
		this.toopMessageBundle.setToopDataResponse(toopDataResponse);
		return this;
	}

	public ToopMessageBundle sign(final OutputStream archiveOutput, final File keystoreFile,
								  final String keystorePassword, final String keyPassword) throws IOException {

		final AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();
		final IAsicWriter asicWriter = asicWriterFactory.newContainer(archiveOutput);

		if (toopMessageBundle.getMsDataRequest() != null) {
			final byte[] msDataRequestBytes = SerializationHelper
					.getSerializedByteArray(toopMessageBundle.getMsDataRequest());
			asicWriter.add(new ByteArrayInputStream(msDataRequestBytes), "MSDataRequest", CMimeType.APPLICATION_XML);
		}

		if (toopMessageBundle.getMsDataResponse() != null) {
			final byte[] msDataResponseBytes = SerializationHelper
					.getSerializedByteArray(toopMessageBundle.getMsDataResponse());
			asicWriter.add(new ByteArrayInputStream(msDataResponseBytes), "MSDataResponse", CMimeType.APPLICATION_XML);
		}

		if (toopMessageBundle.getToopDataRequest() != null) {
			final byte[] toopDataRequestBytes = SerializationHelper
					.getSerializedByteArray(toopMessageBundle.getToopDataRequest());
			asicWriter.add(new ByteArrayInputStream(toopDataRequestBytes), "TOOPDataRequest",
					CMimeType.APPLICATION_XML);
		}

		if (toopMessageBundle.getToopDataResponse() != null) {
			final byte[] toopDataResponseBytes = SerializationHelper
					.getSerializedByteArray(toopMessageBundle.getToopDataResponse());
			asicWriter.add(new ByteArrayInputStream(toopDataResponseBytes), "TOOPDataResponse",
					CMimeType.APPLICATION_XML);
		}

		asicWriter.sign(keystoreFile, keystorePassword, keyPassword);

		return toopMessageBundle;
	}

	public ToopMessageBundle parse(final InputStream archiveInput) throws IOException {
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
				} else if (entryName.equals("MSDataResponse")) {
					try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
						asicReader.writeFile(bos);
						final MSDataResponse msDataResponse = (MSDataResponse) SerializationHelper
								.getDeserializedObject(bos.toByteArray());
						toopMessageBundle.setMsDataResponse(msDataResponse);
					}
				} else if (entryName.equals("TOOPDataRequest")) {
					try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
						asicReader.writeFile(bos);
						final ToopDataRequest toopDataRequest = (ToopDataRequest) SerializationHelper
								.getDeserializedObject(bos.toByteArray());
						toopMessageBundle.setToopDataRequest(toopDataRequest);
					}
				} else if (entryName.equals("TOOPDataResponse")) {
					try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
						asicReader.writeFile(bos);
						final ToopDataResponse toopDataResponse = (ToopDataResponse) SerializationHelper
								.getDeserializedObject(bos.toByteArray());
						toopMessageBundle.setToopDataResponse(toopDataResponse);
					}
				}
			}
		}

		return toopMessageBundle;
	}
}
