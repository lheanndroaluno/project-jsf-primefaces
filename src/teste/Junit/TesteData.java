package teste.Junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;

import org.junit.Test;

import br.com.project.report.util.DateUtils;

public class TesteData {

	@Test
	public void testData() {
		
		//exemplo de teste unitário
		try {
			assertEquals("03092020", DateUtils.getDateAtualReportName());
			
			assertEquals("'2020-09-03'", DateUtils.formatDateSQL(Calendar.getInstance().getTime()));
			
			assertEquals("2020-09-03", DateUtils.formatDateSQLSimple(Calendar.getInstance().getTime()));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
	
	
	

}
