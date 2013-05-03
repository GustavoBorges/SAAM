package br.ucb.saam.managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.mail.EmailException;

import br.ucb.saam.beans.UsuarioBean;
import br.ucb.saam.dao.UsuarioDAO;
import br.ucb.saam.util.EmailUtils;
import br.ucb.saam.util.JSFMensageiro;
import br.ucb.saam.util.Mensagem;




@ManagedBean(name="usuarioMB")
@SessionScoped
public class UsuarioMB {


	private UsuarioBean usuario;
	private UsuarioDAO usuarioDAO;
	private List<UsuarioBean> usuarios;
	private String email;


	public UsuarioMB(){
		setUsuario(new UsuarioBean());
		setUsuarioDAO(new UsuarioDAO());
		setUsuarios(new ArrayList<UsuarioBean>());
		setEmail(new String());
	}


	//Methods
	/**Metodo para buscar todos os usu�rio cadastrados no banco de dados
	 * 
	 * @return ArrayList<UsuarioBean>
	 */
	public  List<UsuarioBean> getListUsuarios(){
		return this.usuarios = this.usuarioDAO.findAll(UsuarioBean.class);
	}
	
	/**Metodo para autenticar o usu�rio no sistema.
	 *  
	 * @return String - P�gina que ser� redirecionada.
	 */
	public String login(){
		
		//Busca a lista de usu�rios gravados no banco de dados
		getListUsuarios();

		//Percorre a lista de usu�rios		
		for (UsuarioBean user : usuarios) {	
			
			//Compara se o usu�rio informado � igual ao da vez
			if(user.equals(this.usuario)){
				//Sicroniza o objeto usu�rio
				user = (UsuarioBean) usuarioDAO.buscarPorId(UsuarioBean.class, user.getId());				
				
				//Adiciona o usu�rio na Sess�o
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", user);
				
				//Redireciona para Home(Menu de Funcionalidades)
				return "home";
			}
		}
		//Caso n�o encontre envia uma mensagem informando o problema		
		JSFMensageiro.info("Usu�rio ou Senha Incorreta");
		return "index";
	}
	
	
	public String logout(){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuario");
		return "index";
	}
	public String relembraSenha(){
		return "relembraSenha";
	}

	public String procuraUsuario(){
		
		Mensagem mensagem = new Mensagem();
		
		getListUsuarios();
		for (UsuarioBean user : usuarios) {	
			if(email.equals(user.getPessoa().getEmail())){
		
				mensagem.setDestino(user.getPessoa().getEmail());
				mensagem.setTitulo("Relembra Senha SAAM");
				mensagem.setMensagem("Prezado Usuario,\n Suas Informa��es de acesso s�o \n\nUsuario:"+user.getNome()+"\nSenha:"+user.getSenha()+" \nAtenciosamente,\n \n Equipe SAAM.");
				
				try {
					EmailUtils.enviaEmail(mensagem);
					JSFMensageiro.info("Sua senha ser� enviada em instantes. Acesse seu e-mail para visualizar","Detalhada");
					this.email = new String();
				} catch (EmailException ex){
					System.out.println("Erro! Ocorreu um erro ao enviar a mensagem"+ex);
				}
				
				return "relembraUsuario";
			}
		}
		JSFMensageiro.info("Sua senha ser� enviada em instantes. Acesse seu e-mail para visualizar","Detalhada");
		this.email = new String();
		return "relembraUsario";
		
		
	}

	public String deletar(UsuarioBean usuario){
		this.usuarios.remove(usuario);
		return null;
	}


	public String salvar(){
		System.out.println("UAUUUUUUUUUUUUUUUUUUUUU");
	
		return "";
	}

	
	
	
	/*Neste m�todo � necess�rio uma nova inst�ncia do usu�rio
	 *Para que o valor acessado pelo m�todo getUsu�rio esteja null
	 */
	public String incluir(){
		this.usuario = new UsuarioBean();
		return "formUsuario";
	}




	//getters and setters
	public UsuarioBean getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBean usuario) {
		this.usuario = usuario;
	}


	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}


	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}


	public List<UsuarioBean> getUsuarios() {
		return usuarios;
	}


	public void setUsuarios(List<UsuarioBean> usuarios) {
		this.usuarios = usuarios;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}









}
