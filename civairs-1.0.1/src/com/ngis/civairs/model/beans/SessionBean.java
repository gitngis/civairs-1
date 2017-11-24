package com.ngis.civairs.model.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.ngis.civairs.model.dao.NGUserDAO;
import com.ngis.civairs.model.dao.occurence.EntityAttributeDAO;
import com.ngis.civairs.model.services.NGViewService;
import com.ngis.core.model.User;
import com.ngis.core.model.occurence.Engine;
import com.ngis.core.services.MessageService;

@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_THEME_COLOR = "Blue";

	private String userName;
	private String password;

	private String sessionTheme;
	private String sessionTopbar;
	private User sessionUser;
	private Locale sessionLocale;
	private String sessionTabmenu;
	private String sessionMainmenu;
	private String sessionSubmenus;
	private String sessionTemplate;
	private int maxInactiveInterval = FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMaxInactiveInterval();
	

	private boolean formSavable ;
	private String formSaveButton;
	private String formCancelButton;
	
	private static int messagesViewType ; 
	
	@EJB
	MessageService messageService;
	
	@EJB
	private NGUserDAO nGUserDAO;
	
	@EJB
	private EntityAttributeDAO attDAO;

	/*
	 * This init method is called after the bean construction. Place here the
	 * operations that need to be perfomed afer bean initialization process.
	 * Load the authenticated user's preferences
	 */
	@PostConstruct
	public void init() {
		setFormSavable(false);
		loadSessionUser();
		loadSessionLocale();
		loadSessionTheme();
		loadSessionMenus();
		setSessionTemplate(NGViewService.DEFAULT_TEMPLATE);
		setMessagesViewType(MessageService.VIEW_MESSAGES_IN_DIALOG);

	}

	public int getLatitudeID(){
		return attDAO.getAttributeId("Date_Of_Inspection", Engine.class);
	}
	

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSessionTheme() {
		return sessionTheme;
	}

	public void setSessionTheme(String sessionTheme) {
		this.sessionTheme = sessionTheme;
	}

	public String getSessionTopbar() {
		return sessionTopbar;
	}

	public User getSessionUser() {
		return sessionUser;
	}

	public Locale getSessionLocale() {
		return sessionLocale;
	}

	public void setSessionTopbar(String sessionTopbar) {
		this.sessionTopbar = sessionTopbar;
	}

	public void setSessionUser(User sessionUser) {
		this.sessionUser = sessionUser;
	}

	public void setSessionLocale(Locale sessionLocale) {
		this.sessionLocale = sessionLocale;
	}

	public String getSessionTabmenu() {
		return sessionTabmenu;
	}

	public String getSessionMainmenu() {
		return sessionMainmenu;
	}

	public String getSessionSubmenus() {
		return sessionSubmenus;
	}

	public void setSessionTabmenu(String sessionTabmenu) {
		this.sessionTabmenu = sessionTabmenu;
	}

	public void setSessionMainmenu(String sessionMainmenu) {
		this.sessionMainmenu = sessionMainmenu;
	}

	public void setSessionSubmenus(String sessionSubmenus) {
		this.sessionSubmenus = sessionSubmenus;
	}

	public String getSessionTemplate() {
		return sessionTemplate;
	}

	public void setSessionTemplate(String sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	}

	public String getSessionTimeout() {
		return NGViewService.SESSION_TIMEOUT;
	}
	
	
	/*
	 * Set the session locale to new locale built from String lang parametre If
	 * the given lang parametre is not a valid language one, the locale will not
	 * change
	 */
	public void changeLocale(String lang) {
		sessionLocale = new Locale(lang);
		if (sessionLocale != null) {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(sessionLocale);
		} else {
			sessionLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		}
	}

	/*
	 * Changed session topbar Color and persist it as user's profile color
	 */
	public void changeTheme(String themeColor) {
		setSessionTopbar(themeColor + "Topbar");
		setSessionTheme(themeColor.toLowerCase() + "-theme.css");
		persistTopbarColor(sessionUser.getLogin(), themeColor);
	}

	public void loadSessionUser() {
		this.sessionUser = new User();
	}

	public void loadSessionMenus() {
		setSessionTabmenu(NGViewService.MENU_TAB_MENU);
		setSessionMainmenu(NGViewService.MENU_MAIN_MENU);
		setSessionSubmenus(NGViewService.MENU_SUB_MENUS);

	}

	public void loadSessionLocale() {
		sessionLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	public void loadSessionTheme() {
		setSessionTopbar(DEFAULT_THEME_COLOR + "Topbar");
		setSessionTheme(DEFAULT_THEME_COLOR.toLowerCase() + "-theme.css");
	}

	public void persistTopbarColor(String userId, String color) {

	}

	public void persistLocale(String lang) {

	}

	public void login() {
		/*
		 * get the current user
		 */
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		Subject currentUser = SecurityUtils.getSubject();

		try {
			/* authenticate the current user */
			currentUser.login(token);

			/* load the current user into session context */
			setSessionUser(nGUserDAO.findById(userName));

			/* load the user's default page */

			loadDefaultView();

		} catch (UnknownAccountException uae) {
			messageService.errorMessage("loginUnknownLogin");
		} catch (IncorrectCredentialsException ice) {
			messageService.errorMessage("loginIncorrectPassword");
		} catch (LockedAccountException lae) {
			messageService.errorMessage("loginLockedAccount");
		} catch (ExcessiveAttemptsException eae) {
			messageService.errorMessage("loginExcessiveAttempt");
		} catch (AuthenticationException ae) {
			messageService.errorMessage("loginUnableToConnect");
		} catch (Exception e) {
			messageService.errorMessage("loginUnableToLoadPage");
		}

	}

	/*
	 * Log out and close session
	 */
	public void logout() throws IOException {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		redirectToView(NGViewService.VIEW_LOGOUT);
	}

	/*
	 * Handle main view cotent
	 */

	
	
	/*
	 * Parametre
	 */
	
	public void loadViewParametres() {
		redirectToView(NGViewService.VIEW_PARAMETRE);
	}

	
	/*
	 * Roles and users VIEW_RESPONSIBLE_ENTITIES  VIEW_ROLES
	 */
	
	public void loadViewResponsibleEntities() {
		redirectToView(NGViewService.VIEW_RESPONSIBLE_ENTITIES);
	}
	
	public void loadViewResponsibleEntityUpdate() {
		redirectToView(NGViewService.VIEW_RESPONSIBLE_ENTITY_UPDATE);
	}
	
	public void loadViewResponsibleEntityCreate() {
		redirectToView(NGViewService.VIEW_RESPONSIBLE_ENTITY_CREATE);
	}
	
	
	/*
	 * VIEW_NOTIFICATIONS
	 */
	
	public void loadViewNewNotifications() {
		
		redirectToView(NGViewService.VIEW_NOTIFICATIONS_NEW);
	}
	
	public void loadViewNewNotificationsInitial() {
		setFormSavable(false);
		redirectToView(NGViewService.VIEW_NOTIFICATIONS_INITIAL);
	}
	
	public String getViewNewNotifications() {
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
		
		return contextPath + NGViewService.VIEW_NOTIFICATIONS_NEW;
	}
	
	public void loadViewInvestigatedNotifications() {
		redirectToView(NGViewService.VIEW_NOTIFICATIONS_INVESTIGATED);
	}
	
	public void loadViewFiledNotifications() {
		redirectToView(NGViewService.VIEW_NOTIFICATIONS_FILED);
	}
	
	public void loadViewNotificationsAccordion() {
		redirectToView(NGViewService.VIEW_NOTIFICATIONS_ACCORDION);
	}
	
	public void loadViewNotificationUpdate() {
		redirectToView(NGViewService.VIEW_NOTIFICATION_UPDATE);
	}
	
	public void loadViewNotificationInitialUpdate() {
		redirectToView(NGViewService.VIEW_NOTIFICATION_INITIAL_UPDATE);
	}
	
	public void loadViewNotificationInitialAnalyse() {
		redirectToView(NGViewService.VIEW_NOTIFICATION_INITIAL_ANALYSE);
	}
	
	public void loadViewUpdateAnalyse() {
		redirectToView(NGViewService.VIEW_NOTIFICATION_UPDATE_ANALYSE);
	}
	
	
	public void loadViewNotificationCreate() {
		redirectToView(NGViewService.VIEW_NOTIFICATION_CREATE);
	}
	
	public void loadViewNotificationInitialCreate() {
		setFormSavable(true);
		setFormSaveButton("createNotificationValidate");
		setFormCancelButton("createNotificationCancel");
		redirectToView(NGViewService.VIEW_NOTIFICATION_INITIAL_CREATE);
	}
	
	

	public void loadViewRoles() {
		redirectToView(NGViewService.VIEW_ROLES);
	}

	public void loadViewRoleUpdate() {
		redirectToView(NGViewService.VIEW_ROLE_UPDATE);
	}

	public void loadViewRoleCreate() {
		redirectToView(NGViewService.VIEW_ROLE_CREATE);
	}

	public void loadViewUsers() {
		redirectToView(NGViewService.VIEW_USERS);
	}

	public void loadViewUserUpdate() {
		redirectToView(NGViewService.VIEW_USER_UPDATE);
	}

	public void loadViewUserCreate() {
		redirectToView(NGViewService.VIEW_USER_CREATE);
	}
	
	public void loadViewChangePasswd() {
		redirectToView(NGViewService.VIEW_CHANGE_PASSWD);
	}

	/*
	 * Permis
	 */

	

	public void loadDefaultView() {
		redirectToView(NGViewService.getDefaultView());
	}

	public void refreshMainView() throws IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
		context.redirect(contextPath + NGViewService.VIEW_MAIN);
	}

	public void redirectToView(String view) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
		try {
			context.redirect(contextPath + view);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void forwardToView(String view) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
		try {
			context.dispatch(contextPath + view);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isFormSavable() {
		return formSavable;
	}
	
	public boolean isNoFormSavable() {
		return !formSavable;
	}

	public void setFormSavable(boolean formSavable) {
		this.formSavable = formSavable;
	}

	public String getFormSaveButton() {
		return formSaveButton;
	}

	public void setFormSaveButton(String formSaveButton) {
		this.formSaveButton = formSaveButton;
	}

	public String getFormCancelButton() {
		return formCancelButton;
	}

	public void setFormCancelButton(String formCancelButton) {
		this.formCancelButton = formCancelButton;
	}

	public static int getMessagesViewType() {
		return messagesViewType;
	}

	public static void setMessagesViewType(int messagesViewType) {
		SessionBean.messagesViewType = messagesViewType;
	}

}
