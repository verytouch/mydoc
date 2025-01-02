package top.verytouch.vkit.mydoc.builder.file;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.xhtmlrenderer.pdf.DefaultPDFCreationListener;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFCreationListener;
import top.verytouch.vkit.mydoc.constant.DocType;
import top.verytouch.vkit.mydoc.model.ApiModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 导出为pdf
 *
 * @author verytouch
 * @since 2024-12
 */
public class PdfDocBuilder extends FreemarkerDocBuilder {

    private static final String FONT_PATH = "/template/simhei.ttf";

    public PdfDocBuilder(AnActionEvent event) {
        super(event, DocType.PDF);
    }

    @Override
    protected OutputStream newOutputStream(ApiModel model) {
        return new ByteArrayOutputStream();
    }

    @Override
    protected OutputStream buildOutputStream(ApiModel model) throws IOException {
        ByteArrayOutputStream html = (ByteArrayOutputStream) super.buildOutputStream(model);
        ITextRenderer renderer = new ITextRenderer();
        OutputStream outputStream;
        try {
            renderer.getFontResolver().addFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.setDocument(html.toByteArray());
            renderer.setListener(footListener());
            renderer.layout();
            outputStream = super.newOutputStream(model);
            renderer.createPDF(outputStream);
        } catch (DocumentException e) {
            throw new IOException(e);
        }
        html.close();
        return outputStream;
    }

    private PDFCreationListener footListener() {
        Font font = FontFactory.getFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 9, Font.NORMAL, BaseColor.GRAY);
        return new DefaultPDFCreationListener() {
            @Override
            public void preWrite(ITextRenderer iTextRenderer, int pageCount) {
                iTextRenderer.getWriter().setPageEvent(new PdfPageEventHelper() {
                    @Override
                    public void onEndPage(PdfWriter writer, Document document) {
                        Phrase footer = new Phrase(String.format("第%s/%s页", writer.getPageNumber(), pageCount), font);
                        float x = document.getPageSize().getRight() / 2;
                        float y = document.getPageSize().getBottom() + 15;
                        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, footer, x, y, 0);
                    }
                });
            }
        };
    }

}
