package br.ucb.saam.managedbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import br.ucb.saam.beans.AreaBean;
import br.ucb.saam.beans.AtendimentoBean;
import br.ucb.saam.beans.Chat;
import br.ucb.saam.beans.Fila;
import br.ucb.saam.beans.ItemFila;
import br.ucb.saam.beans.MensagemBean;
import br.ucb.saam.beans.TipoAtendimentoBean;
import br.ucb.saam.beans.UsuarioBean;
import br.ucb.saam.beans.VoluntarioBean;
import br.ucb.saam.dao.AreaDAO;
import br.ucb.saam.dao.AtendimentoDAO;
import br.ucb.saam.dao.MensagemDAO;
import br.ucb.saam.dao.TipoAtendimentoDAO;
import br.ucb.saam.dao.VoluntarioDAO;
import br.ucb.saam.util.JSFMensageiro;

@ManagedBean
@ApplicationScoped
public class AtendimentoMB implements Serializable{

	private static final long serialVersionUID = 1L;

	private AreaBean area;
	private List<AreaBean> areas;
	private AreaDAO areaDao;
	private Chat chat;
	private List<Chat> chats;
	private Fila fila;


	private Fila filaJ;
	private Fila filaP;
	private Fila filaS;
	private List<UsuarioBean> atendentesDisponiveis;


	private String posicao;
	private MensagemBean mensagem;
	private AtendimentoBean atendimento;
	private List<AtendimentoBean> atendimentos;

	private VoluntarioBean voluntario;
	private VoluntarioDAO voluntarioDao;

	private AtendimentoDAO atendimentoDao;
	private MensagemDAO mensagemDao;

	String canalAcompanha;




	public AtendimentoMB(){
		this.atendimento = new AtendimentoBean();
		this.areas = new ArrayList<AreaBean>();
		this.atendentesDisponiveis = new ArrayList<UsuarioBean>();
		this.chat = new Chat();
		this.chats = new ArrayList<Chat>();
		this.fila = new Fila();
		this.filaJ = new Fila();
		this.filaP = new Fila();
		this.filaS = new Fila();
		this.mensagem = new MensagemBean();
		this.voluntarioDao = new VoluntarioDAO();
		this.voluntario = new VoluntarioBean();
		this.atendimentoDao = new AtendimentoDAO();
		this.mensagemDao = new MensagemDAO();
		this.canalAcompanha = new String();

		this.areaDao = new AreaDAO();
	}

	public String solicitar(){
		this.area = new AreaBean();
		this.areas = this.areaDao.findAll(AreaBean.class);
		return "/atendimento/index";
	}

	public String atender(){
		return "/atendimento/iniciarAtendimento";
	}

	public String acompanhar(){
		return "/atendimento/acompanhaAtendimento";
	}

