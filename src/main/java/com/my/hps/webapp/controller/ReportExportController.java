package com.my.hps.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.dao.DBConnectionFacotry;
import org.appfuse.service.hps.HpsHeatingMaintain2015ChargeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.ElectricTongjiQueryParam;
import com.my.hps.webapp.controller.queryparam.HeatingChargeQueryParam;
import com.my.hps.webapp.controller.queryparam.HeatingMaintainCharge2015QueryParam;
import com.my.hps.webapp.controller.queryparam.HeatingMaintainTongjiQueryParam;
import com.my.hps.webapp.controller.queryparam.QunuanfeiTongjiQueryParam;
import com.my.hps.webapp.controller.queryparam.WeixiufeiTongjiQueryParam;
import com.my.hps.webapp.model.HpsHeatingMaintainPaymentDate2015;
import com.my.hps.webapp.model.enums.ChargeStateEnum;

/**
 * 
 * 取暖费、房屋维修费、电费缴费记录的报表导出
 * 
 * @author root
 *
 */
@Controller
@RequestMapping("/report/")
public class ReportExportController extends BaseFormController {
    
    private static final String REPORT_CONFIG_FILE_CATEGORY_HEATING = "/../reports/heating_charge_record_final";
    private static final String REPORT_CONFIG_FILE_CATEGORY_ELECTRIC = "/../reports/electric_charge_record_final";
    private static final String REPORT_CONFIG_FILE_CATEGORY_MAINTAIN = "/../reports/maintain_charge_record_final";
    
    private static final String REPORT_CONFIG_FILE_CATEGORY_HEATING_ALL = "/../reports/heating_charge_record_all";
    private static final String REPORT_CONFIG_FILE_HEATING_MAINTAIN = "/../reports/heating_maintain_charge_record_all";
    
    private HpsHeatingMaintain2015ChargeManager manager;

