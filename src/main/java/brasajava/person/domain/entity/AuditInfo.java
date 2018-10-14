package brasajava.person.domain.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditInfo {

	private LocalDateTime creationDateTime;
	private String creationUser;
	private LocalDateTime lastUpdateDateTime;
	private String lastUpdateUser;
}
