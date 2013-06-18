package br.ucb.saam.managedbeans;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import br.ucb.saam.beans.FuncionalidadeBean;
import br.ucb.saam.beans.UsuarioBean;
import br.ucb.saam.util.JSFMensageiro;

@ManagedBean
@RequestScoped
public class MenuMB {  

	private MenuModel model;  

	public MenuMB() {  
		
		String metodo;
		FacesContext fc = FacesContext.getCurrentInstance();
		ExpressionFactory ef = fc.getApplication().getExpressionFactory();
		MethodExpression actionExpression;
		
		model = new DefaultMenuModel();  

		//First submenu  
		Submenu submenu = new Submenu();  
		submenu.setLabel("Menu");
		
		MenuItem item = new MenuItem();
		try{
			
			for (FuncionalidadeBean funcionalidade : getUsuarioSessao().getPerfil().getFuncionalidades()) {
				metodo =  funcionalidade.getUrl();
				actionExpression = ef.createMethodExpression(fc.getELContext(),metodo,String.class,new Class[0]);
				
				item = new MenuItem();
				item.setValue(funcionalidade.getNomeTecnico());  
				item.setAjax(false);
				item.setActionExpression(actionExpression);

				submenu.getChildren().add(item); 
			}
		}catch(Exception e){
			System.out.println(e);
		}

		model.addSubmenu(submenu);  

		//Second submenu  
		submenu = new Submenu();  
		submenu.setLabel("");  

		item = new MenuItem();  
		item.setValue("Sair");  
		item.setUrl("/logout.xhtml");	
		item.setIcon("ui-icon ui-icon-power");
		submenu.getChildren().add(item);
		model.addSubmenu(submenu);  
		//          
		//        item = new MenuItem();  
		//        item.setValue("Dynamic Menuitem 2.2");  
		//        item.setUrl("#");  
		//        submenu.getChildren().add(item);  
		//          

	}  

	public UsuarioBean getUsuarioSessao(){
		UsuarioBean usuario = new UsuarioBean();
		usuario = (UsuarioBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		return usuario;		
	}

	public MenuModel getModel() {  
		return model;  
	}     

	public String naoProgramado(){
		return "/naoProgramado";
		
	}
	
	public void save() {  
		addMessage("Data saved");  
	}  

	public void update() {  
		addMessage("Data updated");  
	}  

	public void delete() {  
		addMessage("Data deleted");  
	}  

	public void addMessage(String summary) {  
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);  
		FacesContext.getCurrentInstance().addMessage(null, message);  
	} 
	
	public String mensagem(){
		JSFMensageiro.error("Mensagem de erro");
		JSFMensageiro.info("Mensagem sucesso");
		JSFMensageiro.warn("Mensagem alerta");
		JSFMensageiro.fatal("Mensagem de erro fatal");
		
		return "printmsg";
	}
}  