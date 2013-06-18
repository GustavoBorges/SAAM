package br.ucb.saam.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Classe para objetos do tipo Mensagem, onde ser�o contidos, valores e m�todos para o mesmo.
 *  Representa as mensagens enviadas por usu�rios.
 *  
 * @author William Barreto
 * @version 1.0
 * @since 2013
 *
 */

@Entity
@Table(name="mensagens")
public class MensagemBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="COD_MENSAGEM")
	private int id;
	
	@Column(name="DATA")
	private Date data;
	
	@Column(name="TEXTO")
	private String texto;
	
	@ManyToOne()
	@JoinColumn(name="COD_USUARIO")
	private UsuarioBean usuario;
	
	//Constructor
	public MensagemBean(){
		this.usuario = new  UsuarioBean();
	}
	
	//Methods
	
	//Getters and Setters
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public UsuarioBean getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioBean usuario) {
		this.usuario = usuario;
	}
	
	
}
