package fr.ylyade.courtage.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.data.AbstractApplicationDAOServer;
import fr.ylyade.courtage.dao.ITaTAssuranceDAO;
import fr.ylyade.courtage.droits.service.interfaces.remote.SessionInfo;
import fr.ylyade.courtage.droits.service.interfaces.remote.TenantInfo;
import fr.ylyade.courtage.dto.TaTAssuranceDTO;
import fr.ylyade.courtage.model.TaTAssurance;
import fr.ylyade.courtage.model.mapper.TaTAssuranceMapper;
import fr.ylyade.courtage.service.interfaces.remote.ITaTAssuranceServiceRemote;


/**
 * Session Bean implementation class TaTAssuranceBean
 */
@Stateless
//@Interceptors(ServerTenantInterceptor.class)
public class TaTAssuranceService extends AbstractApplicationDAOServer<TaTAssurance, TaTAssuranceDTO> implements ITaTAssuranceServiceRemote {

	static Logger logger = Logger.getLogger(TaTAssuranceService.class);

	@Inject private ITaTAssuranceDAO dao;
	
	@Inject private	SessionInfo sessionInfo;
	
	@Inject private	TenantInfo tenantInfo;

	/**
	 * Default constructor. 
	 */
	public TaTAssuranceService() {
		super(TaTAssurance.class,TaTAssuranceDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTAssurance a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTAssurance transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTAssurance transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTAssurance persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTAssurance()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTAssurance merge(TaTAssurance detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTAssurance merge(TaTAssurance detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTAssurance findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTAssurance findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTAssurance> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTAssuranceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTAssuranceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTAssurance> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTAssuranceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTAssuranceDTO entityToDTO(TaTAssurance entity) {
//		TaTAssuranceDTO dto = new TaTAssuranceDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTAssuranceMapper mapper = new TaTAssuranceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTAssuranceDTO> listEntityToListDTO(List<TaTAssurance> entity) {
		List<TaTAssuranceDTO> l = new ArrayList<TaTAssuranceDTO>();

		for (TaTAssurance taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTAssuranceDTO> selectAllDTO() {
		System.out.println("List of TaTAssuranceDTO EJB :");
		ArrayList<TaTAssuranceDTO> liste = new ArrayList<TaTAssuranceDTO>();

		List<TaTAssurance> projects = selectAll();
		for(TaTAssurance project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTAssuranceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTAssuranceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTAssuranceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTAssuranceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTAssuranceDTO dto, String validationContext) throws EJBException {
		try {
			TaTAssuranceMapper mapper = new TaTAssuranceMapper();
			TaTAssurance entity = null;
			if(dto.getId()!=null) {
				entity = dao.findById(dto.getId());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
				} else {
					 entity = mapper.mapDtoToEntity(dto,entity);
				}
			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaTAssuranceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTAssuranceDTO dto, String validationContext) throws CreateException {
		try {
			TaTAssuranceMapper mapper = new TaTAssuranceMapper();
			TaTAssurance entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTAssuranceDTO dto) throws RemoveException {
		try {
			TaTAssuranceMapper mapper = new TaTAssuranceMapper();
			TaTAssurance entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTAssurance refresh(TaTAssurance persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTAssurance value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
			//dao.validate(value); //validation automatique via la JSR bean validation
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaTAssurance value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
			//dao.validateField(value,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaTAssuranceDTO dto, String validationContext) {
		try {
			TaTAssuranceMapper mapper = new TaTAssuranceMapper();
			TaTAssurance entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTAssuranceDTO> validator = new BeanValidator<TaTAssuranceDTO>(TaTAssuranceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTAssuranceDTO dto, String propertyName, String validationContext) {
		try {
			TaTAssuranceMapper mapper = new TaTAssuranceMapper();
			TaTAssurance entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTAssuranceDTO> validator = new BeanValidator<TaTAssuranceDTO>(TaTAssuranceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTAssuranceDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTAssuranceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTAssurance value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTAssurance value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
