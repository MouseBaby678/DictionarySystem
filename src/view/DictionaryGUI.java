package view;

import Util.DbUtil;
import dao.WordDao;
import model.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class DictionaryGUI extends JFrame {
    private JTextArea resultTextArea;
    private JLabel lblNewLabel;
    private JTextField textField;
    private JButton searchButton;
    private JButton addButton;
    private JButton manageButton;
    private JTable table;
    private JTextPane translationTextPane;
    private DbUtil dbUtil = new DbUtil();
    private WordDao wordDao = new WordDao();

    public DictionaryGUI() {
        setTitle("电子词典");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 300);
        setLocationRelativeTo(null);

        // 创建组件
        JPanel mainPanel = new JPanel();

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        mainPanel.setLayout(null);


        getContentPane().add(mainPanel);

        lblNewLabel = new JLabel("\u5355\u8BCD\uFF1A");
        lblNewLabel.setBounds(32, 22, 41, 15);
        mainPanel.add(lblNewLabel);

        textField = new JTextField(13);
        textField.setBounds(74, 19, 118, 21);
        mainPanel.add(textField);

        searchButton = new JButton("\u67E5\u8BE2");
        searchButton.setBounds(202, 18, 69, 23);
        mainPanel.add(searchButton);
        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                translationTextPane.setText("");
                try {
                    ResultSet rs = wordDao.list(dbUtil.getCon(), new Word(textField.getText()));
                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    dtm.setRowCount(0);
                    while (rs.next()) {
                        Vector v = new Vector<>();
                        v.add(rs.getString("word"));
                        dtm.addRow(v);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        addButton = new JButton("\u8BCD\u5E93\u6DFB\u52A0");
        addButton.setBounds(296, 18, 95, 23);
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AddFrame addFrame = new AddFrame();
                addFrame.setVisible(true);
            }
        });
        mainPanel.add(addButton);

        manageButton = new JButton("\u8BCD\u5E93\u7BA1\u7406");
        manageButton.setBounds(296, 48, 95, 23);
        manageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ManageFrame manageFrame = new ManageFrame();
                manageFrame.setVisible(true);
            }
        });
        mainPanel.add(manageButton);

        JScrollPane wordScrollPane = new JScrollPane();
        wordScrollPane.setBounds(32, 64, 188, 169);
        mainPanel.add(wordScrollPane);

        table = new JTable();
        table.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        table.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "\u5355\u8BCD"
                }
        ));
        table.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 14));
        wordScrollPane.setViewportView(table);

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                String word = (String) table.getValueAt(selectedRow, 0);
                try {
                    ResultSet rs = wordDao.list(dbUtil.getCon(), new Word(word));
                    rs.next();
                    translationTextPane.setText(rs.getString("translation"));


                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                String word = (String) table.getValueAt(selectedRow, 0);
                try {
                    ResultSet rs = wordDao.list(dbUtil.getCon(), new Word(word));
                    rs.next();
                    translationTextPane.setText(rs.getString("translation"));


                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        translationTextPane = new JTextPane();
        translationTextPane.setBounds(242, 81, 149, 152);
        translationTextPane.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        translationTextPane.setEditable(false);
        mainPanel.add(translationTextPane);

        JLabel lblNewLabel = new JLabel("\u7FFB\u8BD1\uFF1A");
        lblNewLabel.setBounds(242, 62, 47, 21);
        mainPanel.add(lblNewLabel);
        this.fillTable(new Word());
    }

    private void fillTable(Word word) {
        DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
        dtm.setRowCount(0);
        Connection con = null;
        try {
            con = dbUtil.getCon();
            ResultSet rs = wordDao.list(con, word);
            while (rs.next()) {
                Vector v = new Vector<>();
                v.add(rs.getString("word"));
                dtm.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dbUtil.closeCon(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DictionaryGUI dictionaryGUI = new DictionaryGUI();
                dictionaryGUI.setVisible(true);
            }
        });
    }
}
