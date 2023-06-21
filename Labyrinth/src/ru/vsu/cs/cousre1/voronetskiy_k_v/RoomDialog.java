package ru.vsu.cs.cousre1.voronetskiy_k_v;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomDialog extends JDialog {
    private JCheckBox chkAbove;
    private JCheckBox chkBelow;
    private JCheckBox chkRight;
    private JCheckBox chkLeft;
    private JPanel panelRoom;
    private JButton btnOK;
    private JButton btnCancel;

    private Maze.Room room;

    private ActionListener updateRoomAction;

    public RoomDialog(Maze.Room room, ActionListener updateRoomAction) {
        this.setTitle("Редактор стен в комнате");
        this.setContentPane(panelRoom);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.pack();

        this.setResizable(false);

        this.updateRoomAction = updateRoomAction;

        this.room = room;

        chkAbove.setSelected(room.wallAbove.isExist);
        chkAbove.setEnabled(!room.wallAbove.isReadOnly);

        chkBelow.setSelected(room.wallBelow.isExist);
        chkBelow.setEnabled(!room.wallBelow.isReadOnly);

        chkLeft.setSelected(room.wallLeft.isExist);
        chkLeft.setEnabled(!room.wallLeft.isReadOnly);

        chkRight.setSelected(room.wallRight.isExist);
        chkRight.setEnabled(!room.wallRight.isReadOnly);

        btnCancel.addActionListener(e -> {
            this.setVisible(false);
        });
        btnOK.addActionListener(e -> {
            this.room.wallRight.isExist = chkRight.isSelected();
            this.room.wallLeft.isExist = chkLeft.isSelected();
            this.room.wallAbove.isExist = chkAbove.isSelected();
            this.room.wallBelow.isExist = chkBelow.isSelected();
            if (updateRoomAction != null) {
                updateRoomAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "updateRoom"));
            }
            this.setVisible(false);
        });
    }
}
