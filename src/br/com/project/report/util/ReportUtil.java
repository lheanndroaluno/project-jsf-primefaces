package br.com.project.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class ReportUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String UNDERLINE = "_";
	private static final String PONTO = ".";
	private static final String FOLDER_RELATORIOS = "/relatorios";
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
	private static final String EXTENSION_ODS = "ods";
	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_HTML = "html";
	private static final String EXTENSION_PDF = "pdf";
	private String SEPARATOR = File.separator;
	private static final int RELATORIO_PDF = 1;
	private static final int RELATORIO_EXCEL = 2;
	private static final int RELATORIO_HTML = 3;
	private static final int RELATORIO_PLANILHA_OPEN_OFFICE = 4;
	private StreamedContent arquivoRetorno = null;
	private String caminhoArquivoRelatorio = null;
	private JRExporter tipoArquivoExportado = null;
	private String extensaoArquivoExportado = "";
	private String caminhoSubReport_dir = "";
	private File arquivoGerado = null;

	public StreamedContent geraRelatorio(List<?> listDateBeanCollectionReport, HashMap parametrosRelatorio,
			String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio) throws Exception {

		/*
		 * Cria a lista de collectionDataSource de beans que carregam os dados do
		 * relat�rio
		 */
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
				listDateBeanCollectionReport);

		/*
		 * Fornece o caminho f�sico at� a pasta que cont�m os relat�rios compilados
		 * .Jasper
		 */
		FacesContext context = FacesContext.getCurrentInstance();
		context.getResponseComplete();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String caminhoRelatorio = servletContext.getRealPath(FOLDER_RELATORIOS);

		// c:/rel_bairro.jasper -> exemplo
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");

		if (caminhoRelatorio == null || (caminhoRelatorio != null && caminhoRelatorio.isEmpty()) || !file.exists()) {

			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();

			SEPARATOR = "";
		}

		/* caminho para imagens do relat�rio */
		parametrosRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);

		/* caminho completo at� o relat�rio compilado indicado */
		String caminhoArquivoJasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper";

		/* Faz o carregamento do relat�rio indicado */
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);

		/* Seta parametro SUBREPORT_DIR como caminho fisico para sub-reports */
		caminhoSubReport_dir = caminhoRelatorio + SEPARATOR;
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubReport_dir);

		/* Carregando o arquivo compilado para a mem�ria */
		JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio,
				jrBeanCollectionDataSource);

		switch (tipoRelatorio) {
		case RELATORIO_PDF:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;

		case RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();
			extensaoArquivoExportado = EXTENSION_XLS;
			break;

		case RELATORIO_PLANILHA_OPEN_OFFICE:
			tipoArquivoExportado = new JROdtExporter();
			extensaoArquivoExportado = EXTENSION_ODS;
			break;

		case RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();
			extensaoArquivoExportado = EXTENSION_HTML;
			break;

		default:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
		}

		nomeRelatorioSaida += UNDERLINE + DateUtils.getDateAtualReportName();

		/* caminho do Relat�rio exportado */
		caminhoArquivoRelatorio = caminhoRelatorio + SEPARATOR + nomeRelatorioSaida + PONTO + extensaoArquivoExportado;

		/* Cria novo file esportado */
		arquivoGerado = new File(caminhoArquivoRelatorio);

		/* Preparar a impress�o */
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);

		/* Nome do arquivo a ser exportado/impresso */
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);

		/* executa a exporta��o */
		tipoArquivoExportado.exportReport();

		/* Remove o arquivo do servidor ap�s o download, para n�o consumir recursos */
		arquivoGerado.deleteOnExit();

		/* Cria o inputStream para ser usado pelo primefaces */
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);

		/* Faz o retorno para aplica��o */
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/" + extensaoArquivoExportado,
				nomeRelatorioSaida + PONTO + extensaoArquivoExportado);

		return arquivoRetorno;

	}

}
