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
 * Respons�vel por estabelecer conex�o com o hibernate
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
	 * Respons�vel por ler o arquivo de configura��o hibernate.cfg.xml
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
			throw new ExceptionInInitializerError("\nErro ao criar conex�o SessionFactory!!!");
		}

	}

	// M�todos Auxiliares

	/**
	 * Retorna o session factory corrente
	 * 
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Retorna a sess�o do SessionFactory
	 * 
	 * @return Session
	 */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 * Abre uma nova sess�o no SessionFactory
	 * @return Session
	 */
	public static Session openSession() {

		if (sessionFactory == null) {
			buildSessionFactory();
		}

		return sessionFactory.openSession();
	}

	/**
	 * Obt�m a connction do provedor de conex�es configurado
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
