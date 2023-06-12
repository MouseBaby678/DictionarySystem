package dao;

import Util.StringUtil;
import model.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WordDao {
    public int add(Connection con, Word word) throws Exception {
        String sql = "Insert into dictionary values(?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, word.getWord());
        pstmt.setString(2, word.getTranslation());
        return pstmt.executeUpdate();
    }

    public ResultSet list(Connection con, Word word) throws Exception {
        StringBuilder stub = new StringBuilder("select * from dictionary");
        if (StringUtil.isNotEmpty(word.getWord())) {
            stub.append(" where word like '%" + word.getWord() + "%'");
        }
        PreparedStatement pstmt = con.prepareStatement(stub.toString());
        return pstmt.executeQuery();
    }

    public int delete(Connection con, String word) throws Exception {
        String sql = "delete from dictionary where word=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, word);
        return pstmt.executeUpdate();
    }

    public int update(Connection con, Word word) throws Exception {
        String sql = "update dictionary set translation=? where word =?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, word.getTranslation());
        pstmt.setString(2, word.getWord());
        return pstmt.executeUpdate();
    }
}
