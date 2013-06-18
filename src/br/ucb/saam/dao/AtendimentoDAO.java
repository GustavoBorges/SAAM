package br.ucb.saam.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.ucb.saam.beans.AtendimentoBean;
import br.ucb.saam.beans.UsuarioBean;
import br.ucb.saam.factory.ConnectionFactory;

public class AtendimentoDAO extends GenericDAO<AtendimentoBean>{

	Session session;

	@SuppressWarnings("unchecked")
	public List<AtendimentoBean> findAllByAtendente(UsuarioBean u){

		this.session = ConnectionFactory.getSession();
		Criteria criteria = this.session.createCriteria(AtendimentoBean.class);
		criteria.add(Restrictions.like("atendente", u)).setFetchMode("mensagens", FetchMode.SELECT);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<AtendimentoBean> findAllByAtendido(UsuarioBean u){
		this.session = ConnectionFactory.getSession();
		Criteria criteria = this.session.createCriteria(AtendimentoBean.class);
		criteria.add(Restrictions.like("atendido", u)).setFetchMode("mensagens", FetchMode.SELECT);
		return criteria.list();
	}

}
