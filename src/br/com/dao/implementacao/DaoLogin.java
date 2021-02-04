package br.com.dao.implementacao;

import java.io.Serializable;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.repository.interfaces.RepositoryLogin;

@Repository
public class DaoLogin extends ImplementacaoCrud<Object> implements RepositoryLogin, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public boolean autentico(String login, String senha) throws Exception {
		String sql = "select count(1) as autentica from entidade where ent_login = ? and ent_senha = ?";
		SqlRowSet srs = super.getJdbcTemplate().queryForRowSet(sql, new Object[] {login, senha});
		return srs.next() ? srs.getInt("autentica") > 0 : false;//if ternário -> se existir usuário, então é maior que 0, caso contrário é igual a false
	}

}
