/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scandir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Aymaletdinov MH
 */
class GetDirThread implements Runnable {
        Thread gdthread;
        DataListDir listWithAllFileNames;
        private final String pathdirname;
        private final List<String> noscandirlist;
        private final List<String> noscanfilelist;
        GetDirThread (String pathname, DataListDir listf, List<String> dirlist, List<String> filelist) {
            pathdirname = pathname;
            listWithAllFileNames = listf;
            noscandirlist = dirlist;
            noscanfilelist = filelist;
        }

        public DataListDir getListFiles () {
           return listWithAllFileNames;
        }

    @Override
    public void run() {
        try {
            // Выполнение получения файлов из каталога в потоке
            getListFiles(pathdirname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScanDirApplication.thread_finished++;
    }
        
    public String getNameThread() {
        return Thread.currentThread().getName();
    }
    public void getListFiles(String pathname) throws IOException {
        Date dt = null;
        BasicFileAttributes attr = null;

        if(!noscandirlist.isEmpty()) {
            for (int i=0; i<noscandirlist.size(); i++) {
             if(noscandirlist.get(i).equals(pathname))
                 return;
            }
        }
        File dirf = new File(pathname);
        if (!dirf.exists()) {
                    return;
	 }
        
        File flst[];
        flst = dirf.listFiles();
        // Проверка доступа к каталогу, если его нет, то выходим
        if (flst == null) {
            System.out.println("There is no access to the directory: "+pathname);
            return;
        }
        // Сканирование файлов в каталоге
        for (File f : flst) {
            if (f.isFile()) {
                 int isign = 0; 
                 if(!noscanfilelist.isEmpty()) {
                  for (int i=0; i<noscanfilelist.size(); i++) {
                   if(noscanfilelist.get(i).equals(f.getName()))
                     isign = 1;
                   }
                }
                if(isign != 1)
                  
                 try {
                   attr = Files.readAttributes(f.toPath(),BasicFileAttributes.class);
                   dt = new Date(attr.creationTime().to(TimeUnit.MILLISECONDS)); //(fl.lastModified());
                 }catch(IOException exception) {
                     dt = null; 
                     attr = null;
                 }
                    //ScanDirApplication.listWithFileNames.add(new DataFiles(f,dt,attr));
                    listWithAllFileNames.add(new DataFiles(f,dt,attr));
            } else if (f.isDirectory()) {
                getListFiles(f.getAbsolutePath());      
            }
        }
    }

    public void start() {
             if(gdthread == null) {
                 gdthread =  new Thread(this,"Thread:"+pathdirname);
                 gdthread.start();
             }
    }
}
