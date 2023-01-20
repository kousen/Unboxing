package com.kousenit.unboxing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnboxingTest {
    @Mock
    private PrinterService printerService;

    @InjectMocks
    private Unboxing unboxing;

    private final String dropBoxLink =
            "https://www.dropbox.com/s/ilp5iq3al6xhngr/pg1661-images-3.epub?dl=0";
    private final long downloadedBookSize = 813378L;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeEach
    void setUp() {
        new File("src/main/resources/downloaded.pdf").delete();
    }

    @Test
    void downloadFile() {
        long length = unboxing.downloadFile(dropBoxLink);
        assertThat(length).isCloseTo(downloadedBookSize, within(10000L));
    }

    @Test
    void simplerDownloadFile() {
        long length = unboxing.simplerDownloadFile(dropBoxLink);
        assertThat(length).isCloseTo(downloadedBookSize, within(10000L));
    }

    @Test
    void copyURLToFile() {
        long length = unboxing.copyURLToFile(dropBoxLink);
        assertThat(length).isCloseTo(downloadedBookSize, within(10000L));
    }

    @Test @Disabled("Skipping for time being, but the method works")
    void openPdf() {
        unboxing.openPdf("src/main/resources/pg1661-images-3.epub");
    }

    @Test
    void sendToPrinter() {
        when(printerService.printPdf(argThat(s -> s.endsWith(".epub"))))
                .thenReturn("""
                        We printed your file!
                        Thank you for using our service.
                        
                        We also decided, for no good reason,
                        to scan it back into a pdf,
                        which you can pick up at your leisure.
                        
                        Please bring a 3 1/2" floppy disk for the scanned document.
                        """);

        String result = unboxing.sendToPrinter("src/main/resources/pg1661-images-3.epub");
        assertThat(result).contains("We printed your file!");

        verify(printerService).printPdf(anyString());
    }
}

