package org.appfuse.dao.hps;

import java.util.Date;
import java.util.List;

import org.appfuse.dao.GenericDao;

import com.my.hps.webapp.controller.queryparam.ElectricChaobiaoQueryParam;
import com.my.hps.webapp.controller.vo.ElectricChaobiaoPaginationResult;
import com.my.hps.webapp.model.HpsElectricPaymentDate;
import com.my.hps.webapp.model.HpsElectricChaobiao;
import com.my.hps.webapp.model.HpsElectricUnit;
import com.my.hps.webapp.model.PaginationResult;

public interface HpsElectricChaobiaoDao extends GenericDao<HpsElectricChaobiao, Long> {
	
	List updateElectricChaobiao(List<HpsElectricChaobiao> electricityChaobiao);
	
	HpsElectricPaymentDate getElectricPaymentDate(Long paymentDateId);
	
	/**
	 * 获取基地的电费缴纳日期的配置
	 * 
	 * @param baseId
	 * @param month 月份字符串，yyyy-MM格式
	 * @return
	 */
	HpsElectricPaymentDate getPaymentDate(Long baseId, String month);
	
	ElectricChaobiaoPaginationResult getChaobiaos(ElectricChaobiaoQueryParam param);
	
	List<HpsElectricPaymentDate> getPaymentDates(String baseCode);
	List<HpsElectricChaobiao> getChaobiaos(Long paymentDateId);
	
	List<HpsElectricUnit> getElectricUnits(String baseCode);

    List<HpsElectricChaobiao> getPreviousInputedChaobiaos(Long paymentdateId, List<Long> houseIds);
}
