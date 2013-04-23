package br.ucb.saam.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import br.ucb.saam.beans.AreaBean;
import br.ucb.saam.beans.Fila;
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
	private Fila fila;
	private int posicao;
	
	//PushContext pushContext = PushContextFactory.getDefault().getPushContext();
	
	
	public AtendimentoMB(){
		this.areas = new AreaDAO().findAll(AreaBean.class);
		this.atendentesDisponiveis = new ArrayList<UsuarioBean>();
		this.fila = new Fila();
	}
	
	
	public UsuarioBean getUsuarioSessao(){
		UsuarioBean usuario = (UsuarioBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		return usuario;		
	}
	//--------------------------------------
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

	public void sairAtendimento(){
		//Retira usu�rio da lista
		this.atendentesDisponiveis.remove(getUsuarioSessao());
		JSFMensageiro.info("Voc� saiu da area de atendimento de chats");
	}
	
	public String chamarFila(){
		this.fila.remove();
		//pushContext.push("/posicao", fila.posicao(getUsuarioSessao()));
		return "";		
	}
	
	//--------------------------------------
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
	
	public void atualizaPosicao(){
		if(getUsuarioSessao()!= null){
			this.posicao = fila.posicao(getUsuarioSessao());
		}
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
	
	
	
}
