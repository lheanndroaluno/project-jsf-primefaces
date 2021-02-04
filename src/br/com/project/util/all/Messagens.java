package br.com.project.util.all;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class Messagens extends FacesContext implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Messagens() {
		
	}
	
	public static void responseOperation(StatusPersistencia statusPersistencia) {
		if (statusPersistencia != null && statusPersistencia.equals(StatusPersistencia.SUCESSO)) {
			sucesso();
		} else if(statusPersistencia != null && statusPersistencia.equals(StatusPersistencia.OBJETO_REFERENCIADO)) {
			msgSeverityFatal(StatusPersistencia.OBJETO_REFERENCIADO.toString());
		} else {
			erroNaOperacao();
		}
	}
	
	public static void msg(String msg) {
		if (facesContextEValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(msg));
		}
	}
	
	public static void sucesso() {
		msgSeverityInf(Constante.SUCESSO);
	}
	
	public static void erroNaOperacao() {
		if (facesContextEValido()) {
			msgSeverityFatal(Constante.ERRO_NA_OPERACAO);
		}
	}
	
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	private static boolean facesContextEValido() {
		return getFacesContext() != null;
	}
	
	public static void msgSeverityWarn(String msg) {
		if (facesContextEValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
		}
	}
	
	public static void msgSeverityFatal(String msg) {
		if (facesContextEValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg));
		}
	}
	
	public static void msgSeverityErro(String msg) {
		if (facesContextEValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public static void msgSeverityInf(String msg) {
		if (facesContextEValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
		}
	}
}
