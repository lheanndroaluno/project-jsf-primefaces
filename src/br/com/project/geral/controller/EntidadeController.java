package br.com.project.geral.controller;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.model.classes.Entidade;
import br.com.srv.interfaces.SrvEntidade;

@Controller
public class EntidadeController extends ImplementacaoCrud<Entidade> implements InterfaceCrud<Entidade>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SrvEntidade srvEntidade;
	
	public Entidade findUserLogado(String usuarioLogado) throws Exception {
		return super.findUniqueByProperty(Entidade.class, usuarioLogado, "entidade_login",
				" and entity.entidade_inativo is false");
	}
	
	public Date getUltimoAcessoEntidadeLogada(String login) {
		return srvEntidade.getUltimoAcessoEntidadeLogada(login);
	}

	public void updateUltimoAcessoUser(String name) {
		srvEntidade.updateUltimoAcessoUser(name);
	}
	
	

}
