package view;

import Util.DbUtil;
import Util.StringUtil;
import dao.WordDao;
import model.Word;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ManageFrame extends JFrame {

    private JPanel contentPane;
    private JTextField wordTextField;
    private JTable table;
    private DbUtil dbUtil = new DbUtil();
    private WordDao wordDao = new WordDao();
    private JTextPane translationTextPane;
    private JLabel lblNewLabel_1;
    private JButton deleteButton;
    private JButton updateButton;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManageFrame frame = new ManageFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ManageFrame() {
        setTitle("\u7BA1\u7406\u8BCD\u5178");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("\u5355\u8BCD\uFF1A");
        lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        lblNewLabel.setBounds(62, 23, 53, 35);
        contentPane.add(lblNewLabel);

        wordTextField = new JTextField();
        wordTextField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        wordTextField.setBounds(118, 30, 136, 23);
        contentPane.add(wordTextField);
        wordTextField.setColumns(10);

        JButton searchButton = new JButton("\u67E5\u8BE2");
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchButton.setBounds(275, 27, 100, 30);
        contentPane.add(searchButton);
        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                translationTextPane.setText("");
                try {
                    ResultSet rs = wordDao.list(dbUtil.getCon(), new Word(wordTextField.getText()));
                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    dtm.setRowCount(0);
                    while (rs.next()) {
                        Vector v = new Vector<>();
                        v.add(rs.getString("word"));
                        v.add(rs.getString("translation"));
                        dtm.addRow(v);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(62, 69, 312, 214);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        table.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "\u5355\u8BCD", "\u7FFB\u8BD1"
                }
        ));
        table.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 16));
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
        scrollPane.setViewportView(table);

        translationTextPane = new JTextPane();
        translationTextPane.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        translationTextPane.setBounds(62, 312, 312, 100);
        contentPane.add(translationTextPane);

        lblNewLabel_1 = new JLabel("修改翻译：");
        lblNewLabel_1.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        lblNewLabel_1.setBounds(62, 287, 100, 23);
        contentPane.add(lblNewLabel_1);

        deleteButton = new JButton("删除");
        deleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        deleteButton.setBounds(62, 422, 97, 31);
        contentPane.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请选择要删除的单词!");
                    return;
                }
                String word = (String) table.getValueAt(selectedRow, 0);
                Connection con = null;
                try {
                    con = dbUtil.getCon();
                    int num = wordDao.delete(con, word);
                    if (num == 1) {
                        JOptionPane.showMessageDialog(null, "删除成功!");
                        wordTextField.setText("");
                        translationTextPane.setText("");
                        fillTable(new Word());
                    } else {
                        JOptionPane.showMessageDialog(null, "删除失败!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        dbUtil.closeCon(con);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

        updateButton = new JButton("提交");
        updateButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        updateButton.setBounds(275, 422, 100, 31);
        contentPane.add(updateButton);
        updateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "请选择要修改的单词!");
                    return;
                }
                String word = (String) table.getValueAt(selectedRow, 0);
                String translation = translationTextPane.getText();
                Connection con = null;
                try {
                    con = dbUtil.getCon();
                    int num = wordDao.update(con, new Word(word, translation));
                    if (num == 1) {
                        JOptionPane.showMessageDialog(null, "修改成功!");
                        wordTextField.setText("");
                        translationTextPane.setText("");
                        fillTable(new Word());
                    } else {
                        JOptionPane.showMessageDialog(null, "修改失败!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        dbUtil.closeCon(con);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
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
                v.add(rs.getString("translation"));
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
}
