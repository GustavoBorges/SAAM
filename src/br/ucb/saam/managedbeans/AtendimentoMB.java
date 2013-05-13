package br.ucb.saam.managedbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import br.ucb.saam.beans.AreaBean;
import br.ucb.saam.beans.AtendimentoBean;
import br.ucb.saam.beans.Chat;
import br.ucb.saam.beans.Fila;
import br.ucb.saam.beans.ItemFila;
import br.ucb.saam.beans.MensagemBean;
import br.ucb.saam.beans.UsuarioBean;
import br.ucb.saam.beans.VoluntarioBean;
import br.ucb.saam.dao.AreaDAO;
import br.ucb.saam.dao.VoluntarioDAO;
import br.ucb.saam.util.JSFMensageiro;

@ManagedBean
@ApplicationScoped
public class AtendimentoMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private AreaBean area;
	private List<AreaBean> areas;
	private List<Chat> chats;
	private Fila fila;
	
	
	private Fila filaJ;
	private Fila filaP;
	private Fila filaS;
	private List<UsuarioBean> atendentesDisponiveis;
	
	
	private String posicao;
	private MensagemBean mensagem;
	private AtendimentoBean atendimento;
	
	
	
	 
	
	public AtendimentoMB(){
		this.areas = new AreaDAO().findAll(AreaBean.class);
		this.atendentesDisponiveis = new ArrayList<UsuarioBean>();
		this.chats = new ArrayList<Chat>();
		this.fila = new Fila();
		
		this.filaJ = new Fila();
		this.filaP = new Fila();
		this.filaS = new Fila();
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
		
		AreaBean a = (AreaBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area");
		System.out.println("Area do Atendente : "+ a.getNome());
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

	
	/**Metodo para finalizar um atendimento de chat 
	 * 
	 * @return
	 */
	public String encerrarAtendimento(){		
		
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

		ItemFila item; //Remove o usu�rio da fila
		
		VoluntarioBean voluntario = new VoluntarioBean();
		voluntario = (VoluntarioBean) new VoluntarioDAO().buscarPorId(VoluntarioBean.class, getUsuarioSessao().getPessoa().getId());
		
		if(voluntario.getArea().getNome().equalsIgnoreCase("Jur�dica")){
			item = this.filaJ.remove();
		} else {
			if(voluntario.getArea().getNome().equalsIgnoreCase("Psicol�gica")){
				item = this.filaP.remove();
			}else{
				item = this.filaS.remove();
			}
		}
		
		Chat chat = new Chat(getUsuarioSessao(),item.getUsuario()); //Cria objeto para o CHAT, com o atendente e atendido
 		
		chats.add(chat); //Adiciona na lista de chats

		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
		pushContext.push(""+chat.getAtendido().getId(), "Atendente Conectado");
		
		return "chatAtedente";		
	}
	
	
	/**Metodo para um usu�rio solicitar um atendimento. * 
	 * 
	 * @return String - P�gina que ser� redirecionada
	 */
	public String solicitarAtendimento(){
		
		//Verifica se existe a lista de atendentes disponiveis est� vazia
		if(this.atendentesDisponiveis.isEmpty()){
			//Caso a lista de atendentes esteja vazia(isEmpty) o usu�rio � redirecionado para o 
			//formul�rio para enviar uma mensagem off-line.
			return "mensagem";
		}else {
			//Caso a lista tenha pelo menos um atendente
			//Adiciona o usu�rio da sess�o na Fila de atendimento.
			
			ItemFila item = new ItemFila();
			item.setUsuario(getUsuarioSessao());
			item.setArea(this.area);
			
			// Insere na fila de acordo com area solicitada
			if(this.area.getNome().equals("JUR�DICA")){
				filaJ.insere(item);
				System.out.println("FilaJ - Solicitar ");

			} else{
				if(this.area.getNome().equals("PSICOL�GICA")){
					filaP.insere(item);
					System.out.println("FilaP - Solicitar ");
					

				}else{
					filaS.insere(item);
					System.out.println("FilaS - Solicitar ");

				}
			}

			//fila.insere(item);
			
			//Atualiza o status do atendimento
			setPosicao("Adicionando na fila de atendimento ...");
			
			// Limpa o Objeto Area
			setArea(new AreaBean());
			return "aguarde";
		}
	}
	
	
	/**Metodo para notificar a posicao do usuario da fila.<br/>
	 * Utilizado pelo ajax de uma pagina xhtml para notificar usuarios sobre sua posi��o
	 * 
	 */
	public void atualizaPosicao(){
		
		if(buscaFila(getUsuarioSessao()) == null){
			setPosicao("Em Atendimento");
		
		//Se a posicao do usu�rio for igual a 0, significa que ele est� em atendimento
		//if(fila.posicao(getUsuarioSessao()) == 0){
			//setPosicao("Em Atendimento");
			
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("chat.xhtml");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			setPosicao(String.valueOf(buscaFila(getUsuarioSessao()).posicao(getUsuarioSessao())));
		}
	}

	
	/** Metodo para buscar a fila em que um usu�rio foi adicionado
	 * @param usuario
	 * @return Fila
	 */
	public Fila buscaFila(UsuarioBean usuario){
		if(filaJ.isUsuario(usuario) == true){
			System.out.println("FilaJ - Busca");
			return filaJ;			
		}else{
			if(filaP.isUsuario(usuario ) == true){
				System.out.println("FilaP - Busca");				
				return filaP;
			}else{
				if(filaS.isUsuario(usuario) == true){
					System.out.println("FilaS - Busca");
					return filaS;
				}else{
					System.out.println("Fila Nula!");
					return null;
				}
			}
		}
	}
	
	
	/**Metodo para buscar a area do atendente
	 * 
	 * @param atendente
	 * @return AreaBean
	 */
	public AreaBean buscaArea(UsuarioBean atendente){
		int id = atendente.getPessoa().getId();
		VoluntarioBean voluntario = (VoluntarioBean) new VoluntarioDAO().buscarPorId(VoluntarioBean.class, id);
		return voluntario.getArea();
	}
	
	
	/**Metodo utilizado apenas para manter a fila atualizada na p�gina dos usu�rios
	 * 
	 */
	public void atualizaFila(){
		//O componente do p:poll do primefaces necessita de um metodo para atualizar a fila.
		//Como a fila sempre estar atualizada, este metodo � apenas para satisfazer o componente
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

	public String getPosicao() {
		return posicao;
	}
	public void setPosicao(String posicao) {
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

	public AtendimentoBean getAtendimento() {
		return atendimento;
	}
	public void setAtendimento(AtendimentoBean atendimento) {
		this.atendimento = atendimento;
	}
	
	public Fila getFilaJ() {
		return filaJ;
	}
	public void setFilaJ(Fila filaJ) {
		this.filaJ = filaJ;
	}

	public Fila getFilaP() {
		return filaP;
	}
	public void setFilaP(Fila filaP) {
		this.filaP = filaP;
	}
	
	public Fila getFilaS() {
		return filaS;
	}
	public void setFilaS(Fila filaS) {
		this.filaS = filaS;
	}

	
}
