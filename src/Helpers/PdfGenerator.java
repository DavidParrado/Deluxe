package Helpers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PdfGenerator {
  public void downloadPdf(ResultSet results, String[] headers, String[] fields, String filename) {
    try {
      // Create a PDF document
      Document document = new Document();
      PdfWriter.getInstance(document, new FileOutputStream(filename + ".pdf"));
      document.open();

      // Create a table and add data from the database
      PdfPTable table = new PdfPTable(headers.length); // Change 3 to the number of columns in your table

      // Add headers
      for (String header : headers) {
        table.addCell(header);
      }

      // Add fields
      while (results.next()) {
        for (String field : fields) {
          table.addCell(results.getString(field));
        }
      }

      // Add the table to the document
      document.add(table);

      // Close the document
      document.close();
    } catch (SQLException | DocumentException | FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
