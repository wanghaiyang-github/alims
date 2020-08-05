package com.bazl.alims.utils;

import com.bazl.alims.common.Constants;
import com.bazl.alims.controller.BaseController;
import com.bazl.alims.model.po.FugitivesInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author wanghaiyang
 * @date 2020/7/6.
 */
@Service
public class DownloadFileUtils extends BaseController {

    /**
     * 导入在逃人员
     * @param request
     * @param file
     */
    public List<FugitivesInfo> importFugitivesInfo(HttpServletRequest request, MultipartFile file) {
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<FugitivesInfo> fugitivesInfoList = null;
        String cellData = null;
        try {
            wb = readExcel(file);
            if(wb != null){
                //用来存放表中数据
                fugitivesInfoList = new ArrayList<>();
                //获取第一个sheet
                sheet = wb.getSheetAt(0);
                //第一行是列名，所以不读
                int firstRowIndex = sheet.getFirstRowNum()+1;
                int lastRowIndex = sheet.getLastRowNum();
                //遍历行
                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                    System.out.println("rIndex: " + rIndex);
                    row = sheet.getRow(rIndex);
                    //当前这行全部为空或者空格就忽视当前这行
                    if (isAllRowEmpty(row,sheet.getRow(rIndex))) {
                        continue;
                    }
                    if (row != null) {
                        FugitivesInfo fugitivesInfo = new FugitivesInfo();
                        //在逃人员类型
                        fugitivesInfo.setPersonType(Constants.PERSON_TYPE_07);
                        //人员姓名
                        cellData = (String) getCellFormatValue(row.getCell(0));
                        fugitivesInfo.setPersonName(cellData);
                        //性别
                        cellData = (String) getCellFormatValue(row.getCell(1));
                        //转换性别
                        if (Constants.GENDER_MAN.equals(cellData)) {
                            cellData = Constants.GENDER_01;
                        }else if (Constants.GENDER_WOMAN.equals(cellData)){
                            cellData = Constants.GENDER_02;
                        }else {
                            cellData = Constants.GENDER_03;
                        }
                        fugitivesInfo.setPersonGender(cellData);
                        //年龄
                        cellData = (String) getCellFormatValue(row.getCell(2));
                        fugitivesInfo.setPersonAge(cellData);
                        //身份证号
                        cellData = (String) getCellFormatValue(row.getCell(3));
                        fugitivesInfo.setPersonCard(cellData);
                        //涉案编号
                        cellData = (String) getCellFormatValue(row.getCell(4));
                        fugitivesInfo.setInvolvedNo(cellData);
                        //涉案名称
                        cellData = (String) getCellFormatValue(row.getCell(5));
                        fugitivesInfo.setInvolvedName(cellData);
                        //逃犯编号
                        cellData = (String) getCellFormatValue(row.getCell(6));
                        fugitivesInfo.setFugitiveNo(cellData);
                        //创建者
                        cellData = (String) getCellFormatValue(row.getCell(7));
                        fugitivesInfo.setCreatePerson(cellData);

                        fugitivesInfo.setId(UUID.randomUUID().toString());
                        fugitivesInfo.setCreateDatetime(new Date());
                        fugitivesInfoList.add(fugitivesInfo);
                    }
                }
            }
            return fugitivesInfoList;
        } catch (Exception ex) {
            logger.error("导入Excel错误！", ex);
            return null;
        }
    }

    //读取excel
    public static Workbook readExcel(MultipartFile file){
        String filePath = file.getOriginalFilename();
        Workbook wb = null;
        if(filePath == null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = file.getInputStream();
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = DataFormat.convertDoubleToString(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }


    /**
     * 验证excel是否全部为空
     * @param row 当前行
     * @param firstRow 第一行标题行
     * @return
     */
    public static boolean isAllRowEmpty(Row row,Row firstRow) {
        int count = 0;
        //单元格数量
        int rowCount = firstRow.getLastCellNum() - firstRow.getFirstCellNum();
        //判断多少个单元格为空
        for (int c = 0; c < rowCount; c++) {
            Cell cell = row.getCell(c);
            if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK || isEmpty((cell+"").trim())){
                count += 1;
            }
        }
        if (count == rowCount) {
            return true;
        }
        return false;
    }

    /**
     * 检测字符串是否为空(null,"","null")
     * @param s
     * @return 为空则返回true，不否则返回false
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || "null".equals(s);
    }
}
