package br.ucb.saam.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.mail.EmailException;

import br.ucb.saam.beans.AreaBean;
import br.ucb.saam.beans.PerfilBean;
import br.ucb.saam.beans.PessoaBean;
import br.ucb.saam.beans.UsuarioBean;
import br.ucb.saam.beans.VoluntarioBean;
import br.ucb.saam.dao.AreaDAO;
import br.ucb.saam.dao.EnderecoDAO;
import br.ucb.saam.dao.PerfilDAO;
import br.ucb.saam.dao.PessoaDAO;
import br.ucb.saam.dao.UsuarioDAO;
import br.ucb.saam.dao.VoluntarioDAO;
import br.ucb.saam.util.EmailUtils;
import br.ucb.saam.util.JSFMensageiro;
import br.ucb.saam.util.Mensagem;

@ManagedBean
@SessionScoped
public class VoluntarioMB implements Serializable{

	private static final long serialVersionUID = 1L;
	private VoluntarioBean voluntario;
	private List<VoluntarioBean> voluntarios;
	private List<AreaBean> areas;
	private VoluntarioDAO voluntarioDAO;
	

	// Constructor	
	public VoluntarioMB(){
		setVoluntario(new VoluntarioBean());
		setAreas(new AreaDAO().findAll(AreaBean.class));
		setVoluntarioDAO(new VoluntarioDAO());
		setVoluntarios(voluntarioDAO.buscaInativos());
	}

	
	// Methods	
	public String cadastrar(){
		new EnderecoDAO().saveOrUpdate(voluntario.getEndereco());
		voluntario.setSituacao(false);
		voluntarioDAO.saveOrUpdate(voluntario);
		enviaEmail(voluntario.getEmail(),"Cadastro"," Usuario,\n Estamos testando o envio de e-mail da aplica������o saam.\n \nAtenciosamente,\n \n Equipe SAAM.");
		voluntario = new VoluntarioBean();
		return "sucess";
	}
	
	public String index(){
		this.voluntarios = voluntarioDAO.buscaInativos();
		return "/voluntario/index";
	}
	
	public String show(){
		return "show";
	}
	
	public void reprovar(){
		PessoaDAO pessoaDao = new PessoaDAO();
		PessoaBean pessoa = new PessoaBean();
		
		pessoa = (PessoaBean) pessoaDao.buscarPorId(PessoaBean.class, voluntario.getId());
		pessoaDao.delete(pessoa);
		voluntarioDAO.delete(voluntario);
		this.voluntarios = voluntarioDAO.buscaInativos();
		voluntario = new VoluntarioBean();
		JSFMensageiro.info("O cadastro do voluntario foi reprovado com sucesso!");
	}
	
	public String aprovar(){
		
		PessoaBean pessoa = new PessoaBean();
		PessoaDAO pessoaDao = new PessoaDAO();
		
		PerfilDAO perfilDao = new PerfilDAO();
		PerfilBean perfil = new PerfilBean();
		
		UsuarioBean usuario = new UsuarioBean();
		UsuarioDAO usuarioDao = new UsuarioDAO();
		
		
		perfil = (PerfilBean) perfilDao.buscarPorId(PerfilBean.class, 2);
		pessoa = (PessoaBean) pessoaDao.buscarPorId(PessoaBean.class, voluntario.getId());
		
		usuario.setPerfil(perfil);
		usuario.setPessoa(pessoa);
		usuario.setNome(voluntario.getEmail());
		usuario.setSenha("123456");
		
		usuarioDao.saveOrUpdate(usuario);
		
		voluntario.setSituacao(true);
		voluntarioDAO.saveOrUpdate(voluntario);
		
		enviaEmail(voluntario.getEmail(),"Cadastro Aprovado", "Seu cadastro foi aprovado!");
		this.voluntarios = voluntarioDAO.buscaInativos();
		JSFMensageiro.info("O cadastro do voluntario foi aprovado com sucesso!");
		return index();
	}
	
	
// Getters and Setters
	public List<AreaBean> getAreas() {
		return areas;
	}

	public void setAreas(List<AreaBean> areas) {
		this.areas = areas;
	}

	public VoluntarioBean getVoluntario() {
		return voluntario;
	}

	public void setVoluntario(VoluntarioBean voluntario) {
		this.voluntario = voluntario;
	}

	public List<VoluntarioBean> getVoluntarios() {
		return voluntarios;
	}

	public void setVoluntarios(List<VoluntarioBean> voluntarios) {
		this.voluntarios = voluntarios;
	}

	public VoluntarioDAO getVoluntarioDAO() {
		return voluntarioDAO;
	}

	public void setVoluntarioDAO(VoluntarioDAO voluntarioDAO) {
		this.voluntarioDAO = voluntarioDAO;
	}
	
	public void enviaEmail(String destino, String titulo, String msg){
		Mensagem mensagem = new Mensagem();
		
		mensagem.setDestino(destino);
		mensagem.setTitulo(titulo);
		mensagem.setMensagem(msg);
		
		try {
			EmailUtils.enviaEmail(mensagem);
		} catch (EmailException ex){
			System.out.println("Erro! Ocorreu um erro ao enviar a mensagem"+ex);
			
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro! Occoreu um erro ao enviar a mensagem.", "Erro"));
			//Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
