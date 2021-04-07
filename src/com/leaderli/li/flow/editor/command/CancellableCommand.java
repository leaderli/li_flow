package com.leaderli.li.flow.editor.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;

import com.leaderli.li.flow.LiPlugin;

public class CancellableCommand extends Command {
    private Command command;
    private int dialogImageType;
    private boolean didExecute;
    private String message;
    private String preferenceKey;
    private IPreferenceStore preferenceStore;
    private String title;
    private String toggleMessage;

    public CancellableCommand(Command command, String message, String title, int dialogImageType) {
        this(command, message, title, dialogImageType, null, null, null);
    }

    public CancellableCommand(Command command, String message, String title, int dialogImageType, IPreferenceStore preferenceStore, String preferenceKey, String toggleMessage) {
        if (command == null) {
            throw new IllegalArgumentException("Wrapped command cannot be null");
        } else if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        } else {
            this.command = command;
            this.message = message;
            if (title == null) {
                title = "Question";
            }
            this.title = title;
            this.dialogImageType = dialogImageType;
            this.preferenceStore = preferenceStore;
            this.preferenceKey = preferenceKey;
            this.toggleMessage = toggleMessage;
            this.didExecute = false;
        }
    }

    public void execute() {
        MessageDialog dialog;
        Shell shell = LiPlugin.getStandardDisplay().getActiveShell();
        if (this.preferenceStore == null) {
            dialog = new MessageDialog(shell, this.title, null, this.message, this.dialogImageType, new String[]{IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, 1);
        } else if (this.preferenceStore.getString(this.preferenceKey).equals("always")) {
            this.command.execute();
            this.didExecute = true;
            return;
        } else {
            dialog = new MessageDialogWithToggle(shell, this.title, null, this.message, this.dialogImageType, new String[]{IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, 1, this.toggleMessage, false);
            ((MessageDialogWithToggle) dialog).setPrefStore(this.preferenceStore);
            ((MessageDialogWithToggle) dialog).setPrefKey(this.preferenceKey);
        }
        dialog.open();
        if (dialog.getReturnCode() == 0) {
            this.command.execute();
            this.didExecute = true;
        }
    }

    public boolean canExecute() {
        return this.command.canExecute();
    }

    public boolean canUndo() {
        return this.command.canUndo();
    }

    public void dispose() {
        this.command.dispose();
    }

    public String getDebugLabel() {
        return this.command.getDebugLabel();
    }

    public String getLabel() {
        return this.command.getLabel();
    }

    public void setDebugLabel(String label) {
        this.command.setDebugLabel(label);
    }

    public void setLabel(String label) {
        this.command.setLabel(label);
    }

    public String toString() {
        return this.command.toString();
    }

    public void undo() {
        if (this.didExecute) {
            this.command.undo();
        }
    }

    public void redo() {
        if (this.didExecute) {
            this.command.redo();
        }
    }
}