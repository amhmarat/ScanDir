package scandir;
/**
 * @author Aymaletdinov MH
 */
import java.util.ArrayList;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.List;

public class ScanDirApplication  {
    // Создаем безразмерный массив полученных файлов
    static DataListDir lstWithFileNames = new DataListDir();
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
        GetDirThread gth[] = new GetDirThread[thread_count];
        System.out.println("Begin");
        thread_finished = 0;
        for (int i=0; i<gth.length; i++) {
            gth[i] = new GetDirThread(listFiles.get(i),lstWithFileNames, listIgnoreDir, listIgnoreFile);
            // Запуск потока для каждого заданного каталога
            gth[i].start();
        }
        // Ожадание окончания потоков
        while (thread_finished < thread_count) {
         timeSpend1 = System.currentTimeMillis() - startTime1;
            timeSpend2 = System.currentTimeMillis() - startTime2;
            // Выводим палочку каждую минуту
            if (timeSpend1 >= 60000) {
                System.out.print("|");
                startTime1 = System.currentTimeMillis();
            } else {
                // Вывим точку каждые 6 секунд
                if (timeSpend2 >= 6000) {
                    System.out.print(".");
                    startTime2 = System.currentTimeMillis();
                }
            }
        }
        
        // Сортировка полученных файлов
        lstWithFileNames.sort();

        // Вывод в файл
        lstWithFileNames.SaveListFiles("outputfile.txt");
        System.out.println("End.");
    }

}

