package com.kousenit.unboxing;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import java.io.FileInputStream;
import java.io.IOException;

public class PrinterService {

    public String printPdf(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.PDF, null);
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(MediaSizeName.ISO_A4);
            aset.add(new Copies(1));
            aset.add(Sides.DUPLEX);
            PrintService[] services = PrintServiceLookup.lookupPrintServices(
                    DocFlavor.INPUT_STREAM.PDF, aset);
            if (services.length > 0) {
                DocPrintJob job = services[0].createPrintJob();
                job.print(pdfDoc, aset);
                return "Printed " + fileName;
            } else {
                return "No printer services available";
            }
        } catch (PrintException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
