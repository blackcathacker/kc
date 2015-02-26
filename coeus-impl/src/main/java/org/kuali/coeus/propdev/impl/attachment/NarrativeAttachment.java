/*
 * Kuali Coeus, a comprehensive research administration system for higher education.
 * 
 * Copyright 2005-2015 Kuali, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kuali.coeus.propdev.impl.attachment;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;

import org.kuali.coeus.common.framework.attachment.KcAttachmentDataDao;
import org.kuali.coeus.common.framework.print.AttachmentDataSource;
import org.kuali.coeus.propdev.api.attachment.NarrativeAttachmentContract;
import org.kuali.coeus.sys.framework.service.KcServiceLocator;
import org.kuali.rice.krad.data.DataObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static org.kuali.coeus.dc.common.db.PreparedStatementUtils.setString;

@Entity
@Table(name = "NARRATIVE_ATTACHMENT")
@AttributeOverride(name = "data",  column = @Column(name = "NARRATIVE_DATA"))
@IdClass(Narrative.NarrativeId.class)
public class NarrativeAttachment extends AttachmentDataSource implements NarrativeAttachmentContract {

    @Id
    @OneToOne(cascade = { CascadeType.REFRESH })
    @JoinColumns({ @JoinColumn(name = "PROPOSAL_NUMBER", referencedColumnName = "PROPOSAL_NUMBER"), @JoinColumn(name = "MODULE_NUMBER", referencedColumnName = "MODULE_NUMBER") })
    private Narrative narrative;

    @Override
    public Integer getModuleNumber() {
        return this.getNarrative().getModuleNumber();
    }

    public void setModuleNumber(Integer moduleNumber) {
        this.getNarrative().setModuleNumber(moduleNumber);
    }

    @Override
    public String getProposalNumber() {
        return this.getNarrative().getProposalNumber();
    }

    public Narrative getNarrative() {
        return narrative;
    }

    public void setNarrative(Narrative narrative) {
        this.narrative = narrative;
    }

    @Override
    public byte[] getData() {
        if (super.getData() == null) {
            Map<String,String> parameters = new HashMap<String,String>();
            parameters.put("PROPOSAL_NUMBER",this.getProposalNumber());
            parameters.put("MODULE_NUMBER",this.getModuleNumber().toString());
            return getKcAttachmentDao().getData("NARRATIVE_ATTACHMENT", "NARRATIVE_DATA", parameters);

        }
        return super.getData();
    }

    @Override
    protected void postPersist() {
            Map<String,String> parameters = new HashMap<String,String>();
            parameters.put("PROPOSAL_NUMBER",this.getProposalNumber());
            parameters.put("MODULE_NUMBER",this.getModuleNumber().toString());
            getKcAttachmentDao().setData("NARRATIVE_ATTACHMENT", "NARRATIVE_DATA", parameters, this.getData());
            this.setData(null);
    }

    private KcAttachmentDataDao getKcAttachmentDao() {
        return KcServiceLocator.getService(KcAttachmentDataDao.class);
    }

}
