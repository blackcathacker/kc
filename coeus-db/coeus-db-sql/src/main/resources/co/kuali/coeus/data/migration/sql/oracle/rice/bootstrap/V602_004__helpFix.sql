--
-- Kuali Coeus, a comprehensive research administration system for higher education.
--
-- Copyright 2005-2015 Kuali, Inc.
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as
-- published by the Free Software Foundation, either version 3 of the
-- License, or (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU Affero General Public License for more details.
--
-- You should have received a copy of the GNU Affero General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/>.
--

INSERT INTO KRCR_PARM_T (nmspc_cd, cmpnt_cd, parm_nm, OBJ_ID, VER_NBR, parm_typ_cd, val, parm_desc_txt, eval_oprtr_cd, appl_id)
VALUES ('KC-IACUC','Document','iacucProtocolCustomDataHelp',SYS_GUID(),1,'HELP','default.htm?turl=Documents/customdata5.htm','IACUC protocol custom data help','A','KC')
/
UPDATE KRCR_PARM_T set VAL = 'default.htm?turl=Documents/disclosures.htm' where PARM_NM = 'coiDisclosure1Help'
/
UPDATE KRCR_PARM_T set VAL = 'default.htm?turl=Document/contactinformation.htm' where PARM_NM = 'disclosureReporterhelp'
/

