package fr.ensai.outils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ensai.outils.model.Livre;

public class LivreDAO extends DAO<Livre> {

	private static final String UPDATE_QUERY = "UPDATE LIVRE SET NOM = ?, AUTEUR = ? WHERE ID = ?";
	private static final String FIND_BY_ID_QUERY = "SELECTE * FROM LIVRE WHERE ID = ?";
	private static final String INSERT_BY_ID_QUERY = "INSERT INTO LIVRE (NOM, AUTEUR) VALUES (?, ?)";
	private static final String DELETE_QUERY =  "DELETE FROM LIVRE	WHERE ID = ?";

	@Override
	public Livre find(long id) {
		PreparedStatement statement;
		Livre livre = null;
		try {
			statement = Connexion.getInstance().prepareStatement(FIND_BY_ID_QUERY);
			try {
				statement.setLong(1, id);
				ResultSet resultSet = statement.executeQuery(FIND_BY_ID_QUERY);
				livre = build(resultSet);
			} catch (SQLException e) {
				System.out.println("Probleme lors de l'execution de la requete "
						+ FIND_BY_ID_QUERY);
			}
		} catch (SQLException e) {
			System.out.println("Probleme lors de la connection");
		}
		return livre;
	}

	@Override
	public Livre create(Livre obj) {
		PreparedStatement statement;
		try {
			statement = Connexion.getInstance().prepareStatement(INSERT_BY_ID_QUERY);
			try {
				statement.setString(1, obj.getNom());
				statement.setString(2, obj.getAuteur());
				statement.execute();
			} catch (SQLException e) {
				System.out.println("Probleme lors de l'execution de la requete "
						+ INSERT_BY_ID_QUERY);
			}
		} catch (SQLException e) {
			System.out.println("Probleme lors de la connection");
		}
		return obj;
	}

	@Override
	public Livre update(Livre obj) {
		PreparedStatement statement;
		try {
			statement = Connexion.getInstance().prepareStatement(UPDATE_QUERY);
			try {
				statement.setString(1, obj.getNom());
				statement.setString(2, obj.getAuteur());
				statement.executeQuery();
			} catch (SQLException e) {
				System.out.println("Probleme lors de l'execution de la requete "
						+ UPDATE_QUERY);
			}
		} catch (SQLException e) {
			System.out.println("Probleme lors de la connection");
		}
		return obj;
	}

	@Override
	public void delete(Livre obj) {
		PreparedStatement statement;
		try {
			statement = Connexion.getInstance().prepareStatement(DELETE_QUERY);
			try {
				statement.setInt(1, obj.getId());
				statement.execute();
			} catch (SQLException e) {
				System.out.println("Probleme lors de l'execution de la requete "
						+ DELETE_QUERY);
			}
		} catch (SQLException e) {
			System.out.println("Probleme lors de la connection");
		}

	}

	@Override
	protected Livre build(ResultSet resultSet) {
		Livre livre = new Livre();
		try {
			livre.setId(resultSet.getInt("ID"));
			livre.setNom(resultSet.getString("NOM"));
			livre.setAuteur(resultSet.getString("AUTEUR"));
		} catch (SQLException e) {
			System.out.println("Probleme lors de la construction d'un objet livre");
		}
		return livre;
	}

}
