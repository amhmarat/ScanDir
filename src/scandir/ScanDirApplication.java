package scandir;
/**
 * @author Aymaletdinov MH
 */
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.IOException;
import static java.lang.System.exit;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScanDirApplication  {
    // Создаем безразмерный массив полученных файлов
    static final ArrayList<File> listWithFileNames = new ArrayList<>();
    static List<String> listFiles = new ArrayList<>();
    static List<String> listIgnoreDir = new ArrayList<>();
    static List<String> listIgnoreFile = new ArrayList<>();
    static long startTime1 = System.currentTimeMillis();
    static long startTime2 = System.currentTimeMillis();
    static long timeSpend1 = 0;
    static long timeSpend2 = 0;
    static int thread_finished = 0;
    static int thread_count = 0;


    public static void main(String[] args) throws IOException {
        if(args.length != 0) {
            int i=0;
            for(String s: args) {
                if(s.charAt(0) != '-' && s.charAt(0) != '#') {
                   // Добавление каталогов сканирования
                    listFiles.add(s);
                } else {
                    if(s.charAt(0) != '#') {
                        // Добавление каталогов, которые будут игнорированы по ключу -
                        listIgnoreDir.add(s);
                    } else {
                        // Добавление файлов, который будут игнорированы по ключу #
                        listIgnoreFile.add(s);
                    }
                }
                i++;
            }
        } else {
            System.out.println("Do not set start parameters");
            exit(0);
        }

        thread_count = listFiles.size();
        getDirThread gth[] = new getDirThread[thread_count];
        System.out.println("Begin");
        thread_finished = 0;
        for (int i=0; i<gth.length; i++) {
            gth[i] = new getDirThread(listFiles.get(i), listIgnoreDir, listIgnoreFile);
            // Запуск потока для каждого заданного каталога
            gth[i].start();
        }
        // Ожадание окончания потоков
        while (thread_finished < thread_count) {
         timeSpend1 = System.currentTimeMillis() - startTime1;
            timeSpend2 = System.currentTimeMillis() - startTime2;
            // Вывим точку каждые 6 секунд
            if(timeSpend1 >= 360000) {
              System.out.print(".");
              startTime1 = System.currentTimeMillis();
            }
            else {
              // Выводим палочку каждую минуту  
              if(timeSpend2 >= 60000) {
                System.out.print("|");
                startTime2 = System.currentTimeMillis();
              }                
            } 
        }
        
        // Сортировка полученных файлов
        Collections.sort(listWithFileNames);
        // Вывод в файл
        outListFiles();
        System.out.println("End.");
    }

    public static void outListFiles() throws IOException {
        Date dt;
        BasicFileAttributes attr = null;
        try (FileOutputStream outfile = new FileOutputStream("outputfile.txt")) {
            for (File fl : listWithFileNames) {
                try {
                    attr = Files.readAttributes(fl.toPath(),BasicFileAttributes.class);
                    dt = new Date(attr.creationTime().to(TimeUnit.MILLISECONDS)); //(fl.lastModified());
                }catch(IOException exception) {dt = null;}
                
                SimpleDateFormat dtf = new SimpleDateFormat("yyyy.MM.dd");
                outfile.write("[\n".getBytes("UTF-8"));
                outfile.write(("file = " + fl + "\n " + "date = " + dtf.format(dt) + "\n " + "size = " + fl.length() + "]").getBytes("UTF-8"));
            }
        }
    }
}

class getDirThread implements Runnable {
        Thread gdthread;
        private final String pathdirname;
        private final List<String> noscandirlist;
        private final List<String> noscanfilelist;
        getDirThread (String pathname, List<String> dirlist, List<String> filelist) {
            pathdirname = pathname;
            noscandirlist = dirlist;
            noscanfilelist = filelist;
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
        
        for (File f : dirf.listFiles()) {
            if (f.isFile()) {
                 int isign = 0; 
                 if(!noscanfilelist.isEmpty()) {
                  for (int i=0; i<noscanfilelist.size(); i++) {
                   if(noscanfilelist.get(i).equals(f.getName()))
                     isign = 1;
                   }
                }
                if(isign != 1)
                  ScanDirApplication.listWithFileNames.add(f);
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
