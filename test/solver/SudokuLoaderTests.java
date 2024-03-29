package solver;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SudokuLoaderTests {

    @Test
    public void testGetEasySudokuBoard() {
        SudokuLoader loader = new SudokuLoader();
        
        
        SudokuBoard board = loader.getEasySudokuBoard();
        
        
        assertNotNull(board);
    }
    
    
    @Test
    public void testGetHardSudokuBoard() {
        SudokuLoader loader = new SudokuLoader();
        
        
        SudokuBoard board = loader.getHardSudokuBoard();
        
        
        assertNotNull(board);
    }
    
    
    @Test
    public void testGetVeryHardSudokuBoard() {
        SudokuLoader loader = new SudokuLoader();
        
        
        SudokuBoard board = loader.getVeryHardSudokuBoard();
        
        
        assertNotNull(board);
    }
}