    @Autowired
    public void setWHeatingMaintainManager(HpsHeatingMaintain2015ChargeManager manager) {
        this.manager = manager;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "heatingmaintainall")
    @ResponseBody
    public void exportHeatingMaintainChargeAllRecords(@ModelAttribute HeatingMaintainCharge2015QueryParam param, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        String baseCode = param.getBaseCode();
        List<HpsHeatingMaintainPaymentDate2015> allPaymentDates = manager.getPaymentDates();
        HpsHeatingMaintainPaymentDate2015 currentPaymentDate = null;
        for (HpsHeatingMaintainPaymentDate2015 paymentDate : allPaymentDates) {
            if (paymentDate.getBase().getCode().equals(baseCode)) {
                currentPaymentDate = paymentDate;
                break;
            }
        }
        
        // 缴费年份
        params.put("paymentdate_id", currentPaymentDate.getId());
        // 缴费日期
        String chargeDate = param.getChargeDate();
        if (StringUtils.isNotEmpty(chargeDate)) {
            params.put("charge_date", chargeDate);
        }
        // 收费员
        String operName = param.getOperName();
        if (StringUtils.isNotEmpty(operName)) {
            params.put("oper_name", operName);
        }
        String areaCode = param.getAreaCode();
        if (StringUtils.isNotEmpty(areaCode)) {
            // 区域有选择
            params.put("area_code", areaCode);
        }
        String ownerName = param.getOwnerName();
        if (StringUtils.isNotEmpty(ownerName)) {
            params.put("owner_name", ownerName);
        }
        String houseNo = param.getHouseNo();
        if (StringUtils.isNotEmpty(houseNo)) {
            params.put("house_no", houseNo);
        }
        String louzuoCode = param.getLouzuoCode();
        if (StringUtils.isNotEmpty(louzuoCode)) {
            params.put("louzuo", louzuoCode);
        }
        String danyuan = param.getDanyuan();
        if (StringUtils.isNotEmpty(danyuan)) {
            params.put("danyuan", danyuan);
        }
        String ceng = param.getCeng();
        if (StringUtils.isNotEmpty(ceng)) {
            params.put("ceng", ceng);
        }
        String remark = param.getRecordRemarks();
        if (StringUtils.isNotEmpty(remark)) {
            params.put("charge_record_remark", remark);
        }
        String wageNum = param.getWageNum();
        if (StringUtils.isNotEmpty(wageNum)) {
            params.put("wage_num", wageNum);
        }
        String chargeState = param.getChargeState();
        if (StringUtils.isNotEmpty(chargeState)) {
            int chargeStateInt = ChargeStateEnum.valueOf(chargeState).ordinal();
            params.put("charge_state", chargeStateInt + "");
        }
        File reportFile = null;
        try {
            reportFile = exportReportFile(REPORT_CONFIG_FILE_HEATING_MAINTAIN, params);
            downloadFileToResponce(reportFile, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reportFile != null)
                reportFile.delete();
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "heatingall")
    @ResponseBody
    public void exportHeatingChargeAllRecords(@ModelAttribute HeatingChargeQueryParam param, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        // 缴费年份
        params.put("paymentdate_id", param.getPaymentDateId());
        // 缴费日期
        String chargeDate = param.getChargeDate();
        if (StringUtils.isNotEmpty(chargeDate)) {
            params.put("charge_date", chargeDate);
        }
        // 收费员
        String operName = param.getOperName();
        if (StringUtils.isNotEmpty(operName)) {
            params.put("oper_name", operName);
        }
        String areaCode = param.getAreaCode();
        if (StringUtils.isNotEmpty(areaCode)) {
            // 区域有选择
            params.put("area_code", areaCode);
        }
        String ownerName = param.getOwnerName();
        if (StringUtils.isNotEmpty(ownerName)) {
            params.put("owner_name", ownerName);
        }
        String houseNo = param.getHouseNo();
        if (StringUtils.isNotEmpty(houseNo)) {
            params.put("house_no", houseNo);
        }
        String louzuoCode = param.getLouzuoCode();
        if (StringUtils.isNotEmpty(louzuoCode)) {
            params.put("louzuo", louzuoCode);
        }
        String danyuan = param.getDanyuan();
        if (StringUtils.isNotEmpty(danyuan)) {
            params.put("danyuan", danyuan);
        }
        String ceng = param.getCeng();
        if (StringUtils.isNotEmpty(ceng)) {
            params.put("ceng", ceng);
        }
        String shenfenxingzhi = param.getShenfenXingzhiCode();
        if (StringUtils.isNotEmpty(shenfenxingzhi)) {
            params.put("shenfenxingzhi", shenfenxingzhi);
        }
        String yongfangxingzhi = param.getYongfangXingzhiCode();
        if (StringUtils.isNotEmpty(yongfangxingzhi)) {
            params.put("yongfangxingzhi", yongfangxingzhi);
        }
        String remark = param.getRecordRemarks();
        if (StringUtils.isNotEmpty(remark)) {
            params.put("charge_record_remark", remark);
        }
        String wageNum = param.getWageNum();
        if (StringUtils.isNotEmpty(wageNum)) {
            params.put("wage_num", wageNum);
        }
        boolean diverted = param.isDiverted();
        if (diverted) {
            params.put("diverted", "1");
        }
        boolean stopped = param.isStopped();
        if (stopped) {
            params.put("stopped", "1");
        }
        String chargeState = param.getChargeState();
        if (StringUtils.isNotEmpty(chargeState)) {
            int chargeStateInt = ChargeStateEnum.valueOf(chargeState).ordinal();
            params.put("charge_state", chargeStateInt + "");
        }
        boolean livingSohard = param.isLivingSohard();
        if (livingSohard) {
            params.put("living_so_hard", "1");
        }
        if (diverted || stopped || livingSohard) {
            params.put("diverted_sohard_stopped_all", "0");
        } else {
            params.put("diverted_sohard_stopped_all", "1");
        }
        File reportFile = null;
        try {
            reportFile = exportReportFile(REPORT_CONFIG_FILE_CATEGORY_HEATING_ALL, params);
            downloadFileToResponce(reportFile, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reportFile != null)
                reportFile.delete();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "heating")
    @ResponseBody
    public void exportHeatingChargeRecords(@ModelAttribute QunuanfeiTongjiQueryParam param, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        // 缴费年份
        params.put("paymentdate_id", param.getPaymentDateId());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 缴费开始日期
        Date startDate = param.getStarDate();
        if (startDate != null) {
            params.put("charge_start_date", dateFormat.format(startDate));
        }
        // 缴费结束日期
        Date endDate = param.getEndDate();
        if (endDate != null) {
            params.put("charge_end_date", dateFormat.format(endDate));
        }
        // 收费员
        Long operId = param.getOperUserId();
        if (operId != null) {
            params.put("oper_id", operId);
        }
        File reportFile = null;
        try {
            reportFile = exportReportFile(REPORT_CONFIG_FILE_CATEGORY_HEATING, params);
            downloadFileToResponce(reportFile, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reportFile != null)
                reportFile.delete();
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "electric")
    @ResponseBody
    public void exportEletricChargeRecords(@ModelAttribute ElectricTongjiQueryParam param, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        String baseCode = param.getBaseCode();
        params.put("base_code", baseCode);
        String areaCode = param.getAreaCode();
        if (StringUtils.isNotEmpty(areaCode)) {
            params.put("area_code", areaCode);
        }
        String startChargeTime = param.getStartChargeTime();
        if (StringUtils.isNotEmpty(startChargeTime)) {
            params.put("charge_start_date", startChargeTime);
        }
        String endChargeTime = param.getEndChargeTime();
        if (StringUtils.isNotEmpty(endChargeTime)) {
            params.put("charge_end_date", endChargeTime);
        }
        Long operId = param.getOperUserId();
        if (operId != null) {
            params.put("oper_id", operId);
        }
        File reportFile = null;
        try {
            reportFile = exportReportFile(REPORT_CONFIG_FILE_CATEGORY_ELECTRIC, params);
            downloadFileToResponce(reportFile, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reportFile != null)
                reportFile.delete();
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "heatingmaintaintongji")
    @ResponseBody
    public void exportHeatingMaintainChargeRecords(@ModelAttribute HeatingMaintainTongjiQueryParam param, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        String baseCode = param.getBaseCode();
        List<HpsHeatingMaintainPaymentDate2015> allPaymentDates = manager.getPaymentDates();
        HpsHeatingMaintainPaymentDate2015 currentPaymentDate = null;
        for (HpsHeatingMaintainPaymentDate2015 paymentDate : allPaymentDates) {
            if (paymentDate.getBase().getCode().equals(baseCode)) {
                currentPaymentDate = paymentDate;
                break;
            }
        }
        
        // 缴费年份
        params.put("paymentdate_id", currentPaymentDate.getId());
        String areaCode = param.getAreaCode();
        if (StringUtils.isNotEmpty(areaCode)) {
            params.put("area_code", areaCode);
        }
        String startChargeTime = param.getStartChargeTime();
        if (StringUtils.isNotEmpty(startChargeTime)) {
            params.put("charge_start_date", startChargeTime);
        }
        String endChargeTime = param.getEndChargeTime();
        if (StringUtils.isNotEmpty(endChargeTime)) {
            params.put("charge_end_date", endChargeTime);
        }
        Long operId = param.getOperUserId();
        if (operId != null) {
            params.put("oper_id", operId);
        }
        File reportFile = null;
        try {
            reportFile = exportReportFile(REPORT_CONFIG_FILE_HEATING_MAINTAIN, params);
            downloadFileToResponce(reportFile, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reportFile != null)
                reportFile.delete();
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "maintain")
    @ResponseBody
    public void exportMaintainChargeRecords(@ModelAttribute WeixiufeiTongjiQueryParam param, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        // 缴费年份
        params.put("paymentdate_id", param.getPaymentDateId());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 缴费开始日期
        Date startDate = param.getStarDate();
        if (startDate != null) {
            params.put("charge_start_date", dateFormat.format(startDate));
        }
        // 缴费结束日期
        Date endDate = param.getEndDate();
        if (endDate != null) {
            params.put("charge_end_date", dateFormat.format(endDate));
        }
        // 收费员
        Long operId = param.getOperUserId();
        if (operId != null) {
            params.put("oper_id", operId);
        }
        File reportFile = null;
        try {
            reportFile = exportReportFile(REPORT_CONFIG_FILE_CATEGORY_MAINTAIN, params);
            downloadFileToResponce(reportFile, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reportFile != null)
                reportFile.delete();
        }
    }
    
    private void downloadFileToResponce(File downloadFile, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + downloadFile.getName());
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(downloadFile);
            outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, length);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
    
    private File exportReportFile(String reportCategory, Map<String, Object> params) throws Exception {
        Connection hpsDBConnection = null;
        try {
            hpsDBConnection = DBConnectionFacotry.INSTANCE.getDBConnection();
            String fileName = ReportExportController.class.getResource(reportCategory + ".jasper").getFile();
            String jrprintFileName = JasperFillManager.fillReportToFile(fileName, params, hpsDBConnection);
            File sourceFile = new File(jrprintFileName);
            JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String suffix = format.format(new Date());
            File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + "_" + suffix + ".xlsx");
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            return destFile;
        } finally {
            if (hpsDBConnection != null) {
                hpsDBConnection.close();
            }
        }
    }
}
