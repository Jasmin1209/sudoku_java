package br.com.dio.ui.custom.button;

import javax.swing.*;
import java.awt.event.ActionListener;

public class FinishedGameButton extends JButton {

    public FinishedGameButton(final ActionListener actionListener){
        this.setText("Concluir jogo");
        this.addActionListener(actionListener);
    }
}
