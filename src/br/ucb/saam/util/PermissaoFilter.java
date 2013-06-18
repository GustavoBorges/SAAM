package br.ucb.saam.util;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ucb.saam.beans.UsuarioBean;

public class PermissaoFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {

		Boolean permitido = false;
		UsuarioBean usuario = (UsuarioBean) ((HttpServletRequest) request).getSession().getAttribute("usuario"); 
		@SuppressWarnings("unchecked")
		ArrayList<String> permissoes = (ArrayList<String>) ((HttpServletRequest) request).getSession().getAttribute("permissao");
		String uri = ((HttpServletRequest)request).getRequestURI();
		HttpServletRequest rq = (HttpServletRequest) request;
		HttpServletResponse rp = (HttpServletResponse) response;
		
		if(usuario != null){
			if(permissoes.contains(uri)){
				permitido = true;
			}else{
				permitido = false;
			}
		}		

		if(permitido == true){
			try{
				long tempoInicial = System.currentTimeMillis();
				chain.doFilter(request, response);
				long tempoFinal = System.currentTimeMillis();
				System.out.println("Tempo da requisição de " +  uri + " demorou (ms): " + (tempoFinal - tempoInicial));
			
			}catch(Exception e){
				System.out.print(e);
			}
		}else{
			rp.sendRedirect(rq.getContextPath()+"/restrito.xhtml");
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {		
	}
	
	@Override
	public void destroy() {
	}
}
