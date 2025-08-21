package br.com.dio.ui.custom.scream;

import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.EventeEnum;
import br.com.dio.service.NotifierService;
import br.com.dio.ui.custom.button.CheckGameStatusButton;
import br.com.dio.ui.custom.button.FinishedGameButton;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.input.NumberText;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.com.dio.service.EventeEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class MainScream {

    private final static Dimension dimesion = new Dimension(600, 600);
    private final BoardService boardService;
    private final NotifierService notifierService;
    private JButton finishedGameButton;
    private JButton checkGameStatusButton;
    private JButton resetButton;



    public MainScream(final Map<String, String > gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void  buildMainScream(){
        JPanel mainPanel = new MainPanel(dimesion);
        JFrame mainFrame = new MainFrame(dimesion, mainPanel);

        for(int r = 0; r < 9; r+=3){
            var endRow = r + 2;
            for(int c = 0; c < 9; c+=3){
                var endCol = c + 2;
                var spaces = getSpaces(boardService.getSpaces(), c, endCol, r, endRow);
                mainPanel.add(generateSection(spaces));
            }
        }
        addResetButton(mainPanel);
        adsShowGameStatusButton(mainPanel);
        addFinishedButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpaces (final List<List<Space>> spaces, final int initColl, final int endCol, final int initRow, final int endRow){
        List<Space> spaceSector = new ArrayList<>();
        for(int r = initRow; r <= endRow; r++){
            for(int c = initColl; c <= endCol; c++){
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;
    }

    private JPanel generateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscribe(CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }

    private void addFinishedButton(JPanel mainPanel) {
         finishedGameButton = new FinishedGameButton(e -> {
            if(boardService.gameIsFinisih()){
                JOptionPane.showMessageDialog(null, "Parabens, você concluiu o jogo");
                checkGameStatusButton.setEnabled(false);
                finishedGameButton.setEnabled(false);
                resetButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Seu jogo tem algumas inconsistências, tente novamente");
            }
        });
    mainPanel.add(finishedGameButton);
    }

    private void adsShowGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
        var isErros = boardService.hasErros();
        var gameStatus = boardService.getStatus();
        var message = switch (gameStatus){
            case NON_STARDTES -> "O jogo não foi iniciado";
            case INCOMPLETE -> "O jogo está incompleto";
            case COMPLETE -> "O jogo está completo";
        };
        message += isErros ? " e contém erros" : "e não contem erros";
        showMessageDialog(null, message);
    });
    mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
         resetButton = new ResetButton(e -> {
           var dialog = showConfirmDialog(
                   null,
                   "Deseja realmente reiniciar o jogo?",
                   "Limpar o Jogo",
                   YES_NO_OPTION,
                   QUESTION_MESSAGE
           );
           if (dialog == 0){
               boardService.reset();
               notifierService.notify(CLEAR_SPACE);
           }
        });
    mainPanel.add(resetButton);
    }
}
