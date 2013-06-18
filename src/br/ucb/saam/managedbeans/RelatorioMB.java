package br.ucb.saam.managedbeans;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import br.ucb.saam.beans.AtendimentoBean;
import br.ucb.saam.beans.AvaliacaoBean;
import br.ucb.saam.beans.ClassificacaoBean;
import br.ucb.saam.beans.UsuarioBean;
import br.ucb.saam.dao.AtendimentoDAO;
import br.ucb.saam.dao.AvaliacaoDAO;
import br.ucb.saam.dao.ClassificacaoDAO;
import br.ucb.saam.util.ClassificacaoGroup;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.simpleparser.HTMLWorker;

@ManagedBean
@SessionScoped
public class RelatorioMB {
	
	private List<AtendimentoBean> atendimentos;
	private AtendimentoDAO atendimentoDao;
	private AtendimentoBean atendimento;
	
	private List<AvaliacaoBean> avaliacoes;
	private AvaliacaoDAO avaliacaoDao;
	private List<ClassificacaoGroup> avaliacaoByClassificacaoGroups;
	
	public RelatorioMB(){
		atendimentos = new ArrayList<AtendimentoBean>();
		atendimentoDao = new AtendimentoDAO();
		atendimento = new AtendimentoBean();
		
		setAvaliacoes(new ArrayList<AvaliacaoBean>());
		avaliacaoDao = new AvaliacaoDAO();
		avaliacaoByClassificacaoGroups = new ArrayList<ClassificacaoGroup>();
	}
	
	
	public String historico(){
		if (getUsuarioSessao().getPerfil().getId() == 2){
				atendimentos = atendimentoDao.findAllByAtendente(getUsuarioSessao());
			return "/atendimento/historico";
		}else{
			if (getUsuarioSessao().getPerfil().getId() == 5){
					atendimentos = atendimentoDao.findAllByAtendido(getUsuarioSessao());
			return "/atendimento/historico";
			}
		}
		System.out.println("Aqui! no gerente");
		atendimentos = atendimentoDao.findAll(AtendimentoBean.class);
		return "/atendimento/historico";
	}
	
	
	public String buscarAvaliacao(){		
		
		this.avaliacaoByClassificacaoGroups = new ArrayList<ClassificacaoGroup>();
		for (int i = 1; i <= 5; i++) {
			avaliacoes = avaliacaoDao.findAllByClassificacao(i);
			ClassificacaoGroup c = new ClassificacaoGroup();
			c.setClassificacao((ClassificacaoBean) new ClassificacaoDAO().buscarPorId(ClassificacaoBean.class, i));
			c.setQtd(avaliacaoDao.findAllByClassificacao(i).size());
			this.avaliacaoByClassificacaoGroups.add(c);
		}
		
		System.out.println(avaliacaoByClassificacaoGroups.size());
		return "/relatorio/index";
	}
	
	
	public List<ClassificacaoGroup> getAvaliacaoByClassificacaoGroups() {
		return avaliacaoByClassificacaoGroups;
	}


	public void setAvaliacaoByClassificacaoGroups(
			List<ClassificacaoGroup> avaliacaoByClassificacaoGroups) {
		this.avaliacaoByClassificacaoGroups = avaliacaoByClassificacaoGroups;
	}

	public void preProcessPDF() throws IOException, BadElementException, DocumentException {  
	    Document pdf = new Document();  
	    
	   
	    pdf.addTitle("SAAM");
	    pdf.addAuthor("SAAM");
	    pdf.addSubject("Subject teste");
	    pdf.addCreationDate();
	    pdf.setMargins(50f, 100f, 100f, 100f);
	    pdf.setPageSize(PageSize.A4);
	    pdf.open();	    
	    
	    HTMLWorker html = new HTMLWorker(pdf);
	    
	    ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();  
	    String logo = servletContext.getRealPath("") +File.separator+"resources"+ File.separator + "img" + File.separator + "pdf.png";  
	    
	    String str="<html>" +
	    		"<head></head>" +
	    		"<body>" +
	    		"<div>" +
	    		"Sistema de Atendimento - Saam - html" +
	    		"<img src='"+logo+"'" +
	    		"</div>" +
	    		"</body>" +	    		
	    		"</html>";
	    
	    html.parse(new StringReader(str));
	    
	    pdf.addCreationDate();
	    pdf.add(Image.getInstance(logo));
	    
	    Paragraph p = new Paragraph("Sistema de Atendimento e Apoio a Mulher \n\n");  
	    pdf.add(p);
	    
	    Paragraph t = new Paragraph("Relatório de Avaliações\n\n");  
	    t.setAlignment("center");
	    pdf.add(t);

	}
	public String show(){
		
		return "show";
	}
	
	public UsuarioBean getUsuarioSessao(){
		UsuarioBean usuario = (UsuarioBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		return usuario;		
	}
	
	public List<AtendimentoBean> getAtendimentos() {
		return atendimentos;
	}

	public void setAtendimentos(List<AtendimentoBean> atendimentos) {
		this.atendimentos = atendimentos;
	}

	public AtendimentoDAO getAtendimentoDao() {
		return atendimentoDao;
	}

	public void setAtendimentoDao(AtendimentoDAO atendimentoDao) {
		this.atendimentoDao = atendimentoDao;
	}

	public AtendimentoBean getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(AtendimentoBean atendimento) {
		this.atendimento = atendimento;
	}


	public List<AvaliacaoBean> getAvaliacoes() {
		return avaliacoes;
	}


	public void setAvaliacoes(List<AvaliacaoBean> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}


}
