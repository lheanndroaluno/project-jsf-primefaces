package br.com.framework.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framework.implementacao.crud.VariavelConexaoUtil;

/**
 * Responsável por estabelecer conexão com o hibernate
 * 
 * @author Leandro
 *
 */

@ApplicationScoped
public class HibernateUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";

	private static SessionFactory sessionFactory = buildSessionFactory();

	/**
	 * Responsável por ler o arquivo de configuração hibernate.cfg.xml
	 * 
	 * @return SessionFactory
	 */
	private static SessionFactory buildSessionFactory() {

		try {

			if (sessionFactory == null) {// consegue criar o sessionFactory apenas uma vez
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}

			return sessionFactory;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("\nErro ao criar conexão SessionFactory!!!");
		}

	}

	// Métodos Auxiliares

	/**
	 * Retorna o session factory corrente
	 * 
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Retorna a sessão do SessionFactory
	 * 
	 * @return Session
	 */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 * Abre uma nova sessão no SessionFactory
	 * @return Session
	 */
	public static Session openSession() {

		if (sessionFactory == null) {
			buildSessionFactory();
		}

		return sessionFactory.openSession();
	}

	/**
	 * Obtém a connction do provedor de conexões configurado
	 * 
	 * @return Connection SQL
	 * @throws Exception
	 */
	public static Connection getConnectionProvider() throws SQLException {

		return ((SessionFactoryImplementor) sessionFactory).getConnectionProvider().getConnection();
	}

	/**
	 * 
	 * @return Connection no InitialContext java:/comp/env/jdbc/datasource
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {

		InitialContext context = new InitialContext();
		DataSource datasource = (DataSource) context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);

		return datasource.getConnection();
	}
	
	/**
	 * 
	 * @return DataSOurce JNDI Tomcat 7
	 * @throws NamingException
	 */
	public DataSource getDataSourceJNDI() throws NamingException{
		
		InitialContext context = new InitialContext();
		return (DataSource) context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_DATA_SOURCE);
	}

}
