/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scandir;

import java.io.File;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
//import java.io.Serializable;
//import java.util.Comparator;

/**
 *
 *  @author Aymaletdinov MH
 */
public class DataFiles {
    //private final DataListDir listWithAllFileNames;
    private final File f;
    private final Date dtFile;
    private final BasicFileAttributes attrFile;
    public DataFiles (File F, Date dtF, BasicFileAttributes attrF) {
        this.f = F;    
        this.dtFile = dtF;
        this.attrFile = attrF;
    }
    public File getFile () {
        return f;
    }
    public Date getFileDate () {
        return dtFile;
    }
    public BasicFileAttributes getFileAttr () {
        return attrFile;
    }
}