	/**Metodo para capturar o usuário logado na sessão
	 * 
	 * @return UsuarioBean - Usuï¿½rio da Sessï¿½o
	 */
	public UsuarioBean getUsuarioSessao(){
		UsuarioBean usuario = (UsuarioBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		return usuario;		
	}


	/**Metodo para adicionar o usuï¿½rio na lista de atendentesDisponiveis
	 * 
	 * @return String - Pï¿½gina que serï¿½ redirecionada
	 */
	public String iniciarAtendimento(){
		this.atendimento = new AtendimentoBean();
		AreaBean a = (AreaBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area");
		System.out.println("Area do Atendente : "+ a.getNome());

		// Verifica se o usuário da sessão é atendente
		if(getUsuarioSessao().getPerfil().getId()== 2){
			//Adiciona o usuário da sessão na lista

			this.atendentesDisponiveis.add(getUsuarioSessao());
		}else{
			JSFMensageiro.info("Vocï¿½ nï¿½o tem acesso a estï¿½ funcionalidade");
			return "index";
		}
		return "fila";
	}



	/**Metodo para finalizar um atendimento de chat 
	 * 
	 * @return
	 */
	public String encerrarAtendimento(){


		//Captura o chat que o atendete deseja finalizar
		this.chat = getChat();


		if(this.chat.getAtendido().getPerfil().getId() == 6){
			PushContext pushContext = PushContextFactory.getDefault().getPushContext();
			pushContext.push(getChat().getCanalAtendido(), "Atendimento Finalizado.");
			pushContext.push(getChat().getCanalAtendido(), "atendimento-finalizado:");
			this.chats.remove(chat);
			JSFMensageiro.info("Atendimento Finalizado!");
			return "fila";	

		}else{
			TipoAtendimentoBean tipoAtendimento = (TipoAtendimentoBean) new TipoAtendimentoDAO().buscarPorId(TipoAtendimentoBean.class, 1);

			this.atendimento.setArea(buscaArea(chat.getAtendente()));
			this.atendimento.setAtendente(chat.getAtendente());
			this.atendimento.setAtendido(chat.getAtendido());
			this.atendimento.setDataInicio(chat.getData());
			this.atendimento.setDataFim(new Date(System.currentTimeMillis()));
			this.atendimento.setMensagens(chat.getMsgs());
			this.atendimento.setTipoAtendimento(tipoAtendimento);

			this.mensagemDao.saveOrUpdateMensagens(atendimento.getMensagens());
			this.atendimentoDao.saveOrUpdate(this.atendimento);
			PushContext pushContext = PushContextFactory.getDefault().getPushContext();
			pushContext.push(getChat().getCanalAtendido(), "Atendimento Finalizado.");
			pushContext.push(chat.getCanalAtendido(), "atendimento-finalizado.id."+this.atendimento.getId()+"");
			this.atendimento = new AtendimentoBean();
			this.chats.remove(chat);
		}
		return "fila";
	}


	/**Metodo para retirar o usuï¿½rio da lista de atendentesDisponiveis 
	 * 
	 * @return void
	 */
	public void sairAtendimento(){
		//Retira usuï¿½rio da lista
		this.atendentesDisponiveis.remove(getUsuarioSessao());
		JSFMensageiro.info("Vocï¿½ saiu da area de atendimento de chats");
	}


	/**Metodo para chamar um usuï¿½rio da fila de atendimento.<br/>
	 * Retira o usuï¿½rio da vez da fila<br/>
	 * Cria um objeto do tipo Chat<br/>
	 * Seta o Objeto Chat<br/>
	 * Adiciona na lista de chats em execuï¿½ï¿½o
	 * 
	 * @return String - Pï¿½gina que serï¿½ redirecionada
	 */
	public String chamarFila(){

		ItemFila item;

		VoluntarioBean voluntario = new VoluntarioBean();
		voluntario = (VoluntarioBean) new VoluntarioDAO().buscarPorId(VoluntarioBean.class, getUsuarioSessao().getPessoa().getId());

		if(voluntario.getArea().getId() == 1){
			item = this.filaJ.remove();
		} else {
			if(voluntario.getArea().getId() == 2){
				item = this.filaP.remove();
			}else{
				item = this.filaS.remove();
			}
		}

		Chat chat = new Chat(getUsuarioSessao(),String.valueOf(getUsuarioSessao().getId()),item.getUsuario(),item.getCanal(),new Date(System.currentTimeMillis())); //Cria objeto para o CHAT, com o atendente e atendido	
		chats.add(chat); //Adiciona na lista de chats

		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
		pushContext.push(chat.getCanalAtendido(), "Atendente Conectado.");

		return "chatAtedente";		
	}



	/**Metodo para um usuï¿½rio solicitar um atendimento. * 
	 * 
	 * @return String - Pï¿½gina que serï¿½ redirecionada
	 */
	public String solicitarAtendimento(){

		//Verifica se existe a lista de atendentes disponiveis estï¿½ vazia
		if(this.atendentesDisponiveis.isEmpty()){
			//Caso a lista de atendentes esteja vazia(isEmpty) o usuï¿½rio ï¿½ redirecionado para o 
			//formulï¿½rio para enviar uma mensagem off-line.
			if(getUsuarioSessao().getPerfil().getId() == 6){
				JSFMensageiro.error("Não existe atendete disponivel no momento, tente mais tarde.");
				return "index";
			}			
			return "mensagem";		
		}else {
			if (verificaAtendenteParaArea()){

				ItemFila item = new ItemFila();
				item.setUsuario(getUsuarioSessao());
				item.setArea(this.area);
				item.setCanal(MySession());

				// Insere na fila de acordo com area solicitada
				if(this.area.getId() == 1){
					filaJ.insere(item);
					System.out.println("FilaJ - Solicitar ");

				} else{
					if(this.area.getId() == 2){
						filaP.insere(item);
						System.out.println("FilaP - Solicitar ");


					}else{
						filaS.insere(item);
						System.out.println("FilaS - Solicitar ");
					}
				}			
				//Atualiza o status do atendimento
				setPosicao("Adicionando na fila de atendimento ...");

				// Limpa o Objeto Area
				setArea(new AreaBean());
				return "aguarde";
			}
			return "mensagem";
		}	
	}

	public String MySession(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		return "JSESSIONID="+session.getId();		
	}

	/**Metodo para verificar se existe um atendente disponivel para a area solicitada
	 * @return boolean - True: existe, False: nï¿½o existe
	 */

	public boolean verificaAtendenteParaArea(){

		for (UsuarioBean atendente : atendentesDisponiveis) {
			this.voluntario = (VoluntarioBean) voluntarioDao.buscarPorId(VoluntarioBean.class, atendente.getPessoa().getId());
			if(voluntario.getArea().equals(area)){
				return true;
			}
		}
		return false;

	}


	/**Metodo para notificar a posicao do usuario da fila.<br/>
	 * Utilizado pelo ajax de uma pagina xhtml para notificar usuarios sobre sua posiï¿½ï¿½o
	 * 
	 */
	public void atualizaPosicao(){

		if(buscaFila(getUsuarioSessao()) == null){
			setPosicao("Em Atendimento");

			//Se a posicao do usuï¿½rio for igual a 0, significa que ele estï¿½ em atendimento
			//if(fila.posicao(getUsuarioSessao()) == 0){
			//setPosicao("Em Atendimento");

			try {
				if(getUsuarioSessao().getPerfil().getId()== 6){
					FacesContext.getCurrentInstance().getExternalContext().redirect("/SAAM/atendimento/chatAnonimo.xhtml");
				}else{
					FacesContext.getCurrentInstance().getExternalContext().redirect("/SAAM/atendimento/chat.xhtml");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else{
			setPosicao(String.valueOf(buscaFila(getUsuarioSessao()).posicao(getUsuarioSessao())));
		}
	}


	/** Metodo para buscar a fila em que um usuï¿½rio foi adicionado
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


	/**Metodo utilizado apenas para manter a fila atualizada na pï¿½gina dos usuï¿½rios
	 * 
	 */
	public void atualizaFila(){
		//O componente do p:poll do primefaces necessita de um metodo para atualizar a fila.
		//Como a fila sempre estar atualizada, este metodo ï¿½ apenas para satisfazer o componente
	}

	/**Metodo para enviar mensagens para a pï¿½gina html
	 *
	 */
	public synchronized void sendMensagem(){

		this.mensagem.setUsuario(getUsuarioSessao());
		this.mensagem.setData(new Date(System.currentTimeMillis()));
		getChat().getMsgs().add(mensagem);

		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
		pushContext.push(getChat().getCanalAtendente(), getUsuarioSessao().getNome()+" : " + mensagem.getTexto());
		pushContext.push(getChat().getCanalAtendido(), getUsuarioSessao().getNome()+" : " + mensagem.getTexto());

		this.mensagem = new MensagemBean();
	}


	public String acompanharAtendimento(){
		this.canalAcompanha = new String();
		this.canalAcompanha = chat.getCanalAtendido();
		
		this.chat = new Chat();
		
		return "chatGerente";

	}


	/**Metodo para procurar o canal do usuario da sessï¿½o
	 * 
	 * @return Chat - chat correspondente ao usuario da sessï¿½o
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


	public List<AtendimentoBean> getAtendimentos() {
		return atendimentos;
	}


	public void setAtendimentos(List<AtendimentoBean> atendimentos) {
		this.atendimentos = atendimentos;
	}

	public String getCanalAcompanha() {
		return canalAcompanha;
	}

	public void setCanalAcompanha(String canalAcompanha) {
		this.canalAcompanha = canalAcompanha;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

}
