package brasajava.person.domain.utils;

import java.time.LocalDateTime;
import java.util.List;

import brasajava.person.domain.comun.Auditable;

public class AuditableUtils {
	public static <T> T onCreation(Class<T> clazz, Auditable entity, String user) {
		LocalDateTime dateTime = LocalDateTime.now();
		entity.getAuditInfo().setCreationDateTime(dateTime);
		entity.getAuditInfo().setCreationUser(user);
		entity.getAuditInfo().setLastUpdateDateTime(dateTime);
		entity.getAuditInfo().setLastUpdateUser(user);
		return clazz.cast(entity);
	}
	
	public static void onCreation(List<Auditable> entities, String user) {
		LocalDateTime dateTime = LocalDateTime.now();
		entities.forEach(e -> {
			e.getAuditInfo().setCreationDateTime(dateTime);
			e.getAuditInfo().setCreationUser(user);
			e.getAuditInfo().setLastUpdateDateTime(dateTime);
			e.getAuditInfo().setLastUpdateUser(user);
		});
	}

	public static <T> T onUpdate(Class<T> clazz, Auditable entity, String user) {
		LocalDateTime dateTime = LocalDateTime.now();
		if(entity.getAuditInfo().getCreationDateTime() == null) {
			entity.getAuditInfo().setCreationDateTime(dateTime);
			entity.getAuditInfo().setCreationUser(user);
		}
		entity.getAuditInfo().setLastUpdateDateTime(dateTime);
		entity.getAuditInfo().setLastUpdateUser(user);
		return clazz.cast(entity);
	}
	
	public static void onUpdate(List<Auditable> entities, String user) {
		LocalDateTime dateTime = LocalDateTime.now();
		entities.forEach(e -> {
			if(e.getAuditInfo().getCreationDateTime() == null) {
				e.getAuditInfo().setCreationDateTime(dateTime);
				e.getAuditInfo().setCreationUser(user);
			}
			e.getAuditInfo().setLastUpdateDateTime(dateTime);
			e.getAuditInfo().setLastUpdateUser(user);
		});
	}
}
