package br.ucb.saam.managedbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.mail.EmailException;

import br.ucb.saam.beans.AreaBean;
import br.ucb.saam.beans.EnderecoBean;
import br.ucb.saam.beans.FuncionalidadeBean;
import br.ucb.saam.beans.PaginaBean;
import br.ucb.saam.beans.PerfilBean;
import br.ucb.saam.beans.PessoaBean;
import br.ucb.saam.beans.UsuarioBean;
import br.ucb.saam.beans.VoluntarioBean;
import br.ucb.saam.dao.AreaDAO;
import br.ucb.saam.dao.EnderecoDAO;
import br.ucb.saam.dao.PaginaDAO;
import br.ucb.saam.dao.PerfilDAO;
import br.ucb.saam.dao.PessoaDAO;
import br.ucb.saam.dao.UsuarioDAO;
import br.ucb.saam.dao.VoluntarioDAO;
import br.ucb.saam.util.EmailUtils;
import br.ucb.saam.util.JSFMensageiro;
import br.ucb.saam.util.Mensagem;

@ManagedBean(name="usuarioMB")
@SessionScoped
public class UsuarioMB implements Serializable{


	private static final long serialVersionUID = 1L;
	private UsuarioBean usuario;
	private UsuarioDAO usuarioDAO;

	private PessoaBean pessoa;
	private PessoaDAO pessoaDAO;

	private EnderecoBean endereco;
	private EnderecoDAO enderecoDAO;

	private List<UsuarioBean> usuarios;
	private String email;
	private PerfilDAO perfilDAO;
	private List<PerfilBean> perfis;
	private PerfilBean perfil;
	private List<UsuarioBean> resultado;
	private VoluntarioBean voluntario;
	private List<AreaBean> areas;


	
	private List<String> paginasPermitidas;



	public UsuarioMB(){
		this.usuario = new UsuarioBean();
		this.usuarioDAO = new UsuarioDAO();
		this.usuarios = new ArrayList<UsuarioBean>();
		this.email = new String();

		this.pessoa = new PessoaBean();
		this.endereco = new EnderecoBean();
		this.voluntario = new VoluntarioBean();

		this.pessoaDAO = new PessoaDAO();
		this.enderecoDAO = new EnderecoDAO();
		this.perfilDAO = new PerfilDAO();
		this.perfil = new PerfilBean();
		this.resultado = new ArrayList<UsuarioBean>();
		this.areas = new ArrayList<AreaBean>();

		this.paginasPermitidas = new ArrayList<String>();
		
	}


	//Methods

	//Manter Usuario
	public String index(){
		this.usuarios = new ArrayList<UsuarioBean>();
		this.resultado = new ArrayList<UsuarioBean>();
		this.usuarios = this.usuarioDAO.findAll(UsuarioBean.class);
		this.resultado = this.usuarios;
		return "/usuario/index.xhtml";
	}

	public void delete(){
		usuarioDAO.delete(usuario);
		usuario = new UsuarioBean();
		this.usuarios = this.usuarioDAO.findAll(UsuarioBean.class);
		this.resultado = this.usuarios;
		JSFMensageiro.info("Usuario excluido com sucesso");
	}


	public String criar(){
		
		enderecoDAO.saveOrUpdate(this.usuario.getPessoa().getEndereco());
		pessoaDAO.saveOrUpdate(this.usuario.getPessoa());
		usuarioDAO.saveOrUpdate(this.usuario);
		this.usuario = new UsuarioBean();
		JSFMensageiro.info("Usuario Cadastro com sucesso!");
		return index();
	}

	public String show(){
		return "show";
	}


	public String novo(){
		this.usuario = new UsuarioBean();
		this.perfis = new PerfilDAO().findAll(PerfilBean.class);
		return "/usuario/new";
	}

	public String edit(){
		this.perfis = new PerfilDAO().findAll(PerfilBean.class);
		return  "new";
	}

	//Manter atendentes
	public String atendenteIndex(){
		this.usuarios = new ArrayList<UsuarioBean>();
		this.resultado = new ArrayList<UsuarioBean>();
		this.usuarios = (List<UsuarioBean>) new UsuarioDAO().buscaAtendentes();
		this.resultado = this.usuarios;
		return "/atendente/index";
	}

