package rest.todo.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rest.todo.model.Article;
import rest.todo.model.Categorie;
import rest.todo.model.Panier;

public enum PanierDao {
instance;
	private Map<Integer, Panier> contentProvider = new HashMap<>();
	private ArrayList<Article> listeArticles = new ArrayList<>();
	
	private PanierDao() {
		getModels();
	}
	public Map<Integer, Panier> getModel(){
        return contentProvider;
    }
	public ArrayList<Article>getArticles(){
		return listeArticles;
	}
	// afficher la liste des paniers avec les articles et leur quantite
	public void getModels() {
		try {
			DbManager db = new DbManager();
			Connection conn = db.getConnection();
			Statement stmt = conn.createStatement();
			String QUERY = "SELECT * FROM panier p, panierarticle pa, article a WHERE p.id = pa.ref_panier and a.id = pa.ref_article";
			ResultSet rs = stmt.executeQuery(QUERY);
			// Extract data from result set
			while (rs.next()) {
			// Retrieve by column name
			System.out.println("ID: " + rs.getInt("ID"));
			Article article = new Article(rs.getInt("ref_article"),rs.getString("libelle"),rs.getString("marque"),
					rs.getDouble("prix"),rs.getInt("cat_id"),rs.getString("photo"));
			listeArticles.add(article);
			Panier panier = new Panier(rs.getInt("id"),getArticles());
			contentProvider.put(rs.getInt("id"), panier);
		    }
		}	
		catch (SQLException e) {
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	//inserer un panier
	
	public int put(Panier model) {
		try {
			DbManager db = new DbManager();
			Connection conn = db.getConnection();
			Statement stmt = conn.createStatement();
			String QUERY = "INSERT INTO panier('id', 'prixTotal')"+
			"VALUES ("+model.getId()+","+model.getPrixTotal()+")";
			stmt.executeUpdate(QUERY);
			return (1);
		}
		catch (SQLException e) {
			e.getMessage();
			e.printStackTrace();
			return (0);
		} finally {}
	}
	
	//modifier le panier
	
	public int post(Panier model) {
		try {
			DbManager db = new DbManager();
			Connection conn = db.getConnection();
			Statement stmt = conn.createStatement();
			String QUERY ="UPDATE Panier set" + "id=" + model.getId()+
					"," + "prixTotal=" + model.getPrixTotal() + "WHERE id =" +model.getId();
			stmt.executeUpdate(QUERY);
			return 1;
		}catch (SQLException e) {
			e.getMessage();
			e.printStackTrace();
			return (0);
		} finally {}
	}
	
	//supprimer un article dans le panier 
	public String delete(int id) {
		try {
			DbManager db = new DbManager();
			Connection conn = db.getConnection();
			Statement stmt = conn.createStatement();
			String QUERY = "DELETE FROM panierarticle WHERE ref_article=" + id+" ";
			stmt.executeUpdate(QUERY);
			return "element "+ id+" bien supprim�";
		}catch (SQLException e) {
			e.getMessage();
			e.printStackTrace();
			return "erreur";
			} finally {
			}
	}

}
