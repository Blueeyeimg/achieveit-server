package com.ecnu.achieveit.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.ecnu.achieveit.model.ProjectFunctionKey;
import com.ecnu.achieveit.model.ProjectRisk;
import com.ecnu.achieveit.model.ProjectRiskKey;
import com.ecnu.achieveit.modelview.FunctionItem;
import com.ecnu.achieveit.modelview.FunctionView;
import com.ecnu.achieveit.service.ExcelService;
import com.ecnu.achieveit.util.LogUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Override
    public boolean checkFileType(MultipartFile file) {
        if (file == null || file.isEmpty()){
            LogUtil.i("文件不能为空");
            return false;
        }
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!("xlsx".equals(suffix) || "xls".equals(suffix))){
            LogUtil.i("请上传Excel文件");
            return false;
        }
        LogUtil.i("上传文件成功");
        return true;
    }

    @Override
    public FunctionView readProjectFunction(MultipartFile file) {

        if(!checkFileType(file)){
            return null;
        }

        FunctionView functionView = new FunctionView();
        functionView.setProjectId("");

        try{
            //获取文件流
            InputStream is = file.getInputStream();

            LogUtil.i("获取文件流成功");
            //获取文件后缀
            String original = file.getOriginalFilename();
            String suffix = original.substring(original.lastIndexOf("."));

            //创建工作薄
            Workbook workbook = StringUtils.equals(".xsl", suffix) ? new HSSFWorkbook(is) : new XSSFWorkbook(is);

            LogUtil.i("创建工作薄成功");
            //获取工作薄
            Sheet sheet = workbook.getSheetAt(0);

            LogUtil.i("获取工作薄成功");
            //获取行数
            int rowSize = sheet.getPhysicalNumberOfRows();

            LogUtil.i("获取行数成功");
            List<ProjectFunctionKey> functionKeys = new ArrayList<>();

            String lastPrimaryFunction = "";

            for (int i = 1; i < rowSize; i++) {
                LogUtil.i("循环读取中： " + i);

                ProjectFunctionKey projectFunctionKey = new ProjectFunctionKey();

                Row row = sheet.getRow(i);

                LogUtil.i("列数：" + (row.getLastCellNum()));
                if(row.getLastCellNum() != 2){
                    return null;
                }

                LogUtil.i("有效列数：" + row.getPhysicalNumberOfCells());
                if(i == 1 && row.getPhysicalNumberOfCells() != 2){
                    return null;
                }

                if(row.getPhysicalNumberOfCells() == 2){
                    lastPrimaryFunction = row.getCell(0).getStringCellValue();
                }
                projectFunctionKey.setPrimaryFunction(lastPrimaryFunction);
                projectFunctionKey.setSecondaryFunction(row.getCell(1).getStringCellValue());

                functionKeys.add(projectFunctionKey);
            }

            is.close();

            functionView = FunctionView.functionListToView(functionKeys);
            LogUtil.i("获取View成功");

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return functionView;
    }

    @Override
    public XSSFWorkbook writeProjectFunction(XSSFWorkbook workbook, List<ProjectFunctionKey> functionKeys) {

        //获取工作薄
        XSSFSheet sheet = workbook.getSheetAt(0);

        for(int i = 1; i <= functionKeys.size(); i++){
            ProjectFunctionKey functionKey = functionKeys.get(i-1);

            XSSFRow row = sheet.createRow(i);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(functionKey.getPrimaryFunction());
            cell = row.createCell(1);
            cell.setCellValue(functionKey.getSecondaryFunction());

        }
        return workbook;
    }

    @Override
    public List<ProjectRisk> readProjectRisk(MultipartFile file) {
        if(!checkFileType(file)){
            return null;
        }

        List<ProjectRisk> risks = new ArrayList<>();

        try{
            //获取文件流
            InputStream is = file.getInputStream();

            LogUtil.i("获取文件流成功");
            //获取文件后缀
            String original = file.getOriginalFilename();
            String suffix = original.substring(original.lastIndexOf("."));

            //创建工作薄
            Workbook workbook = StringUtils.equals(".xsl", suffix) ? new HSSFWorkbook(is) : new XSSFWorkbook(is);

            LogUtil.i("创建工作薄成功");
            //获取工作薄
            Sheet sheet = workbook.getSheetAt(0);

            LogUtil.i("获取工作薄成功");
            //获取行数
            int rowSize = sheet.getPhysicalNumberOfRows();

            LogUtil.i("获取行数成功");
            List<ProjectFunctionKey> functionKeys = new ArrayList<>();

            for (int i = 1; i < rowSize; i++) {
                LogUtil.i("循环读取中： " + i);

                ProjectRisk risk = new ProjectRisk();

                Row row = sheet.getRow(i);

                if(row.getPhysicalNumberOfCells() != 7){
                    return null;
                }

                risk.setRiskId(row.getCell(0).getStringCellValue());
                risk.setType(row.getCell(1).getStringCellValue());
                risk.setDescription(row.getCell(2).getStringCellValue());
                risk.setRiskLevel((int)row.getCell(3).getNumericCellValue());
                risk.setInfluence(row.getCell(4).getStringCellValue());
                risk.setReactiveStrategy(row.getCell(5).getStringCellValue());
                risk.setRiskTrackFrequency(row.getCell(6).getNumericCellValue());

                risks.add(risk);

            }

            is.close();

            LogUtil.i("解析风险成功");

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return risks;
    }

    @Override
    public XSSFWorkbook writeProjectRisk(XSSFWorkbook workbook, List<ProjectRisk> riskKeys) {
        //获取工作薄
        XSSFSheet sheet = workbook.getSheetAt(0);

        for(int i = 1; i <= riskKeys.size(); i++){
            ProjectRisk risk = riskKeys.get(i-1);

            XSSFRow row = sheet.createRow(i);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(risk.getRiskId());
            cell = row.createCell(1);
            cell.setCellValue(risk.getType());
            cell = row.createCell(2);
            cell.setCellValue(risk.getDescription());
            cell = row.createCell(3);
            cell.setCellValue(risk.getRiskLevel());
            cell = row.createCell(4);
            cell.setCellValue(risk.getInfluence());
            cell = row.createCell(5);
            cell.setCellValue(risk.getReactiveStrategy());
            cell = row.createCell(6);
            cell.setCellValue(risk.getRiskTrackFrequency());

        }
        return workbook;
    }
}
