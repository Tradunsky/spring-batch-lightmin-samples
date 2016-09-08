package org.tuxdevelop.spring.batch.lightmin.address_migrator.domain;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class AddressFileWriter {

    private static final String PATH = "input";
    private static final int FILE_ROWS = 100;

    private final DataFactory dataFactory;


    public AddressFileWriter() {
        this.dataFactory = new DataFactory();
    }


    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
    public void writeFile() {
        final Long currentMillis = System.currentTimeMillis();
        final String fileName = "input_" + currentMillis + ".tmp";
        final File file = new File(PATH + File.separator + fileName);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "utf-8"))) {
            writer.write("zip_code;city;street;house_number");
            for (int i = 0; i < FILE_ROWS; i++) {
                final BatchTaskAddress batchTaskAddress = createBatchTaskAddress();
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(batchTaskAddress.getZipCode());
                stringBuilder.append(";");
                stringBuilder.append(batchTaskAddress.getCity());
                stringBuilder.append(";");
                stringBuilder.append(batchTaskAddress.getStreet());
                stringBuilder.append(";");
                stringBuilder.append(batchTaskAddress.getHouseNumber());
                writer.write("\n");
                writer.write(stringBuilder.toString());
            }
            writer.flush();
            log.info("Created File: {} ", file.getAbsolutePath());
            FileUtils.moveFile(new File(PATH + File.separator + fileName), new File(PATH + File.separator + fileName + "" + ".txt"));
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }


    private BatchTaskAddress createBatchTaskAddress() {
        final BatchTaskAddress batchTaskAddress = new BatchTaskAddress();
        final String street = dataFactory.getStreetName();
        final String houseNumber = dataFactory.getNumberText(4);
        final String zipCode = dataFactory.getNumberText(5);
        final String city = dataFactory.getCity();
        batchTaskAddress.setStreet(street);
        batchTaskAddress.setHouseNumber(houseNumber);
        batchTaskAddress.setZipCode(zipCode);
        batchTaskAddress.setCity(city);
        return batchTaskAddress;
    }

}
