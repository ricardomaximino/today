package brasajava.person.message.event;

import brasajava.person.domain.entity.PersonalDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDocumentCreateEvent implements Event{
	private String id;
	private String personId;
	private PersonalDocument document;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

}
