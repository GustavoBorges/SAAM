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

/** Classe para objetos do tipo Log, onde serão contidos, valores e métodos para o mesmo.
 *  Representa logs de operação realizadas na aplicação.
 *  
 * @author William Barreto
 * @version 1.0
 * @since 2013
 *
 */

@Entity
@Table(name="logs")
public class LogBean implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="COD_LOG")	
	private int id;
	
	@Column(name="DATA")
	private Date data;
	
	@Column(name="MENSAGEM")
	private String mensagem;
	
	@ManyToOne()
	@JoinColumn(name="COD_TIPO_LOG")
	private TipoLogBean tipoLog;
	
	@ManyToOne()
	@JoinColumn(name="COD_USUARIO")
	private UsuarioBean usuario;
	
	
	//Constructor
	
	public LogBean(){
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


	public String getMensagem() {
		return mensagem;
	}


	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}


	public TipoLogBean getTipoLog() {
		return tipoLog;
	}


	public void setTipoLog(TipoLogBean tipoLog) {
		this.tipoLog = tipoLog;
	}


	public UsuarioBean getUsuario() {
		return usuario;
	}


	public void setUsuario(UsuarioBean usuario) {
		this.usuario = usuario;
	}
	
}
