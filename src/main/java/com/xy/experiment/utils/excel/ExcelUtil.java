package com.xy.experiment.utils.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel处理工具类
 *
 */
public class ExcelUtil {

	private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	public static byte[] createWorkBook(String filename, String[] title, String[] key, List<Map<String, String>> values)throws Exception{ 
		   
	       
		   logger.info("excel文件[{}]开始生成****************************************", filename);
		   int maxLine = 1048576;
		   FileOutputStream outputStream=null;
		   try {
			   //rowAccessWindowSize 内存中缓存记录数
			   SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(2000);
			   Sheet sxssfSheet =sxssfWorkbook.createSheet();
			if (sxssfSheet == null) {
				logger.error("未找到对应sheet页!");
	            throw new Exception("未找到对应sheet页!");
			}
			//标题
			Row sxssfTitleRow = sxssfSheet.createRow(0);
			int celCount=0;
			Cell cell0=null;
			Map<String, String> cellMap=null;
			Row sxssfRow=null;
			Cell cell1=null;
			for(String cel:title){
	            cell0 = sxssfTitleRow.createCell(celCount);
	            cell0.setCellValue(cel);
	            celCount++;
			}
			if(values!=null&&values.size()>0&&values.size()<maxLine){
				for(int i=0;i<values.size();i++){
					cellMap=values.get(i);
					sxssfRow = sxssfSheet.createRow(i+1);
					if(key.length>0){
						for(int j=0;j<key.length;j++){
							cell1 = sxssfRow.createCell(j);
				            cell1.setCellValue(cellMap.get(key[j])==null?"":""+cellMap.get(key[j]));
						}
					}
				}
			}
			ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
			sxssfWorkbook.write(byteStream);
			logger.info("excel文件[{}]生成完毕***************************************************", filename);
			return byteStream.toByteArray();
			
		 } catch (Exception e){
			logger.info("excel文件[" + filename + "]生成异常, 原因:", e);
			throw new Exception("excel文件[" + filename + "]生成异常");
		}finally{
			IOUtils.closeQuietly(outputStream);
		}
		   
	}

    public static List<List<String>> readXlsx(InputStream inputStream,String fileName) throws Exception {


        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(inputStream);
        } else {
            wb = new XSSFWorkbook(inputStream);
        }

        List<List<String>> result = new ArrayList<List<String>>();
        // 循环当前页，并处理当前循环页
        Sheet xssfSheet = wb.getSheetAt(0);
        // 循环每一页，并处理当前循环页
        int rows = xssfSheet.getLastRowNum();
        for (int rowNum = 1; rowNum <= rows; rowNum++) {
            Row xssfRow = xssfSheet.getRow(rowNum);
            if(xssfRow == null) {
            	continue;
            }
            Cell cell1 = xssfRow.getCell(0);
            String value1  = DoubleUtils.getRealStringValue(cell1);
            if (StringUtils.isBlank(value1)) {
                continue;
            }
            int minColIx = xssfRow.getFirstCellNum();
            int maxColIx = xssfRow.getLastCellNum();
            List<String> rowList = new ArrayList<String>();
            for (int colIx = minColIx; colIx < maxColIx; colIx++) {
            	//xssfRow.getCell(colIx).setCellType(CellType.STRING);
                Cell cell = xssfRow.getCell(colIx);
                String value  = DoubleUtils.getRealStringValue(cell);
                /*if (StringUtils.isBlank(value)) {
                    break;
                }*/
                rowList.add(value);
            }
            result.add(rowList);
        }
        return result;
    }
    
    public static List<List<String>> readXlsxWithHead(InputStream inputStream,String fileName) throws Exception {
    	boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        Workbook xssfWorkbook = null;
        if (isExcel2003) {
        	xssfWorkbook = new HSSFWorkbook(inputStream);
        } else {
        	xssfWorkbook = new XSSFWorkbook(inputStream);
        }
    	
        //XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

        List<List<String>> result = new ArrayList<List<String>>();
        // 循环当前页，并处理当前循环页
        Sheet xssfSheet = xssfWorkbook.getSheetAt(0);
        // 循环每一页，并处理当前循环页
        int rows = xssfSheet.getLastRowNum();
        for (int rowNum = 0; rowNum <= rows; rowNum++) {
        	Row row = xssfSheet.getRow(rowNum);
            if(row == null) {
            	continue;
            }
            Cell cell1 = row.getCell(0);
            String value1  = DoubleUtils.getRealStringValue(cell1);
            if (StringUtils.isBlank(value1)) {
                break;
            }
            int minColIx = row.getFirstCellNum();
            int maxColIx = row.getLastCellNum();
            List<String> rowList = new ArrayList<String>();
            for (int colIx = minColIx; colIx < maxColIx; colIx++) {
//                row.getCell(colIx).setCellType(CellType.STRING);
                Cell cell = row.getCell(colIx);
                String value;
                if (cell.getCellTypeEnum() != CellType.STRING && HSSFDateUtil.isCellDateFormatted(cell))
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    value = sdf.format(date);
//                    System.out.println(value);
                } else {
                    value = DoubleUtils.getRealStringValue(cell);
                }
                if (StringUtils.isBlank(value)) {
                    break;
                }
                rowList.add(value);
            }
            result.add(rowList);
        }
        return result;
    }
    
    public static List<String> readExcelHead(InputStream inputStream) throws Exception {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
        List<String> rowList = new ArrayList<String>();
        // 循环当前页，并处理当前循环页
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        // 循环每一页，并处理当前循环页
        XSSFRow xssfRow = xssfSheet.getRow(0);
        if(xssfRow == null) {
        	return rowList;
        }
        XSSFCell cell1 = xssfRow.getCell(0);
        String value1  = DoubleUtils.getRealStringValue(cell1);
        if (StringUtils.isBlank(value1)) {
        	return rowList;
        }
        int minColIx = xssfRow.getFirstCellNum();
        int maxColIx = xssfRow.getLastCellNum();
        for (int colIx = minColIx; colIx < maxColIx; colIx++) {
            XSSFCell cell = xssfRow.getCell(colIx);
            String value  = DoubleUtils.getRealStringValue(cell);
            rowList.add(value);
        }
        return rowList;
    }
}
