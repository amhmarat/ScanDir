/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scandir;

//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;
//import static org.junit.Assert.*;

/**
 *
 * @author Aymaletdinov MH
 */
public class ScanDirApplicationTest {
    
    public ScanDirApplicationTest() {
    }
    
    /**
     * Test of main method, of class ScanDirApplication.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = {"c:\\Temp","-c:\\Temp\\Projects"};
        ScanDirApplication.main(args);
    }
  
}
