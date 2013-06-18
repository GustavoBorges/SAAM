package br.ucb.saam.managedbeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ControllerMB implements Serializable{

	private static final long serialVersionUID = 1L;
	String area;
	String urlCadastro;
	boolean renderFormLogin;
	boolean renderCadastre;
	
	public ControllerMB(){
		this.area = new String();
		this.renderCadastre = false;
		this.renderFormLogin = false;
	}
	
	public String apresentarArea(){
		System.out.println(this.area);
		
		if(this.area.equals("mulher")){
			this.renderFormLogin = true;
			this.renderCadastre = true;
			this.urlCadastro = "cadastrarMulher";
		}
		else{
			if(this.area.equals("voluntario")){
				this.renderCadastre = true;
				this.renderFormLogin = true;
				this.urlCadastro = "cadastrarVoluntario";
			}else{
				this.renderFormLogin = true;
				this.renderCadastre = false;
				this.urlCadastro = null;
			}
				
		}
		return "principal";
		
	}
	
	public String voltar(){
		return "principal";
	}
	public String cadastrarVoluntario(){
		return "cadastro-voluntario?faces-redirect=true";
	}
	
	public String cadastrarMulher(){
		return "cadastro-mulher";
	}
	
	public String getUrlCadastro() {
		return urlCadastro;
	}

	public void setUrlCadastro(String urlCadastro) {
		this.urlCadastro = urlCadastro;
	}

	public boolean isRenderCadastre() {
		return renderCadastre;
	}

	public void setRenderCadastre(boolean renderCadastre) {
		this.renderCadastre = renderCadastre;
	}

	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}

	public boolean isRenderFormLogin() {
		return renderFormLogin;
	}

	public void setRenderFormLogin(boolean renderForm) {
		this.renderFormLogin = renderForm;
	}
}
