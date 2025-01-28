package com.mindhub.ms_order.services.Impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.ms_order.models.OrderEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class PDFGeneratorServiceImpl {

    public ByteArrayInputStream export(OrderEntity order) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(20);

        Paragraph title = new Paragraph("New order successfully created: " + order.getId(), fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(14);

        String products = order.getProducts().stream()
                .map(product -> product.getProductId() + " (x" + product.getQuantity() + ")")
                .collect(Collectors.joining(", "));

        Paragraph paragraph = new Paragraph(
                String.format("User: %s%nProducts: %s%nStatus: %s",
                        order.getUserId(),
                        products,
                        order.getStatus()),
                fontParagraph);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(title);
        document.add(paragraph);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }


}
