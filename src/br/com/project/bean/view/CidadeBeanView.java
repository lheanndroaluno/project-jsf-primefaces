package br.com.project.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.project.bean.geral.BeanManagedViewAbstract;
import br.com.project.geral.controller.CidadeController;
import br.com.project.model.classes.Cidade;

@Controller
@Scope(value = "session")
@ManagedBean(name = "cidadeBeanView")
public class CidadeBeanView extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private String url = "/cadastro/cad_cidade.jsf?faces-redirect=true";

	private Cidade objetoSelecionado = new Cidade();
	
	private List<Cidade> list = new ArrayList<Cidade>();

	@Autowired
	private CidadeController cidadeController;
	
	/**
	 * M�todo para pegar todas as cidades cadastradas no banco de dados
	 * @returna uma lista de cidades
	 * @throws Exception
	 */
	public List<Cidade> getList() throws Exception {
		list = cidadeController.findList(Cidade.class);
		return list;
	}

	/**
	 * M�todo para salvar e editar os dados no banco
	 * Merge -> salva e edita dados
	 * return "" -> ele retorna para a mesma p�gina
	 */
	public String save() throws Exception {
		objetoSelecionado = cidadeController.merge(objetoSelecionado);
		this.novo();
		return "";
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		list.clear();
		objetoSelecionado = cidadeController.merge(objetoSelecionado);
		list.add(objetoSelecionado);
		objetoSelecionado = new Cidade();
		sucesso();
	}

	/**
	 * M�todo para criar um novo objeto.
	 * A url -> ele for�a a ir para a mesma p�gina
	 */
	@Override
	public String novo() throws Exception {
		objetoSelecionado = new Cidade();
		return url;
	}
	
	/**
	 * M�todo para editar um objeto, que no caso � cidade
	 */
	@Override
	public String editar() throws Exception {
		return url;
	}
	
	/**
	 * M�todo para excluir um objeto, que no caso � cidade
	 */
	@Override
	public void excluir() throws Exception {
		cidadeController.delete(objetoSelecionado);
		this.novo();
	}

	
	/*Getters e Setters*/
	
	public Cidade getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Cidade objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

}
