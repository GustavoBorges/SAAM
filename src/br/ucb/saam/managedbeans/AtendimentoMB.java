package br.ucb.saam.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import br.ucb.saam.beans.AreaBean;
import br.ucb.saam.beans.Chat;
import br.ucb.saam.beans.Fila;
import br.ucb.saam.beans.MensagemBean;
import br.ucb.saam.beans.UsuarioBean;
import br.ucb.saam.dao.AreaDAO;
import br.ucb.saam.util.JSFMensageiro;

@ManagedBean
@ApplicationScoped
public class AtendimentoMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private AreaBean area;
	private List<AreaBean> areas;
	private List<UsuarioBean> atendentesDisponiveis;
	private List<Chat> chats;
	private Fila fila;
	private int posicao;
	private MensagemBean mensagem;
	private String canal;
	
	
	
	
	public AtendimentoMB(){
		this.areas = new AreaDAO().findAll(AreaBean.class);
		this.atendentesDisponiveis = new ArrayList<UsuarioBean>();
		this.chats = new ArrayList<Chat>();
		this.fila = new Fila();
		this.canal = getCanal();
		this.mensagem = new MensagemBean();
	}
	

	
	/**Metodo para capturar o usu�rio logado na sess�o
	 * 
	 * @return UsuarioBean - Usu�rio da Sess�o
	 */
	public UsuarioBean getUsuarioSessao(){
		UsuarioBean usuario = (UsuarioBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		return usuario;		
	}
	
	
	/**Metodo para adicionar o usu�rio na lista de atendentesDisponiveis
	 * 
	 * @return String - P�gina que ser� redirecionada
	 */
	public String iniciarAtendimento(){
		
		// Verifica se o usu�rio da sess�o � atendente
		if(getUsuarioSessao().getPerfil().getNome().equalsIgnoreCase("atendente")){
			//Adiciona o usu�rio da sess�o na lista
			this.atendentesDisponiveis.add(getUsuarioSessao());
		}else{
			JSFMensageiro.info("Voc� n�o tem acesso a est� funcionalidade");
			return "index";
		}
		return "fila";
	}

	
	/**Metodo para retirar o usu�rio da lista de atendentesDisponiveis 
	 * 
	 * @return void
	 */
	public void sairAtendimento(){
		//Retira usu�rio da lista
		this.atendentesDisponiveis.remove(getUsuarioSessao());
		JSFMensageiro.info("Voc� saiu da area de atendimento de chats");
	}
	
	
	/**Metodo para chamar um usu�rio da fila de atendimento.<br/>
	 * Retira o usu�rio da vez da fila<br/>
	 * Cria um objeto do tipo Chat<br/>
	 * Seta o Objeto Chat<br/>
	 * Adiciona na lista de chats em execu��o
	 * 
	 * @return String - P�gina que ser� redirecionada
	 */
	public String chamarFila(){
		
		//Remove o usu�rio da fila
		UsuarioBean usuario = this.fila.remove();
		
		
		//Cria objeto para o CHAT
		Chat chat = new Chat();
		
		//Seta o atendente 
		chat.setAtendente(getUsuarioSessao());
		
		//Seta o usu�rio a ser atendido
		chat.setAtendido(usuario);
		
		//Seta o canal de comunica��o que ser� o id da mulher
		chat.setCanal(String.valueOf(usuario.getId()));
		
		//Adiciona na lista de chats
		chats.add(chat);
		
		System.out.println("CANAL --> " + chat.getCanal());
		System.out.println("ATENDENTE --> " + chat.getAtendente().getNome());
		System.out.println("ATENDIDO --> " + chat.getAtendido().getNome());
				
		
		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
		pushContext.push(chat.getCanal(), "Atendente Conectado");
		
		return "chat";		
	}
	
	
	/**Metodo para um usu�rio solicitar um atendimento. * 
	 * 
	 * @return String - P�gina que ser� redirecionada
	 */
	public String solicitarAtendimento(){
		
		//Verifica se existe a lista de atendentes est� vazia
		if(this.atendentesDisponiveis.isEmpty()){
			return "mensagem";
			
		}else {
			// Adiciona o usu�rio da sess�o na Fila.
			fila.insere(getUsuarioSessao());			
			return "aguarde";
		}
	}
	
	
	/**Metodo para notificar a posicao do usuario da fila.<br/>
	 * Utilizado pelo ajax de uma pagina xhtml para notificar usuarios sobre sua posi��o
	 * 
	 */
	public void atualizaPosicao(){
		if(getUsuarioSessao()!= null){
			this.posicao = fila.posicao(getUsuarioSessao());
		}
	}
	
	public void atualizaCanal(){
		if(getUsuarioSessao()!= null){
			this.canal = getChat().getCanal();
		}
	}
	
	/**Metodo para enviar mensagens para a p�gina html
	 *
	 */
	public synchronized void sendMensagem(){
		getChat().getMsgs().add(mensagem);
		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
		pushContext.push(""+getChat().getAtendente().getId(), getUsuarioSessao().getNome()+" : " + mensagem.getTexto());
		pushContext.push(""+getChat().getAtendido().getId(), getUsuarioSessao().getNome()+" : " + mensagem.getTexto());
	}
	
	/**Metodo para procurar o canal do usuario da sess�o
	 * 
	 * @return Chat - chat correspondente ao usario da sess�o
	 */
	public Chat getChat(){
		for (Chat c : chats) {
			if(c.getAtendente().getId() == getUsuarioSessao().getId() || c.getAtendido().getId() == getUsuarioSessao().getId()){
				return c;
			}
		}		
		return null;
	}

	
	public AreaBean getArea() {
		return area;
	}
	public void setArea(AreaBean area) {
		this.area = area;
	}
	
	public List<AreaBean> getAreas() {
		return areas;
	}
	public void setAreas(List<AreaBean> areas) {
		this.areas = areas;
	}

	public List<UsuarioBean> getAtendentesDisponiveis() {
		return atendentesDisponiveis;
	}
	public void setAtendentesDisponiveis(List<UsuarioBean> atendentesDisponiveis) {
		this.atendentesDisponiveis = atendentesDisponiveis;
	}

	public Fila getFila() {
		return fila;
	}
	public void setFila(Fila fila) {
		this.fila = fila;
	}

	public int getPosicao() {
		return posicao;
	}
	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public List<Chat> getChats() {
		return chats;
	}
	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public MensagemBean getMensagem() {
		return mensagem;
	}
	public void setMensagem(MensagemBean mensagem) {
		this.mensagem = mensagem;
	}

	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}
	
	


	
	
}
