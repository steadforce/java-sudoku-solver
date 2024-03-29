package solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SudokuSolver {
    
    private SudokuBoard board;

    public boolean solveBoard(SudokuBoard board) {
        processInitialBoard(board);
        board.setCurrentBoard(performSearch(board.getCurrentBoard()));
        return board.getCurrentBoard() != null && isBoardSolved(board.getCurrentBoard());
    }

    public void processInitialBoard(SudokuBoard board) {
        this.board = board;
        for(String square : board.getSquareIndices()) {
            if(board.getInitialBoard().get(square) != null) {
                assign(board.getCurrentBoard(), square, board.getInitialBoard().get(square));
            }
        }
    }
    
    private boolean assign(Map<String, Collection<Character>> currentBoard, String square, Character value) {
        Collection<Character> otherValues = new ArrayList<Character>(currentBoard.get(square));
        otherValues.remove(value);
        boolean boardIsValid = true;
        for(Character otherValue : otherValues) {
            boardIsValid = boardIsValid && eliminate(currentBoard, square, otherValue);
            if(!boardIsValid) return false;
        }
        //System.out.println("Assign " + value + " to " + square + "\n" + this.toString());
        return boardIsValid;
    }

    private boolean eliminate(Map<String, Collection<Character>> currentBoard, String square, Character value) {
        Collection<Character> possibleValuesOnSquare = currentBoard.get(square);
        if (!possibleValuesOnSquare.contains(value)) return true;
        
        possibleValuesOnSquare.remove(value);
        
        if(possibleValuesOnSquare.size() == 0) return false;
        
        if(possibleValuesOnSquare.size() == 1) {
            //Remove possible value for peers
            Character lastRemainingValue = possibleValuesOnSquare.toArray(new Character[1])[0];
            for(String peer : this.board.getSquarePeers().get(square)) {
                boolean boardIsValid = eliminate(currentBoard,  peer, lastRemainingValue);
                if(!boardIsValid) return false;
            }
        }
        
        for(Collection<String> unit : this.board.getSquareUnitRelations().get(square)) {
            List<String> squaresForValueInUnit = unit.stream().filter(squareInUnit -> currentBoard.get(squareInUnit).contains(value))
                    .collect(Collectors.toList());
            
            if(squaresForValueInUnit.isEmpty()) {
                return false;
            } else if(squaresForValueInUnit.size() == 1) {
                boolean boardIsValid = assign(currentBoard, squaresForValueInUnit.get(0), value);
                return boardIsValid;
            }
        }
        
        return true;
    }
    
    private Map<String, Collection<Character>> performSearch(Map<String, Collection<Character>> currentBoard) {        
        if(currentBoard == null) return null;
        
        if(isBoardSolved(currentBoard)) return currentBoard;
        
        String squareToSearch = getUnsolvedSquareWithMinNumberOfRemainingValues(currentBoard);
        
        for(char value : currentBoard.get(squareToSearch)) {
            Map<String, Collection<Character>> deepCopyOfCurrentBoard = deepcopyBoard(currentBoard);
            
            boolean boardStillValid = assign(deepCopyOfCurrentBoard, squareToSearch, value);
            if(boardStillValid) {
                Map<String, Collection<Character>> boardAfterSearch = performSearch(deepCopyOfCurrentBoard);
                if(boardAfterSearch != null) return boardAfterSearch;
            }
        }
        
        return null;
    }

    Map<String, Collection<Character>> deepcopyBoard(Map<String, Collection<Character>> currentBoard) {
        return currentBoard.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> new ArrayList<Character>(e.getValue())));
    }

    private String getUnsolvedSquareWithMinNumberOfRemainingValues(Map<String, Collection<Character>> currentBoard) {
        String minSquare = null;
        int minSquareNumberOfRemainingValues = Integer.MAX_VALUE;
        for(String square : this.board.getSquareIndices()) {
            int currentSquareNumberOfRemainingValues = currentBoard.get(square).size();
            if(currentSquareNumberOfRemainingValues < minSquareNumberOfRemainingValues && currentSquareNumberOfRemainingValues > 1) {
                minSquare = square;
                minSquareNumberOfRemainingValues = currentSquareNumberOfRemainingValues;
            }
        }
        return minSquare;
    }

    private boolean isBoardSolved(Map<String, Collection<Character>> currentBoard) {
        for(String square : this.board.getSquareIndices()) {
            if(currentBoard.get(square).size() != 1) return false;
        }
        return true;
    }
}
