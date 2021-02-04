package br.com.project.model.classes;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;

import br.com.project.annotation.IdentificaCampoPesquisa;

@Audited
@Entity
@Table(name = "cidade")
@SequenceGenerator(name = "cidade_seq", sequenceName = "cidade_seq", initialValue = 1, allocationSize = 1)
public class Cidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@IdentificaCampoPesquisa(descricaoCampo = "Código", campoConsulta = "cid_id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cidade_seq")
	private Long cid_id;

	@IdentificaCampoPesquisa(descricaoCampo = "Nome", campoConsulta = "cid_descricao", principal = 1)
	@Column(nullable = false, length = 100)
	private String cid_descricao;

	@Basic
	@ManyToOne // muitos para um, ou seja, muitas cidades para um estado
	@JoinColumn(name = "estado", nullable = false)
	@ForeignKey(name = "estado_fk")
	@IdentificaCampoPesquisa(descricaoCampo = "Estado", campoConsulta = "estado.est_nome")
	private Estado estado = new Estado();

	@Version
	@Column(name = "versionNum")
	private int versionNum;

	public Long getCid_id() {
		return cid_id;
	}

	public void setCid_id(Long cid_id) {
		this.cid_id = cid_id;
	}

	public String getCid_descricao() {
		return cid_descricao;
	}

	public void setCid_descricao(String cid_descricao) {
		this.cid_descricao = cid_descricao;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid_id == null) ? 0 : cid_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cidade other = (Cidade) obj;
		if (cid_id == null) {
			if (other.cid_id != null)
				return false;
		} else if (!cid_id.equals(other.cid_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cidade [cid_id=" + cid_id + ", cid_descricao=" + cid_descricao + "]";
	}

}
