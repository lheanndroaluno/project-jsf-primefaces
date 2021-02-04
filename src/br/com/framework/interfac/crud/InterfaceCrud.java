package br.com.framework.interfac.crud;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional // Essa anotação identifica que ela realiza transação com o banco de dados
public interface InterfaceCrud<T> extends Serializable {

	// salva os dados
	public void save(T obj) throws Exception;

	public void persist(T obj) throws Exception;

	// salva ou atualiza os dados
	public void saveOrUpdate(T obj) throws Exception;

	// realiza o update/atualização de dados
	public void update(T obj) throws Exception;

	// deleta os dados
	public void delete(T obj) throws Exception;

	// salva ou atualiza e retorna um objeto em estado persistente
	T merge(T obj) throws Exception;

	// carrega a lista de dados de determinada classe
	List<T> findList(Class<T> objetos) throws Exception;

	Object findById(Class<T> entidade, Long id) throws Exception;

	T findByPorId(Class<T> entidade, Long id) throws Exception;

	List<T> findListByDinamic(String s) throws Exception;

	// executar update com HQL
	public void executeUpdateQueryDinamic(String s) throws Exception;

	// executar update com SQL
	public void executeUpdateSQLDinamic(String s) throws Exception;

	// limpa a sessão do hibernate
	public void clearSession() throws Exception;

	// retira um objeto da sessão do hibernate
	public void evict(Object objects) throws Exception;

	Session getSession() throws Exception;

	List<?> getListSQLDinamic(String sql) throws Exception;

	// JDBC do Spring
	JdbcTemplate getJdbcTemplate();

	// JDBC do Spring
	SimpleJdbcTemplate getSimpleJdbcTemplate();

	// JDBC do Spring
	SimpleJdbcInsert getSimpleJdbcInsert();

	Long totalRegistros(String table) throws Exception;

	Query obterQuery(String query) throws Exception;
	
	List<Object[]> getListSQLDinamicArray(String sql) throws Exception;

	// carregamento dinâmico com JSF e Primefaces
	List<T> findListByQueryDinamic(String query, int iniciaNoRegistro, int maximoResultado) throws Exception;

}
