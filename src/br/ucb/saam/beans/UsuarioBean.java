package br.ucb.saam.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *  Classe para objetos do tipo Area, onde ser���o contidos, valores e m���todos para o mesmo.
 *  Representa modelo para um obejto do tipo Usuario.
 *  
 * @author Thiago R. Pereira
 * @version 1.0
 * @since 2013
 *    
 */


@Entity
@Table(name="usuarios")
public class UsuarioBean implements Serializable{	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="COD_USUARIO")
	private int id;

	@Column(name="USUARIO")
	private String nome;

	@Column(name="SENHA")
	private String senha;
	

	@ManyToOne
	@JoinColumn(name="COD_PERFIL")
	private PerfilBean perfil;
	
	@OneToOne
	@JoinColumn(name="COD_PESSOA")
	private PessoaBean pessoa;





	//Constructor-------------------------------------------------------------------------

	public UsuarioBean(){
		setPerfil(new PerfilBean());
		setPessoa(new PessoaBean());
	}




	//Methods------------------------------------------------------------------------------

	@Override
	public boolean equals(Object object){

		UsuarioBean aux = (UsuarioBean) object;

		if(aux.getNome().equalsIgnoreCase(this.getNome()) && 
				aux.getSenha().equalsIgnoreCase(this.getSenha())){
			return true;
		}
		return false;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		return result;
	}



	public static String geraSenha(){

		String[] carct ={"0","1","2","3","4","5","6","7","8","9",
			"a","b","c","d","e","f","g","h","i","j",
			"k","l","m","n","o","p","q","r","s","t","u",
			"v","w","x","y","z","A","B","C","D","E","F",
			"G","H","I","J","K","L","M","N","O","P","Q",
			"R","S","T","U","V","W","X","Y","Z"};
		String senha="";
			
			for (int x=0; x<8; x++){
			int j = (int) (Math.random()*carct.length);
			senha += carct[j];
			
			}
			return senha;
	}


	// Getter and Setters----------------------------------------------------------------------

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public PerfilBean getPerfil() {
		return perfil;
	}
	public void setPerfil(PerfilBean perfil) {
		this.perfil = perfil;
	}

	public PessoaBean getPessoa(){
		return pessoa;
	}
	public void setPessoa(PessoaBean pessoa) {
		this.pessoa = pessoa;
	}
	




}
