package brasajava.person.domain.utils;

import java.util.List;
import java.util.UUID;

import brasajava.person.domain.comun.Identifiable;

public class IdentifiableUtils {

	public static <T> T identify(Class<T> clazz,Identifiable entity) {
		if(entity.getId() == null) {
			entity.setId(UUID.randomUUID().toString());
		}
		return clazz.cast(entity);
	}
	
	public static void identify(List<Identifiable> entities) {
		entities.forEach(e -> {
			if(e.getId() == null) {
				e.setId(UUID.randomUUID().toString());
			}
		});
	}
}
