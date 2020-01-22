/*
package com.javamentor.revseev.boot.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.javamentor.revseev.boot.model.Role;
import com.javamentor.revseev.boot.service.RoleService;

*/
/**
 * A converter class used in views to map id's to actual userProfile objects.
 *//*

@Component
public class IdToRoleConverter implements Converter<Object, Role> {

	@Autowired
	RoleService roleService;

	*/
/**
	 * Gets UserProfile by Id
	 * @see org.springframework.core.convert.converter.Converter#convert(Object)
	 *//*

	public Role convert(Object element) {
		long id = Long.parseLong((String)element);
		return roleService.findById(id);
	}
}*/
