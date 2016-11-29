package com.ngis.civairs.model.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ngis.civairs.model.constants.NGConstants;
import com.ngis.civairs.model.entities.NGRole;
import com.ngis.civairs.model.entities.NGUser;

@Stateless
public class NGUserDAO {
	@PersistenceContext(unitName = "civairs_db_pu")
	private EntityManager em;

	public String insert(NGUser entity) {
		if (entity != null && entity.getLogin() != null && !entity.getLogin().isEmpty()) {
			if (findById(entity.getLogin()) == null) {
				try {
					em.persist(entity);
					
					//remove all managed role from persistence context 
					em.getEntityManagerFactory().getCache().evict(NGRole.class);
				} catch (Exception e) {
					return NGConstants.DB_INSERT_FAILED;
				}

				return NGConstants.DB_INSERT_OK;
			} else {
				return NGConstants.DB_INSERT_EXIST;
			}
		} else {
			return NGConstants.DB_INSERT_FAILED;
		}
	}

	public String update(NGUser entity) {
		if (entity != null && entity.getLogin() != null && !entity.getLogin().isEmpty()) {
			try {
				
				//update user
				em.merge(entity);
				
				//remove all managed role from persistence context 
				em.getEntityManagerFactory().getCache().evict(NGRole.class);
				return NGConstants.DB_UPDATE_OK;
			} catch (Exception e) {
				return NGConstants.DB_UPDATE_FAILED;
			}
		} else {
			return NGConstants.DB_UPDATE_FAILED;
		}
	}
	
	public String remove(NGUser entity) {
		if (entity != null && entity.getLogin() != null && !entity.getLogin().isEmpty()) {
			try {
				NGUser dbEntity = findById(entity.getLogin());
				if(dbEntity != null) {
					em.remove(dbEntity);
					
					//remove all managed role from persistence context 
					em.getEntityManagerFactory().getCache().evict(NGRole.class);
				}
				entity = null;
				return NGConstants.DB_DELETE_OK;
			} catch (Exception e) {
				e.printStackTrace();
				return NGConstants.DB_DELETE_FAILED;
			}
		} else {
			return NGConstants.DB_DELETE_FAILED;
		}
	}

	@SuppressWarnings("unchecked")
	public List<NGUser> getList() {
		Query query = em.createQuery("SELECT p FROM NGUser p");
		return (List<NGUser>) query.getResultList();
	}

	public NGUser findById(String id) {
		try {
			return em.find(NGUser.class, id);
		} catch (Exception e) {
			return null;
		}

	}

}
