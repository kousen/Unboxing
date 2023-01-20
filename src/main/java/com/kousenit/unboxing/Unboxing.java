package com.kousenit.unboxing;

import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Unboxing {
    private static final String FILENAME = "src/main/resources/downloaded.pdf";

    private PrinterService printerService = new PrinterService();

    public void setPrinterService(PrinterService printerService) {
        this.printerService = printerService;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean deleteDownloadedFile() {
        try {
            return Files.deleteIfExists(Path.of(FILENAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long downloadFile(String url) {
        deleteDownloadedFile();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<Path> response =
                client.sendAsync(request,
                        HttpResponse.BodyHandlers.ofFile(Paths.get(FILENAME)))
                        .join();
        Path body = response.body();
        return body.toFile().length();
    }

    public long simplerDownloadFile(String url) {
        deleteDownloadedFile();
        try (InputStream in = URI.create(url).toURL().openStream()) {
            return Files.copy(in, Paths.get(FILENAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long copyURLToFile(String url) {
        deleteDownloadedFile();
        try {
            File file = new File(FILENAME);
            FileUtils.copyURLToFile(URI.create(url).toURL(), file);
            return file.length();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openPdf(String fileName) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(fileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String sendToPrinter(String fileName) {
        return printerService.printPdf(fileName);
    }

    public static void main(String[] args) {
        Unboxing unboxing = new Unboxing();
        String dropBoxLink =
                // "https://dl.dropboxusercontent.com/s/h2s06e2wn4ln7zs/help-your-boss-help-you_P1.0.pdf";
                "https://www.dropbox.com/s/h2s06e2wn4ln7zs/help-your-boss-help-you_P1.0.pdf?dl=1";
        unboxing.downloadFile(dropBoxLink);
        unboxing.simplerDownloadFile(dropBoxLink);
        unboxing.copyURLToFile(dropBoxLink);
        unboxing.openPdf(Unboxing.FILENAME);
        unboxing.sendToPrinter(Unboxing.FILENAME);
    }
}