package fr.ylyade.courtage.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.validators.BeanValidator;
import fr.ylyade.courtage.dao.ITaStatutDAO;
import fr.ylyade.courtage.model.TaStatut;


public class TaStatutDAO implements ITaStatutDAO {

	private static final Logger logger = Logger.getLogger(TaStatutDAO.class);
	
	@PersistenceContext(unitName = "ylyade")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaStatut p";
	
	public TaStatutDAO(){
//		this(null);
	}

//	public TaTaStatutDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaStatut.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaStatut());
//	}


	public void persist(TaStatut transientInstance) {
		logger.debug("persisting TaStatut instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaStatut persistentInstance) {
		logger.debug("removing TaStatut instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdStatut()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaStatut merge(TaStatut detachedInstance) {
		logger.debug("merging TaStatut instance");
		try {
			TaStatut result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaStatut findById(int id) {
		logger.debug("getting TaStatut instance with id: " + id);
		try {
			TaStatut instance = entityManager.find(TaStatut.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStatut> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaStatut> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaStatut entity,String field) throws Exception {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaStatut> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStatut> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStatut> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStatut> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStatut value) throws Exception {
		BeanValidator<TaStatut> validator = new BeanValidator<TaStatut>(TaStatut.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStatut value, String propertyName) throws Exception {
		BeanValidator<TaStatut> validator = new BeanValidator<TaStatut>(TaStatut.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStatut transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaStatut findByCode(String code) {
		logger.debug("getting TaStatut instance with username: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaStatut f where f.identifiant='"+code+"'");
				TaStatut instance = (TaStatut)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
}

