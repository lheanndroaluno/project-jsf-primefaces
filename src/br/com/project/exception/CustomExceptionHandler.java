package br.com.project.exception;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;

import br.com.framework.hibernate.session.HibernateUtil;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapperd;

	final FacesContext facesContext = FacesContext.getCurrentInstance();

	final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

	final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();// estado de
																										// navega��o

	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
		this.wrapperd = exceptionHandler;
	}

	/**
	 * Sobescreve o m�todo ExceptionHandler que retorna a "pilha" de exce��es
	 */
	@Override
	public ExceptionHandler getWrapped() {

		return wrapperd;
	}

	/**
	 * Sobescreve o m�todo Handler que � respons�vel por manipular as exece��es do
	 * JSF
	 */
	@Override
	public void handle() throws FacesException {

		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

		while (iterator.hasNext()) {// enquanto tiver dados nessa lista, vai percorrer
			ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			/* Recuperar a excess�o do context */
			Throwable exceptionThrowable = context.getException();

			/* Aqui trabalhamos a exce��o */
			try {

				requestMap.put("exceptionMessage", exceptionThrowable.getMessage());

				/* Tratando algumas exce��es */
				if (exceptionThrowable != null && exceptionThrowable.getMessage() != null
						&& exceptionThrowable.getMessage().indexOf("ConstraintViolationException") != -1) {

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Registro n�o pode ser removido por " + " estar associado.", ""));

				} else if (exceptionThrowable != null && exceptionThrowable.getMessage() != null
						&& exceptionThrowable.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1) {

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Registro foi atualizado ou exclu�do por outro usu�rio. " + " Consulte novamente.", ""));

				} else {

					/* Avisa ao usu�rio do erro */
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"O sistema se recuperou de um erro insperado.", ""));

					/*
					 * Essa mensagem aparece na sequ�ncia para tranquiliar o usu�rio para que ele
					 * continue usando o sistema sem problemas
					 */
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Pode continuar usando o sistema normalmente!", ""));

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"O ERRO foi causado por:\n" + exceptionThrowable.getMessage(), ""));
					
					/*Um alerta em jacascript -> esse alert � exibido se apenas a p�gina n�o for redirecionar -> Primefaces*/
					RequestContext.getCurrentInstance().execute("alert('O sistema se recuperou de um erro inesperado.')");
					
					/*Mostra isso atrav�s de uma janela do primefaces*/
					RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Erro", "'O sistema se recuperou de um erro inesperado.'"));
					
					/*Redireciona para a p�gina de erro*/
					navigationHandler.handleNavigation(facesContext, null, "/error/error.jsf?faces-redirect=true&expered=true");
					
				}
				
				/*Renderiza a p�gina de rro e exibe a mensagem*/
				facesContext.renderResponse();

			} finally {
				SessionFactory sf = HibernateUtil.getSessionFactory();

				if (sf.getCurrentSession().getTransaction().isActive()) {
					sf.getCurrentSession().getTransaction().rollback();
				}

				/* imprime o erro no console */
				exceptionThrowable.printStackTrace();

				iterator.remove();
			}

		}

		getWrapped().handle();
	}

}
