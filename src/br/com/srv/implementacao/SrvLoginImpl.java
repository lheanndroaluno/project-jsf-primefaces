package br.com.srv.implementacao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryLogin;
import br.com.srv.interfaces.SrvLogin;

@Service
public class SrvLoginImpl implements SrvLogin, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired//inje��o de depend�ncia do spring ou pode ser tamb�m o @Resource
	private RepositoryLogin repositoryLogin;

	@Override
	public boolean autentico(String login, String senha) throws Exception {
		return repositoryLogin.autentico(login, senha);
	}

}
