package fr.uit.univparis8.tpair.tpair1.dao;

import fr.uit.univparis8.tpair.tpair1.db.ConnectionDB;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnonceDAO extends DAO<Annonce> {

    @Override
    public Annonce create(Annonce a) {
        String sql = "INSERT INTO annonce(title,description,adress,mail) VALUES(?,?,?,?) RETURNING id,date";

        try (Connection c = ConnectionDB.getInstance();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getTitle());
            ps.setString(2, a.getDescription());
            ps.setString(3, a.getAdress());
            ps.setString(4, a.getMail());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                a.setId(rs.getInt("id"));
                a.setDate(rs.getTimestamp("date"));
            }

            return a;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Annonce find(int id) {
        String sql = "SELECT * FROM annonce WHERE id=?";

        try (Connection c = ConnectionDB.getInstance();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new Annonce(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("adress"),
                    rs.getString("mail"),
                    rs.getTimestamp("date")
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Annonce> findAll() {
        List<Annonce> list = new ArrayList<>();
        String sql = "SELECT * FROM annonce ORDER BY date DESC";

        try (Connection c = ConnectionDB.getInstance();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Annonce(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("adress"),
                        rs.getString("mail"),
                        rs.getTimestamp("date")
                ));
            }

            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Annonce a) {
        String sql = "UPDATE annonce SET title=?,description=?,adress=?,mail=? WHERE id=?";

        try (Connection c = ConnectionDB.getInstance();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getTitle());
            ps.setString(2, a.getDescription());
            ps.setString(3, a.getAdress());
            ps.setString(4, a.getMail());
            ps.setInt(5, a.getId());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM annonce WHERE id=?";

        try (Connection c = ConnectionDB.getInstance();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
