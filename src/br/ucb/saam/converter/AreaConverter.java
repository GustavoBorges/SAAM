package br.ucb.saam.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.ucb.saam.beans.AreaBean;
import br.ucb.saam.dao.AreaDAO;

@FacesConverter(value="areaConverter", forClass=AreaBean.class)
public class AreaConverter implements Converter{

	@Override

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (!value.equals("")){
			AreaBean area = new AreaBean();
			area = (AreaBean) new AreaDAO().buscarPorId(AreaBean.class, Integer.parseInt(value));
			return area;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if(value instanceof AreaBean){
			AreaBean area = (AreaBean) value;
			String id = String.valueOf(area.getId());
			return id;
		}
		return "";


	}


}
