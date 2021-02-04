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
																										// navegação

	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
		this.wrapperd = exceptionHandler;
	}

	/**
	 * Sobescreve o método ExceptionHandler que retorna a "pilha" de exceções
	 */
	@Override
	public ExceptionHandler getWrapped() {

		return wrapperd;
	}

	/**
	 * Sobescreve o método Handler que é responsável por manipular as execeções do
	 * JSF
	 */
	@Override
	public void handle() throws FacesException {

		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

		while (iterator.hasNext()) {// enquanto tiver dados nessa lista, vai percorrer
			ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			/* Recuperar a excessão do context */
			Throwable exceptionThrowable = context.getException();

			/* Aqui trabalhamos a exceção */
			try {

				requestMap.put("exceptionMessage", exceptionThrowable.getMessage());

				/* Tratando algumas exceções */
				if (exceptionThrowable != null && exceptionThrowable.getMessage() != null
						&& exceptionThrowable.getMessage().indexOf("ConstraintViolationException") != -1) {

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Registro não pode ser removido por " + " estar associado.", ""));

				} else if (exceptionThrowable != null && exceptionThrowable.getMessage() != null
						&& exceptionThrowable.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1) {

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Registro foi atualizado ou excluído por outro usuário. " + " Consulte novamente.", ""));

				} else {

					/* Avisa ao usuário do erro */
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"O sistema se recuperou de um erro insperado.", ""));

					/*
					 * Essa mensagem aparece na sequência para tranquiliar o usuário para que ele
					 * continue usando o sistema sem problemas
					 */
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Pode continuar usando o sistema normalmente!", ""));

					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"O ERRO foi causado por:\n" + exceptionThrowable.getMessage(), ""));
					
					/*Um alerta em jacascript -> esse alert é exibido se apenas a página não for redirecionar -> Primefaces*/
					RequestContext.getCurrentInstance().execute("alert('O sistema se recuperou de um erro inesperado.')");
					
					/*Mostra isso através de uma janela do primefaces*/
					RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Erro", "'O sistema se recuperou de um erro inesperado.'"));
					
					/*Redireciona para a página de erro*/
					navigationHandler.handleNavigation(facesContext, null, "/error/error.jsf?faces-redirect=true&expered=true");
					
				}
				
				/*Renderiza a página de rro e exibe a mensagem*/
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
