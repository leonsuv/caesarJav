import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.awt.Color;

public class caesarForm extends JDialog {
    private JPanel contentPane;
    private JComboBox cryptCombo;
    private JTextArea inTextArea;
    private JTextField keyField;
    private JLabel keyLabel;
    private JButton cryptButton;
    private JTextArea outTextArea;
    private JButton copyButton;
    private JTextArea textArea1;

    //Alphanumeric table
    final int SymbsMod = 103;
    int key;
    public static final char[] Symbs = new char[] {'.',',','1','2','°','3','4','5','=','6','7','8','|','9','0','/','z','y','x','(','w','v','u','t','s','"','r','q','p','!','o','n','>','m','l','k',')','j','i','h','g','f'
                                                    ,'<','e','d','c','b','a',' ','ß','A','B','C','\'','?','D','E','F','*','G','H','I','J','[','^','K','L','M','N','O','P','{','Q','R','S','T','U','}','V','W','`','´','\\'
                                                    ,'ß','X','Y','Z','ä','ö','ü','Ü','Ö','Ä','&','%','-','$','_',':',';','#','+','\n'};

    public void setKey(String Key) {
        if (Key.equals("")) {JOptionPane.showMessageDialog(contentPane, "Bitte geben sie einen Schlüssel ein", "Fehlender Schlüssel!", JOptionPane.INFORMATION_MESSAGE); return;}
        key = Integer.parseInt(Key);
    }

    public String crypt(String Text, int EnOrDecrypt) {
        int cnt = 0;
        int cnt2 = 0;
        int c = 0;
        String bakToStr = "";
        String retrn;
        for (; cnt < Text.length(); cnt++) {
            try {
                while (Text.charAt(cnt) != Symbs[cnt2]) {
                    cnt2++;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(contentPane, "Ein unültiges Zeichen wurde eingegeben!", "Fehler", JOptionPane.INFORMATION_MESSAGE);
            }
            if (EnOrDecrypt == 0) {
                c = (cnt2 + key) % SymbsMod;
            } else if (EnOrDecrypt == 1) {
                c = (cnt2 - key) % SymbsMod;
                while (c < 0) {
                    c += SymbsMod;
                }
            }
            bakToStr += Symbs[c];
            cnt2 = 0;
        }
        retrn = bakToStr;
        return retrn;
    }

    public void copyUp() {
        inTextArea.setText(outTextArea.getText());
    }

    public caesarForm() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        setContentPane(contentPane);
        setModal(true);
        keyField.setTransferHandler(null); //block paste and copy
        String obj = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(obj);
        keyField.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(0,0,0)));
        keyField.setBackground(null);
        keyField.setDisabledTextColor(new Color(0));
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        //Crypt Button
        cryptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int comboFinder = cryptCombo.getSelectedIndex();
                String Key = keyField.getText();
                String output;

                if (Key.equals("")) {
                    JOptionPane.showMessageDialog(contentPane,"Wollen sie wirklich ohne Schlüssel vortfahren?\n\to.O", "Kein Schlüssel!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                setKey(Key); //Key BEFORE calling decrypt
                output = crypt(inTextArea.getText(), comboFinder);
                outTextArea.setText(output);
            }
        });

        keyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {//TODO User can paste string in (int) key. try catch catches even when doesn't paste
                super.keyTyped(e);
                //String keyText = keyField.getText();
                char iNumber = e.getKeyChar();
                if (!(Character.isDigit(iNumber)
                        || (iNumber == KeyEvent.VK_BACK_SPACE)
                        || (iNumber == KeyEvent.VK_DELETE)
                        || (iNumber == KeyEvent.VK_TAB)
                        || (iNumber == KeyEvent.VK_ENTER)) ||
                        ((e.getKeyCode() == KeyEvent.VK_V && e.getKeyCode() == KeyEvent.VK_CONTROL))) {
                    e.consume();
                    JOptionPane.showMessageDialog(contentPane, "Nur Zahlen werden als schlüssel akzeptiert", "Achtung!", JOptionPane.INFORMATION_MESSAGE);
                    /*for (int i = 0; i <= keyText.length(); i++) {
                        if (!(Character.isDigit(keyText.charAt(i)))) {
                            keyText.charAt(i) = '\0';
                        }
                    }*/
                }
            }
        });

        cryptCombo.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
                int combo1 = cryptCombo.getSelectedIndex();
                if (combo1 == 0) {
                    cryptButton.setText("Verschlüsseln");
                } else if (combo1 == 1){
                    cryptButton.setText("Entschlüsseln");
                }
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
                int combo1 = cryptCombo.getSelectedIndex();
                if (combo1 == 0) {
                    cryptButton.setText("Verschlüsseln");
                } else if (combo1 == 1){
                    cryptButton.setText("Entschlüsseln");
                }
            }
            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
                int combo1 = cryptCombo.getSelectedIndex();
                if (combo1 == 0) {
                    cryptButton.setText("Verschlüsseln");
                } else if (combo1 == 1){
                    cryptButton.setText("Entschlüsseln");
                }
            }
        });
        copyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!(inTextArea.getText().equals(""))) {
                    int yo = JOptionPane.showConfirmDialog(contentPane, "Der Text in dem oberen Textfeld wird entfernt und\n    der Text aus dem unterem Textfeld eingefügt!",
                            "Sind Sie sich sicher?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (yo == 0) {
                        copyUp();
                    }
                    return;
                }
                copyUp();
            }
        });
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        caesarForm dialog = new caesarForm();
        dialog.pack();
        dialog.setTitle("Caesar Verschlüsselung");
        Image image = ImageIO.read(new URL("file:///C:/sumstuff/imgs/agb6.png"));
        dialog.setLocationRelativeTo(null);
        dialog.setIconImage(image);
        dialog.setVisible(true);
        dialog.setType(Type.NORMAL);
        System.exit(0);
    }
}


//***METHOD COLLECTION***
/*

    String blo = "";
    StringBuilder blu = new StringBuilder();
    int textLen = inTextArea.getText().length();
    char[] ble = new char[255];
                ble = inTextArea.getText().toCharArray();
                        for (int i=0; i >= keyField.getText().length(); i++) {
                        blu.append(ble[i]);
                        }
                        blo = blu.toString();
                        JOptionPane.showMessageDialog(contentPane, blo);
                        inTextArea.setText(blo);

*/
/*
public static final char[] symbs = new char[] {'.',',','1','2','3','4','5','=','6','7','8','9','0','/','z','y','x','(','w','v','u','t','s','r','q','p','o','n','m','l','k',')','j','i','h','g','f','e','d','c'
            ,'b','a',' ','ß','A','B','C','D','E','F','*','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','ä','ö','ü','Ü','Ö','Ä','&','%','-','$','_',':',';','#','+','\n'};
    final int symbsMod = 86;
 */