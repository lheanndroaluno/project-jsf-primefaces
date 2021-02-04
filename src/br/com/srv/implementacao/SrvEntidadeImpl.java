package br.com.srv.implementacao;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryEntidade;
import br.com.srv.interfaces.SrvEntidade;

@Service
public class SrvEntidadeImpl implements SrvEntidade, Serializable{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private RepositoryEntidade repositoryEntidade;

	@Override
	public Date getUltimoAcessoEntidadeLogada(String login) {
		return repositoryEntidade.getUltimoAcessoEntidadeLogada(login);
	}

	@Override
	public void updateUltimoAcessoUser(String login) {
		repositoryEntidade.updateUltimoAcessoUser(login);
		
	}

	@Override
	public boolean existeUsuario(String entidade_login) {
		return repositoryEntidade.existeUsuario(entidade_login);
	}

}
