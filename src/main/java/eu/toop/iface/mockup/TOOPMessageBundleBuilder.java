package eu.toop.iface.mockup;

import no.difi.asic.*;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;

public class TOOPMessageBundleBuilder {

    private TOOPMessageBundle toopMessageBundle;

    public TOOPMessageBundleBuilder() {
        this.toopMessageBundle = new TOOPMessageBundle();
    }

    public TOOPMessageBundleBuilder setMSDataRequest(MSDataRequest msDataRequest) {
        this.toopMessageBundle.setMsDataRequest(msDataRequest);
        return this;
    }

    public TOOPMessageBundleBuilder setTOOPDataRequest(TOOPDataRequest toopDataRequest) {
        this.toopMessageBundle.setToopDataRequest(toopDataRequest);
        return this;
    }

    public TOOPMessageBundle sign(OutputStream archiveOutput, File keystoreFile, String keystorePassword, String keyPassword) throws IOException {

        AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();
        AsicWriter asicWriter = asicWriterFactory.newContainer(archiveOutput);

        if (toopMessageBundle.getMsDataRequest() != null) {
            byte[] msDataRequestBytes = SerializationUtils.serialize(toopMessageBundle.getMsDataRequest());
            asicWriter.add(new ByteArrayInputStream(msDataRequestBytes), "MSDataRequest", MimeType.forString("application/xml"));
        }
        if (toopMessageBundle.getToopDataRequest() != null) {
            byte[] toopDataRequestBytes = SerializationUtils.serialize(toopMessageBundle.getToopDataRequest());
            asicWriter.add(new ByteArrayInputStream(toopDataRequestBytes), "TOOPDataRequest", MimeType.forString("application/xml"));
        }

        asicWriter.sign(keystoreFile, keystorePassword, keyPassword);

        return toopMessageBundle;
    }

    public TOOPMessageBundle parse(InputStream archiveInput) throws IOException {

        AsicReader asicReader = AsicReaderFactory.newFactory().open(archiveInput);

        String entryName;

        while ((entryName = asicReader.getNextFile()) != null) {

            if (entryName.equals("MSDataRequest")) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                asicReader.writeFile(bos);
                MSDataRequest msDataRequest = (MSDataRequest)SerializationUtils.deserialize(bos.toByteArray());
                toopMessageBundle.setMsDataRequest(msDataRequest);
                bos.close();
            }

            if (entryName.equals("TOOPDataRequest")) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                asicReader.writeFile(bos);
                TOOPDataRequest toopDataRequest = (TOOPDataRequest)SerializationUtils.deserialize(bos.toByteArray());
                toopMessageBundle.setToopDataRequest(toopDataRequest);
                bos.close();
            }
        }
        asicReader.close();

        return toopMessageBundle;
    }
}
