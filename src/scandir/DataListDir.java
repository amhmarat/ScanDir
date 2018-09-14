/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scandir;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Aymaletdinov MH
 */
public class DataListDir {
  // Создаем безразмерный массив полученных файлов
  private static ArrayList<DataFiles> allListWithFileNames;
  public DataListDir () {
      allListWithFileNames =  new ArrayList<>();
  }

  public DataListDir (ArrayList<DataFiles> fList) {
        allListWithFileNames = fList;
  }  

  public ArrayList<DataFiles> getAllList () {
        return allListWithFileNames;
  }

  void add(DataFiles dataFiles) {
        if(dataFiles != null) {
          allListWithFileNames.add(dataFiles);
        }
   }

  void sort() {
        Collections.sort(allListWithFileNames, new DataFilesComparator());      
  }

  void SaveListFiles(String outFileName) throws IOException {
    try (FileOutputStream outfile = new FileOutputStream(outFileName)) {
        for (DataFiles fl : allListWithFileNames) {
            SimpleDateFormat dtf = new SimpleDateFormat("yyyy.MM.dd");
            outfile.write(("[\nfile = " + fl.getFile() + "\n " + "date = " + dtf.format(fl.getFileDate()) + "\n " + "size = " + fl.getFile().length() + "]").getBytes("UTF-8"));
            }
        }
  }

}

 class DataFilesComparator implements Comparator<DataFiles> {   
         public int compare(DataFiles lhs, DataFiles rhs) {
            return lhs.getFile().toString().compareTo(rhs.getFile().toString());
        }   
 }

