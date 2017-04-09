package softax.service;

import static softax.model.FileToProcess.Status.DONE;
import static softax.model.FileToProcess.Status.ERROR;
import static softax.model.FileToProcess.Status.IN_PROGRESS;
import static softax.model.FileToProcess.Status.NEW;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import softax.model.FileToProcess.Status;
import softax.model.UploadedFileData;
import softax.model.UserData;

@Component
public class JmsConsumer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StorageService storageService;

    @Value("${files_path.root}")
    private String rootPath;
    @Value("${files_path.status}")
    private String statusPath;
    @Value("${files_path.pdf}")
    private String pdfPath;

    @JmsListener(destination = "jmsQueue", containerFactory = "jmsFactory")
    public void receiveMessage(String msg) {
        log.debug("Received path to process: {}.", msg);

        Path path = Paths.get(rootPath, msg);
        renameFileStatus(path, NEW, IN_PROGRESS);

        try {
            processFile(path);
        } catch (Exception e) {
            log.error("Error during processing file " + msg, e);
            renameFileStatus(path, IN_PROGRESS, ERROR);
        }
    }

    private void processFile(Path path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UploadedFileData jsonFile = mapper.readValue(path.toFile(), UploadedFileData.class);

        Stopwatch timer = Stopwatch.createStarted();
        log.info("Start processing file {}.", path.getFileName());
        try {
            createPdf(jsonFile, Paths.get(pdfPath, path.getFileName() + ".pdf"));
        } catch (DocumentException e) {
            log.debug("Exception after processing time: {} ms.", timer.stop().elapsed(TimeUnit.MILLISECONDS));
            log.error("DocumentException.", e);
            throw new IOException(e);
        }
        log.info("File {} processing time: {} ms.", path.getFileName(), timer.stop().elapsed(TimeUnit.MILLISECONDS));

        renameFileStatus(path, IN_PROGRESS, DONE);
    }

    private void createPdf(UploadedFileData json, Path dest) throws DocumentException, FileNotFoundException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest.toFile()));
        document.open();

        float[] columnWidths = new float[json.getLabels().size()];
        Arrays.fill(columnWidths, json.getLabels().size());

        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.setHeaderRows(1);

        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        for (String label : json.getLabels()) {
            PdfPCell cell = new PdfPCell(new Phrase(label, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        for (UserData data : json.getData()) {
            table.addCell(data.getFirstName());
            table.addCell(data.getLastName());
            table.addCell(data.getNip());
            table.addCell(data.getAddress());
        }

        document.add(table);
        document.close();
    }

    private void renameFileStatus(Path path, Status oldStatus, Status newStatus) {
        try {
            storageService.rename(Paths.get(statusPath, path.getFileName().toString() + "_" + oldStatus),
                    path.getFileName().toString() + "_" + newStatus);
        } catch (IOException ex) {
            log.error("IOException during rename file.", ex);
        }
    }

}
