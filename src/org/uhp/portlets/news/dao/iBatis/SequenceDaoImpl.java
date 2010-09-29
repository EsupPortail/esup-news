package org.uhp.portlets.news.dao.iBatis;

/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/ 
 * Copyright (C) 2007-2008 University Nancy 1
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;


import org.uhp.portlets.news.dao.SequenceDao;
import org.uhp.portlets.news.domain.Sequence;

@Repository("sequenceDao")
public class SequenceDaoImpl extends SqlMapClientDaoSupport implements SequenceDao {
  
  public Long getNextId(String name) throws DataAccessException {
	  Sequence sequence = (Sequence) getSqlMapClientTemplate().queryForObject("getSequence", name);
    if (sequence == null) {
      throw new DataRetrievalFailureException(
					"Could not get next value of sequence '" + name + "': sequence does not exist");
    }
    Object parameterObject = new Sequence(name, sequence.getValue() + 1);
    getSqlMapClientTemplate().update("updateSequence", parameterObject, 1);
    return sequence.getValue();
  }


}