	public String atendenteShow(){
		this.voluntario = (VoluntarioBean) new VoluntarioDAO().buscarPorId(VoluntarioBean.class, this.usuario.getPessoa().getId());
		return "show";
	}

	public String atendenteEdit(){
		this.voluntario = new VoluntarioBean();
		this.voluntario = (VoluntarioBean) new VoluntarioDAO().buscarPorId(VoluntarioBean.class, this.usuario.getPessoa().getId());
		this.areas = new AreaDAO().findAll(AreaBean.class);
		return "new";
	}

	public String atendenteCriar(){
		enderecoDAO.saveOrUpdate(this.usuario.getPessoa().getEndereco());
		pessoaDAO.saveOrUpdate(this.usuario.getPessoa());
		usuarioDAO.saveOrUpdate(this.usuario);
		new VoluntarioDAO().saveOrUpdate(this.voluntario);
		this.usuario = new UsuarioBean();
		JSFMensageiro.info("Usuario Cadastro com sucesso!");
		return atendenteIndex();
	}


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
		//getListUsuarios();
		
		this.usuarios = usuarioDAO.findAll(UsuarioBean.class);

		//Percorre a lista de usu�rios		
		for (UsuarioBean user : usuarios) {	

			//Compara se o usu�rio informado � igual ao da vez
			if(user.equals(this.usuario)){

				//Sicroniza o objeto usu�rio
				user = (UsuarioBean) usuarioDAO.buscarPorId(UsuarioBean.class, user.getId());
				
				//Adiciona o usu�rio na Sess�o
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", user);
				carregaPaginas(user);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("permissao", this.paginasPermitidas);
				isAtendente(user);
				
				//Redireciona para Home(Menu de Funcionalidades)			

				this.usuario = new UsuarioBean();
				this.paginasPermitidas = new ArrayList<String>();
				return "/home/home";
			}
		}
		//Caso n�o encontre envia uma mensagem informando o problema		
		JSFMensageiro.info("Usu�rio ou Senha Incorreta");
		return "principal";
	}

	/**
	 * Metodo para verificar se o usu�rio � atendente. Caso seja atendente adiciona a area do atendente na sua sess�o
	 * @param usuario
	 */

	public void isAtendente(UsuarioBean usuario){
		if(usuario.getPerfil().getId() == 2){
			VoluntarioBean voluntario = (VoluntarioBean) new VoluntarioDAO().buscarPorId(VoluntarioBean.class, usuario.getPessoa().getId());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("area",voluntario.getArea());
			voluntario = new VoluntarioBean();
		}
	}

	public void carregaPaginas(UsuarioBean usuario){		
		List<PaginaBean>paginas;		
		for ( FuncionalidadeBean f : usuario.getPerfil().getFuncionalidades()) {
			paginas = new PaginaDAO().findAllByFuncionalidade(f);
			for (PaginaBean pagina : paginas) {
				this.paginasPermitidas.add(pagina.getUrl());
			}
		}
	}
	
	public String logout() throws IOException{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuario");
		return "principal";
	}
	public String relembraSenha(){
		return "relembraSenha";
	}

	public String relembrarSenha(){

		Mensagem mensagem = new Mensagem();

		getListUsuarios();
		for (UsuarioBean user : usuarios) {
			System.out.println(user.getPessoa().getEmail());
			if(email.equals(user.getPessoa().getEmail())){

				mensagem.setDestino(user.getPessoa().getEmail());
				mensagem.setTitulo("Credencias de Acesso - SAAM");
				mensagem.setMensagem("Prezado(a) "+user.getPessoa().getNome()+",\n\nConforme solicitado seguem informa��es de acesso:\n\nUsuario: "+user.getNome()+"\nSenha: "+user.getSenha()+" \n\nAtenciosamente,\n\n Associa��o das Mulheres Empreendedoras.");

				try {
					EmailUtils.enviaEmail(mensagem);
					JSFMensageiro.info("Sua senha ser� enviada em instantes. Acesse seu e-mail para visualizar");
					this.email = new String();
				} catch (EmailException ex){
					System.out.println("Erro! Ocorreu um erro ao enviar a mensagem"+ex);
				}

				return "relembraSenha";
			}
		}
		JSFMensageiro.info("Sua senha ser� enviada em instantes. Acesse seu e-mail para visualizar");
		this.email = new String();
		return "relembraUsario";
		
	}
	
	public String loginAnonimo(){
		
		this.pessoa.setNome("Usu�rio An�nimo");
		this.usuario.setPessoa(pessoa);
		this.usuario.setNome("anonimo");
		this.usuario.setSenha("");
		this.usuario.setPerfil((PerfilBean) this.perfilDAO.buscarPorId(PerfilBean.class, 6));
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", this.usuario);
		
		this.pessoa = new PessoaBean();
		this.usuario = new UsuarioBean();
		return "/home/home";
	}

	public String deletar(UsuarioBean usuario){
		this.usuarios.remove(usuario);
		return null;
	}


	public String principal(){
		return "principal";
	}

	public void cadastrarMulher(){

		if(isCadastrado()){
			JSFMensageiro.error("O e-mail informado já possui cadastro no sistema.");
		}else{
			enderecoDAO.saveOrUpdate(this.endereco);

			this.pessoa.setEndereco(endereco);				
			pessoaDAO.saveOrUpdate(this.pessoa);
			usuario.setPessoa(this.pessoa);

			usuario.setNome(this.pessoa.getEmail());
			usuario.setSenha(UsuarioBean.geraSenha());
			usuario.setPerfil((PerfilBean) perfilDAO.buscarPorId(PerfilBean.class, 5));

			usuarioDAO.saveOrUpdate(usuario);

			Mensagem msg = new Mensagem();
			msg.setDestino(this.usuario.getPessoa().getEmail());
			msg.setMensagem("Prezado(a) "+this.usuario.getPessoa().getNome()+", \n\n" +
					"Para ter acesso aos atendimentos, utilize as seguintes informa��es: \n\n" +
					"\t Usu�rio: "+this.usuario.getNome()+"\n"+
					"\t Senha: "+this.usuario.getSenha()+"\n\n" +
					"\nImportante:\n\n" +
					"\t1. Ao informar o login e senha, por favor, verifique se n�o h� espa�os em branco.\n" +
					"\t2. Evite copiar e colar o login e a senha, pois este procedimento, geralmente, " +
					"acrescenta um espa�o em branco nos dados, dificultando seu acesso." +
					"\n\nAtenciosamente,\n\n" +
					"\nAssocia��o de Mulheres Empreendedoras.");

			msg.setTitulo("Cadastro - SAAM");

			enviarEmail(msg);				
			JSFMensageiro.info("Seu cadastro foi realizado com sucesso! As informa��es de acesso ser�o enviadas para o e-mail cadastrado");

			this.usuario = new UsuarioBean();
			this.endereco = new EnderecoBean();
			this.pessoa = new PessoaBean();
		}

	}

	public boolean isCadastrado(){
		for (PessoaBean p : pessoaDAO.findAll(PessoaBean.class)) {
			if(p.getEmail().equalsIgnoreCase(this.usuario.getPessoa().getEmail())){
				return true;
			}
		}
		return false;
	}
	public void enviarEmail(Mensagem msg){
		try {
			EmailUtils.enviaEmail(msg);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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


	public List<PerfilBean> getPerfis() {
		return perfis;
	}


	public PessoaBean getPessoa() {
		return pessoa;
	}


	public void setPessoa(PessoaBean pessoa) {
		this.pessoa = pessoa;
	}


	public EnderecoBean getEndereco() {
		return endereco;
	}


	public void setEndereco(EnderecoBean endereco) {
		this.endereco = endereco;
	}


	public void setPerfis(List<PerfilBean> perfis) {
		this.perfis = perfis;
	}

	public PerfilBean getPerfil() {
		return perfil;
	}


	public void setPerfil(PerfilBean perfil) {
		this.perfil = perfil;
	}


	public List<UsuarioBean> getResultado() {
		return resultado;
	}


	public void setResultado(List<UsuarioBean> resultado) {
		this.resultado = resultado;
	}


	public VoluntarioBean getVoluntario() {
		return voluntario;
	}


	public void setVoluntario(VoluntarioBean voluntario) {
		this.voluntario = voluntario;
	}


	public List<AreaBean> getAreas() {
		return areas;
	}


	public void setAreas(List<AreaBean> areas) {
		this.areas = areas;
	}
}
