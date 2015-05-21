package org.appfuse.dao.hps;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.model.HpsDict;
import com.my.hps.webapp.model.HpsDictItem;

public interface HpsDictDao extends GenericDao<HpsDict, Long>{

	List<HpsDictItem> getDictItems(String dictCode);
}
