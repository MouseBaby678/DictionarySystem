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
import java.sql.Connection;
import java.sql.SQLException;

public class AddFrame extends JFrame {

    private JPanel contentPane;
    private JTextField wordTextField;
    private JTextPane translationTextPane;
    private DbUtil dbUtil = new DbUtil();
    private WordDao wordDao = new WordDao();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddFrame frame = new AddFrame();
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
    public AddFrame() {
        setTitle("\u6DFB\u52A0\u8BCD\u5178");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        wordTextField = new JTextField();
        wordTextField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        wordTextField.setBounds(114, 25, 228, 30);
        contentPane.add(wordTextField);
        wordTextField.setColumns(10);

        JLabel wordLabel = new JLabel("\u5355\u8BCD\uFF1A");
        wordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        wordLabel.setBounds(40, 28, 61, 23);
        contentPane.add(wordLabel);

        JLabel translationLabel = new JLabel("\u7FFB\u8BD1\uFF1A");
        translationLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        translationLabel.setBounds(40, 77, 61, 23);
        contentPane.add(translationLabel);

        JButton resetButton = new JButton("\u6E05\u7A7A");
        resetButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        resetButton.setBounds(114, 200, 97, 46);
        contentPane.add(resetButton);
        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                wordTextField.setText("");
                translationTextPane.setText("");
            }
        });

        JButton addButton = new JButton("\u6DFB\u52A0");
        addButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        addButton.setBounds(245, 200, 97, 46);
        contentPane.add(addButton);
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String word = wordTextField.getText();
                String translation = translationTextPane.getText();

                if (StringUtil.isEmpty(word)) {
                    JOptionPane.showMessageDialog(null, "单词不能为空!");
                    return;
                }
                if (StringUtil.isEmpty(translation)) {
                    JOptionPane.showMessageDialog(null, "翻译不能为空!");
                    return;
                }
                Connection con = null;
                try {
                    con = dbUtil.getCon();
                    int addnum = wordDao.add(con, new Word(word, translation));
                    if (addnum == 1) {
                        JOptionPane.showMessageDialog(null, "添加成功!");
                        wordTextField.setText("");
                        translationTextPane.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "添加失败!");
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

        translationTextPane = new JTextPane();
        translationTextPane.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        translationTextPane.setBounds(114, 79, 228, 111);
        contentPane.add(translationTextPane);
    }
}
