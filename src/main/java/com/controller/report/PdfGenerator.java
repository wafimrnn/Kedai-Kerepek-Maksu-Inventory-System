package com.controller.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.model.InventoryReport;
import com.model.SalesReport;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfGenerator {

    public static byte[] generateSalesReportPDF(List<SalesReport> data) {
        // Initialize ByteArrayOutputStream to write the PDF data
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Create a new document instance
            Document document = new Document();
            // PdfWriter to write the document to outputStream
            PdfWriter.getInstance(document, outputStream);
            document.open();  // Open the document for editing

            // Add Title and Date
            document.add(new Paragraph("Sales Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));
            document.add(new Paragraph(" "));  // Empty line for spacing

            // Create PDF Table with 5 columns
            PdfPTable table = new PdfPTable(5); // Number of columns in the table
            table.setWidthPercentage(100); // Set table width to 100%
            
            // Add table headers
            table.addCell("Sale ID");
            table.addCell("Date");
            table.addCell("User");
            table.addCell("Product");
            table.addCell("Total");

            // Populate table rows with sales data
            for (SalesReport report : data) {
                table.addCell(String.valueOf(report.getSaleId())); // Sale ID
                table.addCell(report.getSaleDate().toString());    // Sale Date
                table.addCell(report.getUserName());               // User Name
                table.addCell(report.getProductName());            // Product Name
                table.addCell(report.getTotalAmount().toString()); // Total Amount
            }
            
            // Add the table to the document
            document.add(table);

            // Close the document to finalize the PDF content
            document.close();
            
            // Return the generated PDF as a byte array
            return outputStream.toByteArray();
        } catch (Exception e) {
            // Handle exception if anything goes wrong
            e.printStackTrace();
            return null;  // Return null if there was an error generating the report
        }
    }
    
    public static byte[] generateInventoryReportPDF(List<InventoryReport> data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Inventory Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4); // Example with 4 columns
            table.setWidthPercentage(100);
            table.addCell("Product Name");
            table.addCell("Quantity in Stock");
            table.addCell("Product Price");
            table.addCell("Total Value");

            for (InventoryReport report : data) {
                table.addCell(report.getProductName());
                table.addCell(String.valueOf(report.getQuantityInStock()));
                table.addCell(report.getProductPrice().toString());
                table.addCell(report.getTotalValue().toString());
            }
            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}