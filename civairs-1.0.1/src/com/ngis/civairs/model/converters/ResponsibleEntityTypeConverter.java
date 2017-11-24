package com.ngis.civairs.model.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.ngis.civairs.model.enums.ResponsibleEntityType;
import com.ngis.civairs.model.services.occurence.ResponsibleEntityService2;

@FacesConverter("responsibleEntityTypeConverter")
public class ResponsibleEntityTypeConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		ResponsibleEntityType obj = null;
		if(value != null && value.trim().length() > 0) {
			ResponsibleEntityService2 service = (ResponsibleEntityService2) context.getExternalContext().getSessionMap().get("responsibleEntityService2");   		
            if(service.getResponsibleEntityTypesMap() != null){
            	obj = service.getResponsibleEntityTypesMap().get(value);
            }
        }
		
		return obj;
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String str = null;
		if(value != null) {
            str= String.valueOf(((ResponsibleEntityType) value).getId());
        }
        return str;
	}

}
