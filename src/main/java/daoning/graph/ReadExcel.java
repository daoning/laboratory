package daoning.graph;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcel {
    static Node[] initializeGraph() throws IOException {
        FileInputStream in = new FileInputStream(new File("e:\\matrix.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(in);
//        workbook.setMissingCellPolicy();
        HSSFSheet sheet = workbook.getSheetAt(0);
        int rowNum = sheet.getFirstRowNum();
        HSSFRow row = sheet.getRow(rowNum);
        int cellNum, beginRow = 0, beginCol = 0, amountOfNode = 0;
        HSSFCell cell;
        while ((cellNum = row.getFirstCellNum()) > 0) {
            cell = row.getCell(cellNum);
            if ("commence".equals(cell.getStringCellValue())) {
                beginRow = rowNum;
                beginCol = cellNum;
                do {
                    cell = row.getCell(++cellNum);
                }
                while (cell.getNumericCellValue() != 0);
                amountOfNode = cellNum - beginCol - 1;
                break;
            }
            row = sheet.getRow(++rowNum);
        }
//        int firstRowBelowDataArea = beginRow + amountOfNode + 1;
//        int firstColRightOfDataArea=beginCol+amountOfNode+1;
        //data area定位结束，开始生成节点
        Node[] graph = new Node[amountOfNode + 1];
        for (int i = 1; i <= amountOfNode; i++) {
            graph[i] = new Node(i);
        }
        //初始化边
        Node nodei, nodej;
        for (int i = 1; i <= amountOfNode; i++) {
            row = sheet.getRow(i + beginRow);
            for (int j = i + 1; j <= amountOfNode; j++) {
                cell = row.getCell(j + beginCol);
                if (null != cell && cell.getCellType() != CellType.BLANK) {
                    int distance = (int) cell.getNumericCellValue();
                    nodei = graph[i];
                    nodej = graph[j];
                    nodei.addNeighbour(nodej, distance);
                    nodej.addNeighbour(nodei, distance);
                }
            }
        }

        return graph;
    }


}
