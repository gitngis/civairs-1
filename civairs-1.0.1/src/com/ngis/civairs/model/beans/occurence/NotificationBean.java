package com.ngis.civairs.model.beans.occurence;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.ngis.civairs.model.beans.SessionBean;
import com.ngis.civairs.model.entities.occurence.Identifier;
import com.ngis.civairs.model.entities.occurence.Notification;
import com.ngis.civairs.model.services.occurence.NotificationService;
import com.ngis.civairs.model.tools.HashcodeUtility;

@ManagedBean
@SessionScoped
public class NotificationBean {

	@ManagedProperty("#{notificationService}")
	private NotificationService notificationService;

	private Notification selectedNotification;

	private Notification notificationToCreate;

	private int activeNotificationIndex = 0;

	public List<Notification> getNotifications() {
		return notificationService.getNotifications();
	}

	public NotificationService getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public Notification getNotificationToCreate() {
		return notificationToCreate;
	}

	public void setNotificationToCreate(Notification notificationToCreate) {
		this.notificationToCreate = notificationToCreate;
	}

	public Notification getSelectedNotification() {
		return selectedNotification;
	}

	public void setSelectedNotification(Notification selectedNotification) {
		this.selectedNotification = selectedNotification;
	}

	public void createNotification() {
		// Set Notification Identifier

		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		SessionBean session = (SessionBean) context.getSessionMap().get("sessionBean");

		Identifier notificationIdentifier = new Identifier();
		String fileNumber = HashcodeUtility
				.hashABS31ToString("" + notificationToCreate.getUtcDate() + notificationToCreate.getUtcTime());
		notificationIdentifier.setFile_Number(fileNumber);
		notificationIdentifier.setResponsibleEntity(session.getSessionUser().getResponsibleEntity());
		notificationIdentifier.setId(session.getSessionUser().getResponsibleEntity().getId() + "-" + fileNumber);

		// Set Notification Id to Identifier's one
		notificationToCreate.setId(notificationIdentifier.getId());

		// Add notification to identifier
		notificationIdentifier.addNotification(notificationToCreate);

		// Persist identifier
		notificationService.insertNotificationByIdentifier(notificationIdentifier);

		// Back to Responsible Entities view
		backToNotificationsView();
	}

	public void updateNotification() {

		// Persist
		notificationService.updateNotification(selectedNotification);

		// Back to Responsible Entities view
		backToNotificationsView();

	}

	public void deleteNotification() {
		if (selectedNotification != null)
			notificationService.deleteNotification(selectedNotification);

	}

	public void initNotificationToCreate() {
		notificationToCreate = new Notification();

		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		SessionBean session = (SessionBean) context.getSessionMap().get("sessionBean");
		session.loadViewNotificationCreate();
	}

	public void initNotificationToUpdate() {
		if (selectedNotification != null) {

			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			SessionBean session = (SessionBean) context.getSessionMap().get("sessionBean");
			session.loadViewNotificationUpdate();

		}
		// if(selectedNotification != null) updateNotification();

	}

	public void selectNotificationToUpdate(String ID) {
		selectedNotification = null;
		for (Notification entity : getNotifications()) {
			if (entity.getId().equals(ID)) {
				selectedNotification = entity;
			}
		}
		if (selectedNotification != null) {

			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			SessionBean session = (SessionBean) context.getSessionMap().get("sessionBean");
			session.loadViewNotificationUpdate();

		}
		// if(selectedNotification != null) updateNotification();

	}

	public void selectNotificationToDelete(String ID) {
		selectedNotification = null;
		for (Notification entity : getNotifications()) {
			if (entity.getId().equals(ID)) {
				selectedNotification = entity;
			}
		}
		if (selectedNotification != null) {
			notificationService.deleteNotification(selectedNotification);
			backToNotificationsView();
		}
	}

	public void cancelNotification() {
		backToNotificationsView();
	}

	public void backToNotificationsView() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		SessionBean session = (SessionBean) context.getSessionMap().get("sessionBean");
		session.loadViewNotifications();

	}

	public int getActiveNotificationIndex() {
		return activeNotificationIndex;
	}

	public void setActiveNotificationIndex(int activeNotificationIndex) {
		this.activeNotificationIndex = activeNotificationIndex;
	}

}
