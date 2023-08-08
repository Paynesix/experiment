package com.xy.experiment.utils.shunde;

import com.xy.experiment.utils.excel.ExcelData;
import com.xy.experiment.utils.excel.ExcelUtil;
import com.xy.experiment.utils.excel.ExportExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MobileDesTest {

    private final static Logger logger = LoggerFactory.getLogger(MobileDesTest.class);

    public static void main(String[] args) throws Exception {
        String excelUrl = "D:\\company\\work\\SmartManagement\\20230705-顺德手机号查询\\顺德注册用户数据0704 - 副本.xlsx";
        String saveName = "顺德注册用户数据0704 - 解密";
        String saveUrl = "D:\\company\\work\\SmartManagement\\20230705-顺德手机号查询\\顺德注册用户数据0704 - 解密.xlsx";
        String key = "16d7f78b9aec40589eaebe5eed824f60";
        InputStream stream = new FileInputStream(excelUrl);
        List<List<String>> lists = ExcelUtil.readXlsxWithHead(stream, excelUrl);
        System.out.println("============>read:" + lists.size());
        int i = 0;
        for(List<String> list : lists){
           i++;
           if(i == 1) continue;
           // 解密
            String decodeMobile = AES.decodeFromBase64(list.get(0), key);
            list.add(decodeMobile);
        }
        System.out.println("============>des:" + i);

        // 保存
        ExcelData data = new ExcelData();
        List<String> titles = new ArrayList<String>();
        List<List<Object>> rows = new ArrayList<List<Object>>();
        setSheetData(titles, rows, lists);
        data.setTitles(titles);
        data.setRows(rows);
        data.setName(saveName);
        OutputStream out = new FileOutputStream(saveUrl);
        ExportExcelUtils.exportExcel(data, out);
        System.out.println("=============save");
    }

    private static void setSheetData(List<String> titles, List<List<Object>> rows, List<List<String>> dataList){
        titles.add("Fmobile");
        titles.add("Fcreate_time");
        titles.add("手机号");
        if (dataList != null){
            int i = 0;
            for (List<String> tmp : dataList) {
                if(++i == 1) continue;
                List<Object> row = new ArrayList();
                row.addAll(tmp);
                rows.add(row);
            }
        }
    }
}
