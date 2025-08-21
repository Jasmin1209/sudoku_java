package br.com.dio.model;

import java.util.Collection;
import java.util.List;

import static br.com.dio.model.GameStatus.COMPLETE;
import static br.com.dio.model.GameStatus.INCOMPLETE;
import static br.com.dio.model.GameStatus.NON_STARDTES;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
public class Board {
    private final List<List<Space>> spaces;

    public Board(List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatus getStatus(){
        if(spaces.stream().flatMap(Collection::stream).noneMatch(s -> s.isFixed() && nonNull(s.getAtual()))){
            return NON_STARDTES;
        }

        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getAtual())) ? INCOMPLETE : COMPLETE;
    }

    public boolean isErrors(){
        if(getStatus() == NON_STARDTES){
            return false;
        }

        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getAtual()) && s.getAtual().equals(s.getExpected()));
    }

    public boolean changeValue(final int col, final int row, final Integer value){
        var space = spaces.get(col).get(row);
        if(space.isFixed()){
            return false;
        }

        space.setAtual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row){
        var space = spaces.get(col).get(row);
        if(space.isFixed()){
            return false;
        }

        space.clearSpace();
        return true;
    }

    public void reset(){
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinishid(){
        return isErrors() && getStatus() == COMPLETE;
    }

    public boolean gameIsFinished(){
        return !isErrors() && getStatus().equals(COMPLETE);
    }
}
