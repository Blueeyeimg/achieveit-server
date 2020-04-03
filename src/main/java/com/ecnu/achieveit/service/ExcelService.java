package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectFunctionKey;
import com.ecnu.achieveit.modelview.FunctionItem;
import com.ecnu.achieveit.modelview.FunctionView;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelService {

    boolean checkFileType(MultipartFile file);

    FunctionView readProjectFunction(MultipartFile file);

    XSSFWorkbook writeProjectFunction(XSSFWorkbook workbook, List<ProjectFunctionKey> functionKeys);
}
