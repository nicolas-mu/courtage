package fr.ylyade.courtage.controller.parametres;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import fr.legrain.courtage.service.interfaces.remote.general.ThrowableExceptionLgr;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.ylyade.courtage.app.ConstWeb;
import fr.ylyade.courtage.dto.TaTRoleDTO;
import fr.ylyade.courtage.model.Const;
import fr.ylyade.courtage.model.TaTRole;
import fr.ylyade.courtage.model.mapping.LgrDozerMapper;
import fr.ylyade.courtage.service.interfaces.remote.ITaTRoleServiceRemote;

@Named
@ViewScoped  
public class TypeRoleController implements Serializable{  

	/**
	 * 
	 */
	private static final long serialVersionUID = 3828802008014762869L;
	private List<TaTRoleDTO> values; 
	private List<TaTRoleDTO> filteredValues; 
	private TaTRoleDTO nouveau ;
	private TaTRoleDTO selection ;
	
	private TaTRole taTRole = new TaTRole();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTRoleServiceRemote taTRoleService;
	
	private LgrDozerMapper<TaTRoleDTO,TaTRole> mapperUIToModel  = new LgrDozerMapper<TaTRoleDTO,TaTRole>();
	private LgrDozerMapper<TaTRole,TaTRoleDTO> mapperModelToUI  = new LgrDozerMapper<TaTRole,TaTRoleDTO>();

	@PostConstruct
	public void postConstruct(){
		try {
			refresh();
	
			if(values != null && !values.isEmpty()){
				selection = values.get(0);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refresh(){
		values = taTRoleService.selectAllDTO();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taTRoleService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			actModifier(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}
	}
	
	public TypeRoleController() {  
	}  

	public void actAnnuler(ActionEvent actionEvent){
		
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaTRoleDTO();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaTRoleDTO retour = null;
			taTRole=new TaTRole();
			if(nouveau.getId()==null || taTRoleService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taTRole);
				taTRole = taTRoleService.merge(taTRole, ITaTRoleServiceRemote.validationContext);
				mapperModelToUI.map(taTRole, nouveau);
				values= taTRoleService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaTRoleDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taTRole = taTRoleService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taTRole);
					taTRole = taTRoleService.merge(taTRole, ITaTRoleServiceRemote.validationContext);
					mapperModelToUI.map(taTRole, nouveau);
					values= taTRoleService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaTRoleDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				RequestContext.getCurrentInstance().closeDialog(taTRole);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeRole", e.getMessage()));
		}
	}
	
	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaTRoleDTO();
	
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaTRole taTRole = new TaTRole();
		try {
			if(selection!=null && selection.getId()!=null){
				taTRole = taTRoleService.findById(selection.getId());
			}

			taTRoleService.remove(taTRole);
			//values.remove(selection);
			values = taTRoleService.selectAllDTO();
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaTRoleDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Type role", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Type role", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

	public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "supprimer":
			case "modifier":
			case "inserer":
			case "imprimer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		return retour;
	}

	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaTRoleDTO>> violations = factory.getValidator().validateValue(TaTRoleDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaTRoleDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				validateUIField(nomChamp,value);
			}
		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	public boolean validateUIField(String nomChamp,Object value) {
		try {
			if(nomChamp.equals(Const.C_CODE_T_ROLE)) {
				boolean changement=false;
				if(selection.getCodeTRole()!=null && value!=null && !selection.getCodeTRole().equals("")) {
					if(value instanceof TaTRole)
						changement=((TaTRole) value).getCodeTRole().equals(selection.getCodeTRole());
					else if(value instanceof String)
					changement=!value.equals(selection.getCodeTRole());
				}
				if(changement && modeEcran.dataSetEnModeModification()) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type de role", Const.C_MESSAGE_CHANGEMENT_CODE));
				}
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}			

	public void actDialogTypes(ActionEvent actionEvent) {
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        RequestContext.getCurrentInstance().openDialog("parametres/dialog_type_role", options, params);
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTRole = (TaTRole) event.getObject();
		}
		refresh();
	}
	
	public void actDialogModifier(ActionEvent actionEvent){
		nouveau = selection;
//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
        list2.add(LibConversion.integerToString(selection.getId()));
        params.put("idEntity", list2);
        
        RequestContext.getCurrentInstance().openDialog("parametres/dialog_type_role", options, params);

	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		RequestContext.getCurrentInstance().closeDialog(null);
	}
	
	public List<TaTRoleDTO> getValues(){  
		return values;
	}

	public TaTRoleDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaTRoleDTO newTaTRole) {
		this.nouveau = newTaTRole;
	}

	public TaTRoleDTO getSelection() {
		return selection;
	}

	public void setSelection(TaTRoleDTO selectedTaTRole) {
		this.selection = selectedTaTRole;
	}

	public void setValues(List<TaTRoleDTO> values) {
		this.values = values;
	}

	public List<TaTRoleDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaTRoleDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}
	
}  
