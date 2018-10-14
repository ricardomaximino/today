package brasajava.person.domain.comun;

import brasajava.person.domain.entity.AuditInfo;

public interface Auditable {

	void setAuditInfo(AuditInfo auditInfo);
	AuditInfo getAuditInfo();

}
