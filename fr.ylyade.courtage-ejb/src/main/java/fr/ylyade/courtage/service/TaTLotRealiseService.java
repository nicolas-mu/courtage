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
import fr.ylyade.courtage.dao.ITaTLotRealiseDAO;
import fr.ylyade.courtage.droits.service.interfaces.remote.SessionInfo;
import fr.ylyade.courtage.droits.service.interfaces.remote.TenantInfo;
import fr.ylyade.courtage.dto.TaTLotRealiseDTO;
import fr.ylyade.courtage.model.TaTLotRealise;
import fr.ylyade.courtage.model.mapper.TaTLotRealiseMapper;
import fr.ylyade.courtage.service.interfaces.remote.ITaTLotRealiseServiceRemote;


/**
 * Session Bean implementation class TaTLotRealiseBean
 */
@Stateless
//@Interceptors(ServerTenantInterceptor.class)
public class TaTLotRealiseService extends AbstractApplicationDAOServer<TaTLotRealise, TaTLotRealiseDTO> implements ITaTLotRealiseServiceRemote {

	static Logger logger = Logger.getLogger(TaTLotRealiseService.class);

	@Inject private ITaTLotRealiseDAO dao;
	
	@Inject private	SessionInfo sessionInfo;
	
	@Inject private	TenantInfo tenantInfo;

	/**
	 * Default constructor. 
	 */
	public TaTLotRealiseService() {
		super(TaTLotRealise.class,TaTLotRealiseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTLotRealise a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTLotRealise transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTLotRealise transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTLotRealise persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLotRealise()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTLotRealise merge(TaTLotRealise detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTLotRealise merge(TaTLotRealise detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTLotRealise findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTLotRealise findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTLotRealise> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTLotRealiseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTLotRealiseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTLotRealise> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTLotRealiseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTLotRealiseDTO entityToDTO(TaTLotRealise entity) {
//		TaTLotRealiseDTO dto = new TaTLotRealiseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTLotRealiseMapper mapper = new TaTLotRealiseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTLotRealiseDTO> listEntityToListDTO(List<TaTLotRealise> entity) {
		List<TaTLotRealiseDTO> l = new ArrayList<TaTLotRealiseDTO>();

		for (TaTLotRealise taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTLotRealiseDTO> selectAllDTO() {
		System.out.println("List of TaTLotRealiseDTO EJB :");
		ArrayList<TaTLotRealiseDTO> liste = new ArrayList<TaTLotRealiseDTO>();

		List<TaTLotRealise> projects = selectAll();
		for(TaTLotRealise project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTLotRealiseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTLotRealiseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTLotRealiseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTLotRealiseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTLotRealiseDTO dto, String validationContext) throws EJBException {
		try {
			TaTLotRealiseMapper mapper = new TaTLotRealiseMapper();
			TaTLotRealise entity = null;
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
	
	public void persistDTO(TaTLotRealiseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTLotRealiseDTO dto, String validationContext) throws CreateException {
		try {
			TaTLotRealiseMapper mapper = new TaTLotRealiseMapper();
			TaTLotRealise entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTLotRealiseDTO dto) throws RemoveException {
		try {
			TaTLotRealiseMapper mapper = new TaTLotRealiseMapper();
			TaTLotRealise entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTLotRealise refresh(TaTLotRealise persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTLotRealise value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTLotRealise value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTLotRealiseDTO dto, String validationContext) {
		try {
			TaTLotRealiseMapper mapper = new TaTLotRealiseMapper();
			TaTLotRealise entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTLotRealiseDTO> validator = new BeanValidator<TaTLotRealiseDTO>(TaTLotRealiseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTLotRealiseDTO dto, String propertyName, String validationContext) {
		try {
			TaTLotRealiseMapper mapper = new TaTLotRealiseMapper();
			TaTLotRealise entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTLotRealiseDTO> validator = new BeanValidator<TaTLotRealiseDTO>(TaTLotRealiseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTLotRealiseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTLotRealiseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTLotRealise value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTLotRealise value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
