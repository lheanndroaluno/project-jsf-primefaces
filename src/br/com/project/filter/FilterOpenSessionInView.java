package br.com.project.filter;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.filter.DelegatingFilterProxy;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.utils.UtilFramework;
import br.com.project.listener.ContextLoaderListenerCaixakiUtils;
import br.com.project.model.classes.Entidade;

@WebFilter(filterName = "conexaoFilter")
public class FilterOpenSessionInView extends DelegatingFilterProxy implements Serializable {

	private static final long serialVersionUID = 1L;

	private static SessionFactory sessionFactory;

	/**
	 * é executado apenas uma vez executado quando a aplicação está sendo iniciada
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		/* JDBC do Spring */
		BasicDataSource springDataSource = (BasicDataSource) ContextLoaderListenerCaixakiUtils
				.getBean("springDataSource");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(springDataSource);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {

			request.setCharacterEncoding("UTF-8");// define a codificação

			/* captura o usuário que faz a operação */
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpSession httpSession = httpServletRequest.getSession();
			Entidade usuarioLogadoSessao = (Entidade) httpSession.getAttribute("usuarioLogadoSessao");

			if (usuarioLogadoSessao != null) {
				UtilFramework.getThreadLocal().set(usuarioLogadoSessao.getEnt_codigo());
			}

			sessionFactory.getCurrentSession().beginTransaction();

			/* antes de executar a ação -> (request) */
			filterChain.doFilter(request, response);// -> executa nossa ação no servidor
			/* após a execução da ação -> (response) */

			transactionManager.commit(status);

			if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
				sessionFactory.getCurrentSession().flush();
				sessionFactory.getCurrentSession().getTransaction().commit();
			}

			if (sessionFactory.getCurrentSession().isOpen()) {
				sessionFactory.getCurrentSession().close();
			}

			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");

		} catch (Exception e) {

			transactionManager.rollback(status);
			e.printStackTrace();

			if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
				sessionFactory.getCurrentSession().getTransaction().rollback();
			}

			if (sessionFactory.getCurrentSession().isOpen()) {
				sessionFactory.getCurrentSession().close();
			}

		} finally {
			if (sessionFactory.getCurrentSession().isOpen()) {
				if (sessionFactory.getCurrentSession().beginTransaction().isActive()) {
					sessionFactory.getCurrentSession().flush();
					sessionFactory.getCurrentSession().clear();
				}

				if (sessionFactory.getCurrentSession().isOpen()) {
					sessionFactory.getCurrentSession().close();
				}
			}
		}
	}
}
