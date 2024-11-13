package project.slash.statistics.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletResponse;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.statistics.dto.response.MonthlyServiceStatisticsDto;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;

@Service
public class PdfService {
	private static final String TEMPLATE_PATH = "evaluationItem";
	private static final String PDF_FILENAME = "statistics.pdf";
	private static final String FONT_PATH = "static/fonts/NanumGothic-Regular.ttf";

	public void generateStatisticsPdf(List<MonthlyServiceStatisticsDto> statistics,
		EvaluationItemDetailDto evaluationItem,
		HttpServletResponse response) throws Exception {
		String htmlContent = generateHtmlContent(statistics, evaluationItem);
		convertToPdfAndWrite(htmlContent, response);
	}

	private String generateHtmlContent(List<MonthlyServiceStatisticsDto> statistics,
		EvaluationItemDetailDto evaluationItem) {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		Context context = new Context(Locale.KOREA);
		context.setVariable("statistics", statistics);
		context.setVariable("evaluationItem", evaluationItem);
		context.setVariable("header", statistics.get(0));

		return templateEngine.process(TEMPLATE_PATH, context);
	}

	private void convertToPdfAndWrite(String htmlContent, HttpServletResponse response) throws Exception {
		// 한글 폰트 설정
		String fontPath = new ClassPathResource(FONT_PATH).getFile().getPath();
		FontProgram fontProgram = FontProgramFactory.createFont(fontPath);
		FontProvider fontProvider = new DefaultFontProvider(false, false, false);
		fontProvider.addFont(fontProgram);

		// PDF 변환 속성 설정
		ConverterProperties converterProperties = new ConverterProperties();
		converterProperties.setFontProvider(fontProvider);
		converterProperties.setCharset("UTF-8");

		// PDF 생성 및 출력
		ByteArrayOutputStream target = new ByteArrayOutputStream();
		HtmlConverter.convertToPdf(htmlContent, target, converterProperties);

		// Response 설정
		setupResponse(response);
		response.getOutputStream().write(target.toByteArray());
	}

	private void setupResponse(HttpServletResponse response) {
		response.setContentType("application/pdf");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment; filename=" + PDF_FILENAME);
	}
}
